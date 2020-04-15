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

    Piece getMovingPiece() {
        return movingPiece;
    }

    void setRemovedPiece(Piece removedPiece) {
        this.removedPiece = removedPiece;
    }

    Piece getRemovedPiece() {
        return removedPiece;
    }

    Move copy() {
        return new Move(colStart, rowStart, colEnd, rowEnd, movingPiece, removedPiece);
    }

    void makeMove(Board board) {
        board.getSquare(rowEnd, colEnd).setPiece(movingPiece);
        board.getSquare(rowStart, colStart).setPiece(null);
    }

    void undoMove(Board board) {
        board.getSquare(rowStart, colStart).setPiece(movingPiece);
        board.getSquare(rowEnd, colEnd).setPiece(removedPiece);
    }

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

        @Override
        void undoMove(Board board) {
            super.undoMove(board);
            board.getSquare(rookRowStart, rookColStart).setPiece(rookPiece);
            board.getSquare(rookRowEnd, rookColEnd).setPiece(null);
        }
    }

}
