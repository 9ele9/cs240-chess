package server.handlers;
import WS.*;
import chess.ChessGame;
import chess.InvalidMoveException;
import chess.myChessGame;
import chess.myChessMove;
import chess_server.Game;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import spark.Request;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {
    public WebSocketHandler(){run();}
    public void run() {
        //Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));
        //System.out.print("\ntest\n");
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        System.out.printf("Received: %s\n", message);
        UserGameCommand phoenix = getBody(message, UserGameCommand.class);
        String response = "";
        GameDAO gameing = new GameDAO();
        AuthDAO authing = new AuthDAO();
        if(phoenix.getCommandType() == UserGameCommand.CommandType.LEAVE){
            UserGameCommand.leaveCommand newCommand = getBody(message,UserGameCommand.leaveCommand.class);
            String username = authing.getUsername(newCommand.getAuthString());
            if(newCommand.getMyColor() == ChessGame.TeamColor.BLACK){
                gameing.removePlayerFromGame(ChessGame.TeamColor.BLACK,newCommand.getGameID());
            }else if(newCommand.getMyColor() == ChessGame.TeamColor.WHITE){
                gameing.removePlayerFromGame(ChessGame.TeamColor.WHITE,newCommand.getGameID());
            }

            response = new Gson().toJson(new ServerMessage.notificationMessage(username + " left the game"));

        }else if(phoenix.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE){
            //re-parse command
            UserGameCommand.makeMoveCommand newCommand = getBody(message,UserGameCommand.makeMoveCommand.class);
            //Simulate move, send error if failed, send notification of move and load game if not
            if(simulateMove(session, gameing.getGameByID(newCommand.getGameID()).getMySerialGame(), newCommand.getMove(), newCommand.getMyColor(), newCommand.getGameID())){
                String loadGame = gameing.getGameByID(newCommand.getGameID()).getMySerialGame();
                session.getRemote().sendString(new Gson().toJson(new ServerMessage.loadGameMessage(loadGame)));
                String playerName = authing.getUsername(newCommand.getAuthString());
                response = new Gson().toJson(new ServerMessage.notificationMessage(playerName + " made a move: " + newCommand.getMove().toString()));
            }
        }else if(phoenix.getCommandType() == UserGameCommand.CommandType.RESIGN){

            response = new Gson().toJson(new ServerMessage.notificationMessage("User resigned"));
        }else if(phoenix.getCommandType() == UserGameCommand.CommandType.JOIN_PLAYER){
            //re-parse command
            UserGameCommand.joinPlayerCommand newCommand = getBody(message,UserGameCommand.joinPlayerCommand.class);
            if(newCommand.getGameID()==0 || newCommand.getPlayerColor() == null || Objects.equals(newCommand.getAuthString(), "")){
                response = new Gson().toJson(new ServerMessage.errorMessage("Failed to join"));
            }else{
                //send load game response
                String loadGame = gameing.getGameByID(newCommand.getGameID()).getMySerialGame();
                session.getRemote().sendString(new Gson().toJson(new ServerMessage.loadGameMessage(loadGame)));
                //send data as notification response
                String playerName = gameing.getPlayerByColor(newCommand.getGameID(), newCommand.getPlayerColor());
                response = new Gson().toJson(new ServerMessage.notificationMessage(playerName + " joined the game as " + newCommand.getPlayerColor().toString() + "."));
            }
        }else if(phoenix.getCommandType() == UserGameCommand.CommandType.JOIN_OBSERVER){
            //re-parse command
            UserGameCommand.joinObserverCommand newCommand = getBody(message,UserGameCommand.joinObserverCommand.class);
            if(newCommand.getGameID()==0){
                response = new Gson().toJson(new ServerMessage.errorMessage("Failed to join"));
            }else{
                //send load game response
                String loadGame = gameing.getGameByID(newCommand.getGameID()).getMySerialGame();
                session.getRemote().sendString(new Gson().toJson(new ServerMessage.loadGameMessage(loadGame)));
                //send data as notification response
                String playerName = authing.getUsername(newCommand.getAuthString());
                response = new Gson().toJson(new ServerMessage.notificationMessage(playerName + " is observing the game."));
            }


        }else{
            response = new Gson().toJson(new ServerMessage.errorMessage("Unknown error occurred"));
        }
        session.getRemote().sendString(response);
    }

    private static <T> T getBody(String request, Class<T> clazz) {
        var body = new Gson().fromJson(request, clazz);
        if (body == null) {
            throw new RuntimeException("missing required body");
        }
        return body;
    }

    private static boolean simulateMove(Session session, String serialBoard, myChessMove move, ChessGame.TeamColor color, int gameID) throws IOException {
        boolean didMove = false;
        myChessGame simulation = new myChessGame();
        simulation.setTeamTurn(color);
        simulation.serialStringIntoBoard(serialBoard);
        if(!simulation.isInStalemate(color)){
            try{
                simulation.makeMove(move);
                String newSerial = simulation.getSerialGame();
                GameDAO gaming = new GameDAO();
                gaming.makeMove(gameID, newSerial, color);
                didMove = true;
            }catch (InvalidMoveException | DataAccessException | SQLException e) {
                String response = new Gson().toJson(new ServerMessage.errorMessage(e.toString()));
                session.getRemote().sendString(response);
            }
        }else{
            String stalemate = new Gson().toJson(new ServerMessage.errorMessage(color.toString() + " is in stalemate."));
            session.getRemote().sendString(stalemate);
        }
        return didMove;
    }

}