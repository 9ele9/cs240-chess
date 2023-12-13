import WS.ServerMessage;
import chess.*;
import chess_server.Game;
import com.google.gson.Gson;
import ui.EscapeSequences;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import static java.lang.Character.isUpperCase;

public class clientFunctions {
    private static final serverFacade facade = new serverFacade();

    public static String parseServerMessage(ServerMessage genericMessage, String jsonString){
        String message = "";
        if(genericMessage.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION){
            ServerMessage.notificationMessage notification = new Gson().fromJson(jsonString, ServerMessage.notificationMessage.class);
            message = notification.getMessage();
        }else if(genericMessage.getServerMessageType() == ServerMessage.ServerMessageType.ERROR){
            ServerMessage.errorMessage error = new Gson().fromJson(jsonString, ServerMessage.errorMessage.class);
            message = error.getErrorMessage();
        }else if(genericMessage.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME){
            ServerMessage.loadGameMessage load = new Gson().fromJson(jsonString, ServerMessage.loadGameMessage.class);
            message = load.getGame();
        }
        return message;
    }
    public static void printInputError(){
        System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
        System.out.print("Invalid input.\n");
        System.out.print("\u001b[39;49m");
    }

    public static void printHelpMenu(boolean login){
        if(!login){
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
            System.out.print("""
                        HELP MENU
                        Commands
                        """);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_YELLOW);
            System.out.print("HELP : get list of commands\n");
            System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
            System.out.print("QUIT : exit this program\n");
            System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
            System.out.print("LOGIN [username] [password] : play chess\n");
            System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            System.out.print("REGISTER [username] [password] [email] : create account\n");
            System.out.print("\u001b[38;5;39m");
        }else{
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
            System.out.print("""
                        HELP MENU
                        Commands
                        """);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_YELLOW);
            System.out.print("HELP : get list of commands\n");
            System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
            System.out.print("LOGOUT : log out of your account\n");
            System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
            System.out.print("CREATE [name] : make a game\n");
            System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            System.out.print("LIST : list all games\n");
            System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
            System.out.print("JOIN [WHITE|BLACK|none] [ID]: join a game\n");
            System.out.print(EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY);
            System.out.print("OBSERVE [ID]: view a game\n");
            System.out.print("\u001b[38;5;39m\n");
        }
    }

    public static String getABoard(int ID, String auth) throws Exception{
        HashSet<Game> allGames = facade.callList(auth);
        String serialGame = "";
        for(Game element : allGames){
            if(element.getGameID() == ID){
                serialGame = element.getMySerialGame();
            }
        }
        return serialGame;
    }

    public static void displayBoardWhite(String serialBoard){
        myChessBoard myB = new myChessBoard();
        String[][] boardArray = myB.deserialize(serialBoard);

        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
        System.out.print("    h  g  f  e  d  c  b  a    ");
        System.out.print("\u001b[49m" + "\n");

        for(int i = 0; i < 8; ++i) {
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
            System.out.print(" " + (i + 1) + " ");
            System.out.print("\u001b[39;49m");
            for (int k = 0; k < 8; ++k) {
                if((k+i)%2 == 0){
                    System.out.print(EscapeSequences.SET_BG_COLOR_BLACK);
                }else{
                    System.out.print(EscapeSequences.SET_BG_COLOR_WHITE);
                }
                if (Objects.equals(boardArray[i][k], "_")) {
                    System.out.print("   ");
                }else{
                    if(isUpperCase(boardArray[i][k].charAt(0))){
                        System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
                    }else{
                        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
                    }
                    System.out.print(" " + boardArray[i][k] + " ");
                    System.out.print("\u001b[39m");
                }
            }
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
            System.out.print(" " + (i + 1) + " ");
            System.out.print("\u001b[39;49m");
            System.out.print("\n");
        }
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
        System.out.print("    h  g  f  e  d  c  b  a    ");
        System.out.print("\u001b[39;49m\n\n");


    }

    public static void displayBoardBlack(String serialBoard){
        myChessBoard myB = new myChessBoard();
        String[][] boardArray = myB.deserialize(serialBoard);
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
        System.out.print("    a  b  c  d  e  f  g  h    ");
        System.out.print("\u001b[49m" + "\n");

        for(int i = 7; i >= 0; --i) {
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
            System.out.print(" " + (i + 1) + " ");
            System.out.print("\u001b[39;49m");
            for (int k = 7; k >= 0; --k) {
                if((k+i)%2 == 0){
                    System.out.print(EscapeSequences.SET_BG_COLOR_BLACK);
                }else{
                    System.out.print(EscapeSequences.SET_BG_COLOR_WHITE);
                }
                if (Objects.equals(boardArray[i][k], "_")) {
                    System.out.print("   ");
                }else{
                    if(isUpperCase(boardArray[i][k].charAt(0))){
                        System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
                    }else{
                        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
                    }
                    System.out.print(" " + boardArray[i][k] + " ");
                    System.out.print("\u001b[39m");
                }
            }
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
            System.out.print(" " + (i + 1) + " ");
            System.out.print("\u001b[39;49m");
            System.out.print("\n");
        }
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
        System.out.print("    a  b  c  d  e  f  g  h    ");
        System.out.print("\u001b[39;49m\n");
    }

    public static void printPossibleMoves(ChessGame.TeamColor color, String serialBoard, myChessPosition piece) throws InvalidMoveException{
        myChessBoard myB = new myChessBoard();
        String[][] boardArray = myB.deserialize(serialBoard);
        myChessGame simulation = new myChessGame();
        simulation.serialStringIntoBoard(serialBoard);
        simulation.setSerialGame(serialBoard);
        Collection<ChessMove> moves = simulation.validMoves(piece);
        if(moves.isEmpty()){
            throw new InvalidMoveException("There are no valid moves for this space");
        }
        myChessMove currentMove = null;
        if(color == ChessGame.TeamColor.BLACK){
            //BLACK
            //print top border
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
            System.out.print("    a  b  c  d  e  f  g  h    ");
            System.out.print("\u001b[49m" + "\n");

            for(int i = 7; i >= 0; --i) {
                //print left border
                System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
                System.out.print(" " + (i + 1) + " ");
                System.out.print("\u001b[39;49m");
                for (int k = 7; k >= 0; --k) {
                    currentMove = new myChessMove(piece, new myChessPosition(i,k));

                    //set BG color
                    if(currentMove.getEndPosition().equals(piece)){
                        System.out.print(EscapeSequences.SET_BG_COLOR_YELLOW);
                    }else if(moves.contains(currentMove)){
                        if((k+i)%2 == 0){
                            System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                        }else{
                            System.out.print(EscapeSequences.SET_BG_COLOR_GREEN);
                        }
                    }else{
                        if((k+i)%2 == 0){
                            System.out.print(EscapeSequences.SET_BG_COLOR_BLACK);
                        }else{
                            System.out.print(EscapeSequences.SET_BG_COLOR_WHITE);
                        }
                    }
                    //set text color
                    if (Objects.equals(boardArray[i][k], "_")) {
                        System.out.print("   ");
                    }else{
                        if(isUpperCase(boardArray[i][k].charAt(0))){
                            System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
                        }else{
                            System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
                        }
                        System.out.print(" " + boardArray[i][k] + " ");
                        System.out.print("\u001b[39m");
                    }
                }
                //print right border
                System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
                System.out.print(" " + (i + 1) + " ");
                System.out.print("\u001b[39;49m");
                System.out.print("\n");
            }
            //print bottom border
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
            System.out.print("    a  b  c  d  e  f  g  h    ");
            System.out.print("\u001b[39;49m\n");
        }else{
            //WHITE OR UNSPECIFIED COLOR
            //print top border
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
            System.out.print("    h  g  f  e  d  c  b  a    ");
            System.out.print("\u001b[49m" + "\n");

            for(int i = 0; i < 8; ++i) {
                //print left border
                System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
                System.out.print(" " + (i + 1) + " ");
                System.out.print("\u001b[39;49m");
                for (int k = 0; k < 8; ++k) {
                    currentMove = new myChessMove(piece, new myChessPosition(i,k));

                    //set BG color
                    if(currentMove.getEndPosition().equals(piece)){
                        System.out.print(EscapeSequences.SET_BG_COLOR_YELLOW);
                    }else if(moves.contains(currentMove)){
                        if((k+i)%2 == 0){
                            System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                        }else{
                            System.out.print(EscapeSequences.SET_BG_COLOR_GREEN);
                        }
                    }else{
                        if((k+i)%2 == 0){
                            System.out.print(EscapeSequences.SET_BG_COLOR_BLACK);
                        }else{
                            System.out.print(EscapeSequences.SET_BG_COLOR_WHITE);
                        }
                    }
                    //set text color
                    if (Objects.equals(boardArray[i][k], "_")) {
                        System.out.print("   ");
                    }else{
                        if(isUpperCase(boardArray[i][k].charAt(0))){
                            System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
                        }else{
                            System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
                        }
                        System.out.print(" " + boardArray[i][k] + " ");
                        System.out.print("\u001b[39m");
                    }
                }
                //print right border
                System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
                System.out.print(" " + (i + 1) + " ");
                System.out.print("\u001b[39;49m");
                System.out.print("\n");
            }
            //print bottom border
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
            System.out.print("    h  g  f  e  d  c  b  a    ");
            System.out.print("\u001b[39;49m\n\n");
        }



    }

    public static myChessPosition parsePosition(String start){
        myChessPosition pos = null;
        if(start.length() == 2){
            int inRow = Integer.parseInt(String.valueOf(start.charAt(1))) -1 ;

            int inCol = 104 - start.charAt(0);

            pos = new myChessPosition(inRow, inCol);
        }
        return pos;
    }
}
