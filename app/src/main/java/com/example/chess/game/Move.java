package com.example.chess.game;

import com.example.chess.game.pieces.Piece;

import java.io.Serializable;

public class Move implements Serializable {

    private final int colStart;
    private final int rowStart;
    private final int colEnd;
    private final int rowEnd;

    private Piece movingPiece;
    private Piece removedPiece;

    private boolean firstMove;

    private Piece pieceBeforePromotion;

    private Move(int colStart,
                 int rowStart,
                 int colEnd,
                 int rowEnd,
                 Piece movingPiece,
                 Piece removedPiece,
                 boolean firstMove,
                 Piece pieceBeforePromotion) {
        this.colStart = colStart;
        this.rowStart = rowStart;
        this.colEnd = colEnd;
        this.rowEnd = rowEnd;
        this.movingPiece = movingPiece;
        this.removedPiece = removedPiece;
        this.firstMove = firstMove;
        this.pieceBeforePromotion = pieceBeforePromotion;
    }

    public Move(Square startingSquare, Square endingSquare) {
        colStart = startingSquare.getCol();
        rowStart = startingSquare.getRow();
        movingPiece = startingSquare.getPiece();
        colEnd = endingSquare.getCol();
        rowEnd = endingSquare.getRow();
        removedPiece = endingSquare.getPiece();
        firstMove = false;
        pieceBeforePromotion = null;
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

    public Piece getMovingPiece() {
        return movingPiece;
    }

    public void setPromotion(Piece piece) {
        pieceBeforePromotion = movingPiece;
        movingPiece = piece;
    }

    Piece getRemovedPiece() {
        return removedPiece;
    }

    void setRemovedPiece(Piece removedPiece) {
        this.removedPiece = removedPiece;
    }

    /**
     * Creates a new Move and copies the attributes of this move to the created move.
     *
     * @return A copy of this move.
     */
    public Move copy() {
        return new Move(colStart, rowStart, colEnd, rowEnd, movingPiece, removedPiece, firstMove, pieceBeforePromotion);
    }

    /**
     * Makes this move on the game board.
     *
     * @param board Current state of the board.
     */
    public void makeMove(Board board) {
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
        if (pieceBeforePromotion != null) {
            board.getSquare(rowStart, colStart).setPiece(pieceBeforePromotion);
        } else {
            board.getSquare(rowStart, colStart).setPiece(movingPiece);
        }
        board.getSquare(rowEnd, colEnd).setPiece(removedPiece);
        if (firstMove) {
            movingPiece.setMoved(false);
        }
    }

    /**
     * Class for the pawn double move. It is identical to the superclass but it is used for
     * detecting double moves to set the en passant target square.
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

        /**
         * Gets the square which was targeted on the pawn en passant move.
         *
         * @param board Current status of the board.
         * @return The square targeted by the en passant move.
         */
        private Square getRemovedSquare(Board board) {
            if (super.movingPiece.isWhite()) {
                return board.getSquare(getRowEnd() + 1, getColEnd());

            } else {
                return board.getSquare(getRowEnd() - 1, getColEnd());
            }
        }

        /**
         * Makes this move on the game board.
         *
         * @param board Current state of the board.
         */
        @Override
        public void makeMove(Board board) {
            super.makeMove(board);
            Square removedSquare = getRemovedSquare(board);
            setRemovedPiece(removedSquare.getPiece());
            removedSquare.setPiece(null);
        }

        /**
         * Undoes this move on the game board. Resets also the pieces moving status, if the move is
         * first move made with the chosen piece.
         *
         * @param board Current state of the board.
         */
        @Override
        void undoMove(Board board) {
            super.undoMove(board);
            Square removedSquare = getRemovedSquare(board);
            removedSquare.setPiece(getRemovedPiece());
        }
    }

    /**
     * Inherited class from the Move superclass. Used to make the castling move on the board.
     * Castling move makes two moves on the board. The super class stores the king's move and
     * this subclass creates a new move for the rook's movement.
     */
    public static class CastlingMove extends Move {

        private final Move rookMove;

        private CastlingMove(int colStart,
                             int rowStart,
                             int colEnd,
                             int rowEnd,
                             Piece movingPiece,
                             Piece removedPiece,
                             boolean firstMove,
                             Piece pieceBeforePromotion,
                             Move rookMove) {
            super(colStart, rowStart, colEnd, rowEnd, movingPiece, removedPiece, firstMove, pieceBeforePromotion);
            this.rookMove = rookMove;
        }

        public CastlingMove(Square startingSquare,
                            Square endingSquare,
                            Square rookStartingSquare,
                            Square rookEndingSquare) {
            super(startingSquare, endingSquare);
            rookMove = new Move(rookStartingSquare, rookEndingSquare);
        }

        /**
         * Makes this move on the game board.
         *
         * @param board Current state of the board.
         */
        @Override
        public void makeMove(Board board) {
            super.makeMove(board);
            rookMove.makeMove(board);
        }

        /**
         * Undoes this move on the game board. Resets also the pieces moving status, if the move is
         * first move made with the chosen piece.
         *
         * @param board Current state of the board.
         */
        @Override
        void undoMove(Board board) {
            super.undoMove(board);
            rookMove.undoMove(board);
        }

        /**
         * Sets the moving pieces first move status, when the player makes first move with the piece.
         */
        @Override
        void setFirstMove() {
            super.setFirstMove();
            rookMove.setFirstMove();
        }

        /**
         * Creates a new Move and copies the attributes of this move to the created move.
         *
         * @return A copy of this move.
         */
        @Override
        public CastlingMove copy() {
            return new CastlingMove(super.colStart,
                    super.rowStart,
                    super.colEnd,
                    super.rowEnd,
                    super.movingPiece,
                    super.removedPiece,
                    super.firstMove,
                    super.pieceBeforePromotion,
                    rookMove.copy());
        }
    }
}
