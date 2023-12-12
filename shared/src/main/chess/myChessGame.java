package chess;

import java.util.Collection;
import java.util.HashSet;

public class myChessGame implements ChessGame {
    ChessGame.TeamColor currTurn;
    myChessBoard game;
    String serialGame;
    public myChessGame(){
        currTurn = ChessGame.TeamColor.WHITE;
        game = new myChessBoard();
        serialGame = game.serialize();
    }
    @Override
    public ChessGame.TeamColor getTeamTurn() {
        return currTurn;
    }

    @Override
    public void setTeamTurn(ChessGame.TeamColor team) {
        currTurn = team;
    }

    @Override
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> thisPiece = game.getPiece(startPosition).pieceMoves(game, startPosition);
        Collection<ChessMove> validMoves = new HashSet<>();

        for(ChessMove eachMove : thisPiece){
            ChessPiece savePiece = game.myBoard[eachMove.getEndPosition().getRow()][eachMove.getEndPosition().getColumn()];
            game.addPiece(eachMove.getEndPosition(), game.getPiece(eachMove.getStartPosition()));
            game.delPiece(eachMove.getStartPosition());
            boolean inCheck = isInCheck(game.getPiece(eachMove.getEndPosition()).getTeamColor());
            game.addPiece(eachMove.getStartPosition(), game.getPiece(eachMove.getEndPosition()));
            if(savePiece != null){
                game.addPiece(eachMove.getEndPosition(), savePiece);
            }else{
                game.delPiece(eachMove.getEndPosition());
            }
            if(!inCheck){
                validMoves.add(eachMove);
            }
        }
        return validMoves;
    }

    @Override
    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> thisPiece = validMoves(move.getStartPosition());
        if(thisPiece == null || game.getPiece(move.getStartPosition()).getTeamColor()!=getTeamTurn()){
            throw new InvalidMoveException();
        }
        if(thisPiece.contains(move)){
            ChessPiece savePiece = null;
            if(game.myBoard[move.getEndPosition().getRow()][move.getEndPosition().getColumn()] != null){
                savePiece = game.myBoard[move.getEndPosition().getRow()][move.getEndPosition().getColumn()];
            }
            boolean currInCheck = isInCheck(currTurn);
            game.addPiece(move.getEndPosition(), game.getPiece(move.getStartPosition()));
            game.delPiece(move.getStartPosition());
            if(currInCheck && isInCheck(currTurn)){
                game.addPiece(move.getStartPosition(), game.getPiece(move.getEndPosition()));
                if(savePiece != null){
                    game.addPiece(move.getEndPosition(), savePiece);
                }else{
                    game.delPiece(move.getEndPosition());
                }
                throw new InvalidMoveException("If you are in check, you must move out of check.");
            }
            if(move.getEndPosition().getRow() == 7 || move.getEndPosition().getRow() == 0){
                if(game.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.PAWN){
                    ChessPiece.PieceType promote = move.getPromotionPiece();
                    if(promote != null){
                        if(promote == ChessPiece.PieceType.BISHOP){
                            game.addPiece(move.getEndPosition(), new myChessPiece.Bishop(currTurn));
                        }
                        if(promote == ChessPiece.PieceType.ROOK){
                            game.addPiece(move.getEndPosition(), new myChessPiece.Rook(currTurn));
                        }
                        if(promote == ChessPiece.PieceType.KNIGHT){
                            game.addPiece(move.getEndPosition(), new myChessPiece.Knight(currTurn));
                        }
                        if(promote == ChessPiece.PieceType.QUEEN){
                            game.addPiece(move.getEndPosition(), new myChessPiece.Queen(currTurn));
                        }
                        if(promote == ChessPiece.PieceType.KING){
                            throw new InvalidMoveException("You cannot promote to a king!");
                        }
                        if(promote == ChessPiece.PieceType.PAWN){
                            throw new InvalidMoveException("You are already a pawn!");
                        }
                    }
                }
            }
        }else{
            throw new InvalidMoveException("Not a valid move");
        }

        if(getTeamTurn() == ChessGame.TeamColor.WHITE){
            setTeamTurn(ChessGame.TeamColor.BLACK);
        }else{
            setTeamTurn(ChessGame.TeamColor.WHITE);
        }

    }

    @Override
    public boolean isInCheck(ChessGame.TeamColor teamColor) {
        myChessPosition myKing = null;
        for(int g = 0; g < 8; ++g){
            for(int h = 0; h < 8; ++h){
                if(game.myBoard[g][h] != null && game.myBoard[g][h].getPieceType() == ChessPiece.PieceType.KING && game.myBoard[g][h].getTeamColor() == teamColor){
                    myKing = new myChessPosition(g, h);
                    break;
                }
            }
            if(myKing != null){
                break;
            }
        }
        if(myKing == null){
            return false;
        }else{
            Collection<ChessPosition> allEnemyMoves = new HashSet<>();
            for(int g = 0; g < 8; ++g){
                for(int h = 0; h < 8; ++h){
                    if(game.myBoard[g][h] != null && game.myBoard[g][h].getTeamColor() != teamColor){
                        Collection<ChessMove> validPieceMoves = game.myBoard[g][h].pieceMoves(game, new myChessPosition(g,h));
                        for(ChessMove each : validPieceMoves){
                            allEnemyMoves.add(each.getEndPosition());
                        }
                    }
                }
            }
            return allEnemyMoves.contains(myKing);
        }
    }

    @Override
    public boolean isInCheckmate(ChessGame.TeamColor teamColor) {
        myChessPosition myKing = null;
        Collection<ChessMove> kingMoves = new HashSet<>();
        boolean hasNoEscape = true;
        for(int g = 0; g < 8; ++g){
            for(int h = 0; h < 8; ++h){
                if(game.myBoard[g][h] != null && game.myBoard[g][h].getPieceType() == ChessPiece.PieceType.KING && game.myBoard[g][h].getTeamColor() == teamColor){
                    myKing = new myChessPosition(g, h);
                    kingMoves = game.myBoard[g][h].pieceMoves(game, new myChessPosition(g,h));
                    break;
                }
            }
            if(myKing != null){
                break;
            }
        }

        if(myKing == null){
            return false;
        }else if(kingMoves.isEmpty()){
            return isInCheck(teamColor);
        }else {
            for(ChessMove eachKingMove : kingMoves) {
                myChessBoard testBoard = new myChessBoard();
                for (int i = 0; i < 8; ++i) {
                    System.arraycopy(game.myBoard[i], 0, testBoard.myBoard[i], 0, 8);
                }
                testBoard.addPiece(eachKingMove.getEndPosition(), new myChessPiece.King(teamColor));
                testBoard.delPiece(eachKingMove.getStartPosition());
                Collection<ChessPosition> allEnemyMoves = new HashSet<>();
                for (int g = 0; g < 8; ++g) {
                    for (int h = 0; h < 8; ++h) {
                        if (testBoard.myBoard[g][h] != null && testBoard.myBoard[g][h].getTeamColor() != teamColor) {
                            Collection<ChessMove> validPieceMoves = testBoard.myBoard[g][h].pieceMoves(testBoard, new myChessPosition(g, h));
                            for (ChessMove eachEnemyMove : validPieceMoves) {
                                allEnemyMoves.add(eachEnemyMove.getEndPosition());
                            }
                        }
                    }
                }
                if(!allEnemyMoves.contains(eachKingMove.getEndPosition())) {
                    hasNoEscape = false;
                    break;
                }
            }
        }
        return hasNoEscape;
    }

    @Override
    public boolean isInStalemate(ChessGame.TeamColor teamColor) {
        myChessPosition myKing = null;
        Collection<ChessMove> kingMoves = new HashSet<>();
        for(int g = 0; g < 8; ++g){
            for(int h = 0; h < 8; ++h){
                if(game.myBoard[g][h] != null && game.myBoard[g][h].getPieceType() == ChessPiece.PieceType.KING && game.myBoard[g][h].getTeamColor() == teamColor){
                    myKing = new myChessPosition(g, h);
                    kingMoves = game.myBoard[g][h].pieceMoves(game, new myChessPosition(g,h));
                    break;
                }
            }
            if(myKing != null){
                break;
            }
        }
        if(!isInCheck(teamColor)){
            Collection<ChessMove> allMyMoves = new HashSet<>();
            for(int g = 0; g < 8; ++g){
                for(int h = 0; h < 8; ++h){
                    if(game.myBoard[g][h] != null && game.myBoard[g][h].getTeamColor() == teamColor){
                        Collection<ChessMove> validPieceMoves = game.myBoard[g][h].pieceMoves(game, new myChessPosition(g,h));
                        allMyMoves.addAll(validPieceMoves);
                    }
                }
            }

            for(ChessMove eachMove : allMyMoves){
                ChessPiece savePiece = game.myBoard[eachMove.getEndPosition().getRow()][eachMove.getEndPosition().getColumn()];

                game.addPiece(eachMove.getEndPosition(), game.getPiece(eachMove.getStartPosition()));
                game.delPiece(eachMove.getStartPosition());
                boolean stillCheck = isInCheck(teamColor);
                game.addPiece(eachMove.getStartPosition(), game.getPiece(eachMove.getEndPosition()));
                if(savePiece != null){
                    game.addPiece(eachMove.getEndPosition(), savePiece);
                }else{
                    game.delPiece(eachMove.getEndPosition());
                }
                if(!stillCheck){
                    return false;
                }
            }
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void setBoard(ChessBoard board) {
        game = (myChessBoard) board;
    }

    @Override
    public ChessBoard getBoard() {
        return game;
    }

    public void setSerialGame(String serialGame) {
        this.serialGame = serialGame;
    }

    public String getSerialGame(){
        return game.serialize();
    }

    public void serialStringIntoBoard(String serial){
        myChessBoard baseBoard = new myChessBoard();
        baseBoard.deserializeToRealBoard(baseBoard.deserialize(serial));
        setBoard(baseBoard);
    }
}
