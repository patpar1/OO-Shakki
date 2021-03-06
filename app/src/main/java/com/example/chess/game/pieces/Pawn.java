package com.example.chess.game.pieces;

import com.example.chess.R;
import com.example.chess.game.Board;
import com.example.chess.game.BoardUtils;
import com.example.chess.game.Game;
import com.example.chess.game.Move;

import java.io.Serializable;
import java.util.ArrayList;

public class Pawn extends Piece implements Serializable {

    private static final int ABSOLUTE_PIECE_VALUE = 10;
    private static final int[][] moveCandidates = {
            {-1, 0}, // White pawn move
            {-2, 0}, // White pawn double-step
            {-1, 1}, // White pawn capture right
            {-1, -1}, // White pawn capture left
    };
    private static final double[][] BOARD_POSITION_BIAS = {
            {0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0},
            {5.0,  5.0,  5.0,  5.0,  5.0,  5.0,  5.0,  5.0},
            {1.0,  1.0,  2.0,  3.0,  3.0,  2.0,  1.0,  1.0},
            {0.5,  0.5,  1.0,  2.5,  2.5,  1.0,  0.5,  0.5},
            {0.0,  0.0,  0.0,  2.0,  2.0,  0.0,  0.0,  0.0},
            {0.5, -0.5, -1.0,  0.0,  0.0, -1.0, -0.5,  0.5},
            {0.5,  1.0, 1.0,  -2.0, -2.0,  1.0,  1.0,  0.5},
            {0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0}
    };

    public Pawn(boolean isWhite) {
        super(isWhite,
                isWhite ? ABSOLUTE_PIECE_VALUE : -ABSOLUTE_PIECE_VALUE,
                isWhite ? BOARD_POSITION_BIAS : BoardUtils.reverseArray(BOARD_POSITION_BIAS));
    }

    /**
     * Calculates the move candidates based on the color of this piece. Black goes to the positive
     * direction and white goes to the negative direction.
     * @return Move candidates.
     */
    private int[][] getMoveCandidates() {
        int[][] playerCandidates = new int[4][2];
        if (isWhite()) {
            for (int i = 0; i < moveCandidates.length; i++) {
                playerCandidates[i][0] = moveCandidates[i][0];
                playerCandidates[i][1] = moveCandidates[i][1];
            }
        } else {
            for (int i = 0; i < moveCandidates.length; i++) {
                playerCandidates[i][0] = moveCandidates[i][0] * -1;
                playerCandidates[i][1] = moveCandidates[i][1] * -1;
            }
        }

        return playerCandidates;
    }

    /**
     * Calculates legal moves of a pawn. These moves are not necessarily legal and the rest of the
     * calculation is done on the player class.
     *
     * @param board Current board object.
     * @param row   Row of calculating piece.
     * @param col   Column of calculating piece.
     * @return An ArrayList of all possible moves.
     */
    @Override
    public ArrayList<Move> legalMoves(Board board, int row, int col) {
        ArrayList<Move> moveArray = new ArrayList<>();
        int[][] moveCandidates = getMoveCandidates();

        // Straight move
        int[] moveCandidate = {(row + moveCandidates[0][0]), col + moveCandidates[0][1]};
        if (isOnBoard(moveCandidate[0], moveCandidate[1])) {
            if (!board.getSquare(moveCandidate[0], moveCandidate[1]).hasPiece()) {
                moveArray.add(new Move(board.getSquare(row, col),
                        board.getSquare(moveCandidate[0], moveCandidate[1])));

                // Double move
                if (!this.isMoved()) {
                    int[] doubleMoveCandidate = {(row + moveCandidates[1][0]),
                            (col + moveCandidates[1][1])};
                    if (isOnBoard(doubleMoveCandidate[0], doubleMoveCandidate[1])) {
                        if (!board.getSquare(doubleMoveCandidate[0], doubleMoveCandidate[1]).hasPiece()) {
                            moveArray.add(new Move.PawnDoubleMove(board.getSquare(row, col),
                                    board.getSquare(doubleMoveCandidate[0], doubleMoveCandidate[1])));
                        }
                    }
                }
            }
        }

        // Attack moves
        for (int i = 2; i < 4; i++) {
            int[] attackMoveCandidate = {(row + moveCandidates[i][0]), col + moveCandidates[i][1]};
            if (isOnBoard(attackMoveCandidate[0], attackMoveCandidate[1])) {

                // Basic attack
                if ((board.getSquare(attackMoveCandidate[0], attackMoveCandidate[1]).hasPiece()
                        && this.isWhite() != board.getSquare(attackMoveCandidate[0], attackMoveCandidate[1]).getPiece().isWhite())) {
                    moveArray.add(new Move(board.getSquare(row, col),
                            board.getSquare(attackMoveCandidate[0], attackMoveCandidate[1])));
                }

                // En Passant
                if (board.getSquare(attackMoveCandidate[0], attackMoveCandidate[1]).equals(Game.getEnPassantTarget())) {
                    if (this.isWhite()) {
                        moveArray.add(new Move.PawnEnPassantMove(board.getSquare(row, col),
                                board.getSquare(attackMoveCandidate[0], attackMoveCandidate[1])));
                    } else {
                        moveArray.add(new Move.PawnEnPassantMove(board.getSquare(row, col),
                                board.getSquare(attackMoveCandidate[0], attackMoveCandidate[1])));
                    }
                }
            }
        }
        return moveArray;
    }

    public int getDrawable() {
        return isWhite() ? R.drawable.wp : R.drawable.bp;
    }
}
