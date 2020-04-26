package com.example.chess.game.pieces;

import com.example.chess.game.Board;
import com.example.chess.game.Move;
import com.example.chess.game.Square;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Piece implements Serializable {
    private final boolean isWhite;
    private boolean isMoved;
    private final double pieceValue;
    private final double[][] BOARD_POSITION_BIAS;

    Piece(boolean isWhite, double pieceValue, double[][] BOARD_POSITION_BIAS) {
        this.isWhite = isWhite;
        this.isMoved = false;
        this.pieceValue = pieceValue;
        this.BOARD_POSITION_BIAS = BOARD_POSITION_BIAS;
    }

    /**
     * Calculates if these coordinates are on the game board.
     *
     * @param row Row of the coordinates.
     * @param col Column of the coordinates.
     * @return Boolean value. True if these coordinates are on the game board.
     */
    static boolean isOnBoard(int row, int col) {
        return (col <= 7) && (row <= 7) && (col >= 0) && (row >= 0);
    }

    /**
     * Calculates legal moves of a piece. These moves are not necessarily legal and the rest of the
     * calculation is done on the player class.
     *
     * @param board Current board object.
     * @param row   Row of calculating piece.
     * @param col   Column of calculating piece.
     * @return An ArrayList of all possible moves.
     */
    protected abstract ArrayList<Move> legalMoves(Board board, int row, int col);

    /**
     * Returns the drawable resource of this piece.
     *
     * @return Drawable resource of this piece.
     */
    public abstract int getDrawable();

    /**
     * Alternative method for calculating the legal moves. Calculates the row and column values of
     * the square object.
     *
     * @param board  Current board object.
     * @param square Square where this piece is.
     * @return An ArrayList of all possible moves.
     */
    public ArrayList<Move> legalMoves(Board board, Square square) {
        return legalMoves(board, square.getRow(), square.getCol());
    }

    /* Some simple getter and setter methods. */

    public boolean isWhite() {
        return isWhite;
    }

    public boolean isMoved() {
        return isMoved;
    }

    public void setMoved(boolean moved) {
        isMoved = moved;
    }

    public double getPieceValue(int row, int col) {
        return pieceValue + getBoardPositionBias(row, col);
    }

    private double getBoardPositionBias(int row, int col) {
        return BOARD_POSITION_BIAS[row][col];
    }
}
