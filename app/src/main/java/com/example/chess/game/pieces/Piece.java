package com.example.chess.game.pieces;

import com.example.chess.game.Board;
import com.example.chess.game.Move;
import com.example.chess.game.Square;

import java.util.ArrayList;

public abstract class Piece {
    private boolean isWhite;
    private boolean isMoved;

    public Piece(boolean isWhite) {
        this.isWhite = isWhite;
        this.isMoved = false;
    }

    // Calculates legal moves. Returns ArrayList of legal moves.
    public abstract ArrayList<Move> legalMoves(Board board, int row, int col);

    public ArrayList<Move> legalMoves(Board board, Square square) {
        return legalMoves(board, square.getRow(), square.getCol());
    }

    public boolean isWhite() {
        return isWhite;
    }

    public boolean isMoved() {
        return isMoved;
    }

    public void hasMoved() {
        isMoved = true;
    }

    static boolean isOnBoard(int y, int x) {
        if ((x > 7) || (y > 7)) {
            return false;
        }
        if ((x < 0) || (y < 0)) {
            return false;
        }
        return true;
    }


}
