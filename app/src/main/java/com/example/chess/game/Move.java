package com.example.chess.game;

import com.example.chess.game.pieces.Piece;

public class Move {

    private int colStart;
    private int rowStart;
    private int colEnd;
    private int rowEnd;

    private Piece movingPiece;
    private Piece removedPiece;

    private boolean isCheck;
    private boolean isStalemate;

    Move(Square startingSquare, Square endingSquare) {
        colStart = startingSquare.getCol();
        rowStart = startingSquare.getRow();
        movingPiece = startingSquare.getPiece();
        colEnd = endingSquare.getCol();
        rowEnd = endingSquare.getRow();
        removedPiece = endingSquare.getPiece();
    }

    int getColStart() {
        return colStart;
    }
    int getRowStart() {
        return rowStart;
    }
    int getColEnd() {
        return colEnd;
    }
    int getRowEnd() {
        return rowEnd;
    }

    Piece getMovingPiece() {
        return movingPiece;
    }
    Piece getRemovedPiece() {
        return removedPiece;
    }

    void setRemovedPiece(Piece removedPiece) {
        this.removedPiece = removedPiece;
    }

    void makeMove(Board board) {
        board.getSquare(rowEnd, colEnd).setPiece(movingPiece);
        board.getSquare(rowStart, colStart).setPiece(null);
    }

    static class PawnEnPassantMove extends Move {
        PawnEnPassantMove(Square startingSquare, Square endingSquare, Piece removedPiece) {
            super(startingSquare, endingSquare);
            super.setRemovedPiece(removedPiece);
        }
    }

    static class CastlingMove extends Move {

        private int rookColStart;
        private int rookRowStart;
        private int rookColEnd;
        private int rookRowEnd;

        private Piece rookPiece;

        CastlingMove(Square kingStartingSquare, Square kingEndingSquare, Square rookStartingSquare, Square rookEndingSquare) {
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
