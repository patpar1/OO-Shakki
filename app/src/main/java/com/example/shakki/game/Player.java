package com.example.shakki.game;

import com.example.shakki.game.pieces.Piece;

import java.util.ArrayList;
import java.util.Scanner;

class Player {

    private boolean isWhite;
    private boolean isCheck;

    Player(boolean isWhite) {
        this.isWhite = isWhite;
        this.isCheck = false;
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

    ArrayList<Square> getLegalMoves(Board board, Square chosenSquare) {

        ArrayList<Square> legalSquares = new ArrayList<>();
        ArrayList<Square> squareCandidates = chosenSquare.getPiece().legalMoves(board, chosenSquare);

        if (squareCandidates == null) {
            return legalSquares;
        }

        for (Square destinationSquareCandidate : squareCandidates) {
            Board copyBoard = board.copy();
            Square chosenSquareCopy = copyBoard.getSquare(chosenSquare.getRow(), chosenSquare.getCol());
            Square destinationSquareCandidateCopy = copyBoard.getSquare(destinationSquareCandidate.getRow(), destinationSquareCandidate.getCol());
            copyBoard.makeMove(new Move(chosenSquareCopy, destinationSquareCandidateCopy));
            if (!copyBoard.isCheck(this)) {
                legalSquares.add(destinationSquareCandidate);
            }
        }

        return legalSquares;
    }

    private Square getDestinationSquare(Board board, Square chosenSquare) {

        Square destinationSquare;
        ArrayList<Square> legalSquares = getLegalMoves(board, chosenSquare);

        if (legalSquares.size() == 0) {
            System.out.println("This piece has no legal moves!");
            return null;
        }

        System.out.println(board.printBoard(legalSquares));

        System.out.print("Choose destination square: ");
        destinationSquare = getSquare(board);

        if (!legalSquares.contains(destinationSquare)) {
            System.out.println("Move is not legal!");
            return null;
        }

        return destinationSquare;
    }

    Move constructMove(Board board) {

        System.out.println(isWhite ? "White player's turn" : "Black player's turn");
        System.out.println(board.printBoard());

        Square chosenSquare = getChosenSquare(board);
        if (chosenSquare == null) {
            return null;
        }

        Square destinationSquare = getDestinationSquare(board, chosenSquare);
        if (destinationSquare == null) {
            return null;
        }

        return new Move(chosenSquare, destinationSquare);
    }
}
