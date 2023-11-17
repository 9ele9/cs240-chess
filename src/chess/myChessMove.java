package chess;

import java.util.Objects;

public class myChessMove implements ChessMove{
    private final myChessPosition startPosition;
    private final myChessPosition endPosition;
    private ChessPiece.PieceType promotion = null;

    public myChessMove(myChessPosition start, myChessPosition end){
        startPosition = start;
        endPosition = end;
    }

    public myChessMove(myChessPosition start, myChessPosition end, ChessPiece.PieceType type){
        startPosition = start;
        endPosition = end;
        promotion = type;
    }

    @Override
    public String toString(){
        return "["+ getStartPosition().toString() + " -> " + getEndPosition().toString() +"]";
    }
    /*
    @Override
    public boolean equals(Object e){
        if(e.getClass() == getClass()){
            return ((myChessMove) e).getStartPosition() == getStartPosition() && ((myChessMove) e).getEndPosition() == getEndPosition();
        }else{
            return false;
        }
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        boolean sameStart = getStartPosition().toString().equals(((myChessMove) o).getStartPosition().toString());
        boolean sameEnd = getEndPosition().toString().equals(((myChessMove) o).getEndPosition().toString());
        return (sameStart && sameEnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPosition, endPosition);
    }

    @Override
    public boolean equals(myChessMove othermove) {
        boolean sameStart = getStartPosition().toString().equals(othermove.getStartPosition().toString());
        boolean sameEnd = getEndPosition().toString().equals(othermove.getEndPosition().toString());
        return (sameStart && sameEnd);
        //return hashCode() == othermove.hashCode();
    }

    @Override
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    @Override
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    @Override
    public ChessPiece.PieceType getPromotionPiece() {
        return promotion;
    }



}
