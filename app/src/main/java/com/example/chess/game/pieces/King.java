package com.example.chess.game.pieces;

import com.example.chess.game.Board;
import com.example.chess.game.Move;

import java.util.ArrayList;

public class King extends Piece {

    private static final int[][] moveCandidates = {
            {1, 0}, // Down
            {1, 1}, // Down Right
            {1, -1}, // Down Left
            {-1, 0}, // Up
            {-1, 1}, // Up Right
            {-1, -1}, // Up Left
            {0, 1}, // Right
            {0, -1} // Left
    };

    public King(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public ArrayList<Move> legalMoves(Board board, int row, int col) {
        ArrayList<Move> moveArray = new ArrayList<>();
        for (int[] move : moveCandidates) {
            int[] moveCandidate = {(row + move[0]), (col + move[1])};
            if (!isOnBoard(moveCandidate[0], moveCandidate[1])) {
                continue;
            }
            if (board.getSquare(moveCandidate[0], moveCandidate[1]).hasPiece()) {
                if (this.isWhite() == board.getSquare(moveCandidate[0], moveCandidate[1]).getPiece().isWhite()) {
                    continue;
                }

            }
            moveArray.add(new Move(board.getSquare(row, col), board.getSquare(moveCandidate[0] , moveCandidate[1])));
        }
        return moveArray;
    }

    @Override
    public String toString() {
        if (this.isWhite()) {
            return "K";
        } else {
            return "k";
        }
    }
}
