package passoffTests;

import chess.*;
import java.lang.Math;

/**
 * Used for testing your code
 * Add in code using your classes for each method for each FIXME
 */
public class TestFactory {

    //Chess Functions
    //------------------------------------------------------------------------------------------------------------------
    public static ChessBoard getNewBoard(){
        int test = 0;
		return new myChessBoard(test);
    }

    public static ChessGame getNewGame(){
		return new myChessGame();
    }

    public static ChessPiece getNewPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type){
        if(type == ChessPiece.PieceType.QUEEN){
            return new myChessPiece.Queen(pieceColor);
        }
        if(type == ChessPiece.PieceType.KING){
            return new myChessPiece.King(pieceColor);
        }
        if(type == ChessPiece.PieceType.BISHOP){
            return new myChessPiece.Bishop(pieceColor);
        }
        if(type == ChessPiece.PieceType.ROOK){
            return new myChessPiece.Rook(pieceColor);
        }
        if(type == ChessPiece.PieceType.KNIGHT){
            return new myChessPiece.Knight(pieceColor);
        }
        if(type == ChessPiece.PieceType.PAWN){
            return new myChessPiece.Pawn(pieceColor);
        }
		return null;
    }

    public static ChessPosition getNewPosition(Integer row, Integer col){
        row = Math.abs(row - 8);
        col = col - 1;
		return new myChessPosition(row, col);
    }

    public static ChessMove getNewMove(ChessPosition startPosition, ChessPosition endPosition, ChessPiece.PieceType promotionPiece){
		return new myChessMove((myChessPosition) startPosition, (myChessPosition) endPosition, promotionPiece);
    }
    //------------------------------------------------------------------------------------------------------------------


    //Server API's
    //------------------------------------------------------------------------------------------------------------------
    public static String getServerPort(){
        return "8080";
    }
    //------------------------------------------------------------------------------------------------------------------


    //Websocket Tests
    //------------------------------------------------------------------------------------------------------------------
    public static Long getMessageTime(){
        /*
        Changing this will change how long tests will wait for the server to send messages.
        3000 Milliseconds (3 seconds) will be enough for most computers. Feel free to change as you see fit,
        just know increasing it can make tests take longer to run.
        (On the flip side, if you've got a good computer feel free to decrease it)
         */
        return 3000L;
    }
    //------------------------------------------------------------------------------------------------------------------
}
