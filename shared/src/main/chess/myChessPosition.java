package chess;

import java.util.Objects;

public class myChessPosition implements ChessPosition {
    int row;
    int column;
    public myChessPosition(int inRow, int inCol){
        row = inRow;
        column = inCol;
    }
    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getColumn() {
        return column;
    }

    @Override
    public String toString(){
        return "(" + row + "," + column + ")";
    }

    public boolean equals(myChessPosition otherPosition){
        return getRow() == otherPosition.getRow() && getColumn() == otherPosition.getColumn();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        boolean sameString = toString().equals(((myChessPosition) o).toString());
        return sameString;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}
