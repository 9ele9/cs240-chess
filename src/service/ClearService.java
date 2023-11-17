package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import service.responses.ClearResponse;

import java.sql.SQLException;

/**
 * Clears the database. Removes all users, games, and authTokens.
 */
public class ClearService {
    /**
     * Clears the database. Removes all users, games, and authTokens.
     */
    public ClearResponse clear() throws SQLException, DataAccessException {
        if(new UserDAO().clear() && new GameDAO().clear() && new AuthDAO().clear()){
            return new ClearResponse();
        }else{
            return new ClearResponse("Error: description");
        }

    }
}
