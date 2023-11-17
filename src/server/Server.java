package server;
import chess_server.AuthToken;
import chess_server.Game;
import chess_server.User;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import spark.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import server.handlers.*;
import dataAccess.Database;
public class Server {
    private ArrayList<String> names = new ArrayList<>();
    public static void main(String[] args) throws DataAccessException {
        new Server().run();
    }

    private void run() throws DataAccessException {
        // Specify the port you want the server to listen on
        Spark.port(8080);

        // Register a directory for hosting static files
        Spark.externalStaticFileLocation("web");

        Database myDB = new Database();
        Connection connect = myDB.getConnection();

        // Register handlers for each endpoint using the method reference syntax
        RegisterHandler register = new RegisterHandler();
        ClearHandler clear = new ClearHandler();
        LoginHandler login = new LoginHandler();
        LogoutHandler logout = new LogoutHandler();
        CreateGameHandler createGame = new CreateGameHandler();
        ListGamesHandler listGames = new ListGamesHandler();
        JoinGamesHandler joinGame = new JoinGamesHandler();

        Spark.post("/echo", this::echoBody);

        Spark.post("/name/:name", this::addName);
        Spark.get("/user", this::listNames);
        Spark.get("/token", this::listToken);
        Spark.delete("/name/:name", this::deleteName);

    }

    private Object addName(Request req, Response res) throws SQLException, DataAccessException {
        names.add(req.params(":name"));
        return listNames(req, res);
    }

    private Object listNames(Request req, Response res) throws SQLException, DataAccessException {
        res.type("application/json");
        UserDAO test = new UserDAO();
        return new Gson().toJson(Map.of("name", test.getAllUsers()));
    }

    private Object listToken(Request req, Response res) throws DataAccessException, SQLException {
        res.type("application/json");
        AuthDAO test = new AuthDAO();
        return new Gson().toJson(Map.of("tokens", test.getAllTokens()));
    }

    private Object deleteName(Request req, Response res) throws SQLException, DataAccessException {
        names.remove(req.params(":name"));
        return listNames(req, res);
    }

    private Object echoBody(Request req, Response res) {
        var bodyObj = getBody(req, Map.class);

        res.type("application/json");
        return new Gson().toJson(bodyObj);
    }

    private static <T> T getBody(Request request, Class<T> clazz) {
        var body = new Gson().fromJson(request.body(), clazz);
        if (body == null) {
            throw new RuntimeException("missing required body");
        }
        return body;
    }
}
