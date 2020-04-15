package com.example.chess.game.pieces;

import com.example.chess.R;
import com.example.chess.game.Board;
import com.example.chess.game.Move;

import java.io.Serializable;
import java.util.ArrayList;

public class Bishop extends Piece implements Serializable {

    private static final int[][] moveVectors = {
            {-1, 1}, // Right Up
            {1, 1}, // Right Down
            {-1, -1}, // Left Up
            {1, -1} // Left Down
    };

    public Bishop(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public ArrayList<Move> legalMoves(Board board, int row, int col) {
        ArrayList<Move> moveArray = new ArrayList<>();
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
                        moveArray.add(new Move(board.getSquare(row, col), board.getSquare(moveCandidate[0] , moveCandidate[1])));
                        break;
                    }
                }
                moveArray.add(new Move(board.getSquare(row, col), board.getSquare(moveCandidate[0] , moveCandidate[1])));
            }
        }
        return moveArray;
    }

    public int getDrawable() {
        return isWhite() ? R.drawable.wb : R.drawable.bb;
    }
}
