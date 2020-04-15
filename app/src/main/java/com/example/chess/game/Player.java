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

    boolean isCheck() {
        return isCheck;
    }

    void setCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    boolean isWhite() {
        return isWhite;
    }

    ArrayList<Move> getLegalMoves(Board board, Square chosenSquare) {
        ArrayList<Move> legalMoveCandidates;
        ArrayList<Move> moveCandidates;
        boolean isInBetween;

        legalMoveCandidates = new ArrayList<>();
        if (chosenSquare == null) {
            return legalMoveCandidates;
        }

        moveCandidates = chosenSquare.getPiece().legalMoves(board, chosenSquare);
        if (moveCandidates == null) {
            return legalMoveCandidates;
        }

        isInBetween = false;

        for (Move moveCandidate : moveCandidates) {
            Board copyBoard = board.copy();
            Move copyMove = moveCandidate.copy();
            copyMove.makeMove(copyBoard);

            // Castling checks
            if (moveCandidate instanceof Move.CastlingMove) {
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
                if (isInBetween) {
                    continue;
                }
            }

            if (!copyBoard.isCheck(this)) {
                // 6. The king does not end up in check. (True of any legal move.)
                legalMoveCandidates.add(moveCandidate);
            }
        }

        return legalMoveCandidates;
    }
}
