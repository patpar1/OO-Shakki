package com.example.chess.game;

import com.example.chess.game.pieces.Pawn;
import com.example.chess.game.pieces.Piece;

import java.util.ArrayList;

class Player {

    private boolean isWhite;
    private boolean isCheck;

    Player(boolean isWhite) {
        this.isWhite = isWhite;
        this.isCheck = false;
    }

    public boolean isCheck() {
        return isCheck;
    }

    void setCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    boolean isWhite() {
        return isWhite;
    }

    /*

    private Square getSquare(Board board) {
        String squareInput = null;
        int[] coordinates = new int[0];

        Scanner sc = new Scanner(System.in);
        if (sc.hasNextLine()) {
            squareInput = sc.nextLine();
        }

        if (squareInput != null) {
            coordinates = transformTextToCoordinates(squareInput);
        }

        if (coordinates == null) {
            return null;
        }

        return board.getSquare(coordinates[0], coordinates[1]);

    }

     */

    /*
    private int[] transformTextToCoordinates(String s) {
        final String columns = "abcdefgh";

        if (s.length() != 2) {
            System.out.println("Coordinate is wrong! It has be a form of 'a5'.");
            return null;
        }

        int i0 = columns.indexOf(s.toLowerCase().charAt(0));
        if (i0 == -1) {
            System.out.println("Column has to be a letter between a-h (eg. a5).");
            return null;
        }

        int i1 = 8 - Character.getNumericValue(s.charAt(1));
        if (i1 < 0) {
            System.out.println("Row has to be a number between 1-8 (eg. a5).");
            return null;
        }

        return new int[] {i1, i0};
    }

     */

    boolean checkChosenSquare(Board board, Square chosenSquare) {

        ArrayList<Piece> pieces = board.getPlayerPieces(isWhite);

        if (chosenSquare == null) {
            return false;
        } else if (chosenSquare.getPiece() == null) {
            System.out.println("There is no piece on the square!");
            return false;
        } else if (!pieces.contains(chosenSquare.getPiece())) {
            System.out.println("You can't move this piece!");
            return false;
        }

        return true;
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

    Move getChosenMove(Board board, Square chosenSquare, Square destinationSquare) {

        Move chosenMove = null;
        ArrayList<Move> legalMoves = getLegalMoves(board, chosenSquare);

        if (legalMoves.size() == 0) {
            System.out.println("This piece has no legal moves!");
            return null;
        }

        ArrayList<Square> legalSquares = new ArrayList<>();
        for (Move m : legalMoves) {
            legalSquares.add(board.getSquare(m.getRowEnd(), m.getColEnd()));
        }

        if (!legalSquares.contains(destinationSquare)) {
            System.out.println("Move is not legal!");
            return null;
        }

        for (Move m : legalMoves) {
            if (board.getSquare(m.getRowEnd(), m.getColEnd()) == destinationSquare) {
                chosenMove = m;
            }
        }

        if (chosenMove == null) {
            return null;
        }

        if (chosenSquare.getPiece() instanceof Pawn) {
            // White pawn
            if (chosenMove.getRowEnd() - chosenMove.getRowStart() == -2) {
                Game.setEnPassantTarget(board.getSquare(chosenSquare.getRow() - 1, chosenSquare.getCol()));
                Game.setEnPassantTargetPlayer(this);
            } else if (chosenMove.getRowEnd() - chosenMove.getRowStart() == 2) {
                Game.setEnPassantTarget(board.getSquare(chosenSquare.getRow() + 1, chosenSquare.getCol()));
                Game.setEnPassantTargetPlayer(this);
            }
        }

        return chosenMove;
    }
}
