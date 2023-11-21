import chess.myChessBoard;
import chess_server.Game;
import chess_server.User;
import requests.*;
import ui.EscapeSequences;

import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;

import static java.lang.Character.isUpperCase;

public class Client {
    private static final serverFacade facade = new serverFacade();
    public static void main(String[] args) throws Exception {
        boolean isLoggedIn = false;
        String auth = "";
        System.out.print("Virtual Chess Simulation. Type \"help\" to learn more.\n");
        while (true) {
            if(isLoggedIn){
                System.out.print("Logged in >>> ");
            }else{
                System.out.print("Logged out >>> ");
            }
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            if(!isLoggedIn){
                if(line.equalsIgnoreCase("help")){
                    printHelpMenu(false);
                }else if(line.equalsIgnoreCase("quit")){
                    System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
                    System.out.print("Goodbye.\n");
                    break;
                }else if(line.substring(0,5).equalsIgnoreCase("login")){
                    // LOGIN
                    String[] loginInfo = line.split(" ");
                    if(loginInfo.length < 3){
                        printInputError();
                    }else{
                        LoginRequest newRequest = new LoginRequest(new User(loginInfo[1], loginInfo[2], ""));
                        // call login service
                        auth = facade.callLogin(newRequest);
                        if(!auth.isEmpty()){
                            System.out.printf("Logged in as: " + newRequest.getUsername() + "\n");
                            isLoggedIn = true;
                        }else{
                            System.out.print("Login failed.\n");
                        }

                    }

                }else if(line.substring(0,8).equalsIgnoreCase("register")){
                    // REGISTER
                    String[] registerInfo = line.split(" ");
                    if(registerInfo.length < 4){
                        printInputError();
                    }else{
                        RegisterRequest newRegister = new RegisterRequest(new User(registerInfo[1], registerInfo[2], registerInfo[3]));
                        auth = facade.callRegister(newRegister);
                        if(!auth.isEmpty()){
                            System.out.printf("Logged in as: " + newRegister.getUsername() + "\n");
                            isLoggedIn = true;
                        }else{
                            System.out.print("Registration failed.\n");
                        }
                    }
                }
            }else{
                if(line.equalsIgnoreCase("help")){
                    printHelpMenu(true);
                }else if(line.equalsIgnoreCase("list")){
                    // call list of all games
                    HashSet<Game> allGames = facade.callList(auth);
                    int i = 1;
                    System.out.print("List of Games: \n");
                    for(Game element : allGames){

                        System.out.print(i + ": " + element.toString());
                        System.out.print("\n");
                        ++i;
                    };
                }else if(line.substring(0,4).equalsIgnoreCase("join")){
                    String[] joinGameInfo = line.split(" ");
                    if(joinGameInfo.length < 3){
                        printInputError();
                    }else{
                        JoinGameRequest newJoin = new JoinGameRequest(joinGameInfo[1], Integer.parseInt(joinGameInfo[2]));
                        facade.callJoin(newJoin, auth);
                        displayBoard(getABoard(Integer.parseInt(joinGameInfo[2]), auth));
                    }

                }else if(line.equalsIgnoreCase("logout")){
                    // call logout service
                    if(facade.callLogout(auth) == 200){
                        isLoggedIn = false;
                    }

                }else if(line.substring(0,6).equalsIgnoreCase("create")){
                    String[] createGameInfo = line.split(" ");
                    if(createGameInfo.length < 1){
                        printInputError();
                    }else{
                        CreateGameRequest newGame = new CreateGameRequest(createGameInfo[1]);
                        int id = facade.callCreate(newGame, auth);
                        if(id == 5){
                            System.out.print("Game creation failed.\n");
                        }else{
                            System.out.print("Your game ID is " + id + "\n");
                        }

                    }
                }else if(line.substring(0,7).equalsIgnoreCase("observe")){
                    String[] observeGameInfo = line.split(" ");
                    if(observeGameInfo.length < 2){
                        printInputError();
                    }else{
                        JoinGameRequest newJoin = new JoinGameRequest(null, Integer.parseInt(observeGameInfo[1]));
                        if(facade.callJoin(newJoin, auth) == 200){
                            displayBoard(getABoard(Integer.parseInt(observeGameInfo[1]), auth));
                        }else{
                            System.out.print("Error occurred while attempting to join.\n");
                        }

                    }

                }else{
                    printInputError();
                }
            }

        }
        if(!auth.isEmpty()){
            facade.callLogout(auth);
        }
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

    public static void printInputError(){
        System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
        System.out.print("Invalid input.\n");
        System.out.print("\u001b[38;5;39m");
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

    public static void displayBoard(String serialBoard){
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
}
