package com.example.chess.ai;

import com.example.chess.game.Board;
import com.example.chess.game.Game;
import com.example.chess.game.Move;
import com.example.chess.game.Player;
import com.example.chess.game.Square;

import java.util.ArrayList;

public class AlphaBetaPlayer extends Player {

    private static final int SEARCH_DEPTH = 3;

    public AlphaBetaPlayer(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public void handleSquareClickEvent(Game game, Square sq) {
    }

    public void calculateMove(Game game) {
        Move m = calculateBestMove(game);
        game.getMoves().add(m);
        currentMove = m;
        setCanEndTurn(true);
    }

    private Move calculateBestMove(Game game) {
        Board board = game.getBoard();
        ArrayList<Square> squares = board.getPlayerSquares(isWhite());
        ArrayList<Move> moves = new ArrayList<>();
        for (Square s : squares) {
            moves.addAll(getLegalMoves(board, s));
        }

        Move bestMove = null;
        int bestScore = -9999;

        for (Move m : moves) {
            Move copyMove = m.copy();
            Board copyBoard = board.copy();
            copyMove.makeMove(copyBoard);

            int boardValue = minimax(SEARCH_DEPTH - 1, copyBoard, false);

            if (boardValue > bestScore) {
                bestScore = boardValue;
                bestMove = m;
            }
        }
        return bestMove;
    }

    private int minimax(int depth, Board board, boolean isMaxPlayer) {
        if (depth == 0) {
            return -evaluateBoard(board);
        }

        ArrayList<Square> squares = board.getPlayerSquares(!isMaxPlayer);
        ArrayList<Move> moves = new ArrayList<>();
        for (Square s : squares) {
            moves.addAll(getLegalMoves(board, s));
        }

        if (isMaxPlayer) {
            int bestMove = -9999;
            for (Move m : moves) {
                Move copyMove = m.copy();
                Board copyBoard = board.copy();
                copyMove.makeMove(copyBoard);
                bestMove = Math.max(bestMove, minimax(depth - 1, copyBoard, false));
            }
            return bestMove;
        } else {
            int bestMove = 9999;
            for (Move m : moves) {
                Move copyMove = m.copy();
                Board copyBoard = board.copy();
                copyMove.makeMove(copyBoard);
                bestMove = Math.min(bestMove, minimax(depth - 1, copyBoard, true));
            }
            return bestMove;
        }
    }

    private int evaluateBoard(Board board) {
        int totalScore = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.getSquare(i, j).getPiece() != null) {
                    totalScore += board.getSquare(i, j).getPiece().getPieceValue();
                }
            }
        }
        return totalScore;
    }
}
