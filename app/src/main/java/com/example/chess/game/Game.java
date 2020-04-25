package com.example.chess.game;

import com.example.chess.game.pieces.Piece;

import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable {

    private Board board;
    private ArrayList<Move> moves;
    private boolean whiteTurn;
    private Player whitePlayer;
    private Player blackPlayer;
    private static Square enPassantTarget;
    private static Player enPassantTargetPlayer;
    private int moveIndex;
    private static boolean canEndTurn;

    /**
     * Main constructor for the Game class. This class tracks the Game Board, players, current
     * turn etc. All classes under this class are Serializable for the saving functionality.
     */
    public Game() {
        board = new Board();
        moves = new ArrayList<>();
        whiteTurn = true;
        whitePlayer = new Player(true);
        blackPlayer = new Player(false);
        // blackPlayer = new AlphaBetaPlayer(false);
        enPassantTarget = null;
        enPassantTargetPlayer = null;
        moveIndex = 0;
        canEndTurn = false;
    }

    /* Some simple getter and setter methods which I will not describe further */

    public Board getBoard() {
        return board;
    }

    public ArrayList<Move> getMoves() {
        return moves;
    }

    public boolean isWhiteTurn() {
        return whiteTurn;
    }

    public Square getChosenSquare() {
        return getCurrentPlayer().getChosenSquare();
    }

    private Player getPlayer(boolean playerTurn) {
        if (playerTurn) {
            return whitePlayer;
        } else {
            return blackPlayer;
        }
    }

    public Player getCurrentPlayer() {
        return getPlayer(whiteTurn);
    }

    public Move getCurrentPlayerMove() {
        return getCurrentPlayer().getCurrentMove();
    }

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

    public boolean isCheck() {
        return getCurrentPlayer().isCheck();
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

    public static boolean canEndTurn() {
        return canEndTurn;
    }

    static void setCanEndTurn(boolean b) {
        Game.canEndTurn = b;
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
     * @return integer value representing the current state of the game.
     */
    public int makeNextMove() {
        return getCurrentPlayer().makeMove(this, board, moves.get(moveIndex));
    }

    /**
     * Undoes the previous move made.
     *
     * @return integer value representing the current state of the game.
     */
    public int undoPreviousMove() {
        return getCurrentPlayer().undoMove(this, board, moves.get(moveIndex - 1));
    }

    /**
     * Method for promoting a pawn on the game board.
     *
     * @param row             Row of the pawn to be promoted.
     * @param col             Column of the pawn to be promoted.
     * @param promotablePiece The Piece which the pawn is promoted to.
     */
    public void promotePawn(int row, int col, Piece promotablePiece) {
        board.getSquare(row, col).setPiece(promotablePiece);
    }

    /**
     * Calculates the squares possible for the current player to move to.
     *
     * @return An ArrayList containing Squares of all possible destination squares.
     */
    public ArrayList<Square> getCurrentMoveSquares() {
        return getCurrentPlayer().getCurrentMoveSquares(board);
    }

    public int endTurn() {
        return getCurrentPlayer().endTurn(this);
    }
}
