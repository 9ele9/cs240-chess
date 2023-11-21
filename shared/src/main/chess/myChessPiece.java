package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class myChessPiece implements ChessPiece {
    public ChessPiece.PieceType myType;
    public ChessGame.TeamColor myColor;

    public myChessPiece(){

    }
    public myChessPiece(ChessPiece.PieceType assignment, ChessGame.TeamColor color){
        myType = assignment;
        myColor = color;
    }

    public void typeUpdate(){
        myType = ChessPiece.PieceType.QUEEN;
    }
    public void colorUpdate(ChessGame.TeamColor color){ myColor = color;}
    public void typeUpdate(ChessPiece.PieceType assignment){
        myType = assignment;
    }
    public char getMarker(){
        if(myType==null){
            return '_';
        }
        if(myType == ChessPiece.PieceType.QUEEN){
            if(myColor == ChessGame.TeamColor.WHITE){
                return 'Q';
            }else{
                return 'q';
            }

        }
        if(myType == ChessPiece.PieceType.KING){
            if(myColor == ChessGame.TeamColor.WHITE){
                return 'K';
            }else{
                return 'k';
            }
        }
        if(myType == ChessPiece.PieceType.BISHOP){
            if(myColor == ChessGame.TeamColor.WHITE){
                return 'B';
            }else{
                return 'b';
            }
        }
        if(myType == ChessPiece.PieceType.ROOK){
            if(myColor == ChessGame.TeamColor.WHITE){
                return 'R';
            }else{
                return 'r';
            }
        }
        if(myType == ChessPiece.PieceType.KNIGHT){
            if(myColor == ChessGame.TeamColor.WHITE){
                return 'N';
            }else{
                return 'n';
            }
        }
        if(myType == ChessPiece.PieceType.PAWN){
            if(myColor == ChessGame.TeamColor.WHITE){
                return 'P';
            }else{
                return 'p';
            }
        }
        return '.';
    }

    @Override
    public ChessGame.TeamColor getTeamColor() {
        return myColor;
    }

    @Override
    public ChessPiece.PieceType getPieceType() {
        return myType;
    }

    @Override
    public String toString(){
        return myColor.name() + " " + myType.name();
    }


    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        myColor = ((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getTeamColor();
        if(((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getPieceType() == ChessPiece.PieceType.PAWN) {
            Pawn myPawn = new Pawn();
            return myPawn.pieceMoves(board, myPosition);
        }else if(((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getPieceType() == ChessPiece.PieceType.KING) {
            myType = ChessPiece.PieceType.KING;
            King myKing = new King();
            return myKing.pieceMoves(board, myPosition);
        }else if(((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getPieceType() == ChessPiece.PieceType.QUEEN) {
            myType = ChessPiece.PieceType.QUEEN;
            Queen myQueen = new Queen();
            return myQueen.pieceMoves(board, myPosition);
        }else if(((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getPieceType() == ChessPiece.PieceType.BISHOP) {
            myType = ChessPiece.PieceType.BISHOP;
            Bishop myBishop = new Bishop();
            return myBishop.pieceMoves(board, myPosition);
        }
        else if(((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getPieceType() == ChessPiece.PieceType.KNIGHT) {
            myType = ChessPiece.PieceType.KNIGHT;
            Knight myKnight = new Knight();
            return myKnight.pieceMoves(board, myPosition);
        }
        else if(((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getPieceType() == ChessPiece.PieceType.ROOK) {
            myType = ChessPiece.PieceType.ROOK;
            Rook myRook = new Rook();
            return myRook.pieceMoves(board, myPosition);
        }else{
            return null;
        }
    }

    public static class Pawn extends myChessPiece {
        public ChessPiece.PieceType myType;
        public ChessGame.TeamColor myColor;

        public Pawn(){
            myType = ChessPiece.PieceType.PAWN;
            myColor = ChessGame.TeamColor.WHITE;
            super.typeUpdate(ChessPiece.PieceType.PAWN);

        }

        public Pawn(ChessGame.TeamColor color){
            myType = ChessPiece.PieceType.PAWN;
            myColor = color;
            super.typeUpdate(ChessPiece.PieceType.PAWN);
            super.colorUpdate(color);
        }
        @Override
        public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
            Set<ChessMove> allMoves = new HashSet<>();

            if(((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getTeamColor() == ChessGame.TeamColor.WHITE){
                //white is down
                if(myPosition.getRow() >= 1){
                    if((((myChessBoard) board).myBoard[myPosition.getRow()-1][myPosition.getColumn()] == null)){
                        //can move up if space directly above is null
                        myChessPosition newSpot = new myChessPosition(myPosition.getRow()-1, myPosition.getColumn());
                        allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                    }else if((((myChessBoard) board).myBoard[myPosition.getRow()-1][myPosition.getColumn()].getTeamColor() != myColor)){
                        //cannot do anything if space directly above is opposite-color piece
                    }
                    if (myPosition.getColumn() < 7 && (((myChessBoard) board).myBoard[myPosition.getRow() - 1][myPosition.getColumn() + 1] == null)){
                        //cannot do anything if down-right position is empty
                    }else if(myPosition.getColumn() < 7 && (((myChessBoard) board).myBoard[myPosition.getRow() - 1][myPosition.getColumn() + 1].getTeamColor() != myColor)){
                        //can capture if down-right position has opposite-color piece
                        myChessPosition newSpot = new myChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
                        allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                    }
                    if (myPosition.getColumn() > 0 && (((myChessBoard) board).myBoard[myPosition.getRow() - 1][myPosition.getColumn() - 1] == null)){
                        //cannot do anything if down-left position is empty
                    }else if(myPosition.getColumn() > 0 && (((myChessBoard) board).myBoard[myPosition.getRow() - 1][myPosition.getColumn() - 1].getTeamColor() != myColor)){
                        //can capture if down-left position has opposite color piece
                        myChessPosition newSpot = new myChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
                        allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                    }
                    if(myPosition.getRow() == 6 && ((myChessBoard) board).myBoard[myPosition.getRow() - 2][myPosition.getColumn()] == null && (((myChessBoard) board).myBoard[myPosition.getRow() - 1][myPosition.getColumn()] == null)){
                        //can move two spaces if on first move and not obstructed. Cannot capture.
                        myChessPosition newSpot = new myChessPosition(myPosition.getRow() - 2, myPosition.getColumn());
                        allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                    }
                }
            }else {
                //black is up
                if(myPosition.getRow() <= 6){
                    if((((myChessBoard) board).myBoard[myPosition.getRow()+1][myPosition.getColumn()] == null)){
                        //can move down if space directly above is null
                        myChessPosition newSpot = new myChessPosition(myPosition.getRow()+1, myPosition.getColumn());
                        allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                    }else if((((myChessBoard) board).myBoard[myPosition.getRow()+1][myPosition.getColumn()].getTeamColor() != myColor)){
                        //cannot do anything if space directly above is opposite-color piece
                    }
                    if (myPosition.getColumn() < 7 && (((myChessBoard) board).myBoard[myPosition.getRow() +1][myPosition.getColumn() + 1] == null)){
                        //cannot do anything if up-right position is empty
                    }else if(myPosition.getColumn() < 7 && (((myChessBoard) board).myBoard[myPosition.getRow() + 1][myPosition.getColumn() + 1].getTeamColor() != myColor)){
                        //can capture if up-right position has opposite-color piece
                        myChessPosition newSpot = new myChessPosition(myPosition.getRow()+1, myPosition.getColumn() + 1);
                        allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                    }
                    if (myPosition.getColumn() > 0 && (((myChessBoard) board).myBoard[myPosition.getRow() +1][myPosition.getColumn() - 1] == null)){
                        //cannot do anything if up-left position is empty
                    }else if(myPosition.getColumn() > 0 && (((myChessBoard) board).myBoard[myPosition.getRow() +1][myPosition.getColumn() - 1].getTeamColor() != myColor)){
                        //can capture if up-left position has opposite color piece
                        myChessPosition newSpot = new myChessPosition(myPosition.getRow()+1, myPosition.getColumn() - 1);
                        allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                    }
                    if(myPosition.getRow() == 1 && ((myChessBoard) board).myBoard[myPosition.getRow() + 2][myPosition.getColumn()] == null && (((myChessBoard) board).myBoard[myPosition.getRow() + 1][myPosition.getColumn()] == null)){
                        //can move two spaces if on first move and not obstructed. Cannot capture.
                        myChessPosition newSpot = new myChessPosition(myPosition.getRow() + 2, myPosition.getColumn());
                        allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                    }
                }
            }
            allMoves.remove(new myChessMove((myChessPosition) myPosition, (myChessPosition) myPosition));
            return allMoves;
        }
    }

    public static class King extends myChessPiece {
        public ChessPiece.PieceType myType;
        public ChessGame.TeamColor myColor;

        public King(){
            myType = ChessPiece.PieceType.KING;
            myColor = ChessGame.TeamColor.WHITE;
            super.typeUpdate(ChessPiece.PieceType.KING);
        }

        public King(ChessGame.TeamColor color){
            myType = ChessPiece.PieceType.KING;
            myColor = color;
            super.typeUpdate(ChessPiece.PieceType.KING);
            super.colorUpdate(color);
        }
        @Override
        public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
            Set<ChessMove> allMoves = new HashSet<>();
            if (myPosition.getRow() != 0) {
                myChessPosition newSpot = new myChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
                if (((myChessBoard) board).myBoard[newSpot.getRow()][newSpot.getColumn()] == null || ((myChessBoard) board).myBoard[newSpot.getRow()][newSpot.getColumn()].getTeamColor() != ((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getTeamColor()) {
                    allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                }
                //move up
                if (myPosition.getColumn() != 0) {
                    myChessPosition newSpotLeft = new myChessPosition(myPosition.getRow()-1, myPosition.getColumn() - 1);
                    if (((myChessBoard) board).myBoard[newSpotLeft.getRow()][newSpotLeft.getColumn()] == null || ((myChessBoard) board).myBoard[newSpotLeft.getRow()][newSpotLeft.getColumn()].getTeamColor() != ((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getTeamColor()) {
                        allMoves.add(new myChessMove((myChessPosition) myPosition, newSpotLeft));
                    }
                    //move left
                }
                if (myPosition.getColumn() != 7) {
                    myChessPosition newSpotRight = new myChessPosition(myPosition.getRow()-1, myPosition.getColumn() + 1);
                    if (((myChessBoard) board).myBoard[newSpotRight.getRow()][newSpotRight.getColumn()] == null || ((myChessBoard) board).myBoard[newSpotRight.getRow()][newSpotRight.getColumn()].getTeamColor() != ((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getTeamColor()) {
                        allMoves.add(new myChessMove((myChessPosition) myPosition, newSpotRight));
                    }
                    //move right
                }

            }
            if (myPosition.getColumn() != 0) {
                myChessPosition newSpot = new myChessPosition(myPosition.getRow(), myPosition.getColumn() - 1);
                if (((myChessBoard) board).myBoard[newSpot.getRow()][newSpot.getColumn()] == null || ((myChessBoard) board).myBoard[newSpot.getRow()][newSpot.getColumn()].getTeamColor() != ((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getTeamColor()) {
                    allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                }
                //move left
                if (myPosition.getRow() != 0) {
                    myChessPosition newSpotUp = new myChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
                    if (((myChessBoard) board).myBoard[newSpotUp.getRow()][newSpotUp.getColumn()] == null || ((myChessBoard) board).myBoard[newSpotUp.getRow()][newSpotUp.getColumn()].getTeamColor() != ((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getTeamColor()) {
                        allMoves.add(new myChessMove((myChessPosition) myPosition, newSpotUp));
                    }
                    //move up
                }
                if (myPosition.getRow() != 7) {
                    myChessPosition newSpotDown = new myChessPosition(myPosition.getRow() + 1, myPosition.getColumn()-1);
                    if (((myChessBoard) board).myBoard[newSpotDown.getRow()][newSpotDown.getColumn()] == null || ((myChessBoard) board).myBoard[newSpotDown.getRow()][newSpotDown.getColumn()].getTeamColor() != ((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getTeamColor()) {
                        allMoves.add(new myChessMove((myChessPosition) myPosition, newSpotDown));
                    }
                    //move down
                }
            }
            if (myPosition.getColumn() != 7) {
                myChessPosition newSpot = new myChessPosition(myPosition.getRow(), myPosition.getColumn() + 1);
                if (((myChessBoard) board).myBoard[newSpot.getRow()][newSpot.getColumn()] == null || ((myChessBoard) board).myBoard[newSpot.getRow()][newSpot.getColumn()].getTeamColor() != ((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getTeamColor()) {
                    allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                }
                //move right
                if (myPosition.getRow() != 0) {
                    myChessPosition newSpotUp = new myChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
                    if (((myChessBoard) board).myBoard[newSpotUp.getRow()][newSpotUp.getColumn()] == null || ((myChessBoard) board).myBoard[newSpotUp.getRow()][newSpotUp.getColumn()].getTeamColor() != ((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getTeamColor()) {
                        allMoves.add(new myChessMove((myChessPosition) myPosition, newSpotUp));
                    }
                    //move up
                }
                if (myPosition.getRow() != 7) {
                    myChessPosition newSpotDown = new myChessPosition(myPosition.getRow() + 1, myPosition.getColumn()+1);
                    if (((myChessBoard) board).myBoard[newSpotDown.getRow()][newSpotDown.getColumn()] == null || ((myChessBoard) board).myBoard[newSpotDown.getRow()][newSpotDown.getColumn()].getTeamColor() != ((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getTeamColor()) {
                        allMoves.add(new myChessMove((myChessPosition) myPosition, newSpotDown));
                    }
                    //move down
                }

            }
            if (myPosition.getRow() != 7) {
                myChessPosition newSpot = new myChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
                if (((myChessBoard) board).myBoard[newSpot.getRow()][newSpot.getColumn()] == null || ((myChessBoard) board).myBoard[newSpot.getRow()][newSpot.getColumn()].getTeamColor() != ((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getTeamColor()) {
                    allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                }
                //move down
                if (myPosition.getColumn() != 0) {
                    myChessPosition newSpotLeft = new myChessPosition(myPosition.getRow()+1, myPosition.getColumn() - 1);
                    if (((myChessBoard) board).myBoard[newSpotLeft.getRow()][newSpotLeft.getColumn()] == null || ((myChessBoard) board).myBoard[newSpotLeft.getRow()][newSpotLeft.getColumn()].getTeamColor() != ((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getTeamColor()) {
                        allMoves.add(new myChessMove((myChessPosition) myPosition, newSpotLeft));
                    }
                    //move left
                }
                if (myPosition.getColumn() != 7) {
                    myChessPosition newSpotRight = new myChessPosition(myPosition.getRow()+1, myPosition.getColumn() + 1);
                    if (((myChessBoard) board).myBoard[newSpotRight.getRow()][newSpotRight.getColumn()] == null || ((myChessBoard) board).myBoard[newSpotRight.getRow()][newSpotRight.getColumn()].getTeamColor() != ((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getTeamColor()) {
                        allMoves.add(new myChessMove((myChessPosition) myPosition, newSpotRight));
                    }
                    //move right
                }
            }
            allMoves.remove(new myChessMove((myChessPosition) myPosition, (myChessPosition) myPosition));
            return allMoves;
        }
    }

    public static class Queen extends myChessPiece{
        public ChessPiece.PieceType myType;
        public ChessGame.TeamColor myColor;

        public Queen(){
            myType = ChessPiece.PieceType.QUEEN;
            myColor = ChessGame.TeamColor.WHITE;
            super.typeUpdate(ChessPiece.PieceType.QUEEN);
        }

        public Queen(ChessGame.TeamColor color){
            myType = ChessPiece.PieceType.QUEEN;
            myColor = color;
            super.typeUpdate(ChessPiece.PieceType.QUEEN);
            super.colorUpdate(color);
        }
        @Override
        public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
            Set<ChessMove> allMoves = new HashSet<>();
            int rowCount = myPosition.getRow();
            int columnCount = myPosition.getColumn();
            while(rowCount <= 7 && columnCount <= 7){
                if(((myChessBoard) board).myBoard[rowCount][columnCount] != null && rowCount != myPosition.getRow() && columnCount != myPosition.getColumn()){
                    if(((myChessBoard) board).myBoard[rowCount][columnCount].getTeamColor() == ((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getTeamColor()){
                        break;
                    }else{
                        myChessPosition newSpot = new myChessPosition(rowCount, columnCount);
                        allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                        break;
                    }
                }
                myChessPosition newSpot = new myChessPosition(rowCount, columnCount);
                allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                ++rowCount;
                ++columnCount;
            }
            rowCount = myPosition.getRow();
            columnCount = myPosition.getColumn();
            while(rowCount >= 0 && columnCount <= 7){
                if(((myChessBoard) board).myBoard[rowCount][columnCount] != null && rowCount != myPosition.getRow() && columnCount != myPosition.getColumn()){
                    if(((myChessBoard) board).myBoard[rowCount][columnCount].getTeamColor() == ((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getTeamColor()){
                        break;
                    }else{
                        myChessPosition newSpot = new myChessPosition(rowCount, columnCount);
                        allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                        break;
                    }
                }
                myChessPosition newSpot = new myChessPosition(rowCount, columnCount);
                allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                --rowCount;
                ++columnCount;


            }
            rowCount = myPosition.getRow();
            columnCount = myPosition.getColumn();
            while(rowCount <= 7 && columnCount >= 0){
                if(((myChessBoard) board).myBoard[rowCount][columnCount] != null && rowCount != myPosition.getRow() && columnCount != myPosition.getColumn()){
                    if(((myChessBoard) board).myBoard[rowCount][columnCount].getTeamColor() == ((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getTeamColor()){
                        break;
                    }else{
                        myChessPosition newSpot = new myChessPosition(rowCount, columnCount);
                        allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                        break;
                    }
                }
                myChessPosition newSpot = new myChessPosition(rowCount, columnCount);
                allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                ++rowCount;
                --columnCount;
            }
            rowCount = myPosition.getRow();
            columnCount = myPosition.getColumn();
            while(rowCount >= 0 && columnCount >= 0){
                if(((myChessBoard) board).myBoard[rowCount][columnCount] != null && rowCount != myPosition.getRow() && columnCount != myPosition.getColumn()){
                    if(((myChessBoard) board).myBoard[rowCount][columnCount].getTeamColor() == ((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getTeamColor()){
                        break;
                    }else{
                        myChessPosition newSpot = new myChessPosition(rowCount, columnCount);
                        allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                        break;
                    }
                }
                myChessPosition newSpot = new myChessPosition(rowCount, columnCount);
                allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                --rowCount;
                --columnCount;
            }
            rowCount = myPosition.getRow();
            columnCount = myPosition.getColumn();

            while(rowCount <= 7){
                if(myPosition.getRow() != rowCount && ((myChessBoard) board).myBoard[rowCount][columnCount] != null){
                    if(((myChessBoard) board).myBoard[rowCount][columnCount].getTeamColor() == ((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getTeamColor()){
                        //System.out.println("blar1: " + rowCount + ", " + columnCount);
                        break;
                    }else{
                        //System.out.println("dar1");
                        myChessPosition newSpot = new myChessPosition(rowCount, myPosition.getColumn());
                        allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                        break;
                    }
                }
                myChessPosition newSpot = new myChessPosition(rowCount, myPosition.getColumn());
                allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                ++rowCount;
            }
            rowCount = myPosition.getRow();
            columnCount = myPosition.getColumn();
            while(rowCount >= 0){
                if(myPosition.getRow() != rowCount && ((myChessBoard) board).myBoard[rowCount][columnCount] != null){
                    if(((myChessBoard) board).myBoard[rowCount][columnCount].getTeamColor() == ((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getTeamColor()){
                        //System.out.println("blar2");
                        break;
                    }else{
                        ///System.out.println("dar2");
                        myChessPosition newSpot = new myChessPosition(rowCount, myPosition.getColumn());
                        allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                        break;
                    }
                }


                myChessPosition newSpot = new myChessPosition(rowCount, myPosition.getColumn());
                allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                --rowCount;
            }
            rowCount = myPosition.getRow();
            columnCount = myPosition.getColumn();
            while(columnCount <= 7){
                if(myPosition.getColumn() != columnCount && ((myChessBoard) board).myBoard[rowCount][columnCount] != null){
                    if(((myChessBoard) board).myBoard[rowCount][columnCount].getTeamColor() == ((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getTeamColor()){
                        //System.out.println("blar3");
                        break;
                    }else{
                        //System.out.println("dar3");
                        myChessPosition newSpot = new myChessPosition(myPosition.getRow(), columnCount);
                        allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                        break;
                    }
                }

                myChessPosition newSpot = new myChessPosition(myPosition.getRow(), columnCount);
                allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                ++columnCount;
            }
            rowCount = myPosition.getRow();
            columnCount = myPosition.getColumn();
            while(columnCount >= 0){
                if(myPosition.getColumn() != columnCount && ((myChessBoard) board).myBoard[rowCount][columnCount] != null){
                    if(((myChessBoard) board).myBoard[rowCount][columnCount].getTeamColor() == ((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getTeamColor()){
                        //System.out.println("blar4");
                        break;
                    }else{
                        //System.out.println("dar4");
                        myChessPosition newSpot = new myChessPosition(myPosition.getRow(), columnCount);
                        allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                        break;
                    }
                }

                myChessPosition newSpot = new myChessPosition(myPosition.getRow(), columnCount);
                allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                --columnCount;
            }
            allMoves.remove(new myChessMove((myChessPosition) myPosition, (myChessPosition) myPosition));
            return allMoves;
        }
    }

    public static class Bishop extends myChessPiece{
        public ChessPiece.PieceType myType;
        public ChessGame.TeamColor myColor;

        public Bishop(){
            myType = ChessPiece.PieceType.BISHOP;
            myColor = ChessGame.TeamColor.WHITE;
            super.typeUpdate(ChessPiece.PieceType.BISHOP);
        }

        public Bishop(ChessGame.TeamColor color){
            myType = ChessPiece.PieceType.BISHOP;
            myColor = color;
            super.typeUpdate(ChessPiece.PieceType.BISHOP);
            super.colorUpdate(color);
        }
        @Override
        public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
            Set<ChessMove> allMoves = new HashSet<>();
            int rowCount = myPosition.getRow();
            int columnCount = myPosition.getColumn();
            while(rowCount <= 7 && columnCount <= 7){
                if(((myChessBoard) board).myBoard[rowCount][columnCount] != null && rowCount != myPosition.getRow() && columnCount != myPosition.getColumn()){
                    if(((myChessBoard) board).myBoard[rowCount][columnCount].getTeamColor() == ((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getTeamColor()){
                        break;
                    }else{
                        myChessPosition newSpot = new myChessPosition(rowCount, columnCount);
                        allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                        break;
                    }
                }
                myChessPosition newSpot = new myChessPosition(rowCount, columnCount);
                allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                ++rowCount;
                ++columnCount;
            }
            rowCount = myPosition.getRow();
            columnCount = myPosition.getColumn();
            while(rowCount >= 0 && columnCount <= 7){
                if(((myChessBoard) board).myBoard[rowCount][columnCount] != null && rowCount != myPosition.getRow() && columnCount != myPosition.getColumn()){
                    if(((myChessBoard) board).myBoard[rowCount][columnCount].getTeamColor() == ((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getTeamColor()){
                        break;
                    }else{
                        myChessPosition newSpot = new myChessPosition(rowCount, columnCount);
                        allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                        break;
                    }
                }
                myChessPosition newSpot = new myChessPosition(rowCount, columnCount);
                allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                --rowCount;
                ++columnCount;


            }
            rowCount = myPosition.getRow();
            columnCount = myPosition.getColumn();
            while(rowCount <= 7 && columnCount >= 0){
                if(((myChessBoard) board).myBoard[rowCount][columnCount] != null && rowCount != myPosition.getRow() && columnCount != myPosition.getColumn()){
                    if(((myChessBoard) board).myBoard[rowCount][columnCount].getTeamColor() == ((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getTeamColor()){
                        break;
                    }else{
                        myChessPosition newSpot = new myChessPosition(rowCount, columnCount);
                        allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                        break;
                    }
                }
                myChessPosition newSpot = new myChessPosition(rowCount, columnCount);
                allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                ++rowCount;
                --columnCount;
            }
            rowCount = myPosition.getRow();
            columnCount = myPosition.getColumn();
            while(rowCount >= 0 && columnCount >= 0){
                if(((myChessBoard) board).myBoard[rowCount][columnCount] != null && rowCount != myPosition.getRow() && columnCount != myPosition.getColumn()){
                    if(((myChessBoard) board).myBoard[rowCount][columnCount].getTeamColor() == ((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getTeamColor()){
                        break;
                    }else{
                        myChessPosition newSpot = new myChessPosition(rowCount, columnCount);
                        allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                        break;
                    }
                }
                myChessPosition newSpot = new myChessPosition(rowCount, columnCount);
                allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                --rowCount;
                --columnCount;
            }
            allMoves.remove(new myChessMove((myChessPosition) myPosition, (myChessPosition) myPosition));
            return allMoves;
        }
    }

    public static class Knight extends myChessPiece{
        public ChessPiece.PieceType myType;
        public ChessGame.TeamColor myColor;

        public Knight(){
            myType = ChessPiece.PieceType.KNIGHT;
            myColor = ChessGame.TeamColor.WHITE;
            super.typeUpdate(ChessPiece.PieceType.KNIGHT);
        }

        public Knight(ChessGame.TeamColor color){
            myType = ChessPiece.PieceType.KNIGHT;
            myColor = color;
            super.typeUpdate(ChessPiece.PieceType.KNIGHT);
            super.colorUpdate(color);
        }
        @Override
        public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
            Set<ChessMove> allMoves = new HashSet<>();
            if(myPosition.getRow() >= 2){
                if(myPosition.getColumn() < 7 && (((myChessBoard) board).myBoard[myPosition.getRow()-2][myPosition.getColumn()+1] == null ||(((myChessBoard) board).myBoard[myPosition.getRow()-2][myPosition.getColumn()+1].getTeamColor() != (((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getTeamColor())))){
                    myChessPosition newSpot = new myChessPosition(myPosition.getRow()-2, myPosition.getColumn()+1);
                    allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                }
                if(myPosition.getColumn() > 0 && (((myChessBoard) board).myBoard[myPosition.getRow()-2][myPosition.getColumn()-1] == null || (((myChessBoard) board).myBoard[myPosition.getRow()-2][myPosition.getColumn()-1].getTeamColor() != (((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getTeamColor())))){
                    myChessPosition newSpot2 = new myChessPosition(myPosition.getRow()-2, myPosition.getColumn()-1);
                    allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot2));
                }
            }
            if(myPosition.getRow() <= 5){
                if(myPosition.getColumn() < 7 && (((myChessBoard) board).myBoard[myPosition.getRow()+2][myPosition.getColumn()+1] == null || (((myChessBoard) board).myBoard[myPosition.getRow()+2][myPosition.getColumn()+1].getTeamColor() != (((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getTeamColor())))){
                    myChessPosition newSpot = new myChessPosition(myPosition.getRow()+2, myPosition.getColumn()+1);
                    allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                }
                if(myPosition.getColumn() > 0 && (((myChessBoard) board).myBoard[myPosition.getRow()+2][myPosition.getColumn()-1] == null || (((myChessBoard) board).myBoard[myPosition.getRow()+2][myPosition.getColumn()-1].getTeamColor() != (((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getTeamColor())))){
                    myChessPosition newSpot2 = new myChessPosition(myPosition.getRow()+2, myPosition.getColumn()-1);
                    allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot2));
                }
            }
            if(myPosition.getColumn() >= 2){
                if(myPosition.getRow() < 7 && (((myChessBoard) board).myBoard[myPosition.getRow()+1][myPosition.getColumn()-2] == null ||(((myChessBoard) board).myBoard[myPosition.getRow()+1][myPosition.getColumn()-2].getTeamColor() != (((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getTeamColor())))){
                    myChessPosition newSpot = new myChessPosition(myPosition.getRow()+1, myPosition.getColumn()-2);
                    allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                }
                if(myPosition.getRow() > 0 && (((myChessBoard) board).myBoard[myPosition.getRow()-1][myPosition.getColumn()-2] == null || (((myChessBoard) board).myBoard[myPosition.getRow()-1][myPosition.getColumn()-2].getTeamColor() != (((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getTeamColor())))){
                    myChessPosition newSpot2 = new myChessPosition(myPosition.getRow()-1, myPosition.getColumn()-2);
                    allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot2));
                }
            }
            if(myPosition.getColumn() <= 5){
                if(myPosition.getRow() < 7 && (((myChessBoard) board).myBoard[myPosition.getRow()+1][myPosition.getColumn()+2] == null || (((myChessBoard) board).myBoard[myPosition.getRow()+1][myPosition.getColumn()+2].getTeamColor() != (((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getTeamColor())))){
                    myChessPosition newSpot = new myChessPosition(myPosition.getRow()+1, myPosition.getColumn()+2);
                    allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                }
                if(myPosition.getRow() > 0 && (((myChessBoard) board).myBoard[myPosition.getRow()-1][myPosition.getColumn()+2] == null || (((myChessBoard) board).myBoard[myPosition.getRow()-1][myPosition.getColumn()+2].getTeamColor() != (((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getTeamColor())))){
                    myChessPosition newSpot2 = new myChessPosition(myPosition.getRow()-1, myPosition.getColumn()+2);
                    allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot2));
                }
            }

            allMoves.remove(new myChessMove((myChessPosition) myPosition, (myChessPosition) myPosition));
            return allMoves;
        }
    }

    public static class Rook extends myChessPiece{
        public ChessPiece.PieceType myType;
        public ChessGame.TeamColor myColor;

        public Rook(){
            myType = ChessPiece.PieceType.ROOK;
            myColor = ChessGame.TeamColor.WHITE;
            super.typeUpdate(ChessPiece.PieceType.ROOK);
        }

        public Rook(ChessGame.TeamColor color){
            myType = ChessPiece.PieceType.ROOK;
            myColor = color;
            super.typeUpdate(ChessPiece.PieceType.ROOK);
            super.colorUpdate(color);
        }
        @Override
        public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
            Set<ChessMove> allMoves = new HashSet<>();
            int rowCount = myPosition.getRow();
            int columnCount = myPosition.getColumn();
            while(rowCount <= 7){
                if(myPosition.getRow() != rowCount && ((myChessBoard) board).myBoard[rowCount][columnCount] != null){
                    if(((myChessBoard) board).myBoard[rowCount][columnCount].getTeamColor() == ((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getTeamColor()){
                        //System.out.println("blar1: " + rowCount + ", " + columnCount);
                        break;
                    }else{
                        //System.out.println("dar1");
                        myChessPosition newSpot = new myChessPosition(rowCount, myPosition.getColumn());
                        allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                        break;
                    }
                }
                myChessPosition newSpot = new myChessPosition(rowCount, myPosition.getColumn());
                allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                ++rowCount;
            }
            rowCount = myPosition.getRow();
            columnCount = myPosition.getColumn();
            while(rowCount >= 0){
                if(myPosition.getRow() != rowCount && ((myChessBoard) board).myBoard[rowCount][columnCount] != null){
                    if(((myChessBoard) board).myBoard[rowCount][columnCount].getTeamColor() == ((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getTeamColor()){
                        //System.out.println("blar2");
                        break;
                    }else{
                        ///System.out.println("dar2");
                        myChessPosition newSpot = new myChessPosition(rowCount, myPosition.getColumn());
                        allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                        break;
                    }
                }


                myChessPosition newSpot = new myChessPosition(rowCount, myPosition.getColumn());
                allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                --rowCount;
            }
            rowCount = myPosition.getRow();
            columnCount = myPosition.getColumn();
            while(columnCount <= 7){
                if(myPosition.getColumn() != columnCount && ((myChessBoard) board).myBoard[rowCount][columnCount] != null){
                    if(((myChessBoard) board).myBoard[rowCount][columnCount].getTeamColor() == ((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getTeamColor()){
                        //System.out.println("blar3");
                        break;
                    }else{
                        //System.out.println("dar3");
                        myChessPosition newSpot = new myChessPosition(myPosition.getRow(), columnCount);
                        allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                        break;
                    }
                }

                myChessPosition newSpot = new myChessPosition(myPosition.getRow(), columnCount);
                allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                ++columnCount;
            }
            rowCount = myPosition.getRow();
            columnCount = myPosition.getColumn();
            while(columnCount >= 0){
                if(myPosition.getColumn() != columnCount && ((myChessBoard) board).myBoard[rowCount][columnCount] != null){
                    if(((myChessBoard) board).myBoard[rowCount][columnCount].getTeamColor() == ((myChessBoard) board).myBoard[myPosition.getRow()][myPosition.getColumn()].getTeamColor()){
                        //System.out.println("blar4");
                        break;
                    }else{
                        //System.out.println("dar4");
                        myChessPosition newSpot = new myChessPosition(myPosition.getRow(), columnCount);
                        allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                        break;
                    }
                }

                myChessPosition newSpot = new myChessPosition(myPosition.getRow(), columnCount);
                allMoves.add(new myChessMove((myChessPosition) myPosition, newSpot));
                --columnCount;
            }
            allMoves.remove(new myChessMove((myChessPosition) myPosition, (myChessPosition) myPosition));
            return allMoves;
        }
    }
}
