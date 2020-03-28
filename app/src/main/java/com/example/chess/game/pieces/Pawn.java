package com.example.chess.game.pieces;

import com.example.chess.game.Board;
import com.example.chess.game.Game;
import com.example.chess.game.Square;

import java.util.ArrayList;

public class Pawn extends Piece {

    private static final int[][] moveCandidates = {
            {-1, 0}, // White pawn move
            {-2, 0}, // White pawn double-step
            {-1, 1}, // White pawn capture right
            {-1, -1}, // White pawn capture left
    };

    public Pawn(boolean isWhite) {
        super(isWhite);
    }

    private int[][] getMoveCandidates() {
        int[][] playerCandidates = moveCandidates.clone();
        if (!isWhite()) {
            for (int i = 0; i < moveCandidates.length; i++) {
                playerCandidates[i][0] *= -1;
                playerCandidates[i][1] *= -1;
            }
        }

        return playerCandidates;
    }

    @Override
    public ArrayList<Square> legalMoves(Board board, int row, int col) {
        ArrayList<Square> moveArray = new ArrayList<>();
        int[][] moveCandidates = getMoveCandidates();

        // Straight move
        int[] moveCandidate = {(row + moveCandidates[0][0]), col + moveCandidates[0][1]};
        if (isOnBoard(moveCandidate[0], moveCandidate[1])) {
            if (!board.getSquare(moveCandidate[0], moveCandidate[1]).hasPiece()) {
                moveArray.add(board.getSquare(moveCandidate[0] , moveCandidate[1]));

                // Double move
                if (!this.isMoved()) {
                    int[] doubleMoveCandidate = {(row += moveCandidates[1][0]),
                            (col += moveCandidates[1][1])};
                    if (isOnBoard(doubleMoveCandidate[0], doubleMoveCandidate[1])) {
                        if (!board.getSquare(doubleMoveCandidate[0], doubleMoveCandidate[1]).hasPiece()) {
                            moveArray.add(board.getSquare(doubleMoveCandidate[0] , doubleMoveCandidate[1]));
                            Game.setEnPassantTarget(board.getSquare(moveCandidate[0] , moveCandidate[1]));
                        }
                    }
                }
            }
        }

        // Attack moves
        for (int i = 2; i < 4; i++) {
            int[] attackMoveCandidate = {(row + moveCandidates[i][0]), col + moveCandidates[i][1]};
            if (isOnBoard(attackMoveCandidate[0], attackMoveCandidate[1])) {
                if (board.getSquare(attackMoveCandidate[0], attackMoveCandidate[1]).hasPiece()
                && this.isWhite() != board.getSquare(attackMoveCandidate[0], attackMoveCandidate[1]).getPiece().isWhite()) {
                    moveArray.add(board.getSquare(attackMoveCandidate[0], attackMoveCandidate[1]));
                }
            }
        }

        return moveArray;
    }

    @Override
    public String toString() {
        if (this.isWhite()) {
            return "P";
        } else {
            return "p";
        }
    }

}
