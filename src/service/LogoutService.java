package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import responses.LogoutResponse;

import java.sql.SQLException;

/**
 * Logs out the user represented by the authToken.
 */
public class LogoutService {
    /**
     * Logs out the user represented by the authToken.
     */
    public LogoutResponse logout(String token) throws SQLException, DataAccessException {
        if(token == null || token.isEmpty()){
            return new LogoutResponse("Error: unauthorized", 401);
        }
        AuthDAO removeToken = new AuthDAO();
        if(removeToken.validate(token)){
            if(removeToken.deleteAuth(token)){
                return new LogoutResponse();
            }else{
                return new LogoutResponse("Error: unauthorized", 401);
            }
        }
        return new LogoutResponse("Error: unauthorized", 401);
    }
}
