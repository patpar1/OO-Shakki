package com.example.chess.game;

import com.example.chess.game.pieces.Piece;

import java.io.Serializable;

public class Square implements Serializable {

    private Piece piece;
    private final int col;
    private final int row;

    Square(int row, int col) {
        piece = null;
        this.col = col;
        this.row = row;
    }

    Square(Piece piece, int row, int col) {
        this.piece = piece;
        this.col = col;
        this.row = row;
    }

    /**
     * Creates a new square and copies the attributes of this square to the created square.
     *
     * @return A copy of this square.
     */
    Square copy() {
        return new Square(piece, row, col);
    }

    /* Basic getter and setter methods. */

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
