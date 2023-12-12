package WS;

import chess.ChessGame;
import chess.myChessMove;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class UserGameCommand {

    public UserGameCommand(String authToken) {
        this.authToken = authToken;
    }
    public UserGameCommand(){}

    public enum CommandType {
        JOIN_PLAYER,
        JOIN_OBSERVER,
        MAKE_MOVE,
        LEAVE,
        RESIGN
    }

    protected CommandType commandType;

    protected String authToken;

    public String getAuthString() {
        return authToken;
    }

    public CommandType getCommandType() {
        return this.commandType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserGameCommand))
            return false;
        UserGameCommand that = (UserGameCommand) o;
        return getCommandType() == that.getCommandType() && Objects.equals(getAuthString(), that.getAuthString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommandType(), getAuthString());
    }

    public static class joinPlayerCommand extends UserGameCommand{
        public joinPlayerCommand(){
            commandType = CommandType.JOIN_PLAYER;
        }
        public joinPlayerCommand(int id, ChessGame.TeamColor color){
            commandType = CommandType.JOIN_PLAYER;
            gameID = id;
            playerColor = color;
        }
        int gameID = 0;
        ChessGame.TeamColor playerColor = null;

        public int getGameID() {
            return gameID;
        }

        public ChessGame.TeamColor getPlayerColor() {
            return playerColor;
        }

        @Override
        public String toString() {
            return "joinPlayerCommand{" +
                    "gameID=" + gameID +
                    ", playerColor=" + playerColor +
                    '}';
        }
    }

    public static class joinObserverCommand extends UserGameCommand{
        public joinObserverCommand(){
            commandType = CommandType.JOIN_OBSERVER;
        }
        public joinObserverCommand(int id, String auth){
            commandType = CommandType.JOIN_OBSERVER;
            gameID = id;
            authToken = auth;
        }
        int gameID = 0;

        public int getGameID() {
            return gameID;
        }

        @Override
        public String toString() {
            return "joinObserverCommand{" +
                    "gameID=" + gameID +
                    '}';
        }
    }

    public static class makeMoveCommand extends UserGameCommand{
        public makeMoveCommand(){
            commandType = CommandType.MAKE_MOVE;
            authToken = null;
        }
        public makeMoveCommand(myChessMove aMove, int ID, String auth, ChessGame.TeamColor color){
            commandType = CommandType.MAKE_MOVE;
            move = aMove;
            gameID = ID;
            authToken = auth;
            myColor = color;
        }
        int gameID = 0;
        myChessMove move;
        ChessGame.TeamColor myColor;

        public myChessMove getMove() {
            return move;
        }

        public ChessGame.TeamColor getMyColor() {
            return myColor;
        }

        public int getGameID() {
            return gameID;
        }

        @Override
        public String toString() {
            return "makeMoveCommand{" +
                    "gameID=" + gameID +
                    ", move=" + move +
                    '}';
        }
    }

    public static class leaveCommand extends UserGameCommand{
        public leaveCommand(){
            commandType = CommandType.LEAVE;
        }
        public leaveCommand(int id, ChessGame.TeamColor color, String auth){
            commandType = CommandType.LEAVE;
            gameID = id;
            myColor = color;
            authToken = auth;
        }
        int gameID = 0;
        ChessGame.TeamColor myColor;

        public ChessGame.TeamColor getMyColor() {
            return myColor;
        }

        @Override
        public String toString() {
            return "leaveCommand{" +
                    "gameID=" + gameID +
                    '}';
        }

        public int getGameID() {
            return gameID;
        }
    }

    public static class resignCommand extends UserGameCommand{
        public resignCommand(){
            commandType = CommandType.RESIGN;
        }
        int gameID = 0;

        @Override
        public String toString() {
            return "resignCommand{" +
                    "gameID=" + gameID +
                    '}';
        }
    }
}


