package com.example.chess.game;

import java.io.Serializable;
import java.util.ArrayList;

class Player implements Serializable {

    private boolean isWhite;
    private boolean isCheck;

    Player(boolean isWhite) {
        this.isWhite = isWhite;
        this.isCheck = false;
    }

    /* Simple getter and setter methods */

    boolean isCheck() {
        return isCheck;
    }

    void setCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    boolean isWhite() {
        return isWhite;
    }

    /**
     * Calculates the current legal moves of this player. Gets the piece on the chosen square,
     * gets all legal moves of the piece and checks if the move candidate results on check on the
     * game board. Also checks if Castling is allowed on the board by checking, if the castling
     * move goes through an attacked square. The rest of the castling checking is done on the
     * King class.
     *
     * @param board        Current game board.
     * @param chosenSquare The square currently chosen on the Game class.
     * @return an ArrayList of current legal moves.
     */
    ArrayList<Move> getLegalMoves(Board board, Square chosenSquare) {
        ArrayList<Move> legalMoveCandidates;
        ArrayList<Move> moveCandidates;

        // If player has not chosen a square, return an empty ArrayList.
        legalMoveCandidates = new ArrayList<>();
        if (chosenSquare == null) {
            return legalMoveCandidates;
        }

        // If the piece in the chosen square has no legal moves, return an empty ArrayList.
        moveCandidates = chosenSquare.getPiece().legalMoves(board, chosenSquare);
        if (moveCandidates == null) {
            return legalMoveCandidates;
        }

        // Loop through all move candidates.
        for (Move moveCandidate : moveCandidates) {
            // Copy current board state and do the move on the copied board.
            Board copyBoard = board.copy();
            Move copyMove = moveCandidate.copy();
            copyMove.makeMove(copyBoard);

            // If the move is a castling move, do the rest of the castling checks.
            if (moveCandidate instanceof Move.CastlingMove) {
                boolean isInBetween = false;

                // 4. The king is not currently in check.
                if (isCheck) {
                    continue;
                }

                // 5. The king does not pass through a square that is attacked by an enemy piece.
                if (moveCandidate.getColEnd() < moveCandidate.getColStart()) {
                    // Queen side
                    for (int i = moveCandidate.getColEnd() + 1; i < moveCandidate.getColStart(); i++) {
                        if (board.squareInEnemyLine(this, board.getSquare(moveCandidate.getRowEnd(), i))) {
                            isInBetween = true;
                            break;
                        }
                    }
                } else {
                    // King side
                    for (int i = moveCandidate.getColStart() + 1; i < moveCandidate.getColEnd(); i++) {
                        if (board.squareInEnemyLine(this, board.getSquare(moveCandidate.getRowEnd(), i))) {
                            isInBetween = true;
                            break;
                        }
                    }
                }
                // If the castling goes through enemy square, do not add it to the current legal
                // moves.
                if (isInBetween) {
                    continue;
                }
            }

            // 6. The king does not end up in check. (True of any legal move.)
            if (!copyBoard.isCheck(this)) {
                legalMoveCandidates.add(moveCandidate);
            }
        }

        return legalMoveCandidates;
    }
}
