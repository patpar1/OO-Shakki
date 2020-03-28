package com.example.chess.game.pieces;

import com.example.chess.game.Board;
import com.example.chess.game.Square;

import java.util.ArrayList;

public class Rook extends Piece {

    private static final int[][] moveVectors = {
            {-1, 0},
            {1, 0},
            {0, -1},
            {0, 1}
    };

    public Rook(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public ArrayList<Square> legalMoves(Board board, int row, int col) {
        ArrayList<Square> moveArray = new ArrayList<Square>();
        for (int[] direction : moveVectors) {
            int[] moveCandidate = {row, col};
            while (true) {
                moveCandidate[0] += direction[0];
                moveCandidate[1] += direction[1];
                if (!isOnBoard(moveCandidate[0], moveCandidate[1])) {
                    break;
                }
                if (board.getSquare(moveCandidate[0], moveCandidate[1]).hasPiece()){
                    if (this.isWhite() == board.getSquare(moveCandidate[0], moveCandidate[1]).getPiece().isWhite()) {
                        break;
                    } else {
                        moveArray.add(board.getSquare(moveCandidate[0] , moveCandidate[1]));
                        break;
                    }
                }
                moveArray.add(board.getSquare(moveCandidate[0] , moveCandidate[1]));
            }
        }
        return moveArray;
    }

    @Override
    public String toString() {
       if (this.isWhite()) {
           return "R";
       } else {
           return "r";
       }
    }
}
