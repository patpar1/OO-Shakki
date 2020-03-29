package com.example.chess.game;

import java.util.ArrayList;

public class Game {

    private Board board;
    private ArrayList<Move> moves;
    private boolean whiteTurn;

    private Player whitePlayer;
    private Player blackPlayer;

    private static Square enPassantTarget;
    private static Player enPassantTargetPlayer;

    public Game() {
        board = new Board();
        moves = new ArrayList<>();
        whiteTurn = true;

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

    public ArrayList<Move> getMoves() {
        return moves;
    }

    private Player getCurrentPlayer() {
        return getPlayer(whiteTurn);
    }

    private Player getPlayer(boolean playerTurn) {
        if (playerTurn) {
            return whitePlayer;
        } else {
            return blackPlayer;
        }
    }

    public int gameLoop(int maxLoops) {

        // 1 = White wins
        // 2 = Draw
        // 3 = Black wins

        int i = 0;
        int gameState;

        while (i++ < maxLoops) {

            if ((gameState = checkGameState()) != 0) {
                return gameState;
            }

            Move playerMove = getCurrentPlayer().constructMove(board);
            if (playerMove == null) {
                continue;
            }

            moves.add(playerMove);
            playerMove.makeMove(board);

            playerMove.getMovingPiece().hasMoved();

            getCurrentPlayer().setCheck(false);
            resetEnPassant();

            this.whiteTurn = !whiteTurn;
        }

        return 3;
    }

    private void resetEnPassant() {
        if (enPassantTarget != null && enPassantTargetPlayer != getCurrentPlayer()) {
            setEnPassantTarget(null);
            setEnPassantTargetPlayer(null);
        }
    }

    private int checkGameState() {

        ArrayList<Move> currentPlayerMoves = new ArrayList<>();
        for (Square r : board.getPlayerSquares(whiteTurn)) {
            currentPlayerMoves.addAll(getCurrentPlayer().getLegalMoves(board, r));
        }

        boolean check = board.isCheck(getCurrentPlayer());
        boolean stalemate = currentPlayerMoves.size() == 0;

        if (check && stalemate) {
            return !whiteTurn ? 1 : 2;
        }

        if (stalemate) {
            return 3;
        }

        if (check) {
            getCurrentPlayer().setCheck(true);
        }
        return 0;
    }


}
