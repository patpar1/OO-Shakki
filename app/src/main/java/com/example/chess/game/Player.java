package com.example.chess.game;

import com.example.chess.game.pieces.Pawn;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {

    protected Move currentMove;
    private final boolean isWhite;
    private boolean isCheck;
    private Square chosenSquare;
    private Square destinationSquare;
    private boolean canEndTurn;

    public Player(boolean isWhite) {
        this.isWhite = isWhite;
        this.isCheck = false;
        chosenSquare = null;
        destinationSquare = null;
        canEndTurn = false;
        currentMove = null;
    }

    /* Simple getter and setter methods */

    public boolean isCheck() {
        return isCheck;
    }

    private void setCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public Square getChosenSquare() {
        return chosenSquare;
    }

    public boolean canEndTurn() {
        return canEndTurn;
    }

    protected void setCanEndTurn(boolean b) {
        canEndTurn = b;
    }

    public Move getCurrentMove() {
        return currentMove;
    }

    /**
     * Calculates the current legal moves of this player. Gets the piece on the chosen square,
     * gets all legal moves of the piece and checks if the move candidate results on check on the
     * game board. Also checks if Castling is allowed on the board by checking, if the castling
     * move goes through an attacked square. The rest of the castling checking is done on the
     * King class.
     *
     * @param board        Current game board.
     * @param chosenSquare The square currently chosen on the Game class.
     * @return an ArrayList of current legal moves.
     */
    protected ArrayList<Move> getLegalMoves(Board board, Square chosenSquare) {
        ArrayList<Move> legalMoveCandidates;
        ArrayList<Move> moveCandidates;

        // If player has not chosen a square, return an empty ArrayList.
        legalMoveCandidates = new ArrayList<>();
        if (chosenSquare == null) {
            return legalMoveCandidates;
        }

        // If the piece in the chosen square has no legal moves, return an empty ArrayList.
        moveCandidates = chosenSquare.getPiece().legalMoves(board, chosenSquare);
        if (moveCandidates == null) {
            return legalMoveCandidates;
        }

        // Loop through all move candidates.
        for (Move moveCandidate : moveCandidates) {
            // Copy current board state and do the move on the copied board.
            Board copyBoard = board.copy();
            Move copyMove = moveCandidate.copy();
            copyMove.makeMove(copyBoard);

            // If the move is a castling move, do the rest of the castling checks.
            if (moveCandidate instanceof Move.CastlingMove) {
                boolean isInBetween = false;

                // 4. The king is not currently in check.
                if (isCheck) {
                    continue;
                }

                // 5. The king does not pass through a square that is attacked by an enemy piece.
                if (moveCandidate.getColEnd() < moveCandidate.getColStart()) {
                    // Queen side
                    for (int i = moveCandidate.getColEnd() + 1; i < moveCandidate.getColStart(); i++) {
                        if (board.squareInEnemyLine(this, board.getSquare(moveCandidate.getRowEnd(), i))) {
                            isInBetween = true;
                            break;
                        }
                    }
                } else {
                    // King side
                    for (int i = moveCandidate.getColStart() + 1; i < moveCandidate.getColEnd(); i++) {
                        if (board.squareInEnemyLine(this, board.getSquare(moveCandidate.getRowEnd(), i))) {
                            isInBetween = true;
                            break;
                        }
                    }
                }
                // If the castling goes through enemy square, do not add it to the current legal
                // moves.
                if (isInBetween) {
                    continue;
                }
            }

            // 6. The king does not end up in check. (True of any legal move.)
            if (!copyBoard.isCheck(this)) {
                legalMoveCandidates.add(moveCandidate);
            }
        }
        return legalMoveCandidates;
    }

    /**
     * Handles the square click event on the player side.
     *
     * @param game Current game instance.
     * @param sq   Clicked square.
     */
    public void handleSquareClickEvent(Game game, Square sq) {
        Board board = game.getBoard();

        // Make sure the given square is not null.
        if (sq == null) {
            return;
        }

        // If the player has not chosen square before, sets the given square to chosen square.
        if (chosenSquare == null) {
            // If there is not piece on the chosen square, reset the move.
            if (sq.getPiece() == null) {
                System.out.println("There is no piece on the square!");
                resetMove();
                return;
            }

            // If the piece on the square is opposite color, reset the move.
            if (!board.getPlayerSquares(isWhite).contains(sq)) {
                System.out.println("You can't move this piece!");
                resetMove();
                return;
            }

            // Add the square to chosenSquare.
            chosenSquare = sq;
        }

        // If there is a chosen square (chosenSquare != null), set this square to a destination
        // square and try to construct a move based on these 2 squares.
        else {
            // If the chosenSquare equals destinationSquare, reset the move.
            if (chosenSquare.equals(sq)) {
                resetMove();
                return;
            }

            // If the destinationSquare contains another white piece, set the chosenSquare to the
            // sq value.
            if (board.getPlayerSquares(isWhite).contains(sq)) {
                chosenSquare = sq;
                return;
            }

            // Set the destinationSquare and try to construct a move.
            destinationSquare = sq;
            constructMove(game, board);
        }
    }

    /**
     * Resets the move squares when the move is made or when the move can't be constructed.
     */
    private void resetMove() {
        chosenSquare = null;
        destinationSquare = null;
        setCanEndTurn(false);
    }

    /**
     * Method for constructing a move based on clicked squares of the player. Asks all legal moves
     * from player and determines the current move from it. If the move is legal, method makes it
     * on the game board and stores the move.
     */
    private void constructMove(Game game, Board board) {
        Move currentMove = null;
        ArrayList<Move> moves = game.getMoves();

        // Get all legal moves of the current player.
        ArrayList<Move> legalMoves = getLegalMoves(board, chosenSquare);

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
            return;
        }

        // If current move couldn't be determined, reset the move.
        if (currentMove == null) {
            System.out.println("Current move was not found!");
            resetMove();
            return;
        }

        // If the user has undone moves and is not in the latest move made, delete the moves after
        // the game state shown to the user.
        if (game.getMoveIndex() < moves.size()) {
            moves.subList(game.getMoveIndex(), moves.size()).clear();
        }

        // Add the move to the current move. Move is made after GameFragment calls endTurn method.
        this.currentMove = currentMove;
        game.getMoves().add(currentMove);
        setCanEndTurn(true);
    }

    /**
     * Method for making the move on the game board.
     *
     * @param m Move needed to be made on the game board.
     * @return integer value representing the current state of the game.
     */
    int makeMove(Game game, Board board, Move m) {
        // Make the final move on the game board.
        m.makeFinalMove(board);
        game.increaseMoveIndex();

        // If the moved piece is a pawn and move is an en passant move, set the en passant.
        if (m.getMovingPiece() instanceof Pawn
                && m instanceof Move.PawnDoubleMove) {
            setEnPassant(board, m);
        }

        // Reset the move after the move has been done.
        resetMove();
        return checkGameState(game, board);
    }

    /**
     * Checks if the move made can be targeted by an en passant move. If it can, sets the current
     * status of enPassantTarget and enPassantTargetPlayer to the en passant target.
     *
     * @param m Move needed to be checked.
     */
    private void setEnPassant(Board board, Move m) {
        if (m.getMovingPiece().isWhite()) {
            Game.setEnPassantTarget(board.getSquare(m.getRowEnd() + 1, m.getColEnd()));
            Game.setEnPassantTargetPlayer(this);
        } else {
            Game.setEnPassantTarget(board.getSquare(m.getRowEnd() - 1, m.getColEnd()));
            Game.setEnPassantTargetPlayer(this);
        }
    }

    /**
     * Checks the state of the game after a move has been made. Resets en passant targets, switches
     * player turns and checks if there is check, mate, or stalemate on the board.
     *
     * @return integer value representing the current state of the game.
     */
    private int checkGameState(Game game, Board board) {
        // If the game is in this point, the check of the current player can be unset and the en
        // passant can be reset.
        setCheck(false);
        if (Game.getEnPassantTarget() != null && Game.getEnPassantTargetPlayer() != this) {
            resetEnPassant();
        }

        // Give the turn to the other player
        game.switchPlayerTurn();

        // Get the other player.
        Player opposingPlayer = game.getCurrentPlayer();

        // Calculate the check status.
        boolean check = board.isCheck(opposingPlayer);

        // Calculate all legal moves of the other player.
        ArrayList<Move> currentPlayerMoves = new ArrayList<>();
        for (Square r : board.getPlayerSquares(opposingPlayer.isWhite())) {
            currentPlayerMoves.addAll(opposingPlayer.getLegalMoves(board, r));
        }

        // If there is not legal moves, set stalemate to true.
        boolean stalemate = currentPlayerMoves.size() == 0;

        // Determine the status of the game.
        if (check && stalemate) {
            return opposingPlayer.isWhite() ? 2 : 1;
        } else if (stalemate) {
            return 3;
        } else if (check) {
            opposingPlayer.setCheck(true);
        }
        return 0;
    }

    /**
     * Resets the en passant target square and the en passant target player.
     */
    private void resetEnPassant() {
        Game.setEnPassantTarget(null);
        Game.setEnPassantTargetPlayer(null);
    }

    /**
     * Method for undoing a move on the game board. Updates the board, decreases the move index and
     * sets the pawn en passant, if the move previous to move needed to undo was pawn double move.
     *
     * @param m Move needed to be undone on the game board.
     * @return integer value representing the current state of the game.
     */
    int undoMove(Game game, Board board, Move m) {
        m.undoMove(board);
        game.decreaseMoveIndex();
        ArrayList<Move> moves = game.getMoves();

        // Check if the move previous to the Move "m" was pawn double move. If it was, reset the
        // en passant.
        Move dp;
        if (game.getMoveIndex() > 0 && (dp = moves.get(game.getMoveIndex() - 1)) instanceof Move.PawnDoubleMove) {
            if (dp.getMovingPiece().isWhite()) {
                Game.setEnPassantTarget(board.getSquare(dp.getRowEnd() + 1, dp.getColEnd()));
                Game.setEnPassantTargetPlayer(this);
            } else {
                Game.setEnPassantTarget(board.getSquare(dp.getRowEnd() - 1, dp.getColEnd()));
                Game.setEnPassantTargetPlayer(this);
            }
        } else {
            resetEnPassant();
        }
        resetMove();
        return checkGameState(game, board);
    }

    /**
     * Calculates the squares possible for the current player to move to.
     *
     * @return An ArrayList containing Squares of all possible destination squares.
     */
    ArrayList<Square> getCurrentMoveSquares(Board board) {
        ArrayList<Square> currentMoveSquares = new ArrayList<>();
        ArrayList<Move> currentMoves = getLegalMoves(board, chosenSquare);
        for (Move m : currentMoves) {
            currentMoveSquares.add(board.getSquare(m.getRowEnd(), m.getColEnd()));
        }
        return currentMoveSquares;
    }

    /**
     * Make the final move on the game board and return the game state to the main method.
     *
     * @param game Current game.
     * @return integer value representing the current state of the game.
     */
    int endTurn(Game game) {
        int gameState = makeMove(game, game.getBoard(), currentMove);
        currentMove = null;
        return gameState;
    }
}
