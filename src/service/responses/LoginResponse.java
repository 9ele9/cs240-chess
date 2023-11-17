package service.responses;

import chess_server.AuthToken;

/**
 * Returns a response from the server.
 * If successful, includes an authtoken and a username.
 * Includes a message if the request was not fulfilled.
 */
public class LoginResponse {
    /**
     * Authtoken for user; only valid if response succeeds.
     */
    private AuthToken myAuthToken = null;
    /**
     * Username associated with the authtoken.
     */
    private String userName = null;
    /**
     * Failed response message.
     */
    private String message = null;
    private transient int returnCode;
    /**
     * Constructor
     */
    public LoginResponse(String name){
        setUserName(name);
        setAuthToken(new AuthToken(name));
        setReturnCode(200);
    }

    public LoginResponse(String failure, int error){
        setMessage(failure);
        setReturnCode(error);
    }

    public AuthToken getAuthToken() {
        return myAuthToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.myAuthToken = authToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
