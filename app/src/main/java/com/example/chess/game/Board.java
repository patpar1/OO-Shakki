package com.example.chess.game;

import com.example.chess.game.pieces.*;

import java.io.Serializable;
import java.util.ArrayList;

public class Board implements Serializable {

    private static final int BOARD_SIZE = 8;

    private Square[][] squares;

    /*  Board structure at the start:

            8       r n b q k b n r
            7       p p p p p p p p
            6       - - - - - - - -
            5       - - - - - - - -
            4       - - - - - - - -
            3       - - - - - - - -
            2       P P P P P P P P
            1       R N B Q K B N R

                    a b c d e f g h

            P = Pawn
            N = Knight
            B = Bishop
            R = Rook
            Q = Queen
            K = King

            White player's pieces are in uppercase.

            Square[y][x]:
                y = Row index (between 0-7, index 0 means row '8' on board)
                x = Column index (between 0-7, index 0 means column 'a' on board)
     */

    public Board() {
        squares = new Square[BOARD_SIZE][BOARD_SIZE];

        // White pieces
        squares[7][0] = new Square(new Rook(true), 7, 0);
        squares[7][1] = new Square(new Knight(true), 7, 1);
        squares[7][2] = new Square(new Bishop(true), 7, 2);
        squares[7][3] = new Square(new Queen(true), 7, 3);
        squares[7][4] = new Square(new King(true), 7, 4);
        squares[7][5] = new Square(new Bishop(true), 7, 5);
        squares[7][6] = new Square(new Knight(true), 7, 6);
        squares[7][7] = new Square(new Rook(true), 7, 7);

        for (int i = 0; i < BOARD_SIZE; i++) {
            squares[6][i] = new Square(new Pawn(true), 6, i);
        }

        // Black pieces
        squares[0][0] = new Square(new Rook(false), 0, 0);
        squares[0][1] = new Square(new Knight(false), 0, 1);
        squares[0][2] = new Square(new Bishop(false), 0, 2);
        squares[0][3] = new Square(new Queen(false), 0, 3);
        squares[0][4] = new Square(new King(false), 0, 4);
        squares[0][5] = new Square(new Bishop(false), 0, 5);
        squares[0][6] = new Square(new Knight(false), 0, 6);
        squares[0][7] = new Square(new Rook(false), 0, 7);

        for (int i = 0; i < BOARD_SIZE; i++) {
            squares[1][i] = new Square(new Pawn(false), 1, i);
        }

        // Empty squares
        for (int j = 2; j < (BOARD_SIZE - 2); j++) {
            for (int i = 0; i < BOARD_SIZE; i++) {
                squares[j][i] = new Square(j, i);
            }
        }
    }

    public Board(Square[][] squares) {
        this.squares = squares;
    }

    /**
     * Creates a new board and copies all squares of this board to it.
     *
     * @return A copy of this board.
     */
    public Board copy() {
        Board b = new Board(new Square[8][8]);
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                b.squares[j][i] = squares[j][i].copy();
            }
        }
        return b;
    }

    /**
     * Finds a square on the game board based on the row and the column number.
     *
     * @param row Row of the square.
     * @param col Column of the square.
     * @return A square on the given row and column.
     */
    public Square getSquare(int row, int col) {
        return squares[row][col];
    }

    /**
     * Finds players king on the game board. If the method does not find player king, throws an
     * IllegalStateException, since the game can't ever be in this state.
     *
     * @param player Player searching the king.
     * @return A square where the player king is.
     */
    public Square findPlayerKing(Player player) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (squares[i][j].getPiece() instanceof King &&
                        squares[i][j].getPiece().isWhite() == player.isWhite()) {
                    return getSquare(i, j);
                }
            }
        }
        // Shouldn't ever be in this part of the program.
        throw new IllegalStateException();
    }

    /**
     * Get all squares where a player's piece is currently.
     *
     * @param isWhite Current player's color.
     * @return An ArrayList of all squares with current players pieces.
     */
    public ArrayList<Square> getPlayerSquares(boolean isWhite) {
        ArrayList<Square> playerSquares = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (squares[i][j].getPiece() != null
                        && squares[i][j].getPiece().isWhite() == isWhite) {
                    playerSquares.add(squares[i][j]);
                }
            }
        }
        return playerSquares;
    }

    /**
     * Searches if the square is in enemy's line of sight. Used for determining the legality
     * of a castling move.
     *
     * @param currentPlayer Current player's turn.
     * @param square        Searchable square.
     * @return boolean value representing, if the given square is in enemy line.
     */
    boolean squareInEnemyLine(Player currentPlayer, Square square) {
        ArrayList<Square> enemySquares = new ArrayList<>();
        ArrayList<Square> pieceSquareCandidates = new ArrayList<>();
        for (Square s : getPlayerSquares(!currentPlayer.isWhite())) {
            for (Move m : s.getPiece().legalMoves(this, s)) {
                pieceSquareCandidates.add(getSquare(m.getRowEnd(), m.getColEnd()));
            }
            enemySquares.addAll(pieceSquareCandidates);
        }
        return enemySquares.contains(square);
    }

    /**
     * Calculates if there is a check on the game board.
     *
     * @param player Current player.
     * @return A boolean value representing the check status of the game board.
     */
    boolean isCheck(Player player) {
        return squareInEnemyLine(player, findPlayerKing(player));
    }
}
