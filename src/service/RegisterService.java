package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import requests.RegisterRequest;
import responses.RegisterResponse;

import java.sql.SQLException;
import java.util.Objects;

/**
 * Register a new user.
 */
public class RegisterService {
    /**
     * Register a user.
     */
    public RegisterService(){}

    public RegisterResponse register(RegisterRequest request) throws DataAccessException, SQLException {
        //calls the UserDAO to add the information in "request" to the database.
        //if it succeeds, return a response with the name and authtoken
        //if not, return a response with failure message
        if(request.getEmail() == null || request.getPassword() == null || request.getUsername() == null){
            return new RegisterResponse("Error: bad request", 400);
        }

        if(Objects.equals(request.getEmail(), "") || Objects.equals(request.getPassword(), "") || Objects.equals(request.getUsername(), "")){
            return new RegisterResponse("Error: bad request", 400);
        }
        UserDAO addUser = new UserDAO();
        if(addUser.checkExists(request.getUsername())){
            return new RegisterResponse("Error: already taken", 403);
        }
        if(!addUser.CreateUser(request.getUsername(), request.getPassword(), request.getEmail())){
            return new RegisterResponse("Error: description", 500);
        }
        RegisterResponse validUser = new RegisterResponse(request.getUsername());
        AuthDAO addAuthToken = new AuthDAO();
        addAuthToken.addAuth(validUser.getAuthToken());
        return validUser;

    }

}
