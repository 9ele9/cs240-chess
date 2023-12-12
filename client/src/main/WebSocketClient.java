import WS.ServerMessage;
import WS.UserGameCommand;
import chess.ChessGame;
import com.google.gson.JsonObject;
import ui.EscapeSequences;
import com.google.gson.Gson;
import java.net.URI;
import java.util.Objects;
import java.util.Scanner;
import javax.websocket.*;
import chess.myChessMove;
import chess.myChessPosition;
public class WebSocketClient extends Endpoint{
    public Session session;
    public static String currentGame = "";

    public static void run(String auth, int id, ChessGame.TeamColor color, boolean isObserving) throws Exception {
        var ws = new WebSocketClient();
        Scanner scanner = new Scanner(System.in);
        System.out.print(EscapeSequences.SET_TEXT_COLOR_YELLOW);
        System.out.println("Type \"Help\" for a list of commands.");
        System.out.print("\u001b[38;5;39m\n");
        if(isObserving){
            ws.send(new Gson().toJson(new UserGameCommand.joinObserverCommand(id, auth)));
        }else{
            ws.send(new Gson().toJson(new UserGameCommand.joinPlayerCommand(id, color)));
        }

        while (true) {
            System.out.print("Playing game >>> ");
            String next = scanner.nextLine();
            if(next.equalsIgnoreCase("quit")){
                break;
            }else if(next.equalsIgnoreCase("help")){
                displayMenu(isObserving);
            }else if(next.equalsIgnoreCase("redraw")){
                if(currentGame.isEmpty()){
                    //no moves made yet
                    if(color == ChessGame.TeamColor.BLACK){
                        clientFunctions.displayBoardBlack(clientFunctions.getABoard(id, auth));
                    }else{
                        clientFunctions.displayBoardWhite(clientFunctions.getABoard(id, auth));
                    }
                }else{
                    if(color == ChessGame.TeamColor.BLACK){
                        clientFunctions.displayBoardBlack(currentGame);
                    }else{
                        clientFunctions.displayBoardWhite(currentGame);
                    }
                }

            }else if(next.equalsIgnoreCase("leave")){
                if(isObserving){
                    break;
                }else{
                    String test1 = new Gson().toJson(new UserGameCommand.leaveCommand(id, color, auth));
                    ws.send(test1);
                    break;
                }

            }else if(next.length() < 11){
                try{
                    if(next.substring(0,4).equalsIgnoreCase("move")){
                        String[] moveInfo = next.split(" ");
                        if(moveInfo.length == 3){
                            UserGameCommand.makeMoveCommand aMove = new UserGameCommand.makeMoveCommand(parseMove(moveInfo[1],moveInfo[2]), id, auth, color);
                            String gsonTest = new Gson().toJson(aMove);
                            ws.send(gsonTest);
                        }
                    }
                }catch(Exception e){
                    clientFunctions.printInputError();
                }

            }else{
                clientFunctions.printInputError();
            }
        }
    }



    public WebSocketClient() throws Exception {
        URI uri = new URI("ws://localhost:8080/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);
        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                ServerMessage initial = new Gson().fromJson(message, ServerMessage.class);
                String newMessage = clientFunctions.parseServerMessage(initial, message);
                if(initial.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME){
                    System.out.println();
                    currentGame = newMessage;
                    clientFunctions.displayBoardWhite(newMessage);
                }else{
                    System.out.println("\nNotification: " + newMessage);
                }

            }
        });

    }
    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        System.out.print("Entering gameplay mode.\n");
    }

    public static void displayMenu(boolean isObserving){
        if(isObserving){
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
            System.out.print("""
                        HELP MENU
                        Commands
                        You are OBSERVING
                        """);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_YELLOW);
            System.out.print("REDRAW : redraw the chess board\n");
            System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
            System.out.print("LEAVE : leave the game\n");
            System.out.print("\u001b[39;49m");
        }else{
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
            System.out.print("""
                        HELP MENU
                        Commands
                        """);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_YELLOW);
            System.out.print("REDRAW : redraw the chess board\n");
            System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
            System.out.print("LEAVE : leave the game\n");
            System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
            System.out.print("MOVE [a,#] : make a move\n");
            System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            System.out.print("RESIGN : admit defeat\n");
            System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
            System.out.print("OPTIONS : highlights legal moves\n");
            System.out.print("\u001b[39;49m");
        }

    }

    public static myChessMove parseMove(String start, String end){
        myChessPosition startPos = null;
        myChessPosition endPos = null;
        if(start.length() == 2 && end.length() == 2){
            int inRowStart = Integer.parseInt(String.valueOf(start.charAt(1))) -1 ;
            int inRowEnd = Integer.parseInt(String.valueOf(end.charAt(1))) -1 ;

            int inColStart = start.charAt(0) - 97;
            int inColEnd = end.charAt(0) - 97;

            startPos = new myChessPosition(inRowStart, inColStart);
            endPos = new myChessPosition(inRowEnd, inColEnd);

        }
        //System.out.print(new myChessMove(startPos,endPos));
        return new myChessMove(startPos, endPos);
    }
}
