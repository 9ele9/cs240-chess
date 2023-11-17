package dataAccess;

import chess_server.AuthToken;

import java.sql.SQLException;
import java.util.*;
import java.util.ArrayList;
import dataAccess.Database;

/**
 * Retrieves Authtoken data from the server.
 */
public class AuthDAO {
    static ArrayList<AuthToken> ATDB = new ArrayList<>();

    public AuthDAO(){}
    /**
     * checks given token against the token needed
     * READ operation
     */
    public boolean validate(String query) throws DataAccessException, SQLException {
        var conn = new Database().getConnection();
        boolean exists = false;
        try(var preparedStatement = conn.prepareStatement("SELECT authtoken FROM auth WHERE authtoken=\"" + query + "\"")){
            var rows = preparedStatement.executeQuery();
            while(rows.next()){
                //String name = rows.getString("username");
                String test = rows.getString("authtoken");
                exists = test != null;
            }

        }
        conn.close();
        return exists;
    }

    public String getUsername(String authQuery) throws DataAccessException, SQLException {
        var conn = new Database().getConnection();
        String exists = "";
        try(var preparedStatement = conn.prepareStatement("SELECT username FROM auth WHERE authtoken=\"" + authQuery + "\"")){
            var rows = preparedStatement.executeQuery();
            while(rows.next()){
                String name = rows.getString("username");
                exists = name;
            }
        }
        conn.close();
        return exists;
    }

    public String getTokenFromName(String nameQuery) throws DataAccessException, SQLException {
        var conn = new Database().getConnection();
        String exists = "";
        try(var preparedStatement = conn.prepareStatement("SELECT username, authtoken FROM auth WHERE username=\"" + nameQuery + "\"")){
            var rows = preparedStatement.executeQuery();
            while(rows.next()){
                String token = rows.getString("authtoken");
                String name = rows.getString("username");
                exists = token;
            }
        }
        conn.close();
        return exists;
    }

    public boolean addAuth(AuthToken newToken) throws DataAccessException, SQLException {
        var conn = new Database().getConnection();
        if(newToken.getUsername() == null){
            throw new DataAccessException("Username cannot be null");
        }
        boolean isCreated;
        try(var preparedStatement = conn.prepareStatement("INSERT INTO auth VALUES (\"" + newToken.getUsername() + "\", \"" + newToken.getTokenValue() + "\")")){
            var rows = preparedStatement.executeUpdate();
            isCreated = rows > 0;

        }
        conn.close();
        return isCreated;
    }

    public boolean deleteAuth(String query) throws DataAccessException, SQLException {
        var conn = new Database().getConnection();
        boolean deleteSuccess = false;
        try(var preparedStatement = conn.prepareStatement("DELETE FROM auth WHERE authtoken=\"" + query + "\"")){
            var rows = preparedStatement.executeUpdate();
            deleteSuccess = rows > 0;
        }
        conn.close();
        return deleteSuccess;

    }

    public Collection<AuthToken> getAllTokens() throws DataAccessException, SQLException {
        var conn = new Database().getConnection();
        ArrayList<AuthToken> DB = new ArrayList<>();

        try(var preparedStatement = conn.prepareStatement("SELECT username, authtoken FROM auth")){
            var rows = preparedStatement.executeQuery();
            while(rows.next()) {
                String name = rows.getString("username");
                String token = rows.getString("authtoken");
                DB.add(new AuthToken(name, token));
            }
        }
        conn.close();
        return DB;
    }

    public boolean clear() throws SQLException, DataAccessException {
        /*ATDB = new ArrayList<>();
        return ATDB.isEmpty();*/
        boolean isCleared = false;
        var conn = new Database().getConnection();
        try(var preparedStatement = conn.prepareStatement("DELETE FROM auth")){
            int rows = preparedStatement.executeUpdate();
            isCleared = true;
        }
        conn.close();
        return isCleared;
    }

}
