package com.example.chess.game;

import com.example.chess.game.pieces.Pawn;
import com.example.chess.game.pieces.Piece;

import java.util.ArrayList;
import java.util.Scanner;

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

    private Square getChosenSquare(Board board) {

        ArrayList<Piece> pieces;

        pieces = board.getPlayerPieces(isWhite);

        System.out.print("Choose piece: ");

        Square chosenSquare = getSquare(board);
        if (chosenSquare == null) {
            return null;
        }

        if (chosenSquare.getPiece() == null) {
            System.out.println("There is no piece on the square!");
            return null;
        } else if (!pieces.contains(chosenSquare.getPiece())) {
            System.out.println("You can't move this piece!");
            return null;
        }

        return chosenSquare;
    }

    ArrayList<Move> getLegalMoves(Board board, Square chosenSquare) {

        ArrayList<Move> legalMoveCandidates = new ArrayList<>();
        ArrayList<Move> moveCandidates = chosenSquare.getPiece().legalMoves(board, chosenSquare);

        if (moveCandidates == null) {
            return legalMoveCandidates;
        }

        for (Move moveCandidate : moveCandidates) {
            Board copyBoard = board.copy();
            Move copyMove = moveCandidate.copy();
            copyMove.makeMove(copyBoard);
            if (!copyBoard.isCheck(this)) {
                legalMoveCandidates.add(moveCandidate);
            }
        }

        return legalMoveCandidates;
    }

    private Move getChosenMove(Board board, Square chosenSquare) {

        Move chosenMove = null;
        Square destinationSquare;
        ArrayList<Move> legalMoves = getLegalMoves(board, chosenSquare);

        if (legalMoves.size() == 0) {
            System.out.println("This piece has no legal moves!");
            return null;
        }

        ArrayList<Square> legalSquares = new ArrayList<>();
        for (Move m : legalMoves) {
            legalSquares.add(board.getSquare(m.getRowEnd(), m.getColEnd()));
        }

        System.out.println(board.printBoard(legalSquares));

        System.out.print("Choose destination square: ");
        destinationSquare = getSquare(board);

        if (!legalSquares.contains(destinationSquare)) {
            System.out.println("Move is not legal!");
            return null;
        }

        for (Move m : legalMoves) {
            if (board.getSquare(m.getRowEnd(), m.getColEnd()) == destinationSquare) {
                chosenMove = m;
            }
        }

        return chosenMove;
    }

    Move constructMove(Board board) {

        System.out.println(isWhite ? "White player's turn" : "Black player's turn");
        System.out.println(board.printBoard());

        Square chosenSquare = getChosenSquare(board);
        if (chosenSquare == null) {
            return null;
        }

        Move chosenMove = getChosenMove(board, chosenSquare);
        if (chosenMove == null) {
            return null;
        }

        // En Passant
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
