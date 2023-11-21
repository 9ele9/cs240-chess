package responses;
/**
 * Returns a response from the server. If successful, includes a gameID.
 * Includes an error message if the game creation request was not fulfilled.
 */
public class CreateGameResponse {
    /**
     * ID of the newly created game; only valid if response was successful.
     */
    private Integer gameID;
    /**
     * Failed response message.
     */
    private String message = null;
    private transient int returnCode;
    /**
     * Constructor
     */
    public CreateGameResponse(){}

    public CreateGameResponse(int myID){
        setGameID(myID);
        setReturnCode(200);
    }

    public CreateGameResponse(String failure, int errorCode){
        setMessage(failure);
        setReturnCode(errorCode);
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
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

