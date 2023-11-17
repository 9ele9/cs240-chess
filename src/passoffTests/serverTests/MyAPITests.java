package passoffTests.serverTests;
import chess.ChessGame;
import chess_server.User;
import dataAccess.DataAccessException;
import org.junit.jupiter.api.*;
import service.*;
import service.requests.*;
import service.responses.*;

import java.sql.SQLException;
@SuppressWarnings("unused")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MyAPITests {
    private static final int HTTP_OK = 200;
    private static User existingUser;
    private static User newUser;
    private static CreateGameRequest createRequest;
    private String existingAuth;


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

        createRequest = new CreateGameRequest();
        createRequest.setGameName("testgame");

    }


    @BeforeEach
    public void setup() throws SQLException, DataAccessException {
        ClearResponse reset = new ClearService().clear();
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(existingUser.getUsername());
        registerRequest.setPassword(existingUser.getPassword());
        registerRequest.setEmail(existingUser.getEmail());
        RegisterResponse registerResponse = new RegisterService().register(registerRequest);
        existingAuth = registerResponse.getAuthToken().getTokenValue();
    }


    @Test
    @Order(1)
    @DisplayName("My Login Test")
    public void LoginTest() throws SQLException, DataAccessException {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(existingUser.getUsername());
        loginRequest.setPassword(existingUser.getPassword());
        LoginResponse loginResponse = new LoginService().login(loginRequest);
        //test login success (positive test)
        Assertions.assertEquals(HTTP_OK, loginResponse.getReturnCode(), "Unsuccessful login");
        //test empty login (negative test)
        loginRequest.setUsername("");
        LoginResponse loginResponse2 = new LoginService().login(loginRequest);
        Assertions.assertNotEquals(HTTP_OK, loginResponse2.getReturnCode(),"Returned okay on empty username");
    }

    @Test
    @Order(2)
    @DisplayName("My Logout Test")
    public void LogoutTest() throws SQLException, DataAccessException {
        LogoutResponse result = new LogoutService().logout(existingAuth);
        //test successful logout (positive test)
        Assertions.assertEquals(HTTP_OK, result.getReturnCode(), "could not logout");

        //test duplicate logout (negative test)
        result = new LogoutService().logout(existingAuth);
        Assertions.assertNotEquals(HTTP_OK, result.getReturnCode(), "can't logout twice");

    }
    @Test
    @Order(3)
    @DisplayName("My Register Test")
    public void RegisterTest() throws SQLException, DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(newUser.getUsername());
        registerRequest.setPassword(newUser.getPassword());
        registerRequest.setEmail(newUser.getEmail());
        RegisterResponse registerResult = new RegisterService().register(registerRequest);

        //test registration (positive test)
        Assertions.assertEquals(HTTP_OK, registerResult.getReturnCode(), "could not register");

        //test duplicate registration (negative test)
        registerResult = new RegisterService().register(registerRequest);
        Assertions.assertNotEquals(HTTP_OK, registerResult.getReturnCode(), "can't register twice");

    }

    @Test
    @Order(4)
    @DisplayName("My List Test")
    public void ListTest() throws SQLException, DataAccessException {
        ListGameResponse result = new ListGamesService().listGames(existingAuth);

        //positive test
        Assertions.assertEquals(HTTP_OK, result.getReturnCode(), "couldn't get list");

        //negative test (no auth)
        result = new ListGamesService().listGames(existingAuth + "vpmqpcpmqspdm");
        Assertions.assertNotEquals(HTTP_OK, result.getReturnCode(), "bad auth");
    }

    @Test
    @Order(5)
    @DisplayName("My Join Test")
    public void JoinTest() throws SQLException, DataAccessException {
        CreateGameResponse createResult = new CreateGameService().createGame(createRequest, existingAuth);
        JoinGameRequest joinRequest = new JoinGameRequest();
        joinRequest.setGameID(createResult.getGameID());
        joinRequest.setPlayerColor(ChessGame.TeamColor.WHITE);
        JoinGameResponse joinResult = new JoinGameService().joinGame(joinRequest, existingAuth);

        //join (positive test)
        Assertions.assertEquals(HTTP_OK, joinResult.getReturnCode(), "join failed");

        //bad auth join (negative test)
        joinResult = new JoinGameService().joinGame(joinRequest, existingAuth + "asdnasdjkaksjdhaksjdhkajsdhkajdshkjas");
        Assertions.assertNotEquals(HTTP_OK, joinResult.getReturnCode(), "didn't return not successful");
    }

    @Test
    @Order(6)
    @DisplayName("My Create Test")
    public void CreateTest() throws SQLException, DataAccessException {
        CreateGameResponse createResult = new CreateGameService().createGame(createRequest, existingAuth);

        //test creation (positive test)
        Assertions.assertEquals(HTTP_OK, createResult.getReturnCode(), "creation failed");

        //test creation with no auth (negative test)
        new LogoutService().logout(existingAuth);
        createResult = new CreateGameService().createGame(createRequest, existingAuth);
        Assertions.assertNotEquals(HTTP_OK, createResult.getReturnCode(), "no authentication");

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
