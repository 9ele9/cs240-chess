package service;

import chess.ChessGame;
import chess_server.User;
import dataAccess.*;
import service.requests.JoinGameRequest;
import service.responses.JoinGameResponse;

import java.sql.SQLException;

/**
 * Verifies that the specified game exists, and, if a color is specified, adds the caller as the requested color to the game.
 * If no color is specified the user is joined as an observer. This request is idempotent.
 */
public class JoinGameService {
    /**
     * Verifies that the specified game exists, and, if a color is specified, adds the caller as the requested color to the game.
     * If no color is specified the user is joined as an observer. This request is idempotent.
     */
    public JoinGameResponse joinGame(JoinGameRequest request, String token) throws SQLException, DataAccessException {
        GameDAO games = new GameDAO();
        AuthDAO auths = new AuthDAO();
        UserDAO player = new UserDAO();
        //bad request if no game ID
        if(games.getGameByID(request.getGameID()) == null || request.getGameID() == 0){
            return new JoinGameResponse("Error: bad request", 400);
        }
        //unauthorized if wrong or empty token
        if(!auths.validate(token) || token.isEmpty()){
            return new JoinGameResponse("Error: unauthorized", 401);
        }
        //joining
        if(request.getPlayerColor() == null){
            String username = auths.getUsername(token);
            int myID = request.getGameID();
            games.getGameByID(myID).addSpectator(username);
            games.spectator(myID, username);
            return new JoinGameResponse();
        }

        //adding as black
        if(request.getPlayerColor() == ChessGame.TeamColor.BLACK){
            if(auths.getUsername(token).equals(games.getGameByID(request.getGameID()).getBlackUsername())){
                return new JoinGameResponse();
            }else if(games.getGameByID(request.getGameID()).getBlackUsername().isEmpty()){
                games.getGameByID(request.getGameID()).setBlackUsername(auths.getUsername(token));
                games.pickColor(auths.getUsername(token), ChessGame.TeamColor.BLACK, request.getGameID());
                return new JoinGameResponse();
            }else{
                return new JoinGameResponse("Error: already taken", 403);
            }
            //adding as white
        }else if(request.getPlayerColor() == ChessGame.TeamColor.WHITE){
            if(auths.getUsername(token).equals(games.getGameByID(request.getGameID()).getWhiteUsername())){
                return new JoinGameResponse();
            }else if(games.getGameByID(request.getGameID()).getWhiteUsername().isEmpty()){
                games.getGameByID(request.getGameID()).setWhiteUsername(auths.getUsername(token));
                games.pickColor(auths.getUsername(token), ChessGame.TeamColor.WHITE, request.getGameID());
                return new JoinGameResponse();
            }else{
                return new JoinGameResponse("Error: already taken", 403);
            }
        }else{
            return new JoinGameResponse("Error: unauthorized", 401);
        }
    }
}
