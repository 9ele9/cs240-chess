package chess_server;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;

/**
 * An authtoken. Represents a logged-in user, so credentials don't have to be constantly re-sent.
 */
public class AuthToken {
    /**
     * The token.
     */
    String username;
    String authToken;
    /**
     * Username associated with the token.
     */


    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe

    public AuthToken(String userName) {

        this.username = userName;
        authToken = generateToken();
    }

    public AuthToken(String userName, String token) {
        this.username = userName;
        this.authToken = token;
    }

    /**
     * Makes an auth token.
     */
    public String generateToken(){
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    public String getTokenValue() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthToken authToken1 = (AuthToken) o;
        return Objects.equals(username, authToken1.username) && Objects.equals(authToken, authToken1.authToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, authToken);
    }
}
