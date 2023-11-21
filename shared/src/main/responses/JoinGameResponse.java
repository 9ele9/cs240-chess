package responses;

/**
 * Returns a response from the server.
 * Includes a message if the request was not fulfilled.
 */
public class JoinGameResponse{
    /**
     * Failed response message.
     */
    private String message;
    private transient int returnCode;
    /**
     * Constructor
     */
    public JoinGameResponse(){
        setReturnCode(200);
    }

    public JoinGameResponse(String failure, int error){
        setMessage(failure);
        setReturnCode(error);
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
