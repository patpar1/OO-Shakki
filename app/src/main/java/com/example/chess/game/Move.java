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

    public Move(int colStart, int rowStart, int colEnd, int rowEnd, Piece movingPiece, Piece removedPiece, boolean firstMove) {
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
        return new Move(colStart, rowStart, colEnd, rowEnd, movingPiece, removedPiece, firstMove);
    }

    public void makeMove(Board board) {
        board.getSquare(rowEnd, colEnd).setPiece(movingPiece);
        board.getSquare(rowStart, colStart).setPiece(null);
    }

    public void setFirstMove() {
        if (!movingPiece.isMoved()) {
            firstMove = true;
            movingPiece.setMoved(true);
        }
    }

    public void makeFinalMove(Board board) {
        makeMove(board);
        setFirstMove();
    }

    public void undoMove(Board board) {
        board.getSquare(rowStart, colStart).setPiece(movingPiece);
        board.getSquare(rowEnd, colEnd).setPiece(removedPiece);
        if (firstMove) {
            movingPiece.setMoved(false);
        }
    }

    public static class PawnDoubleMove extends Move {
        public PawnDoubleMove(Square startingSquare, Square endingSquare) {
            super(startingSquare, endingSquare);
        }
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
        public void makeMove(Board board) {
            super.makeMove(board);
            Square removedSquare = getRemovedSquare(board);
            setRemovedPiece(removedSquare.getPiece());
            removedSquare.setPiece(null);
        }

        @Override
        public void undoMove(Board board) {
            super.undoMove(board);
            Square removedSquare = getRemovedSquare(board);
            removedSquare.setPiece(getRemovedPiece());
        }

        @Override
        PawnEnPassantMove copy() {
            return (PawnEnPassantMove) super.copy();
        }
    }

    public static class CastlingMove extends Move {

        private int rookColStart;
        private int rookRowStart;
        private int rookColEnd;
        private int rookRowEnd;

        private Piece rookPiece;
        private boolean rookFirstMove;

        public CastlingMove(Square kingStartingSquare, Square kingEndingSquare, Square rookStartingSquare, Square rookEndingSquare) {
            super(kingStartingSquare, kingEndingSquare);
            rookColStart = rookStartingSquare.getCol();
            rookRowStart = rookStartingSquare.getRow();
            rookPiece = rookStartingSquare.getPiece();
            rookColEnd = rookEndingSquare.getCol();
            rookRowEnd = rookEndingSquare.getRow();
            rookFirstMove = false;
        }

        @Override
        public void makeMove(Board board) {
            super.makeMove(board);
            board.getSquare(rookRowEnd, rookColEnd).setPiece(rookPiece);
            board.getSquare(rookRowStart, rookColStart).setPiece(null);
        }

        @Override
        public void undoMove(Board board) {
            super.undoMove(board);
            board.getSquare(rookRowStart, rookColStart).setPiece(rookPiece);
            board.getSquare(rookRowEnd, rookColEnd).setPiece(null);
            if (rookFirstMove) {
                rookPiece.setMoved(false);
            }
        }

        @Override
        public void setFirstMove() {
            super.setFirstMove();
            if (!rookPiece.isMoved()) {
                rookFirstMove = true;
                rookPiece.setMoved(true);
            }
        }

        @Override
        CastlingMove copy() {
            CastlingMove cm = (CastlingMove) super.copy();
            cm.rookColStart = rookColStart;
            cm.rookRowStart = rookRowStart;
            cm.rookColEnd = rookColEnd;
            cm.rookRowEnd = rookRowEnd;
            cm.rookPiece = rookPiece;
            cm.rookFirstMove = rookFirstMove;
            return cm;
        }
    }

}
