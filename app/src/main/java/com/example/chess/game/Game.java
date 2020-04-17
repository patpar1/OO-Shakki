package com.example.chess.game;

import com.example.chess.game.pieces.Pawn;
import com.example.chess.game.pieces.Piece;

import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable {

    private Board board;
    private ArrayList<Move> moves;
    private boolean whiteTurn;
    private Square chosenSquare;
    private Square destinationSquare;
    private Player whitePlayer;
    private Player blackPlayer;
    private static Square enPassantTarget;
    private static Player enPassantTargetPlayer;
    private int moveIndex;

    /**
     * Main constructor for the Game class. This class tracks the Game Board, players, current
     * turn etc. All classes under this class are Serializable for the saving functionality.
     */
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
        moveIndex = 0;
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
        return chosenSquare;
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

    public static Square getEnPassantTarget() {
        return enPassantTarget;
    }

    private static void setEnPassantTarget(Square enPassantTarget) {
        Game.enPassantTarget = enPassantTarget;
    }

    private static void setEnPassantTargetPlayer(Player enPassantTargetPlayer) {
        Game.enPassantTargetPlayer = enPassantTargetPlayer;
    }

    public boolean isCheck() {
        return getCurrentPlayer().isCheck();
    }

    public int getMoveIndex() {
        return moveIndex;
    }

    private void switchPlayerTurn() {
        this.whiteTurn = !whiteTurn;
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
     * @return integer value representing the current state of the game.
     */
    public int handleSquareClickEvent(Square sq) {
        // Make sure the given square is not null.
        if (sq == null) {
            return -1;
        }

        // If the player has not chosen square before, sets the given square to chosen square.
        if (chosenSquare == null) {
            // If there is not piece on the chosen square, reset the move.
            if (sq.getPiece() == null) {
                System.out.println("There is no piece on the square!");
                resetMove();
                return 0;
            }

            // If the piece on the square is opposite color, reset the move.
            if (!board.getPlayerSquares(whiteTurn).contains(sq)) {
                System.out.println("You can't move this piece!");
                resetMove();
                return 0;
            }

            // Add the square to chosenSquare.
            chosenSquare = sq;
        }

        // If there has chosen a square (chosenSquare != null), sets this square to a destination
        // square and tries to construct a move based on these 2 squares.
        else {
            // If the chosenSquare equals destinationSquare, reset the move.
            if (chosenSquare.equals(sq)) {
                resetMove();
                return 0;
            }

            // If the destinationSquare contains another white piece, set the chosenSquare to the
            // sq value.
            if (board.getPlayerSquares(whiteTurn).contains(sq)) {
                chosenSquare = sq;
                return 0;
            }

            // Set the destinationSquare and try to construct a move.
            destinationSquare = sq;
            return constructMove();
        }
        return 0;
    }

    /**
     * Method for constructing a move based on clicked squares of the player. Asks all legal moves
     * from player and determines the current move from it. If the move is legal, method makes it
     * on the game board and stores the move.
     *
     * @return integer value representing the current state of the game.
     */
    private int constructMove() {
        Move currentMove = null;

        // Get all legal moves of the current player.
        ArrayList<Move> legalMoves = getCurrentPlayer().getLegalMoves(board, chosenSquare);

        // Get all possible destination squares and determine the current move made by the player.
        ArrayList<Square> legalSquares = new ArrayList<>();
        for (Move m : legalMoves) {
            legalSquares.add(board.getSquare(m.getRowEnd(), m.getColEnd()));
            if (board.getSquare(m.getRowEnd(), m.getColEnd()) == destinationSquare) {
                currentMove = m;
            }
        }

        // If destinationSquare is not in legal squares, reset the move.
        if (!legalSquares.contains(destinationSquare)) {
            System.out.println("Move is not legal!");
            resetMove();
            return -1;
        }

        // If current move couldn't be determined, reset the move.
        if (currentMove == null) {
            System.out.println("Current move was not found!");
            resetMove();
            return -1;
        }

        // If the user has undone moves and is not in the latest move made, delete the moves after
        // the game state shown to the user.
        if (moveIndex < moves.size()) {
            moves.subList(moveIndex, moves.size()).clear();
        }

        // Make the move on the game board and check the game's current state.
        moves.add(currentMove);
        return makeMove(currentMove);
    }

    /**
     * Method for making the move on the game board.
     *
     * @param m Move needed to be made on the game board.
     */
    private int makeMove(Move m) {
        // Make the final move on the game board.
        m.makeFinalMove(board);
        moveIndex++;

        // If the moved piece is a pawn and move is an en passant move, set the en passant.
        if (m.getMovingPiece() instanceof Pawn
                && m instanceof Move.PawnDoubleMove) {
            setEnPassant(m);
        }

        // Reset the move after the move has been done.
        resetMove();
        return checkGameState();
    }

    public int makeNextMove() {
        return makeMove(moves.get(moveIndex));
    }

    private int undoMove(Move m) {
        m.undoMove(board);
        moveIndex--;

        Move dp;
        if (moveIndex > 0
                && (dp = moves.get(moveIndex - 1)) instanceof Move.PawnDoubleMove) {
            if (dp.getMovingPiece().isWhite()) {
                Game.setEnPassantTarget(board.getSquare(dp.getRowEnd() + 1, dp.getColEnd()));
                Game.setEnPassantTargetPlayer(getCurrentPlayer());
            } else {
                Game.setEnPassantTarget(board.getSquare(dp.getRowEnd() - 1, dp.getColEnd()));
                Game.setEnPassantTargetPlayer(getCurrentPlayer());
            }
        } else {
            Game.setEnPassantTarget(null);
            Game.setEnPassantTargetPlayer(null);
        }
        resetMove();
        return checkGameState();
    }

    public int undoPreviousMove() {
        return undoMove(moves.get(moveIndex - 1));
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
     * Checks if the move made can be targeted by an en passant move. If it can, sets the current
     * status of enPassantTarget and enPassantTargetPlayer to the en passant target.
     *
     * @param m Move needed to be checked.
     */
    private void setEnPassant(Move m) {
        if (m.getMovingPiece().isWhite()) {
            Game.setEnPassantTarget(board.getSquare(m.getRowEnd() + 1, m.getColEnd()));
            Game.setEnPassantTargetPlayer(getCurrentPlayer());
        } else {
            Game.setEnPassantTarget(board.getSquare(m.getRowEnd() - 1, m.getColEnd()));
            Game.setEnPassantTargetPlayer(getCurrentPlayer());
        }
    }

    private void resetEnPassant() {
        if (enPassantTarget != null && enPassantTargetPlayer != getCurrentPlayer()) {
            setEnPassantTarget(null);
            setEnPassantTargetPlayer(null);
        }
    }

    /**
     * Resets the move squares when the move is made or when the move can't be constructed.
     */
    private void resetMove() {
        chosenSquare = null;
        destinationSquare = null;
    }

    /**
     * Checks the state of the game after a move has been made. Resets en passant targets, switches
     * player turns and checks if there is check, mate, or stalemate on the board.
     *
     * @return integer value representing the current state of the game.
     */
    private int checkGameState() {
        // If the game is in this point, the check of the current player can be unset and the en
        // passant can be reset.
        getCurrentPlayer().setCheck(false);
        resetEnPassant();

        // Give the turn to the other player
        switchPlayerTurn();

        // Calculate the check status.
        boolean check = board.isCheck(getCurrentPlayer());

        // Calculate all legal moves of the other player.
        ArrayList<Move> currentPlayerMoves = new ArrayList<>();
        for (Square r : board.getPlayerSquares(whiteTurn)) {
            currentPlayerMoves.addAll(getCurrentPlayer().getLegalMoves(board, r));
        }

        // If there is not legal moves, set stalemate to true.
        boolean stalemate = currentPlayerMoves.size() == 0;

        // Determine the status of the game.
        if (check && stalemate) {
            return whiteTurn ? 2 : 1;
        } else if (stalemate) {
            return 3;
        } else if (check) {
            getCurrentPlayer().setCheck(true);
        }
        return 0;
    }

    /**
     * Calculates the squares possible for the current player to move to.
     *
     * @return An ArrayList containing Squares of all possible destination squares.
     */
    public ArrayList<Square> getCurrentMoveSquares() {
        ArrayList<Square> currentMoveSquares = new ArrayList<>();
        ArrayList<Move> currentMoves = getCurrentPlayer().getLegalMoves(board, chosenSquare);
        for (Move m : currentMoves) {
            currentMoveSquares.add(board.getSquare(m.getRowEnd(), m.getColEnd()));
        }
        return currentMoveSquares;
    }
}
