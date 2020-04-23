package com.example.chess.game;

import com.example.chess.game.pieces.Piece;

import java.io.Serializable;

public class Move implements Serializable {

    private int colStart;
    private int rowStart;
    private int colEnd;
    private int rowEnd;

    private Piece movingPiece;
    private Piece removedPiece;

    private boolean firstMove;

    private Move(int colStart,
                 int rowStart,
                 int colEnd,
                 int rowEnd,
                 Piece movingPiece,
                 Piece removedPiece,
                 boolean firstMove) {
        this.colStart = colStart;
        this.rowStart = rowStart;
        this.colEnd = colEnd;
        this.rowEnd = rowEnd;
        this.movingPiece = movingPiece;
        this.removedPiece = removedPiece;
        this.firstMove = firstMove;
    }

    public Move(Square startingSquare, Square endingSquare) {
        colStart = startingSquare.getCol();
        rowStart = startingSquare.getRow();
        movingPiece = startingSquare.getPiece();
        colEnd = endingSquare.getCol();
        rowEnd = endingSquare.getRow();
        removedPiece = endingSquare.getPiece();
        firstMove = false;
    }

    /* Basic getter and setter methods. */

    public int getColEnd() {
        return colEnd;
    }

    public int getRowEnd() {
        return rowEnd;
    }

    public int getColStart() {
        return colStart;
    }

    public int getRowStart() {
        return rowStart;
    }

    Piece getMovingPiece() {
        return movingPiece;
    }

    void setRemovedPiece(Piece removedPiece) {
        this.removedPiece = removedPiece;
    }

    Piece getRemovedPiece() {
        return removedPiece;
    }

    /**
     * Creates a new square and copies the attributes of this move to the created move.
     *
     * @return A copy of this move.
     */
    Move copy() {
        return new Move(colStart, rowStart, colEnd, rowEnd, movingPiece, removedPiece, firstMove);
    }

    /**
     * Makes this move on the game board.
     *
     * @param board Current state of the board.
     */
    void makeMove(Board board) {
        board.getSquare(rowEnd, colEnd).setPiece(movingPiece);
        board.getSquare(rowStart, colStart).setPiece(null);
    }

    /**
     * Sets the moving pieces first move status, when the player makes first move with the piece.
     */
    void setFirstMove() {
        if (!movingPiece.isMoved()) {
            firstMove = true;
            movingPiece.setMoved(true);
        }
    }

    /**
     * Makes the final move on the board. Used to prevent the isCheck function changing the isMoved
     * status.
     *
     * @param board Current state of the board.
     */
    void makeFinalMove(Board board) {
        makeMove(board);
        setFirstMove();
    }

    /**
     * Undoes this move on the game board. Resets also the pieces moving status, if the move is
     * first move made with the chosen piece.
     *
     * @param board Current state of the board.
     */
    void undoMove(Board board) {
        board.getSquare(rowStart, colStart).setPiece(movingPiece);
        board.getSquare(rowEnd, colEnd).setPiece(removedPiece);
        if (firstMove) {
            movingPiece.setMoved(false);
        }
    }

    /**
     * Class for the pawn double move. It is identical to the superclass but it is used for
     * detecting this kind of move.
     */
    public static class PawnDoubleMove extends Move {
        public PawnDoubleMove(Square startingSquare, Square endingSquare) {
            super(startingSquare, endingSquare);
        }
    }

    /**
     * Inherited class from the Move superclass. Used to make the pawn en passant move on the
     * game board. En passant move has different moving piece ending square from the attacked
     * square so this class overrides the methods for making and undoing moves and sets the
     * correct removed squares and ending squares to this move.
     */
    public static class PawnEnPassantMove extends Move {

        public PawnEnPassantMove(Square startingSquare, Square endingSquare) {
            super(startingSquare, endingSquare);
        }

        private Square getRemovedSquare(Board board) {
            if (super.movingPiece.isWhite()) {
                return board.getSquare(getRowEnd() + 1, getColEnd());

            } else {
                return board.getSquare(getRowEnd() - 1, getColEnd());
            }
        }

        @Override
        void makeMove(Board board) {
            super.makeMove(board);
            Square removedSquare = getRemovedSquare(board);
            setRemovedPiece(removedSquare.getPiece());
            removedSquare.setPiece(null);
        }

        @Override
        void undoMove(Board board) {
            super.undoMove(board);
            Square removedSquare = getRemovedSquare(board);
            removedSquare.setPiece(getRemovedPiece());
        }

        @Override
        PawnEnPassantMove copy() {
            return (PawnEnPassantMove) super.copy();
        }
    }

    /**
     * Inherited class from the Move superclass. Used to make the castling move on the board.
     * Castling move makes two moves on the board. The super class stores the king's move and
     * this subclass creates a new move for the rook's movement.
     */
    public static class CastlingMove extends Move {

        private Move rookMove;

        private CastlingMove(int colStart,
                             int rowStart,
                             int colEnd,
                             int rowEnd,
                             Piece movingPiece,
                             Piece removedPiece,
                             boolean firstMove,
                             Move rookMove) {
            super(colStart, rowStart, colEnd, rowEnd, movingPiece, removedPiece, firstMove);
            this.rookMove = rookMove;
        }

        public CastlingMove(Square startingSquare,
                            Square endingSquare,
                            Square rookStartingSquare,
                            Square rookEndingSquare) {
            super(startingSquare, endingSquare);
            rookMove = new Move(rookStartingSquare, rookEndingSquare);
        }

        @Override
        void makeMove(Board board) {
            super.makeMove(board);
            rookMove.makeMove(board);
        }

        @Override
        void undoMove(Board board) {
            super.undoMove(board);
            rookMove.undoMove(board);
        }

        @Override
        void setFirstMove() {
            super.setFirstMove();
            rookMove.setFirstMove();
        }

        @Override
        CastlingMove copy() {
            return new CastlingMove(super.colStart,
                    super.rowStart,
                    super.colEnd,
                    super.rowEnd,
                    super.movingPiece,
                    super.removedPiece,
                    super.firstMove,
                    rookMove.copy());
        }
    }
}
