import chess_server.Game;
import chess_server.User;
import org.junit.jupiter.api.*;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import requests.LoginRequest;
import requests.RegisterRequest;
import java.util.HashSet;

@SuppressWarnings("unused")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MyFacadeTests {
    private static final int HTTP_OK = 200;
    private static User existingUser;
    private static User newUser;
    private static String existingAuth = "";

    serverFacade facade = new serverFacade();


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
    public void setup() throws Exception{
        facade.callClear();
        existingAuth = facade.callRegister(new RegisterRequest(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail()));
        String auth = facade.callRegister(new RegisterRequest(newUser.getUsername(), newUser.getPassword(), newUser.getEmail()));
        facade.callLogout(auth);
    }


    @Test
    @Order(1)
    @DisplayName("Positive 'CallLogin' Method Test")
    public void PosCallLoginTest() throws Exception {
        //testing login
        LoginRequest requestTest = new LoginRequest(newUser);
        Assertions.assertNotEquals("", facade.callLogin(requestTest));
    }

    @Test
    @Order(2)
    @DisplayName("Negative 'CallLogin' Method Test")
    public void NegCallLoginTest() throws Exception {
        //testing invalid credentials
        LoginRequest requestTest = new LoginRequest(new User("blargie", "doogie", "blooble"));
        Assertions.assertEquals("", facade.callLogin(requestTest));

        //testing already logged-in user
        LoginRequest requestTest2 = new LoginRequest(existingUser);
        Assertions.assertEquals("", facade.callLogin(requestTest));
    }

    @Test
    @Order(3)
    @DisplayName("Positive 'CallRegister' Method Test")
    public void PosCallRegisterTest() throws Exception {
        //testing new registration
        Assertions.assertNotEquals("", facade.callRegister((new RegisterRequest("newname", "newpass", "newmail"))));
    }

    @Test
    @Order(4)
    @DisplayName("Negative 'CallRegister' Method Test")
    public void NegCallRegisterTest() throws Exception {
        //trying to register already existing user
        Assertions.assertEquals("",facade.callRegister(new RegisterRequest(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail())));

    }
    @Test
    @Order(5)
    @DisplayName("Positive 'CallList' Method Test")
    public void PosCallListTest() throws Exception {
        //adding same list to server
        facade.callCreate(new CreateGameRequest("aGame"), existingAuth);
        facade.callCreate(new CreateGameRequest("bGame"), existingAuth);
        facade.callCreate(new CreateGameRequest("cGame"), existingAuth);

        //testing correct return
        Assertions.assertEquals(HashSet.class, facade.callList(existingAuth).getClass());
        Assertions.assertEquals(3, facade.callList(existingAuth).size());

    }

    @Test
    @Order(6)
    @DisplayName("Negative 'CallList' Method Test")
    public void NegCallListTest() throws Exception {
        //creating comparison list
        HashSet<Game> listTest = new HashSet<>();
        listTest.add(new Game(null, null, "aGame"));
        listTest.add(new Game(null, null, "bGame"));
        listTest.add(new Game(null, null, "cGame"));

        //adding different list to server
        facade.callCreate(new CreateGameRequest("xGame"), existingAuth);
        facade.callCreate(new CreateGameRequest("yGame"), existingAuth);
        facade.callCreate(new CreateGameRequest("zGame"), existingAuth);

        Assertions.assertNotEquals(listTest, facade.callList(existingAuth));
    }

    @Test
    @Order(7)
    @DisplayName("Positive 'CallLogout' Method Test")
    public void PosCallLogoutTest() throws Exception {
        //testing a logout
        Assertions.assertEquals(HTTP_OK, facade.callLogout(existingAuth));
    }

    @Test
    @Order(8)
    @DisplayName("Negative 'CallLogout' Method Test")
    public void NegCallLogoutTest() throws Exception {
        facade.callLogout(existingAuth);

        //attempting to logout twice
        Assertions.assertNotEquals(HTTP_OK, facade.callLogout(existingAuth));

        //attempting to log out with wrong credentials
        Assertions.assertNotEquals(HTTP_OK, facade.callLogout("zxvoqwdcm"));
    }

    @Test
    @Order(9)
    @DisplayName("Positive 'CallJoin' Method Test")
    public void PosCallJoinTest() throws Exception {
        int id = facade.callCreate(new CreateGameRequest("xGame"), existingAuth);
        Assertions.assertEquals(HTTP_OK, facade.callJoin(new JoinGameRequest("BLACK", id), existingAuth));
    }

    @Test
    @Order(10)
    @DisplayName("Negative 'CallJoin' Method Test")
    public void NegCallJoinTest() throws Exception {
        facade.callCreate(new CreateGameRequest("xGame"), existingAuth);
        Assertions.assertNotEquals(HTTP_OK, facade.callJoin(new JoinGameRequest(), "qwpovmas"));
    }

    @Test
    @Order(11)
    @DisplayName("Positive 'CallCreate' Method Test")
    public void PosCallCreateTest() throws Exception {
        facade.callCreate(new CreateGameRequest("yGame"), existingAuth);
        HashSet<Game> gameList = facade.callList(existingAuth);
        Assertions.assertFalse(gameList.isEmpty());
    }

    @Test
    @Order(12)
    @DisplayName("Negative 'CallCreate' Method Test")
    public void NegCallCreateTest() throws Exception {
        int id = facade.callCreate(new CreateGameRequest("yGame"), "bmdvopwemv");
        HashSet<Game> gameList = facade.callList(existingAuth);
        facade.callClear();
    }
}
