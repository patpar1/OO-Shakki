package com.example.chess.game;

import com.example.chess.game.pieces.Piece;

public class Move {

    private int colStart;
    private int rowStart;
    private int colEnd;
    private int rowEnd;

    private Piece movingPiece;
    private Piece removedPiece;

    public Move(int colStart, int rowStart, int colEnd, int rowEnd, Piece movingPiece, Piece removedPiece) {
        this.colStart = colStart;
        this.rowStart = rowStart;
        this.colEnd = colEnd;
        this.rowEnd = rowEnd;
        this.movingPiece = movingPiece;
        this.removedPiece = removedPiece;
    }

    public Move(Square startingSquare, Square endingSquare) {
        colStart = startingSquare.getCol();
        rowStart = startingSquare.getRow();
        movingPiece = startingSquare.getPiece();
        colEnd = endingSquare.getCol();
        rowEnd = endingSquare.getRow();
        removedPiece = endingSquare.getPiece();
    }

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

    void setRemovedPiece(Piece removedPiece) {
        this.removedPiece = removedPiece;
    }

    Move copy() {
        return new Move(colStart, rowStart, colEnd, rowEnd, movingPiece, removedPiece);
    }

    void makeMove(Board board) {
        board.getSquare(rowEnd, colEnd).setPiece(movingPiece);
        board.getSquare(rowStart, colStart).setPiece(null);
    }

    public static class PawnEnPassantMove extends Move {
        public PawnEnPassantMove(Square startingSquare, Square endingSquare) {
            super(startingSquare, endingSquare);
        }

        @Override
        void makeMove(Board board) {
            super.makeMove(board);
            Square removedSquare;
            if (super.movingPiece.isWhite()) {
                removedSquare = board.getSquare(getRowEnd() + 1, getColEnd());

            } else {
                removedSquare = board.getSquare(getRowEnd() - 1, getColEnd());
            }
            super.setRemovedPiece(removedSquare.getPiece());
            removedSquare.setPiece(null);
        }
    }

    public static class CastlingMove extends Move {

        private int rookColStart;
        private int rookRowStart;
        private int rookColEnd;
        private int rookRowEnd;

        private Piece rookPiece;

        public CastlingMove(Square kingStartingSquare, Square kingEndingSquare, Square rookStartingSquare, Square rookEndingSquare) {
            super(kingStartingSquare, kingEndingSquare);
            rookColStart = rookStartingSquare.getCol();
            rookRowStart = rookStartingSquare.getRow();
            rookPiece = rookStartingSquare.getPiece();
            rookColEnd = rookEndingSquare.getCol();
            rookRowEnd = rookEndingSquare.getRow();
        }

        @Override
        void makeMove(Board board) {
            super.makeMove(board);
            board.getSquare(rookRowEnd, rookColEnd).setPiece(rookPiece);
            board.getSquare(rookRowStart, rookColStart).setPiece(null);
        }
    }

}
