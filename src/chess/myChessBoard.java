package chess;

import java.util.Objects;

public class myChessBoard implements ChessBoard {
    myChessPiece[][] myBoard = new myChessPiece[8][8];

    public myChessBoard(){
        resetBoard();
    }

    public myChessBoard(int test){
        for(int i = 0; i < 8; ++i){
            for(int k = 0; k < 8; ++k){
                myBoard[i][k] = null;
            }
        }
    }
    @Override
    public void addPiece(ChessPosition position, ChessPiece piece) {
        myBoard[position.getRow()][position.getColumn()] = (myChessPiece) piece;

    }

    public void delPiece(ChessPosition position) {
        myBoard[position.getRow()][position.getColumn()] = null;

    }

    @Override
    public ChessPiece getPiece(ChessPosition position) {

        return myBoard[position.getRow()][position.getColumn()];
    }

    @Override
    public void resetBoard() {
        for(int i = 0; i < 8; ++i){
            ChessGame.TeamColor color = null;
            if(i < 3){
                color = ChessGame.TeamColor.BLACK;
            }else{
                color = ChessGame.TeamColor.WHITE;
            }
            for(int k = 0; k < 8; ++k){

                if((i==0) || (i==7)){
                    //Rook, Knight, Bishop, Queen, King, Bishop, Knight, Rook
                    myBoard[i][0]= new myChessPiece.Rook(color);
                    myBoard[i][1]= new myChessPiece.Knight(color);
                    myBoard[i][2]= new myChessPiece.Bishop(color);
                    myBoard[i][3]= new myChessPiece.Queen(color);
                    myBoard[i][4]= new myChessPiece.King(color);
                    myBoard[i][5]= new myChessPiece.Bishop(color);
                    myBoard[i][6]= new myChessPiece.Knight(color);
                    myBoard[i][7]= new myChessPiece.Rook(color);
                    k = 8;
                }else if((i==1) || (i==6)){
                    myBoard[i][k] = new myChessPiece.Pawn(color);
                    // pawns
                }else{
                    myBoard[i][k] = null;
                }

            }
        }

    }

    public void displayBoard(){
        for(int i = 0; i < 8; ++i){
            for(int k = 0; k < 8; ++k){
                if(myBoard[i][k] == null){
                    System.out.print("_");
                }else{
                    System.out.print(myBoard[i][k].getMarker());
                }
                System.out.print("|");

            }
            System.out.println("");
        }
    }

    public String serialize(){
        StringBuilder serialString = new StringBuilder();
        for(int i = 0; i < 8; ++i){
            for(int k = 0; k < 8; ++k){
                if(myBoard[i][k] == null){
                    serialString.append("_");
                }else{
                    serialString.append(myBoard[i][k].getMarker());
                }
            }
        }
        return serialString.toString();
    }

    public String[][] deserialize(String serializedString){
        String[][] deserialStrings = new String[8][8];
        for(int i = 0; i < 8; ++i){
            for(int k = 0; k < 8; ++k){
                deserialStrings[i][k] = String.valueOf(serializedString.charAt(0));
                serializedString = serializedString.substring(1);
            }
        }
        return deserialStrings;
    }

    public void deserializeToRealBoard(String[][] sBoard){
        myChessPiece[][] rBoard = new myChessPiece[8][8];
        for(int i = 0; i < 8; ++i){
            for(int k = 0; k < 8; ++k){
                if(Objects.equals(sBoard[i][k], "_")){
                    rBoard[i][k] = null;
                }else{
                    rBoard[i][k] = markerToPiece(sBoard[i][k]);
                }
            }
        }
        this.myBoard = rBoard;
    }

    public myChessPiece markerToPiece(String piece) {
        if (Objects.equals(piece, "p")) {
            return new myChessPiece.Pawn(ChessGame.TeamColor.BLACK);
        } else if (Objects.equals(piece, "P")) {
            return new myChessPiece.Pawn(ChessGame.TeamColor.WHITE);
        }
        if (Objects.equals(piece, "n")) {
            return new myChessPiece.Knight(ChessGame.TeamColor.BLACK);
        } else if (Objects.equals(piece, "N")) {
            return new myChessPiece.Knight(ChessGame.TeamColor.WHITE);
        }
        if (Objects.equals(piece, "r")) {
            return new myChessPiece.Rook(ChessGame.TeamColor.BLACK);
        } else if (Objects.equals(piece, "R")) {
            return new myChessPiece.Rook(ChessGame.TeamColor.WHITE);
        }
        if (Objects.equals(piece, "b")) {
            return new myChessPiece.Bishop(ChessGame.TeamColor.BLACK);
        } else if (Objects.equals(piece, "B")) {
            return new myChessPiece.Bishop(ChessGame.TeamColor.WHITE);
        }
        if (Objects.equals(piece, "k")) {
            return new myChessPiece.King(ChessGame.TeamColor.BLACK);
        } else if (Objects.equals(piece, "K")) {
            return new myChessPiece.King(ChessGame.TeamColor.WHITE);
        }
        if (Objects.equals(piece, "q")) {
            return new myChessPiece.Queen(ChessGame.TeamColor.BLACK);
        } else if (Objects.equals(piece, "Q")) {
            return new myChessPiece.Queen(ChessGame.TeamColor.WHITE);
        }
        return null;
    }
}


