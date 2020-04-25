package com.example.chess.game.pieces;

import com.example.chess.R;
import com.example.chess.game.Board;
import com.example.chess.game.Move;

import java.io.Serializable;
import java.util.ArrayList;

public class Queen extends Piece implements Serializable {

    private static final int ABSOLUTE_PIECE_VALUE = 90;
    private static final int[][] moveVectors = {
            {-1, 0}, // Up
            {1, 0}, // Down
            {0, -1}, // Left
            {0, 1}, // Right
            {-1, 1}, // Right Up
            {1, 1}, // Right Down
            {-1, -1}, // Left Up
            {1, -1} // Left Down
    };

    public Queen(boolean isWhite) {
        super(isWhite, isWhite ? ABSOLUTE_PIECE_VALUE : -ABSOLUTE_PIECE_VALUE);
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
        return isWhite() ? R.drawable.wq : R.drawable.bq;
    }

}
