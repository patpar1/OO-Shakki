package com.example.chess.game.pieces;

import com.example.chess.game.Board;
import com.example.chess.game.Move;
import com.example.chess.game.Square;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Piece implements Serializable {
    private boolean isWhite;
    private boolean isMoved;
    private int pieceValue;

    protected Piece(boolean isWhite, int pieceValue) {
        this.isWhite = isWhite;
        this.isMoved = false;
        this.pieceValue = pieceValue;
    }

    // Calculates legal moves. Returns ArrayList of legal moves.
    public abstract ArrayList<Move> legalMoves(Board board, int row, int col);

    public abstract int getDrawable();

    public ArrayList<Move> legalMoves(Board board, Square square) {
        return legalMoves(board, square.getRow(), square.getCol());
    }

    public boolean isWhite() {
        return isWhite;
    }

    public boolean isMoved() {
        return isMoved;
    }

    public void setMoved(boolean moved) {
        isMoved = moved;
    }

    public int getPieceValue() {
        return pieceValue;
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
