import chess.ChessGame;
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
            try{
                if(!isLoggedIn){
                    if(line.equalsIgnoreCase("help")){
                        clientFunctions.printHelpMenu(false);
                    }else if(line.equalsIgnoreCase("quit")){
                        System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
                        System.out.print("Goodbye.\n");
                        break;
                    }else if(line.substring(0,5).equalsIgnoreCase("login")){
                        // LOGIN
                        String[] loginInfo = line.split(" ");
                        if(loginInfo.length < 3){
                            clientFunctions.printInputError();
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
                            clientFunctions.printInputError();
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
                        clientFunctions.printHelpMenu(true);
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
                            clientFunctions.printInputError();
                        }else{
                            try{
                                JoinGameRequest newJoin = new JoinGameRequest(joinGameInfo[1], Integer.parseInt(joinGameInfo[2]));
                                int code = facade.callJoin(newJoin, auth);
                                if(code == 200){
                                    WebSocketClient.run(auth, newJoin.getGameID(), newJoin.getPlayerColor(), false);
                                }else{
                                    System.out.print("Could not complete request: error code " + code);
                                }
                            }catch(Exception e){
                                clientFunctions.printInputError();
                            }
                        }

                    }else if(line.equalsIgnoreCase("logout")){
                        // call logout service
                        if(facade.callLogout(auth) == 200){
                            isLoggedIn = false;
                        }

                    }else if(line.substring(0,6).equalsIgnoreCase("create")){
                        String[] createGameInfo = line.split(" ");
                        if(createGameInfo.length < 1){
                            clientFunctions.printInputError();
                        }else{
                            try{
                                CreateGameRequest newGame = new CreateGameRequest(createGameInfo[1]);
                                int id = facade.callCreate(newGame, auth);
                                if(id == 5){
                                    System.out.print("Game creation failed.\n");
                                }else{
                                    System.out.print("Your game ID is " + id + "\n");
                                }
                            }catch(Exception e){
                                clientFunctions.printInputError();
                            }


                        }
                    }else if(line.substring(0,7).equalsIgnoreCase("observe")){
                        String[] observeGameInfo = line.split(" ");
                        if(observeGameInfo.length < 2){
                            clientFunctions.printInputError();
                        }else{
                            try{
                                JoinGameRequest newJoin = new JoinGameRequest(null, Integer.parseInt(observeGameInfo[1]));
                                if(facade.callJoin(newJoin, auth) == 200){
                                    WebSocketClient.run(auth, newJoin.getGameID(), newJoin.getPlayerColor(), true);
                                }else{
                                    System.out.print("Error occurred while attempting to join.\n");
                                }
                            }catch(Exception e){
                                clientFunctions.printInputError();
                            }


                        }

                    }else{
                        clientFunctions.printInputError();
                    }
                }
            }catch(StringIndexOutOfBoundsException e){
                clientFunctions.printInputError();
            }


        }
        if(!auth.isEmpty()){
            facade.callLogout(auth);
        }
    }






}
