package requests;

import chess.ChessGame;

/**
 * Sends a request to the server to join a game (specified by the ID) as a certain color
 */
public class JoinGameRequest {
    /**
     * Color a player wishes to join as (to be sent to the server)
     */
    private ChessGame.TeamColor playerColor = null;
    /**
     * ID of game a player wishes to join (to be sent to the server)
     */
    private int gameID;
    /**
     * Constructor
     */
    public JoinGameRequest(){}
    public JoinGameRequest(int id){
        setGameID(id);
        setPlayerColor(null);
    }
    public JoinGameRequest(String color, int id){
        if(color == null){
            setPlayerColor(null);
        }else if(color.equals("BLACK")){
            setPlayerColor(ChessGame.TeamColor.BLACK);
        }else if(color.equals("WHITE")){
            setPlayerColor(ChessGame.TeamColor.WHITE);
        }else{
            setPlayerColor(null);
        }
        setGameID(id);
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(ChessGame.TeamColor playerColor) {
        this.playerColor = playerColor;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }
}
