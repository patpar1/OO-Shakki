package com.example.shakki.game.pieces;

import com.example.shakki.game.Board;
import com.example.shakki.game.Square;

import java.util.ArrayList;

public class Queen extends Piece {

    public Queen(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public ArrayList<Square> legalMoves(Board board, int row, int col) {
        return null;
    }

    @Override
    public String toString() {
        if (this.isWhite()) {
            return "Q";
        } else {
            return "q";
        }
    }

}
