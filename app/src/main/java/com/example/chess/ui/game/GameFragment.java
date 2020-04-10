package com.example.chess.ui.game;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.chess.R;
import com.example.chess.game.Game;
import com.example.chess.game.Move;
import com.example.chess.game.Square;
import com.example.chess.game.pieces.*;

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
        drawGameBoard(null, null, false);
    }

    @Override
    public void onClick(View v) {
        int gameState = game.handleSquareClickEvent(drawableTiles.get(v));

       ArrayList<Square> moveHints = null;
        if (game.getChosenSquare() != null) {
            moveHints = game.getCurrentMoveSquares();
        }

        Move lastMove = null;
        ArrayList<Move> moves = game.getMoves();
        if (moves.size() > 0) {
            lastMove = moves.get(moves.size() - 1);
        }

        drawGameBoard(moveHints, lastMove, game.isCheck());

        // Handle pawn promotion
        if (lastMove != null && lastMove.getMovingPiece() instanceof Pawn &&
                (lastMove.getRowEnd() == 7 || lastMove.getRowEnd() == 0)) {
            Piece promotablePiece = pawnPromotion();
            game.promotePawn(lastMove.getRowEnd(), lastMove.getColEnd(), promotablePiece);
            drawGameBoard(moveHints, lastMove, game.isCheck());
        }

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

    private int pawnPromotionDialog() {
        final int[] clickedItem = new int[1];
        AlertDialog.Builder builder;

        if (getActivity() != null) {
            builder = new AlertDialog.Builder(getActivity());
        } else {
            return -1;
        }

        builder.setTitle("Pick a piece to promote:")
                .setItems(R.array.promotablePieces, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clickedItem[0] = which;
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

        return clickedItem[0];
    }

    private Piece pawnPromotion() {
        Piece p;
        boolean currentPlayer = !game.isWhiteTurn();

        switch (pawnPromotionDialog()) {
            case 0:
                p = new Queen(currentPlayer);
                break;
            case 1:
                p = new Knight(currentPlayer);
                break;
            case 2:
                p = new Rook(currentPlayer);
                break;
            case 3:
                p = new Bishop(currentPlayer);
                break;
            default:
                return null;
        }
        return p;
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

    private void drawGameBoard(ArrayList<Square> moveHints, Move lastMove, boolean isCheck) {
        Square s;
        ImageView iv;
        LayerDrawable ld;
        ArrayList<Square> moveSquares = new ArrayList<>();
        Square chosenSquare;
        Square kingSquare = null;

        if (lastMove != null) {
            moveSquares.add(game.getBoard().getSquare(lastMove.getRowStart(), lastMove.getColStart()));
            moveSquares.add(game.getBoard().getSquare(lastMove.getRowEnd(), lastMove.getColEnd()));
        }

        if (isCheck) {
            kingSquare = game.getBoard().findPlayerKing(game.getCurrentPlayer());
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

            if (isCheck && s.equals(kingSquare)) {
                ld.addLayer(getResources().getDrawable(R.drawable.legal_attack_hint));
            }

            if (s.getPiece() != null) {
                ld.addLayer(getResources().getDrawable(s.getPiece().getDrawable()));
            }

            iv.setImageDrawable(ld);
        }
    }
}