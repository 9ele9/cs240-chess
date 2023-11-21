package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import responses.ListGameResponse;

import java.sql.SQLException;

/**
 * Gives a list of all games.
 */
public class ListGamesService {
    /**
     * Gives a list of all games.
     */
    public ListGameResponse listGames(String token) throws SQLException, DataAccessException {
        if(token.isEmpty()){
            return new ListGameResponse("Error: description", 500);
        }
        AuthDAO checkLoggedIn = new AuthDAO();
        if(!checkLoggedIn.validate(token)){
            return new ListGameResponse("Error: unauthorized", 401);
        }else{
            GameDAO newGame = new GameDAO();
            return new ListGameResponse(newGame.getAllGames());
        }

    }
}
