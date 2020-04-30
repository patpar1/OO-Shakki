package com.example.chess.game;

import com.example.chess.ai.AlphaBetaPlayer;

import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable {

    private static Square enPassantTarget;
    private static Player enPassantTargetPlayer;
    private final Board board;
    private final ArrayList<Move> moves;
    private boolean whiteTurn;
    private final Player whitePlayer;
    private final Player blackPlayer;
    private int moveIndex;

    private boolean isFinished;

    private final GameInformation gameInformation;

    /**
     * Main constructor for the Game class. This class tracks the Game Board, players, current
     * turn etc. All classes under this class are Serializable for the saving functionality.
     */
    public Game(boolean isHumanPlayer, int aiLevel) {
        board = new Board();
        moves = new ArrayList<>();
        whiteTurn = true;
        whitePlayer = new Player(true);
        if (isHumanPlayer) {
            blackPlayer = new Player(false);
        } else {
            blackPlayer = new AlphaBetaPlayer(false, aiLevel);
        }
        enPassantTarget = null;
        enPassantTargetPlayer = null;
        moveIndex = 0;
        isFinished = false;
        gameInformation = new GameInformation();
    }

    /* Some simple getter and setter methods */

    public static Square getEnPassantTarget() {
        return enPassantTarget;
    }

    static void setEnPassantTarget(Square enPassantTarget) {
        Game.enPassantTarget = enPassantTarget;
    }

    static Player getEnPassantTargetPlayer() {
        return enPassantTargetPlayer;
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

    public boolean isWhiteTurn() {
        return whiteTurn;
    }

    public Player getCurrentPlayer() {
        return whiteTurn ? whitePlayer : blackPlayer;
    }

    public int getMoveIndex() {
        return moveIndex;
    }

    void increaseMoveIndex() {
        moveIndex++;
    }

    void decreaseMoveIndex() {
        moveIndex--;
    }

    void switchPlayerTurn() {
        this.whiteTurn = !whiteTurn;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public GameInformation getGameInformation() {
        return gameInformation;
    }

    /**
     * Handles the square click event sent by GameFragment. Determines if player has clicked a
     * square before and if true, makes the move on the game board. This method does not calculate
     * the legality of a move, it only calculates if a square clicked by the player is reasonable
     * candidate for a move. The legality is calculated on the constructMove() method.
     * States of the game:
     * -1 - Error
     * 0 - Continue
     * 1 - White wins
     * 2 - Black wins
     * 3 - Stalemate
     *
     * @param sq Square clicked by the user.
     */
    public void handleSquareClickEvent(Square sq) {
        getCurrentPlayer().handleSquareClickEvent(this, sq);
    }

    /**
     * Makes the last move on the moves array.
     *
     */
    public void makeNextMove() {
        getCurrentPlayer().makeMove(this, board, moves.get(moveIndex));
    }

    /**
     * Undoes the previous move made.
     *
     */
    public void undoPreviousMove() {
        getCurrentPlayer().undoMove(this, board, moves.get(moveIndex - 1));
    }

    /**
     * Calculates the squares possible for the current player to move to.
     *
     * @return An ArrayList containing Squares of all possible destination squares.
     */
    public ArrayList<Square> getCurrentMoveSquares() {
        return getCurrentPlayer().getCurrentMoveSquares(board);
    }

    /**
     * Method for telling the current player to end the turn.
     *
     * @return integer value representing the current state of the game.
     */
    public int endTurn() {
        return getCurrentPlayer().endTurn(this);
    }
}
