package responses;

import chess_server.Game;

import java.util.HashSet;

/**
 * Returns a result from the server.
 * If successful, includes a list of games.
 * Includes a message if the request was not fulfilled.
 */
public class ListGameResponse {
    /**
     * List of games in the database; only valid if response is successful.
     */
    private HashSet<Game> games;
    /**
     * Failed response message.
     */
    private String message;
    private transient int returnCode;
    /**
     * Constructor
     */
    public ListGameResponse(){}

    public ListGameResponse(HashSet<Game> gameDB){
        setGames(gameDB);
        setReturnCode(200);
    }

    public ListGameResponse(String failure, int error){
        setMessage(failure);
        setReturnCode(error);
    }

    public HashSet<Game> getGames() {
        return games;
    }

    public void setGames(HashSet<Game> games) {
        this.games = games;
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
