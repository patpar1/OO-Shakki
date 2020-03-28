package com.example.shakki.game.pieces;

import com.example.shakki.game.Board;
import com.example.shakki.game.Square;

import java.util.ArrayList;

public abstract class Piece {
    private boolean isWhite;

    public Piece(boolean isWhite) {
        this.isWhite = isWhite;
    }

    // Calculates legal moves. Returns ArrayList of legal moves.
    public abstract ArrayList<Square> legalMoves(Board board, int row, int col);

    public ArrayList<Square> legalMoves(Board board, Square square) {
        return legalMoves(board, square.getRow(), square.getCol());
    }

    public boolean isWhite() {
        return isWhite;
    }

    public static boolean isOnBoard(int y, int x) {
        if ((x > 7) || (y > 7)) {
            return false;
        }
        if ((x < 0) || (y < 0)) {
            return false;
        }
        return true;
    }

}
