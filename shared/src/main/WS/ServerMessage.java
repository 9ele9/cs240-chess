package WS;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    ServerMessageType serverMessageType;

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    public ServerMessage(ServerMessageType type) {
        this.serverMessageType = type;
    }
    public ServerMessage(){};

    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }
    protected String authToken;

    public String getAuthToken() {
        return authToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ServerMessage))
            return false;
        ServerMessage that = (ServerMessage) o;
        return getServerMessageType() == that.getServerMessageType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }

    public static class loadGameMessage extends ServerMessage{
        public loadGameMessage(){
            serverMessageType = ServerMessageType.LOAD_GAME;
        }
        public loadGameMessage(String serialGame, String auth){
            serverMessageType = ServerMessageType.LOAD_GAME;
            game = serialGame;
            authToken = auth;
        }
        String game = "";


        public String getGame() {
            return game;
        }

        @Override
        public String toString() {
            return "loadGameMessage{" +
                    "game='" + game + '\'' +
                    ", serverMessageType=" + serverMessageType +
                    '}';
        }
    }

    public static class errorMessage extends ServerMessage{
        public errorMessage(){
            serverMessageType = ServerMessageType.ERROR;
        }
        public errorMessage(String message, String auth){
            serverMessageType = ServerMessageType.ERROR;
            errorMessage = message;
            authToken = auth;
        }
        String errorMessage = "";

        public String getErrorMessage() {
            return errorMessage;
        }

        @Override
        public String toString() {
            return "errorMessage{" +
                    "errorMessage='" + errorMessage + '\'' +
                    ", serverMessageType=" + serverMessageType +
                    '}';
        }
    }

    public static class notificationMessage extends ServerMessage{
        public notificationMessage(){
            serverMessageType = ServerMessageType.NOTIFICATION;
        }
        public notificationMessage(String aMessage, String auth){
            serverMessageType = ServerMessageType.NOTIFICATION;
            message = aMessage;
            authToken = auth;
        }
        String message = "";

        public String getMessage() {
            return message;
        }

        @Override
        public String toString() {
            return message;
        }
    }
}
