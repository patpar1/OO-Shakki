package com.example.chess.ai;

import com.example.chess.game.Board;
import com.example.chess.game.Game;
import com.example.chess.game.Move;
import com.example.chess.game.Player;
import com.example.chess.game.Square;

import java.util.ArrayList;
import java.util.Random;

public class AlphaBetaPlayer extends Player {
    public AlphaBetaPlayer(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public void handleSquareClickEvent(Game game, Square sq) {
    }

    public void makeMove(Game game) {
        Board b = game.getBoard();
        ArrayList<Square> squares = b.getPlayerSquares(this.isWhite());
        Move m;
        while (true) {
            Square s = squares.get(new Random().nextInt(squares.size()));
            ArrayList<Move> moves = getLegalMoves(b, s);
            if (moves.size() > 0) {
                m = moves.get(new Random().nextInt(moves.size()));
                game.getMoves().add(m);
                makeMove(game, b, m);
                return;
            }
        }
    }
}
