package com.example.shakki.game.pieces;

import com.example.shakki.game.Board;
import com.example.shakki.game.Square;

import java.util.ArrayList;

public class Knight extends Piece {

    private static final int[][] moveCandidates = {
            {-2, -1},
            {-1, -2},
            {1, -2},
            {2, -1},
            {2, 1},
            {1, 2},
            {-1, 2},
            {-2, 1}
    };

    public Knight(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public ArrayList<Square> legalMoves(Board board, int row, int col) {
        ArrayList<Square> moveArray = new ArrayList<Square>();
        for (int[] move : moveCandidates) {
            int[] moveCandidate = {(row + move[0]), (col + move[1])};

            if (!isOnBoard(moveCandidate[0], moveCandidate[1])) {
                continue;
            }

            if (board.getSquare(moveCandidate[0], moveCandidate[1]).hasPiece()){
                if (this.isWhite() == board.getSquare(moveCandidate[0], moveCandidate[1]).getPiece().isWhite()) {
                    continue;
                }
            }

            moveArray.add(board.getSquare(moveCandidate[0] , moveCandidate[1]));
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
