package com.example.chess.ui.game;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.chess.R;
import com.example.chess.game.Game;
import com.example.chess.game.Move;
import com.example.chess.game.Square;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameFragment extends Fragment implements View.OnClickListener {

    private GridLayout chessboard;
    private Map<ImageView, Square> drawableTiles;
    private Game game;
    private int gameState;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game, container, false);

        chessboard = v.findViewById(R.id.chessboard);
        drawableTiles = new HashMap<>();
        game = new Game();

        initializeDrawableTiles();

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        drawGameBoard(null, null);
    }

    @Override
    public void onClick(View v) {
        gameState = game.handleSquareClickEvent(drawableTiles.get(v));

       ArrayList<Square> moveHints = null;
        if (game.getChosenSquare() != null) {
            moveHints = game.getCurrentMoveSquares();
        }

        Move lastMove = null;
        ArrayList<Move> moves = game.getMoves();
        if (moves.size() > 0) {
            lastMove = moves.get(moves.size() - 1);
        }
        drawGameBoard(moveHints, lastMove);

        switch (gameState) {
            case 1:
                // White won
                break;
            case 2:
                // Black won
                break;
            case 3:
                // Stalemate
                break;
            case -1:
                // Error
                return;
            default:
                // Game continues
                break;
        }
    }

    private void initializeDrawableTiles() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                int index = 8 * row + col;
                Square sq = game.getBoard().getSquare(row, col);
                View tempV = chessboard.getChildAt(index);
                if (tempV != null) {
                    if (tempV instanceof ImageView) {
                        tempV.setOnClickListener(this);
                        drawableTiles.put((ImageView) tempV, sq);
                    }
                }
            }
        }
    }

    private void drawGameBoard(ArrayList<Square> moveHints, Move lastMove) {
        Square s;
        ImageView iv;
        LayerDrawable ld;
        ArrayList<Square> moveSquares = new ArrayList<>();
        Square chosenSquare;

        if (lastMove != null) {
            moveSquares.add(game.getBoard().getSquare(lastMove.getRowStart(), lastMove.getColStart()));
            moveSquares.add(game.getBoard().getSquare(lastMove.getRowEnd(), lastMove.getColEnd()));
        }

        for (Map.Entry entry : drawableTiles.entrySet()) {
            s = (Square) entry.getValue();
            iv = (ImageView) entry.getKey();
            ld = new LayerDrawable(new Drawable[]{});
            chosenSquare = game.getChosenSquare();

            if (s.equals(chosenSquare)) {
                ld.addLayer(getResources().getDrawable(R.drawable.last_moved));
            }

            if (lastMove != null) {
                if (moveSquares.contains(s)) {
                    ld.addLayer(getResources().getDrawable(R.drawable.last_moved));
                }
            }

            if (moveHints != null && moveHints.contains(s)) {
                if (s.getPiece() != null || s.equals(Game.getEnPassantTarget())) {
                    ld.addLayer(getResources().getDrawable(R.drawable.legal_attack_hint));
                } else {
                    ld.addLayer(getResources().getDrawable(R.drawable.legal_move_hint));
                }
            }

            if (s.getPiece() != null) {
                ld.addLayer(getResources().getDrawable(s.getPiece().getDrawable()));
            }

            iv.setImageDrawable(ld);
        }
    }
}