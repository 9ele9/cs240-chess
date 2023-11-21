package dataAccess;

import chess_server.User;

import java.sql.SQLException;
import java.util.*;

/**
 * Does CRUD operations on users.
 */
public class UserDAO {
    public UserDAO(){}
    static ArrayList<User> UserDB = new ArrayList<>();

    /**
     * Creates a new user and adds it to the database.
     * POST operation
     * @param name Username
     * @param pass Password
     * @param mail Email
     * @return User to be created
     */
    public boolean CreateUser(String name, String pass, String mail) throws DataAccessException, SQLException {
        var conn = new Database().getConnection();
        boolean isCreated = false;
        if(name == null){
            throw new DataAccessException("Must provide values");
        }
        try(var preparedStatement = conn.prepareStatement("INSERT INTO user VALUES (\"" + name + "\", \"" + pass + "\", \"" + mail + "\")")){
            var rows = preparedStatement.executeUpdate();
            isCreated = rows > 0;
        }
        conn.close();
        return isCreated;

        //User newUser = new User(name, pass, mail);



    }

    /*
    /**
     * deletes a given user from the database. Requires authorization.
     * DELETE operation
     * @param u User
     * @param deleteAuth Auth token
     * @throws DataAccessException
     */
    /*
    void DeleteUser(User u, AuthDAO deleteAuth) throws DataAccessException{

    }
    */
    /*
    /**
     * update the details of a given user. Requires authorization.
     * UPDATE operation
     * @param u User
     * @param updateAuth Auth token
     * @throws DataAccessException
     */
    /*
    void EditUser(User u, AuthDAO updateAuth) throws DataAccessException{
    }
    */
    /*
    /**
     * Fetches a specified user from the database.
     * READ operation
     * @param name User
     */
    /*
    public User getUser(String name){
        for (User thisUser : UserDB) {

            if(name != null && thisUser.getUsername() != null && (name.equals(thisUser.getUsername()))){
                return thisUser;
            }
        }
        return null;
    }
    */

    public boolean checkExists(String nameQuery) throws DataAccessException, SQLException {
        var conn = new Database().getConnection();
        boolean exists = false;
        try(var preparedStatement = conn.prepareStatement("SELECT username FROM user WHERE username=\"" + nameQuery + "\"")){
            var rows = preparedStatement.executeQuery();
            while(rows.next()){
                String test = rows.getString("username");
                exists = (test != null);
            }
        }
        conn.close();
        return exists;
        /*
        for (User thisUser : UserDB) {

            if(nameQuery != null && thisUser.getUsername() != null && (nameQuery.equals(thisUser.getUsername()))){
                return true;
            }
        }
        return false;*/
    }

    public boolean samePass(String passQuery) throws DataAccessException, SQLException {
        var conn = new Database().getConnection();
        boolean exists = false;
        try(var preparedStatement = conn.prepareStatement("SELECT password FROM user WHERE password=\"" + passQuery + "\"")){
            var rows = preparedStatement.executeQuery();
            while(rows.next()){
                String test = rows.getString("password");
                exists = (test != null);
            }
        }
        conn.close();
        return exists;
    }

    /**
     * Gets all users from the database.
     * READ operation
     * @return List of all users.
     * @throws DataAccessException
     */
    public Collection<User> getAllUsers() throws SQLException, DataAccessException{

        var conn = new Database().getConnection();
        ArrayList<User> DB = new ArrayList<>();

        try(var preparedStatement = conn.prepareStatement("SELECT username, password, email FROM user")){
            var rows = preparedStatement.executeQuery();
            while(rows.next()) {
                String name = rows.getString("username");
                String pass = rows.getString("password");
                String mail = rows.getString("email");
                DB.add(new User(name, pass, mail));
            }
        }
        conn.close();
        return DB;
    }
    public boolean clear() throws SQLException, DataAccessException{
        /*for (User thisUser : UserDB) {
            UserDB.remove(thisUser);
        }*/
        //UserDB = new ArrayList<>();
        //return UserDB.isEmpty();
        boolean isCleared = false;
        var conn = new Database().getConnection();
        try(var preparedStatement = conn.prepareStatement("DELETE FROM user")){
            int rows = preparedStatement.executeUpdate();
            isCleared = true;
        }

        conn.close();
        return isCleared;
    }

}
