package com.example.shakki.game.pieces;

import com.example.shakki.game.Board;
import com.example.shakki.game.Square;

import java.util.ArrayList;

public class Pawn extends Piece {

    private static final int[][] moveCandidates = {
            {-1, 0}, // White pawn move
            {-2, 0}, // White pawn double-step
            {-1, 1}, // White pawn capture right
            {-1, -1}, // White pawn capture left

            {1, 0}, // Black pawn move
            {2, 0}, // Black pawn double-step
            {1, 1}, // Black pawn capture right
            {1, -1} // Black pawn capture left
    };

    public Pawn(boolean isWhite) {
        super(isWhite);
    }


    @Override
    public ArrayList<Square> legalMoves(Board board, int row, int col) {
        return null;
    }

    @Override
    public String toString() {
        if (this.isWhite()) {
            return "P";
        } else {
            return "p";
        }
    }

}
