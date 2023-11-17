package service.responses;
/**
 * Returns a response from the server.
 * Includes a message if the request was not fulfilled.
 */
public class LogoutResponse {
    /**
     * Failed response message.
     */
    private String message;
    private transient int returnCode;
    /**
     * Constructor
     */
    public LogoutResponse(){
        setReturnCode(200);
    }

    public LogoutResponse(String failure, int error){
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
