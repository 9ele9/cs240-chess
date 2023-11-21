package requests;

import chess_server.User;

/**
 * Sends a request to the server to create a new user with the specified username, password, and email.
 */
public class RegisterRequest {
    /**
     * Username to be sent to the server for registry
     */
    private String username = null;
    /**
     * User password to be sent to the server for registry
     */
    private String password = null;
    /**
     * User email address to be sent to the server for registry
     */
    private String email = null;
    /**
     * Constructor
     */
    public RegisterRequest(){}

    public RegisterRequest(String name, String pass, String mail){
        username = name;
        password = pass;
        email = mail;
    }
    public RegisterRequest(User info){
        setUsername(info.getUsername());
        setPassword(info.getPassword());
        setEmail(info.getEmail());
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
