package service.responses;

import chess_server.AuthToken;
import chess_server.User;

/**
 * Returns a response from the server.
 * If successful, includes an authtoken and a username.
 * Includes a message if the request was not fulfilled.
 */
public class RegisterResponse {
    /**
     * Username for new user; only valid if response is successful.
     */
    private String username = null;
    /**
     * Authtoken associated with the username; only valid if response is successful.
     */
    private AuthToken myAuthToken = null;
    /**
     * Failed response message.
     */
    private String message = null;
    private transient int returnCode;
    /**
     * Constructor
     */
    public RegisterResponse(){}

    public RegisterResponse(String name){
        setUsername(name);
        setReturnCode(200);
        setAuthToken(new AuthToken(name));
    }

    public RegisterResponse(String failure, int ErrorCode){
        message = failure;
        setReturnCode(ErrorCode);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public AuthToken getAuthToken() {
        return myAuthToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.myAuthToken = authToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }
}
