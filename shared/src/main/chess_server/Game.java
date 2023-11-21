package chess_server;

import chess.myChessGame;

import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A chess game. Stores the game ID, usernames of both colors, and the game name.
 */
public class Game {
    /**
     * The ID of the game.
     */
    int gameID;
    /**
     * The username of the white side.
     */
    String whiteUsername = null;
    /**
     * The username of the black side.
     */
    String blackUsername = null;
    /**
     * The name of the game.
     */
    String gameName = "";
    /**
     * The chess game object.
     */
    private transient myChessGame game;
    private transient String mySerialGame = "";

    private transient HashSet<String> spectators = new HashSet<>();

    public Game() {
        this.game = new myChessGame();
        this.gameID = generateID();
        this.mySerialGame = game.getSerialGame();
    }

    public Game(String name) {
        this.game = new myChessGame();
        setGameName(name);
        this.gameID = generateID();
        this.mySerialGame = game.getSerialGame();
    }

    /**
     * Initializing constructor to set usernames and game name.
     * @param whiteUsername Username of the white side.
     * @param blackUsername Username of the black side.
     * @param gameName Name of the game.
     */
    public Game(String whiteUsername, String blackUsername, String gameName) {
        this.gameID = generateID();
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
        this.game = new myChessGame();
        this.mySerialGame = game.getSerialGame();
    }

    public Game(int id, String gameName) {
        this.gameID = id;
        this.gameName = gameName;
        this.game = new myChessGame();
        this.mySerialGame = game.getSerialGame();
    }

    /**
     * Generates a unique ID for a game.
     * @return An ID
     */
    public int generateID(){
        int number = ThreadLocalRandom.current().nextInt(1000, 9999 + 1);
        return number;
    }

    public int rerollID(int compareID){
        int newID = getGameID();
        while(compareID == newID){
            newID = generateID();
        }
        return newID;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getWhiteUsername() {

        return Objects.requireNonNullElse(whiteUsername, "");

    }

    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }

    public String getBlackUsername() {
        return Objects.requireNonNullElse(blackUsername, "");
    }

    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }

    public void addSpectator(String u){
        spectators.add(u);
    }

    public String getMySerialGame() {
        return game.getSerialGame();
    }

    public void setMySerialGame(String mySerialGame) {
        this.mySerialGame = mySerialGame;
    }

    public void gameStringToObject(String mySerialGame){
        game.serialStringIntoBoard(mySerialGame);
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameID=" + gameID +
                ", whiteUsername='" + whiteUsername + '\'' +
                ", blackUsername='" + blackUsername + '\'' +
                ", gameName='" + gameName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game1 = (Game) o;
        return gameID == game1.gameID && Objects.equals(whiteUsername, game1.whiteUsername) && Objects.equals(blackUsername, game1.blackUsername) && Objects.equals(gameName, game1.gameName) && Objects.equals(game, game1.game) && Objects.equals(spectators, game1.spectators);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameID, whiteUsername, blackUsername, gameName, game, spectators);
    }
}
