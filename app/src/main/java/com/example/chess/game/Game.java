package com.example.chess.game;

import java.util.ArrayList;

public class Game {

    private Board board;
    // private ArrayList<Move> moves;
    private boolean whiteTurn;

    private Player whitePlayer;
    private Player blackPlayer;

    public Game() {
        board = new Board();
        // moves = new ArrayList<Move>();
        whiteTurn = true;

        whitePlayer = new Player(true);
        blackPlayer = new Player(false);
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

            // moves.add(playerMove);
            board.makeMove(playerMove);

            getCurrentPlayer().setCheck(false);

            this.whiteTurn = !whiteTurn;
        }

        return 3;
    }

    private int checkGameState() {

        ArrayList<Square> currentPlayerSquares = new ArrayList<>();
        for (Square r : board.getPlayerSquares(whiteTurn)) {
            currentPlayerSquares.addAll(getCurrentPlayer().getLegalMoves(board, r));
        }

        boolean check = board.isCheck(getCurrentPlayer());
        boolean stalemate = currentPlayerSquares.size() == 0;

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
