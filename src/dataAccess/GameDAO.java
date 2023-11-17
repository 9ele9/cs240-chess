package dataAccess;

import chess.ChessGame;
import chess.myChessGame;
import chess_server.AuthToken;
import chess_server.Game;
import chess_server.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Does CRUD operations on a game.
 */
public class GameDAO {
    static HashSet<Game> GameDB = new HashSet<>();
    /**
     * makes a new game, adds it to the database
     * CREATE operation
     */
    public void addGame(String name) throws SQLException, DataAccessException {
        var conn = new Database().getConnection();
        boolean isAdded;
        Game initializeGame = new Game(name);
        try(var preparedStatement = conn.prepareStatement("INSERT INTO game VALUES (" + initializeGame.getGameID() + ", NULL, NULL, \"" + name + "\", \"" + initializeGame.getMySerialGame() + "\")")){
              var rows = preparedStatement.executeUpdate();
            isAdded = rows > 0;

        }
        conn.close();

    }

    /**
     * retrieves game from database
     * READ operation
     * @param ID ID of game to be retrieved
     * @return Game that is retrieved
     */
    public Game getGameByID(int ID) throws SQLException, DataAccessException {
        var conn = new Database().getConnection();
        Game myGame = new Game();
        try(var preparedStatement = conn.prepareStatement("SELECT id, black, white, name, game FROM game WHERE id=\"" + ID + "\"")){
            var rows = preparedStatement.executeQuery();
            while(rows.next()){
                int ident = rows.getInt("id");
                String black = rows.getString("black");
                String white = rows.getString("white");
                String name = rows.getString("name");
                String gem = rows.getString("game");
                myGame.setGameName(name);
                myGame.setWhiteUsername(white);
                myGame.setBlackUsername(black);
                myGame.setGameID(ident);
                myGame.gameStringToObject(gem);

            }
        }
        conn.close();
        return myGame;
    }

    public Game getGameByName(String name) throws DataAccessException, SQLException {
        var conn = new Database().getConnection();
        Game myGame = new Game();
        try(var preparedStatement = conn.prepareStatement("SELECT id, black, white, name, game FROM game WHERE name=\"" + name + "\"")){
            var rows = preparedStatement.executeQuery();
            while(rows.next()){
                int id = rows.getInt("id");
                String black = rows.getString("black");
                String white = rows.getString("white");
                String myName = rows.getString("name");
                String gem = rows.getString("game");
                myGame.setGameName(myName);
                myGame.setWhiteUsername(white);
                myGame.setBlackUsername(black);
                myGame.setGameID(id);
                myGame.gameStringToObject(gem);

            }
        }
        conn.close();
        return myGame;
    }

    /**
     * retrieves a list of all games in the database
     * READ operation
     * @return Collection of all games
     */
    public HashSet<Game> getAllGames() throws DataAccessException, SQLException {
        var conn = new Database().getConnection();
        HashSet<Game> DB = new HashSet<>();

        try(var preparedStatement = conn.prepareStatement("SELECT id, black, white, name, game FROM game")){
            var rows = preparedStatement.executeQuery();
            while(rows.next()) {
                int id = rows.getInt("id");
                String black = rows.getString("black");
                String white = rows.getString("white");
                String myName = rows.getString("name");
                String gem = rows.getString("game");
                Game myGame = new Game();
                myGame.setGameName(myName);
                myGame.setWhiteUsername(white);
                myGame.setBlackUsername(black);
                myGame.setGameID(id);
                myGame.gameStringToObject(gem);
                DB.add(myGame);
            }
        }
        conn.close();
        return DB;
    }

    /**
     * sets a given player as a certain color in a given game
     * @param username name
     * @param gameID ID of game
     * @throws DataAccessException
     */
    public void pickColor(String username, ChessGame.TeamColor color, int gameID) throws DataAccessException, SQLException {
        var conn = new Database().getConnection();
        boolean isCreated;


        if(color == ChessGame.TeamColor.BLACK){
            try(var preparedStatement = conn.prepareStatement("UPDATE game SET black =\"" + username + "\" WHERE id =" + gameID)){
                var rows = preparedStatement.executeUpdate();
                isCreated = rows > 0;

            }
        }else if(color == ChessGame.TeamColor.WHITE){
            try(var preparedStatement = conn.prepareStatement("UPDATE game SET white =\"" + username + "\" WHERE id =" + gameID)){
                var rows = preparedStatement.executeUpdate();
                isCreated = rows > 0;

            }
        }
        conn.close();
    }
    /*
    /**
     * deletes a given game from the database
     * DELETE operation
     * @param toDelete Game that is being deleted
     * @throws DataAccessException
     */
    /*
    public void deleteGame(Game toDelete) throws DataAccessException{
    }

     */
    /*
    /**
     * updates a given game
     * UPDATE operation
     * @param toEdit Game that is being updated
     * @param newID ID to set
     * @throws DataAccessException
     */
    /*
    public void updateGame(Game toEdit, String newID) throws DataAccessException{
    }
    */

    /**
     * deletes the whole database
     * DELETE operation
     */
    public boolean clear() throws DataAccessException, SQLException {
        boolean isCleared = false;
        var conn = new Database().getConnection();
        try(var preparedStatement = conn.prepareStatement("DELETE FROM game")){
            int rows = preparedStatement.executeUpdate();
            isCleared = true;
        }
        conn.close();
        return isCleared;
    }

    public boolean checkExistsByID(int idQuery) throws SQLException, DataAccessException {
        var conn = new Database().getConnection();
        boolean exists = false;
        try(var preparedStatement = conn.prepareStatement("SELECT name FROM game WHERE id=\"" + idQuery + "\"")){
            var rows = preparedStatement.executeQuery();
            while(rows.next()){
                String test = rows.getString("name");
                exists = (test != null);
            }
        }
        conn.close();
        return exists;
    }

    public boolean checkExistsByName(String nameQuery) throws DataAccessException, SQLException {
        var conn = new Database().getConnection();
        boolean exists = false;
        try(var preparedStatement = conn.prepareStatement("SELECT name FROM game WHERE name=\"" + nameQuery + "\"")){
            var rows = preparedStatement.executeQuery();
            while(rows.next()){
                String test = rows.getString("name");
                exists = (test != null);
            }
        }
        conn.close();
        return exists;
    }

    public void spectator(int id, String username) throws SQLException, DataAccessException{
        var conn = new Database().getConnection();
        boolean isCreated = false;
        try(var preparedStatement = conn.prepareStatement("INSERT INTO spectators VALUES (\"" + id + "\", \"" + username + "\")")){
            var rows = preparedStatement.executeUpdate();
            isCreated = rows > 0;
        }
        conn.close();
    }

}
