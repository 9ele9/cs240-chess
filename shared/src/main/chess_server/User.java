package chess_server;

import java.util.Objects;

/**
 * A user. Stores username, password, and email.
 */
public class User {
    /**
     * Username of the user.
     */
    String username;
    /**
     * Password of the user.
     */
    String password;
    /**
     * Email of the user.
     */
    String email;

    public User() {}

    /**
     * Initializing constructor with all user info.
     * @param username User's username.
     * @param password User's password.
     * @param email User's email.
     */
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, email);
    }
}
