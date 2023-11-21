package responses;

/**
 * Returns a response from the server.
 * Includes an error message if the clear request was not fulfilled.
 */
public class ClearResponse {
    /**
     * Failed response message.
     */
    private String message;
    private transient int returnCode;
    /**
     * Constructor
     */
    public ClearResponse(){
        setReturnCode(200);
    }

    public ClearResponse(String failure){
        setMessage(failure);
        setReturnCode(500);
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
