package com.example.chess.ui.game;

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
import com.example.chess.game.Square;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameFragment extends Fragment implements View.OnClickListener {

    private GridLayout chessboard;
    private Map<ImageView, Square> drawableTiles;
    private Game game;

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
        drawGameBoard(null);
    }

    @Override
    public void onClick(View v) {
        Square sq = drawableTiles.get(v);
        game.setClickEvent(sq);
        if (game.getChosenSquare() != null) {
            drawGameBoard(sq);
        } else {
            drawGameBoard(null);
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

    private void drawGameBoard(Square sq) {
        Square s;
        ImageView iv;
        ArrayList<Square> squares = new ArrayList<>();

        if (sq != null) {
            squares = game.getCurrentLegalMoves(sq);
        }

        for (Map.Entry e : drawableTiles.entrySet()) {
            s = (Square) e.getValue();
            iv = (ImageView) e.getKey();

            if (squares.contains(sq)) {
                iv.setImageResource(s.getPiece().getDrawable());
            } else if (s.getPiece() == null) {
                iv.setImageDrawable(null);
            } else {
                iv.setImageResource(s.getPiece().getDrawable());
            }
        }
    }
}