package service;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import service.requests.LoginRequest;
import service.responses.LoginResponse;

import java.sql.SQLException;

/**
 * Logs in an existing user (returns a new authToken).
 */
public class LoginService {
    /**
     * Logs in an existing user (returns a new authToken).
     */
    public LoginResponse login(LoginRequest request) throws SQLException, DataAccessException {
        if(request.getPassword() == null || request.getUsername() == null){
            return new LoginResponse("Error: description", 500);
        }
        UserDAO addUser = new UserDAO();
        if(addUser.checkExists(request.getUsername()) && !addUser.samePass(request.getPassword())){
            return new LoginResponse("Error: unauthorized", 401);
        }else if(addUser.checkExists(request.getUsername()) && addUser.samePass(request.getPassword())){
            LoginResponse validUser = new LoginResponse(request.getUsername());
            AuthDAO addAuthToken = new AuthDAO();
            addAuthToken.addAuth(validUser.getAuthToken());
            return validUser;
        }
        return new LoginResponse("Error: unauthorized", 401);

    }

}
