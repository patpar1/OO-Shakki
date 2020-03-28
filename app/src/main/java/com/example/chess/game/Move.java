package com.example.chess.game;

import com.example.chess.game.pieces.Piece;

public class Move {

    private int colStart;
    private int rowStart;
    private int colEnd;
    private int rowEnd;

    private Piece piece;
    private Piece removedPiece;

    private boolean isCheck;
    private boolean isStalemate;
    private boolean isCastle;

    Move(Square startingSquare, Square endingSquare) {
        colStart = startingSquare.getCol();
        rowStart = startingSquare.getRow();
        piece = startingSquare.getPiece();
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

    public Piece getPiece() {
        return piece;
    }
    Piece getRemovedPiece() {
        return removedPiece;
    }

    public void setRemovedPiece(Piece removedPiece) {
        this.removedPiece = removedPiece;
    }

    public boolean isCheck() {
        return isCheck;
    }
    public void setCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public boolean isStalemate() {
        return isStalemate;
    }
    public void setStalemate(boolean isStalemate) {
        this.isStalemate = isStalemate;
    }

    public boolean isCastle() {
        return isCastle;
    }
    public void setCastle(boolean isCastle) {
        this.isCastle = isCastle;
    }

}
