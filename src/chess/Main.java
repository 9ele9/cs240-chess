package chess;

import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] string){
        myChessBoard boardtest = new myChessBoard();
        boardtest.resetBoard();
        /*
        Set<ChessMove> allMoves = new HashSet<>();
        int num1 = 5;
        int num2 = 4;
        myChessPosition pos1 = new myChessPosition(4,5);
        myChessPosition pos2 = new myChessPosition(4,5);
        myChessPosition pos3 = new myChessPosition(num1-1,num2+1);
        myChessPosition pos4 = new myChessPosition(num2,num1);
        allMoves.add(new myChessMove(pos1, pos2));
        allMoves.add(new myChessMove(pos4, pos2));
        allMoves.add(new myChessMove(pos3, pos4));
        allMoves.add(new myChessMove(pos4, pos4));
        System.out.println(allMoves);
        */


        //boardtest.displayBoard();

        myChessPosition debug2 = new myChessPosition(5,5);
        boardtest.addPiece(debug2, new myChessPiece(ChessPiece.PieceType.KING, ChessGame.TeamColor.BLACK));
        boardtest.displayBoard();
        System.out.println(boardtest.getPiece(debug2).toString());
        /*System.out.println(boardtest.myBoard[6][4].getTeamColor());
        System.out.println(boardtest.myBoard[6][5].getTeamColor());
        System.out.println(boardtest.myBoard[6][6].getTeamColor());*/
        System.out.println(boardtest.getPiece(debug2).pieceMoves(boardtest, debug2));
        System.out.println(boardtest.serialize());

        }
}
