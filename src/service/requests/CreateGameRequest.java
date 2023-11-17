package service.requests;

/**
 * Sends a request to the server with a request to create a game with the specified name.
 */
public class CreateGameRequest {
    /**
     * Name of the game to be sent to the server
     */
    private String gameName = null;

    /**
     * Constructor
     */
    public CreateGameRequest(){}

    public CreateGameRequest(String name){
        setGameName(name);
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
