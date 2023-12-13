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

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {
    public WebSocketHandler(){run();}
    public void run() {
        //Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));
        //System.out.print("\ntest\n");
    }
    HashSet<Session> activeSessions = new HashSet<>();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        addNewSession(session);
        System.out.printf("Received: %s\n", message);
        UserGameCommand phoenix = getBody(message, UserGameCommand.class);
        String response = "";
        GameDAO gaming = new GameDAO();
        AuthDAO authing = new AuthDAO();
        if(phoenix.getCommandType() == UserGameCommand.CommandType.LEAVE){
            UserGameCommand.leaveCommand newCommand = getBody(message,UserGameCommand.leaveCommand.class);
            String username = authing.getUsername(newCommand.getAuthString());
            ChessGame.TeamColor ingame = isInGame(newCommand.getGameID(), newCommand.getAuthString());
            if(ingame != null){
                if(newCommand.getMyColor() == ChessGame.TeamColor.BLACK){
                    gaming.removePlayerFromGame(ChessGame.TeamColor.BLACK,newCommand.getGameID());
                }else if(newCommand.getMyColor() == ChessGame.TeamColor.WHITE){
                    gaming.removePlayerFromGame(ChessGame.TeamColor.WHITE,newCommand.getGameID());
                }
                response = new Gson().toJson(new ServerMessage.notificationMessage(username + " left the game", newCommand.getAuthString()));
                sendToAllClientsButRoot(session, response, getActiveSessions());
            }else{
                gaming.leaveSpectator(username);
                response = new Gson().toJson(new ServerMessage.notificationMessage(username + " stopped observing", newCommand.getAuthString()));
                sendToAllClientsButRoot(session, response, getActiveSessions());
            }

        }else if(phoenix.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE){
            //re-parse command
            UserGameCommand.makeMoveCommand newCommand = getBody(message,UserGameCommand.makeMoveCommand.class);
            ChessGame.TeamColor userColor = isInGame(newCommand.getGameID(), newCommand.getAuthString());
            if(userColor != null){
                //Simulate move, send error if failed, send notification of move and load game if not
                String game = gaming.getGameByID(newCommand.getGameID()).getMySerialGame();
                ChessGame.TeamColor currTurn = gaming.currTurn(newCommand.getGameID());
                boolean alreadyOver = gaming.isWinner(newCommand.getGameID());
                boolean moveSuccess = simulateMove(session, game, newCommand.getMove(), userColor, currTurn, newCommand.getGameID(), newCommand.getAuthString(), getActiveSessions());
                if(!alreadyOver && moveSuccess){
                    String loadGame = new Gson().toJson(new ServerMessage.loadGameMessage(game, newCommand.getAuthString()));
                    sendToAllClients(loadGame, getActiveSessions());
                    String playerName = authing.getUsername(newCommand.getAuthString());
                    response = new Gson().toJson(new ServerMessage.notificationMessage(playerName + " made a move: " + newCommand.getMove().toString(), newCommand.getAuthString()));
                    sendToAllClientsButRoot(session, response, getActiveSessions());
                }
                if(alreadyOver){
                    response = new Gson().toJson(new ServerMessage.errorMessage("Game is already over", newCommand.getAuthString()));
                    sendToJustClient(session, response);
                }

            }else{
                response = new Gson().toJson(new ServerMessage.errorMessage("You have not joined this game", newCommand.getAuthString()));
                sendToJustClient(session, response);
            }

        }else if(phoenix.getCommandType() == UserGameCommand.CommandType.RESIGN){
            UserGameCommand.resignCommand newCommand = getBody(message,UserGameCommand.resignCommand.class);
            //check if in game
            if(isInGame(newCommand.getGameID(), newCommand.getAuthString()) != null){
                if(gaming.isWinner(newCommand.getGameID())){
                    response = new Gson().toJson(new ServerMessage.errorMessage("Game is already over", newCommand.getAuthString()));
                    sendToJustClient(session, response);
                }else{
                    ChessGame.TeamColor otherColor;
                    if(newCommand.getMyColor() == ChessGame.TeamColor.BLACK){
                        otherColor = ChessGame.TeamColor.WHITE;
                    }else{
                        otherColor = ChessGame.TeamColor.BLACK;
                    }
                    gaming.setWinner(newCommand.getGameID(), otherColor.toString());
                    String playerName = gaming.getPlayerByColor(newCommand.getGameID(), newCommand.getMyColor());
                    response = new Gson().toJson(new ServerMessage.notificationMessage(playerName + " resigned. The winner is " + otherColor, newCommand.getAuthString()));
                    sendToAllClients(response, getActiveSessions());
                }
            }else{
                response = new Gson().toJson(new ServerMessage.errorMessage("You have not joined this game", newCommand.getAuthString()));
                sendToJustClient(session, response);
            }

            }else if(phoenix.getCommandType() == UserGameCommand.CommandType.JOIN_PLAYER){
            //re-parse command
            UserGameCommand.joinPlayerCommand newCommand = getBody(message,UserGameCommand.joinPlayerCommand.class);
            if(newCommand.getGameID()==0 || newCommand.getPlayerColor() == null || Objects.equals(newCommand.getAuthString(), "")){
                response = new Gson().toJson(new ServerMessage.errorMessage("Failed to join", newCommand.getAuthString()));
                sendToJustClient(session, response);
            }else if(canJoin(newCommand.getGameID(), newCommand.getPlayerColor(), newCommand.getAuthString())){
                //send load game response
                boolean HTTPSuccess = isInGame(newCommand.getGameID(), newCommand.getAuthString()) != null;
                if(HTTPSuccess){
                    String loadGame = gaming.findGameSerial(newCommand.getGameID());
                    String playerName = gaming.getPlayerByColor(newCommand.getGameID(), newCommand.getPlayerColor());
                    String joinGame = new Gson().toJson(new ServerMessage.notificationMessage(playerName + " joined the game as " + newCommand.getPlayerColor().toString() + ".", newCommand.getAuthString()));
                    //sendToAllClientsButRoot(session, joinGame);

                    response = new Gson().toJson(new ServerMessage.loadGameMessage(loadGame, newCommand.getAuthString()));

                    //send notification to all clients except root
                    sendToAllClientsButRoot(session, joinGame, getActiveSessions());

                    //load game for just root clients
                    sendToJustClient(session, response);
                }else{
                    response = new Gson().toJson(new ServerMessage.errorMessage("Couldn't join the game", newCommand.getAuthString()));
                    sendToJustClient(session, response);
                }
            }else{
                response = new Gson().toJson(new ServerMessage.errorMessage("Couldn't join the game", newCommand.getAuthString()));
                sendToJustClient(session, response);
            }

        }else if(phoenix.getCommandType() == UserGameCommand.CommandType.JOIN_OBSERVER){
            //re-parse command
            UserGameCommand.joinObserverCommand newCommand = getBody(message,UserGameCommand.joinObserverCommand.class);
            if(newCommand.getGameID()==0){
                response = new Gson().toJson(new ServerMessage.errorMessage("Failed to join", newCommand.getAuthString()));
                sendToJustClient(session, response);
            }else{
                //add to spectators
                gaming.spectator(newCommand.getGameID(), authing.getUsername(newCommand.getAuthString()));
                //send load game response
                try{
                    String game = gaming.getGameByID(newCommand.getGameID()).getMySerialGame();


                    String playerName = authing.getUsername(newCommand.getAuthString());
                    //send load game to root client
                    String loadGame = new Gson().toJson(new ServerMessage.loadGameMessage(game, newCommand.getAuthString()));
                    sendToJustClient(session, loadGame);
                    //send data as notification response to all but root client
                    response = new Gson().toJson(new ServerMessage.notificationMessage(playerName + " is observing the game", newCommand.getAuthString()));
                    sendToAllClientsButRoot(session, response, getActiveSessions());
                }catch(DataAccessException | IOException e){
                    response = new Gson().toJson(new ServerMessage.errorMessage("Error trying to observe", newCommand.getAuthString()));
                    sendToJustClient(session, response);
                }
            }


        }else{
            response = new Gson().toJson(new ServerMessage.errorMessage("Unknown error occurred", phoenix.getAuthString()));
            sendToJustClient(session, response);
        }

        //session.getRemote().sendString(response);

        if(phoenix.getCommandType() == UserGameCommand.CommandType.LEAVE){
            HashSet<Session> newSessions = new HashSet<>();
            for(Session sesh : activeSessions){
                if(sesh.getRemoteAddress() != session.getRemoteAddress()){
                    newSessions.add(sesh);
                }
            }
            activeSessions = newSessions;
        }
    }

    private static <T> T getBody(String request, Class<T> clazz) {
        var body = new Gson().fromJson(request, clazz);
        if (body == null) {
            throw new RuntimeException("missing required body");
        }
        return body;
    }

    public void addNewSession(Session session){
        boolean sameAddress = false;
        for(Session sesh : this.activeSessions){
            if(sesh.getRemoteAddress() == session.getRemoteAddress()){
                sameAddress = true;
                break;
            }
        }
        if(!sameAddress){
            activeSessions.add(session);
        }
    }


    private static void sendToJustClient(Session session, String jsonRequest) throws IOException {
        session.getRemote().sendString(jsonRequest);
    }

    private static void sendToAllClientsButRoot(Session session, String jsonRequest, HashSet<Session> mySessions) throws IOException {
        int numSent = 0;
        String err;
        for(Session sesh : mySessions){
            InetSocketAddress test1 = sesh.getRemoteAddress();
            InetSocketAddress test2 = session.getRemoteAddress();
            if(test1 != test2){
                try{
                    sesh.getRemote().sendString(jsonRequest);
                    numSent++;
                }catch(Exception e){
                    err = e.toString();
                    System.out.println("Sending error: " + err);
                }
            }
        }
    }
    private static void sendToAllClients(String jsonRequest, HashSet<Session> mySessions) throws IOException{
        int numSent = 0;
        String err;
        for(Session sesh : mySessions){
            try{
                sesh.getRemote().sendString(jsonRequest);
                numSent++;
            }catch(Exception e){
                err = e.toString();
                System.out.println(err);
            }
        }
    }

    public HashSet<Session> getActiveSessions() {
        return activeSessions;
    }

    private static boolean simulateMove(Session session, String serialBoard, myChessMove move, ChessGame.TeamColor myColor, ChessGame.TeamColor currColor, int gameID, String auth, HashSet<Session> mySessions) throws IOException {
        boolean didMove = false;
        ChessGame.TeamColor otherColor = null;
        myChessGame simulation = new myChessGame();
        //simulation.setTeamTurn(color);
        simulation.serialStringIntoBoard(serialBoard);
        String err = "";
        GameDAO gaming = new GameDAO();
        if(currColor == myColor){
            try{
                simulation.setTeamTurn(currColor);
                simulation.makeMove(move);
                String newSerial = simulation.getSerialGame();
                didMove = gaming.makeMove(gameID, newSerial);
                //didMove = attemptMove(gaming, gameID, newSerial);
            }catch (Exception e) {
                String err1 = e.toString();
                System.out.println(err1);
            }
        }else{
            String response = new Gson().toJson(new ServerMessage.errorMessage("It is not your turn", auth));
            sendToJustClient(session, response);
        }


        if(didMove){
            if(myColor == ChessGame.TeamColor.BLACK){
                otherColor = ChessGame.TeamColor.WHITE;
            }else{
                otherColor = ChessGame.TeamColor.BLACK;
            }

            if(simulation.isInCheck(myColor)){
                //send notification of being in check
                String meCheck = new Gson().toJson(new ServerMessage.notificationMessage(myColor.toString() + " is in check", auth));
                sendToAllClients(meCheck, mySessions);
            }
            if(simulation.isInCheckmate(myColor)){
                //send notification of being in checkmate
                String meCheckMate = new Gson().toJson(new ServerMessage.notificationMessage(myColor.toString() + " is in checkmate. The winner is " + otherColor, auth));
                GameDAO winner = new GameDAO();
                try{
                    winner.setWinner(gameID, otherColor.toString());
                    sendToAllClients(meCheckMate, mySessions);
                }catch(Exception e){
                    String failMate = new Gson().toJson(new ServerMessage.errorMessage("Attempted to declare the winner but encountered a database error.", auth));
                    session.getRemote().sendString(failMate);
                }

            }
            if(simulation.isInCheck(otherColor)){
                //send notification of being in check
                String theyCheck = new Gson().toJson(new ServerMessage.notificationMessage(otherColor + " is in check", auth));
                sendToAllClients(theyCheck, mySessions);
            }
            if(simulation.isInCheckmate(otherColor)){
                //send notification of being in checkmate
                String theyCheckMate = new Gson().toJson(new ServerMessage.notificationMessage(otherColor + " is in checkmate. The winner is " + myColor.toString(), auth));
                GameDAO winner = new GameDAO();
                try{
                    winner.setWinner(gameID, myColor.toString());
                    sendToAllClients(theyCheckMate, mySessions);
                }catch(Exception e){
                    String failMate = new Gson().toJson(new ServerMessage.errorMessage("Attempted to declare the winner but encountered a database error.", auth));
                    session.getRemote().sendString(failMate);
                }


            }
        }


        return didMove;
    }

    public ChessGame.TeamColor isInGame(int gameID, String auth){
        AuthDAO authing = new AuthDAO();
        GameDAO gaming = new GameDAO();
        String authedUser = "";

        ChessGame.TeamColor color = null;
        try{
            authedUser = authing.getUsername(auth);
            if(authedUser.isEmpty()){

            }else{
                color = gaming.isPlayerInGame(gameID, authedUser);
            }
        }catch(Exception e){
            String err = e.toString();
            System.out.println(err);
        }
        return color;

    }


    public boolean canJoin(int gameID, ChessGame.TeamColor color, String auth){
        GameDAO gaming = new GameDAO();
        AuthDAO authing = new AuthDAO();

        try{
            String name = authing.getUsername(auth);
            return gaming.canPlayerJoin(gameID, color, name);
        }catch(Exception e){
            String test = e.toString();
            return false;
        }
    }

    public static boolean attemptMove(GameDAO gaming, int gameID, String serialGame) throws SQLException, DataAccessException {
        return gaming.makeMove(gameID, serialGame);
    }
    @OnWebSocketError
    public void Error(Throwable error) {
        System.out.println(error.getMessage());

    }

}