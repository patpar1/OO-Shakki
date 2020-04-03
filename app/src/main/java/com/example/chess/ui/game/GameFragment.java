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
import com.example.chess.game.pieces.Piece;

import java.util.ArrayList;

public class GameFragment extends Fragment implements View.OnClickListener {

    private ArrayList<ImageView> viewArray = new ArrayList<>();

    private Game game;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game, container, false);

        GridLayout chessboard = v.findViewById(R.id.chessboard);

        for (int i = 0; i < chessboard.getChildCount(); i++) {
            View tempV = chessboard.getChildAt(i);
            if (tempV instanceof ImageView) {
                tempV.setOnClickListener(this);
                viewArray.add((ImageView) tempV);
            }
        }

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        game = new Game();

        drawGameBoard();
    }

    @Override
    public void onClick(View v) {
        System.out.println(getResources().getResourceEntryName(v.getId()));
    }

    void drawGameBoard() {
        Piece p;

        for (int i = 0; i < 64; i++) {
            int y = i >> 3;
            int x = i % 8;

            if ((p = game.getBoard().getSquare(y, x).getPiece()) == null) {
                viewArray.get(i).setImageDrawable(null);
            } else {
                viewArray.get(i).setImageResource(p.getDrawable());
            }
        }
    }
}