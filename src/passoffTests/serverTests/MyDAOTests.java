package passoffTests.serverTests;
import chess.ChessGame;
import chess_server.AuthToken;
import chess_server.User;
import dataAccess.DataAccessException;
import org.junit.jupiter.api.*;
import service.*;
import service.requests.*;
import service.responses.*;
import dataAccess.*;
import java.sql.SQLException;
import java.util.ArrayList;

@SuppressWarnings("unused")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MyDAOTests {
    private static final int HTTP_OK = 200;
    private static User existingUser;
    private static User newUser;



    @BeforeAll
    public static void init() {
        existingUser = new User();
        existingUser.setUsername("Nephi");
        existingUser.setPassword("Lehi");
        existingUser.setEmail("faith@testimony.com");

        newUser = new User();
        newUser.setUsername("testUsername");
        newUser.setPassword("testPassword");
        newUser.setEmail("test@mail.gov");

    }


    @BeforeEach
    public void setup() throws SQLException, DataAccessException {
        ClearResponse reset = new ClearService().clear();

        //UserDAO userTest = new UserDAO();
        //AuthDAO authTest = new AuthDAO();
        //userTest.CreateUser(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail());

    }


    @Test
    @Order(1)
    @DisplayName("My Positive User DAO Test")
    public void PosUserTest() throws SQLException, DataAccessException {
        UserDAO userTest = new UserDAO();
        ArrayList<User> testDB = new ArrayList<>();

        //Positive add user test
        Assertions.assertTrue(userTest.CreateUser(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail()));

        //Positive search user test
        Assertions.assertTrue(userTest.checkExists(existingUser.getUsername()));

        //Positive password test
        Assertions.assertTrue(userTest.samePass(existingUser.getPassword()));

        //Positive get all users test
        //add users
        userTest.CreateUser("Jeremy", "Topgear", "cool@beans.net");
        userTest.CreateUser(newUser.getUsername(), newUser.getPassword(), newUser.getEmail());
        testDB.add(new User(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail()));
        testDB.add(new User("Jeremy", "Topgear", "cool@beans.net"));
        testDB.add(new User(newUser.getUsername(), newUser.getPassword(), newUser.getEmail()));

        Assertions.assertEquals(testDB, userTest.getAllUsers());

    }

    @Test
    @Order(2)
    @DisplayName("My Negative User DAO Test")
    public void NegUserTest() throws SQLException, DataAccessException {
        UserDAO userTest = new UserDAO();
        ArrayList<User> testDB = new ArrayList<>();
        userTest.CreateUser(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail());

        //add user with null values
        DataAccessException thrown = Assertions.assertThrows(DataAccessException.class, ()-> userTest.CreateUser(null,null,null),"Expected create user to fail, but it didn't.");
        Assertions.assertTrue(thrown.getMessage().contains("Must provide values"));

        //search non-existent user
        Assertions.assertFalse(userTest.checkExists("blarge"));

        //confirm non-existent password
        Assertions.assertFalse(userTest.samePass("fakePasswordNOTREALWARGBGHVHFG"));

        //comparing different databases for equality
        testDB.add(new User("Jose", "quembly", "ti@juana.gov"));
        Assertions.assertNotEquals(testDB, userTest.getAllUsers());

    }

    @Test
    @Order(3)
    @DisplayName("My Positive Auth DAO Test")
    public void PosAuthTest() throws SQLException, DataAccessException {
        UserDAO userTest = new UserDAO();
        AuthDAO authTest = new AuthDAO();
        ArrayList<AuthToken> testDB = new ArrayList<>();
        AuthToken myToken = new AuthToken(existingUser.getUsername());

        //add auth token (positive insert test)
        Assertions.assertTrue(authTest.addAuth(myToken));

        String myAuth = authTest.getTokenFromName(existingUser.getUsername());

        //search for authentication by token (positive select test)
        Assertions.assertTrue(authTest.validate(myAuth));

        //search for username by name (positive select test)
        Assertions.assertEquals(existingUser.getUsername(), authTest.getUsername(myAuth));

        //get all users and compare to identical database (positive select all test)
        AuthToken test1 = new AuthToken("bob", "123456789");
        AuthToken test2 = new AuthToken("billy", "987654321");
        testDB.add(new AuthToken(existingUser.getUsername(), myAuth));
        testDB.add(test1);
        testDB.add(test2);
        authTest.addAuth(test1);
        authTest.addAuth(test2);
        Assertions.assertEquals(testDB, authTest.getAllTokens());

        //delete token (positive drop test)
        authTest.deleteAuth(myAuth);
        Assertions.assertFalse(authTest.validate(myAuth));


    }

    @Test
    @Order(4)
    @DisplayName("My Negative Auth DAO Test")
    public void NegAuthTest() throws SQLException, DataAccessException {
        UserDAO userTest = new UserDAO();
        AuthDAO authTest = new AuthDAO();
        ArrayList<AuthToken> testDB = new ArrayList<>();
        AuthToken myToken = new AuthToken(newUser.getUsername());
        Assertions.assertTrue(authTest.addAuth(myToken));
        String myAuth = authTest.getTokenFromName(newUser.getUsername());

        //add auth token (negative insert test)
        AuthToken fakeToken = new AuthToken(null, null);
        DataAccessException thrown = Assertions.assertThrows(DataAccessException.class, ()-> authTest.addAuth(fakeToken),"Expected add auth to fail, but it didn't.");
        Assertions.assertTrue(thrown.getMessage().contains("Username cannot be null"));

        //search for fake username by name (negative select test)
        Assertions.assertNotEquals("fake name", authTest.getUsername(myAuth));

        //search for fake authentication by token (negative select test)
        Assertions.assertFalse(authTest.validate("qwertyuiop"));

        //check double deletion for failure (negative drop test)
        Assertions.assertTrue(authTest.validate(myAuth));
        Assertions.assertTrue(authTest.deleteAuth(myAuth));
        Assertions.assertFalse(authTest.deleteAuth(myAuth));

        //get all users and compare to different database (negative select all test)
        AuthToken test1 = new AuthToken("bob", "123456789");
        AuthToken test2 = new AuthToken("billy", "987654321");
        testDB.add(new AuthToken(newUser.getUsername(), myAuth));
        testDB.add(test1);
        testDB.add(test2);
        authTest.addAuth(test1);
        authTest.addAuth(test2);

        Assertions.assertNotEquals(testDB, authTest.getAllTokens());


    }
    @Test
    @Order(5)
    @DisplayName("My Positive Game DAO test")
    public void PosGameTest() throws SQLException, DataAccessException {
        GameDAO testGame = new GameDAO();

        testGame.addGame("Test");
        int testID = testGame.getGameByName("Test").getGameID();

        //positive select by id test
        Assertions.assertTrue(testGame.checkExistsByID(testID));

        //positive select by name test
        Assertions.assertEquals("Test", testGame.getGameByName("Test").getGameName());

        //positive check serialization test
        Assertions.assertEquals("rnbqkbnrpppppppp________________________________PPPPPPPPRNBQKBNR", testGame.getGameByName("Test name for a test game").getMySerialGame());
        //Assertions.assertNull(testGame.getGameByID(testID).getBlackUsername());
        testGame.pickColor(existingUser.getUsername(), ChessGame.TeamColor.BLACK, testID);

        //positive update color player test
        Assertions.assertNotNull(testGame.getGameByID(testID).getBlackUsername());

        //positive select game player test
        Assertions.assertEquals(existingUser.getUsername(), testGame.getGameByID(testID).getBlackUsername());

    }

    @Test
    @Order(6)
    @DisplayName("My Negative Game DAO test")
    public void NegGameTest() throws SQLException, DataAccessException {
        GameDAO testGame = new GameDAO();

        testGame.addGame("Test");
        int testID = testGame.getGameByName("Test").getGameID();

        //negative select by id test
        Assertions.assertFalse(testGame.checkExistsByID(1234));

        //negative select by name test
        Assertions.assertFalse(testGame.checkExistsByName("homdinger"));

        //negative select game by name test
        Assertions.assertNotEquals("Realdeal", testGame.getGameByName("Test").getGameName());
        //Assertions.assertEquals("rnbqkbnrpppppppp________________________________PPPPPPPPRNBQKBNR", testGame.getGameByName("Test name for a test game").getMySerialGame());
        //Assertions.assertNull(testGame.getGameByID(testID).getBlackUsername());

        //negative update game player test
        testGame.pickColor(existingUser.getUsername(), ChessGame.TeamColor.BLACK, testID);
        Assertions.assertNotNull(testGame.getGameByID(testID).getBlackUsername());

        //negative select game player test
        Assertions.assertEquals(existingUser.getUsername(), testGame.getGameByID(testID).getBlackUsername());

    }


    @Test
    @Order(7)
    @DisplayName("My Clear Test")
    public void ClearTest() throws SQLException, DataAccessException {
        ClearResponse clearResult = new ClearService().clear();
        //test response code
        Assertions.assertEquals(HTTP_OK, clearResult.getReturnCode(), "Server response code was not 200 OK");
    }
}
