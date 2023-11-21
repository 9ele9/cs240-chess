package requests;

import chess_server.User;

/**
 * Sends a request to the server to generate an authtoken based on the submitted credentials (username and password).
 */
public class LoginRequest {
    /**
     * Username to be sent to the server for validation.
     */
    private String username = null;
    /**
     * Password to be sent to the server for validation
     */
    private String password = null;
    /**
     * Constructor
     */
    public LoginRequest(){}

    public LoginRequest(User info){
        setUsername(info.getUsername());
        setPassword(info.getPassword());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
