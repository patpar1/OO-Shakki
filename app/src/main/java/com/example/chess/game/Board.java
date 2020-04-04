package com.example.chess.game;

import com.example.chess.game.pieces.*;

import java.util.ArrayList;

public class Board {

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
            K = Knight
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
            //squares[6][i] = new Square(6, i);
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
            //squares[1][i] = new Square(1, i);
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

    Board copy() {
        Board b = new Board(new Square[8][8]);
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                b.squares[j][i] = squares[j][i].copy();
            }
        }
        return b;
    }

    public Square getSquare(int row, int col) {
        return squares[row][col];
    }

    ArrayList<Square> getPlayerSquares(boolean isWhite) {
        ArrayList<Square> r = new ArrayList<>();

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (squares[i][j].getPiece() != null) {
                    if (squares[i][j].getPiece().isWhite() == isWhite) {
                        r.add(squares[i][j]);
                    }
                }
            }
        }
        return r;
    }

    ArrayList<Piece> getPlayerPieces(boolean isWhite) {
        ArrayList<Piece> pieces = new ArrayList<>();
        for (Square s : getPlayerSquares(isWhite)) {
            pieces.add(s.getPiece());
        }
        return pieces;
    }

    boolean squareInEnemyLine(Player currentPlayer, Square square) {
        ArrayList<Square> enemySquares = new ArrayList<>();
        ArrayList<Square> pieceSquareCandidates = new ArrayList<>();
        for (Square r : getPlayerSquares(!currentPlayer.isWhite())) {
            for (Move m : r.getPiece().legalMoves(this, r)) {
                pieceSquareCandidates.add(getSquare(m.getRowEnd(), m.getColEnd()));
            }
            enemySquares.addAll(pieceSquareCandidates);
        }

        return enemySquares.contains(square);
    }

    boolean isCheck(Player player) {
        int kingX = -1;
        int kingY = -1;

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (squares[i][j].getPiece() instanceof King &&
                        squares[i][j].getPiece().isWhite() == player.isWhite()) {
                    kingY = i;
                    kingX = j;
                    break;
                }
            }
        }

        if (kingX == -1) {
            throw new IllegalStateException();
        }

        return squareInEnemyLine(player, getSquare(kingY, kingX));

    }

    String printBoard(ArrayList<Square> legalSquares) {
        StringBuilder sb = new StringBuilder();

        sb.append("\n");

        for (int i = 0; i < BOARD_SIZE; i++) {
            sb.append(8 - i).append("\t");

            for (int j = 0; j < BOARD_SIZE; j++) {
                if (legalSquares != null) {
                    if (legalSquares.contains(this.getSquare(i, j))) {
                        sb.append('*').append(" ");
                        continue;
                    }
                }
                if (this.getSquare(i, j).getPiece() == null) {
                    sb.append('-');
                } else {
                    sb.append(this.getSquare(i, j).getPiece().toString());
                }
                sb.append(" ");
            }
            sb.append("\n");
        }
        sb.append("\n \ta b c d e f g h");

        return sb.toString();
    }

    public String printBoard() {
        return printBoard(null);
    }

}
