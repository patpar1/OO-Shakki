package com.example.chess.ai;

import com.example.chess.game.Board;
import com.example.chess.game.Game;
import com.example.chess.game.Move;
import com.example.chess.game.Player;
import com.example.chess.game.Square;

import java.util.ArrayList;
import java.util.Collections;

/**
 * An AI player that uses MiniMax-algorithm with Alpha-Beta pruning to calculate optimal move on
 * the game board. This code is inspired by a guide made by Lauri Hartikka named 'A step-by-step
 * guide to building a simple chess AI'.
 * Available at: https://www.freecodecamp.org/news/simple-chess-ai-step-by-step-1d55a9266977/
 */
public class AlphaBetaPlayer extends Player {

    // How deep to search the minimax algorithm.
    private int searchDepth;

    public AlphaBetaPlayer(boolean isWhite, int aiLevel) {
        super(isWhite);
        searchDepth = aiLevel;
    }

    /**
     * This overrides the parent class's method so that this class doesn't respond to user input.
     *
     * @param game Current game instance.
     * @param sq   Clicked square.
     */
    @Override
    public void handleSquareClickEvent(Game game, Square sq) {
    }

    /**
     * Method that calculates the current move and adds it to the moves list.
     *
     * @param game Current game object.
     */
    public void calculateMove(Game game) {
        currentMove = calculateBestMove(game);
        game.getMoves().add(currentMove);
        setCanEndTurn(true);
    }

    /**
     * Calculates the best possible move by using MiniMax-algorithm.
     *
     * @param game Current game object.
     * @return Move determined by the algorithm.
     */
    private Move calculateBestMove(Game game) {
        Board board = game.getBoard();

        // Calculate all possible legal moves.
        ArrayList<Square> squares = board.getPlayerSquares(isWhite());
        ArrayList<Move> moves = new ArrayList<>();
        for (Square s : squares) {
            moves.addAll(getLegalMoves(board, s));
        }

        Collections.shuffle(moves);

        Move bestMove = null;
        double bestScore = -9999;

        // Iterate through each move
        for (Move m : moves) {
            Move copyMove = m.copy();
            Board copyBoard = board.copy();
            copyMove.makeMove(copyBoard);

            // Search the moves boardValue with MiniMax.
            double boardValue = minimax(searchDepth - 1, copyBoard, -10000.0, 10000.0, false);

            // If the board value is larger than previous best score, this is current best move.
            if (boardValue > bestScore) {
                bestScore = boardValue;
                bestMove = m;
            }
        }
        return bestMove;
    }

    /**
     * Main recursive loop for the MiniMax-algorithm.
     *
     * @param depth       Level of the search tree.
     * @param board       The game board needed to be evaluated.
     * @param isMaxPlayer Boolean value representing, if current player is maximizing the board value.
     * @return Largest board value.
     */
    private double minimax(int depth, Board board, double alpha, double beta, boolean isMaxPlayer) {
        // If this is lowest level of the search tree, evaluate the game board.
        if (depth == 0) {
            return -evaluateBoard(board);
        }

        // Calculate all possible legal moves.
        ArrayList<Square> squares = board.getPlayerSquares(!isMaxPlayer);
        ArrayList<Move> moves = new ArrayList<>();
        for (Square s : squares) {
            moves.addAll(s.getPiece().legalMoves(board, s));
        }

        Collections.shuffle(moves);

        // If the current player tries to maximize the board value, set the best move value to a
        // large negative value. Otherwise set it to a large positive value
        if (isMaxPlayer) {
            double bestMove = -9999.0;
            for (Move m : moves) {
                // Make the move on the copy board.
                Move copyMove = m.copy();
                Board copyBoard = board.copy();
                copyMove.makeMove(copyBoard);

                // Call this function recursively until depth is 0.
                bestMove = Math.max(bestMove, minimax(depth - 1, copyBoard, alpha, beta, false));
                alpha = Math.max(alpha, bestMove);
                if (beta <= alpha) {
                    return bestMove;
                }
            }
            return bestMove;
        } else {
            double bestMove = 9999.0;
            for (Move m : moves) {
                // Make the move on the copy board.
                Move copyMove = m.copy();
                Board copyBoard = board.copy();
                copyMove.makeMove(copyBoard);

                // Call this function recursively until depth is 0.
                bestMove = Math.min(bestMove, minimax(depth - 1, copyBoard, alpha, beta, true));
                beta = Math.min(beta, bestMove);
                if (beta <= alpha) {
                    return bestMove;
                }
            }
            return bestMove;
        }
    }

    /**
     * Evaluates the board's value by calculating all pieces' scores.
     *
     * @param board Board object needed to be evaluated.
     * @return Board's value.
     */
    private double evaluateBoard(Board board) {
        double totalScore = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.getSquare(i, j).getPiece() != null) {
                    totalScore += board.getSquare(i, j).getPiece().getPieceValue(i, j);
                }
            }
        }
        return totalScore;
    }
}
