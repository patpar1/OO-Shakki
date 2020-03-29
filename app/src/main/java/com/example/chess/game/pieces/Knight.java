package com.example.chess.game.pieces;

import com.example.chess.game.Board;
import com.example.chess.game.Move;

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
