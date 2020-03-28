package com.example.shakki.game;

import com.example.shakki.game.pieces.Piece;

public class Square {

    private Piece piece;
    private int col;
    private int row;

    public Square(int row, int col) {
        piece = null;
        this.col = col;
        this.row = row;
    }

    public Square(Piece piece, int row, int col) {
        this.piece = piece;
        this.col = col;
        this.row = row;
    }

    Square copy() {
        return new Square(piece, row, col);
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public boolean hasPiece() {
        return (piece != null);
    }
}
