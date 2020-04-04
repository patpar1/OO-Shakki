package com.example.chess.game;

import com.example.chess.game.pieces.Pawn;
import com.example.chess.game.pieces.Piece;

import java.util.ArrayList;

public class Game {

    private Board board;
    private ArrayList<Move> moves;
    private boolean whiteTurn;

    private Square chosenSquare;
    private Square destinationSquare;

    private Player whitePlayer;
    private Player blackPlayer;

    private static Square enPassantTarget;
    private static Player enPassantTargetPlayer;

    public Game() {
        board = new Board();
        moves = new ArrayList<>();
        whiteTurn = true;

        chosenSquare = null;
        destinationSquare = null;

        whitePlayer = new Player(true);
        blackPlayer = new Player(false);

        enPassantTarget = null;
        enPassantTargetPlayer = null;
    }

    public Board getBoard() {
        return board;
    }

    private Player getPlayer(boolean playerTurn) {
        if (playerTurn) {
            return whitePlayer;
        } else {
            return blackPlayer;
        }
    }

    private Player getCurrentPlayer() {
        return getPlayer(whiteTurn);
    }

    public Square getChosenSquare() {
        return chosenSquare;
    }

    public static Square getEnPassantTarget() {
        return enPassantTarget;
    }

    private static void setEnPassantTarget(Square enPassantTarget) {
        Game.enPassantTarget = enPassantTarget;
    }

    private static void setEnPassantTargetPlayer(Player enPassantTargetPlayer) {
        Game.enPassantTargetPlayer = enPassantTargetPlayer;
    }

    private void resetEnPassant() {
        if (enPassantTarget != null && enPassantTargetPlayer != getCurrentPlayer()) {
            setEnPassantTarget(null);
            setEnPassantTargetPlayer(null);
        }
    }

    public void handleSquareClickEvent(Square sq) {
        if (chosenSquare == null) {
            chosenSquare = sq;
        } else {
            destinationSquare = sq;
        }
        updateGameState();
    }

    private void updateGameState() {
        if (chosenSquare == null) {
            return;
        }
        if (!checkChosenSquareValidity()) {
            chosenSquare = null;
            return;
        }
        if (destinationSquare == null) {
            return;
        }
        constructMove();
    }

    private boolean checkChosenSquareValidity() {
        ArrayList<Piece> pieces = board.getPlayerPieces(whiteTurn);

        if (chosenSquare == null) {
            resetMove();
            return false;
        } else if (chosenSquare.getPiece() == null) {
            System.out.println("There is no piece on the square!");
            resetMove();
            return false;
        } else if (!pieces.contains(chosenSquare.getPiece())) {
            System.out.println("You can't move this piece!");
            resetMove();
            return false;
        }

        ArrayList<Move> legalMoves = getCurrentPlayer().getLegalMoves(board, chosenSquare);

        if (legalMoves.size() == 0) {
            System.out.println("This piece has no legal moves!");
            resetMove();
            return false;
        }

        return true;
    }

    private void constructMove() {
        Move currentMove = null;

        ArrayList<Move> legalMoves = getCurrentPlayer().getLegalMoves(board, chosenSquare);

        ArrayList<Square> legalSquares = new ArrayList<>();
        for (Move m : legalMoves) {
            legalSquares.add(board.getSquare(m.getRowEnd(), m.getColEnd()));
            if (board.getSquare(m.getRowEnd(), m.getColEnd()) == destinationSquare) {
                currentMove = m;
            }
        }

        if (!legalSquares.contains(destinationSquare)) {
            System.out.println("Move is not legal!");
            resetMove();
            return;
        }

        if (currentMove == null) {
            System.out.println("Current move was not found!");
            resetMove();
            return;
        }

        if (chosenSquare.getPiece() instanceof Pawn) {
            // White pawn
            if (currentMove.getRowEnd() - currentMove.getRowStart() == -2) {
                Game.setEnPassantTarget(board.getSquare(chosenSquare.getRow() - 1, chosenSquare.getCol()));
                Game.setEnPassantTargetPlayer(getCurrentPlayer());
            } else if (currentMove.getRowEnd() - currentMove.getRowStart() == 2) {
                Game.setEnPassantTarget(board.getSquare(chosenSquare.getRow() + 1, chosenSquare.getCol()));
                Game.setEnPassantTargetPlayer(getCurrentPlayer());
            }
        }

        makeMove(currentMove);
        checkGameState();
    }

    private void makeMove(Move m) {
        moves.add(m);
        m.makeMove(board);
        m.getMovingPiece().hasMoved();
        resetMove();
    }

    private void resetMove() {
        chosenSquare = null;
        destinationSquare = null;
    }

    private void checkGameState() {
        getCurrentPlayer().setCheck(false);
        resetEnPassant();

        this.whiteTurn = !whiteTurn;

        ArrayList<Move> currentPlayerMoves = new ArrayList<>();
        for (Square r : board.getPlayerSquares(whiteTurn)) {
            currentPlayerMoves.addAll(getCurrentPlayer().getLegalMoves(board, r));
        }

        boolean check = board.isCheck(getCurrentPlayer());
        boolean stalemate = currentPlayerMoves.size() == 0;

        if (check && stalemate) {
            return;
        }

        if (stalemate) {
            return;
        }

        if (check) {
            getCurrentPlayer().setCheck(true);
        }
    }

    public ArrayList<Square> getCurrentMoveSquares() {
        ArrayList<Square> currentMoveSquares = new ArrayList<>();
        ArrayList<Move> currentMoves = getCurrentPlayer().getLegalMoves(board, chosenSquare);
        for (Move m : currentMoves) {
            currentMoveSquares.add(board.getSquare(m.getRowEnd(), m.getColEnd()));
        }
        return currentMoveSquares;
    }
}
