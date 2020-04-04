package com.example.chess.game;

import java.util.ArrayList;

public class Game {

    private Board board;
    private ArrayList<Move> moves;
    private boolean whiteTurn;

    private Square chosenSquare;

    private Player whitePlayer;
    private Player blackPlayer;

    private static Square enPassantTarget;
    private static Player enPassantTargetPlayer;

    public Game() {
        board = new Board();
        moves = new ArrayList<>();
        whiteTurn = true;

        chosenSquare = null;

        whitePlayer = new Player(true);
        blackPlayer = new Player(false);

        enPassantTarget = null;
        enPassantTargetPlayer = null;
    }

    public static Square getEnPassantTarget() {
        return enPassantTarget;
    }

    static void setEnPassantTarget(Square enPassantTarget) {
        Game.enPassantTarget = enPassantTarget;
    }

    static void setEnPassantTargetPlayer(Player enPassantTargetPlayer) {
        Game.enPassantTargetPlayer = enPassantTargetPlayer;
    }

    public Board getBoard() {
        return board;
    }

    public ArrayList<Move> getMoves() {
        return moves;
    }

    public Player getCurrentPlayer() {
        return getPlayer(whiteTurn);
    }

    private Player getPlayer(boolean playerTurn) {
        if (playerTurn) {
            return whitePlayer;
        } else {
            return blackPlayer;
        }
    }

    public Square getChosenSquare() {
        return chosenSquare;
    }

    private void resetEnPassant() {
        if (enPassantTarget != null && enPassantTargetPlayer != getCurrentPlayer()) {
            setEnPassantTarget(null);
            setEnPassantTargetPlayer(null);
        }
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

    public void setClickEvent(Square sq) {
        if (chosenSquare == null) {
            if (getCurrentPlayer().checkChosenSquare(board, sq)) {
                chosenSquare = sq;
            }
        } else {
            Move playerMove = getCurrentPlayer().getChosenMove(board, chosenSquare, sq);
            if (playerMove != null) {
                makeMove(playerMove);
                checkGameState();
                chosenSquare = null;
            }
        }
    }

    private void makeMove(Move m) {
        moves.add(m);
        m.makeMove(board);
        m.getMovingPiece().hasMoved();
    }

    public ArrayList<Square> getCurrentLegalMoves(Square chosenSquare) {
        ArrayList<Square> squares = new ArrayList<>();
        ArrayList<Move> legalMoves = getCurrentPlayer().getLegalMoves(board, chosenSquare);

        for (Move m : legalMoves) {
            squares.add(board.getSquare(m.getRowEnd(), m.getColEnd()));
        }

        return squares;
    }
}
