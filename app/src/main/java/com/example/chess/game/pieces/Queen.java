package com.example.chess.game.pieces;

import com.example.chess.game.Board;
import com.example.chess.game.Square;

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
