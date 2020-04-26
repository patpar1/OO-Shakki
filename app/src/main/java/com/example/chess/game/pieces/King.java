package com.example.chess.game.pieces;

import com.example.chess.R;
import com.example.chess.game.Board;
import com.example.chess.game.BoardUtils;
import com.example.chess.game.Move;
import com.example.chess.game.Square;

import java.io.Serializable;
import java.util.ArrayList;

public class King extends Piece implements Serializable {

    private static final int KING_COLUMN = 4;
    private static final int ABSOLUTE_PIECE_VALUE = 900;
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
    private static final double[][] BOARD_POSITION_BIAS = {
            { -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
            { -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
            { -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
            { -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
            { -2.0, -3.0, -3.0, -4.0, -4.0, -3.0, -3.0, -2.0},
            { -1.0, -2.0, -2.0, -2.0, -2.0, -2.0, -2.0, -1.0},
            {  2.0,  2.0,  0.0,  0.0,  0.0,  0.0,  2.0,  2.0 },
            {  2.0,  3.0,  1.0,  0.0,  0.0,  1.0,  3.0,  2.0 }
    };


    public King(boolean isWhite) {
        super(isWhite,
                isWhite ? ABSOLUTE_PIECE_VALUE : -ABSOLUTE_PIECE_VALUE,
                isWhite ? BOARD_POSITION_BIAS : BoardUtils.reverseArray(BOARD_POSITION_BIAS));
    }

    /**
     * Calculates legal moves of a king. These moves are not necessarily legal and the rest of the
     * calculation is done on the player class.
     *
     * @param board Current board object.
     * @param row   Row of calculating piece.
     * @param col   Column of calculating piece.
     * @return An ArrayList of all possible moves.
     */
    @Override
    public ArrayList<Move> legalMoves(Board board, int row, int col) {
        // Basic moveCandidate moves
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
            moveArray.add(new Move(board.getSquare(row, col), board.getSquare(moveCandidate[0], moveCandidate[1])));
        }

        // Check rooks
        ArrayList<Square> rooks = new ArrayList<>();
        int pieceRow = isWhite() ? 7 : 0;
        if (board.getSquare(pieceRow, 7).getPiece() instanceof Rook) {
            rooks.add(board.getSquare(pieceRow, 7));
        }
        if (board.getSquare(pieceRow, 0).getPiece() instanceof Rook) {
            rooks.add(board.getSquare(pieceRow, 0));
        }

        // Castling moves
        for (Square rookStart : rooks) {
            boolean isPieceBetween = false;
            if (rookStart.getPiece() == null) {
                continue;
            }
            // From wikipedia:
            // 1. The king and the chosen rook are on the player's first rank
            //      -Always true if neither piece has moved
            // 2. Neither the king nor the chosen rook has previously moved.
            if (this.isMoved() || rookStart.getPiece().isMoved()) {
                continue;
            }
            // 3. There are no pieces between the king and the chosen rook.
            if (rookStart.getCol() < KING_COLUMN) {
                // Queen side
                for (int i = rookStart.getCol() + 1; i < KING_COLUMN; i++) {
                    if (board.getSquare(pieceRow, i).getPiece() != null) {
                        isPieceBetween = true;
                    }
                }
            } else {
                // King side
                for (int i = KING_COLUMN + 1; i < rookStart.getCol(); i++) {
                    if (board.getSquare(pieceRow, i).getPiece() != null) {
                        isPieceBetween = true;
                    }
                }
            }
            if (isPieceBetween) {
                continue;
            }

            // 4. The king is not currently in check.
            // 5. The king does not pass through a square that is attacked by an enemy piece.
            // 6. The king does not end up in check. (True of any legal move.)
            //      -Last three are checked on player class

            Square kingEnd;
            Square rookEnd;

            if (rookStart.getCol() < KING_COLUMN) {
                // Queen side
                kingEnd = board.getSquare(row, col - 2);
                rookEnd = board.getSquare(row, col - 1);
            } else {
                // King side
                kingEnd = board.getSquare(row, col + 2);
                rookEnd = board.getSquare(row, col + 1);
            }
            moveArray.add(new Move.CastlingMove(board.getSquare(row, col), kingEnd, rookStart, rookEnd));
        }
        return moveArray;
    }

    public int getDrawable() {
        return isWhite() ? R.drawable.wk : R.drawable.bk;
    }
}
