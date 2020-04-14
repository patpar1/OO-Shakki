package com.example.chess.ui.game;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.chess.R;
import com.example.chess.game.Game;
import com.example.chess.game.Move;
import com.example.chess.game.Square;
import com.example.chess.game.pieces.Bishop;
import com.example.chess.game.pieces.Knight;
import com.example.chess.game.pieces.Pawn;
import com.example.chess.game.pieces.Piece;
import com.example.chess.game.pieces.Queen;
import com.example.chess.game.pieces.Rook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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

        if (gameState > 0) {
            gameEndingDialog(gameState);
        }

        // Handle pawn promotion
        if (lastMove != null
                && game.getBoard().getSquare(lastMove.getRowEnd(), lastMove.getColEnd()).getPiece() instanceof Pawn
                && (lastMove.getRowEnd() == 7 || lastMove.getRowEnd() == 0)) {
            pawnPromotionDialog(lastMove.getRowEnd(), lastMove.getColEnd(), lastMove);
        }

    }

    public Game getGame() {
        return game;
    }

    private void gameEndingDialog(final int gameState) {
        AlertDialog.Builder builder;

        if (getActivity() != null) {
            builder = new AlertDialog.Builder(getActivity());
        } else {
            return;
        }

        switch (gameState) {
            case 1:
                // White won
                builder.setTitle("White player won!")
                        .setMessage("White player has won the game!");
                break;
            case 2:
                // Black won
                builder.setTitle("Black player won!")
                        .setMessage("Black player has won the game!");
                break;
            case 3:
                // Stalemate
                builder.setTitle("Stalemate!")
                        .setMessage("Player has no legal moves!");
                break;
            default:
                // Error
                builder.setTitle("Error occurred!")
                        .setMessage("An error has occurred!");
                break;
        }

        builder.setPositiveButton("New Game", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                View v;
                if ((v = getView()) != null) {
                    Navigation.findNavController(v).popBackStack();
                    Navigation.findNavController(v).navigate(R.id.nav_game);
                }
            }
        });

        builder.setNeutralButton("Save Game", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveGameFragment();
            }
        });

        builder.setNegativeButton("Main Menu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                View v;
                if ((v = getView()) != null) {
                    // Navigation.findNavController(v).navigateUp();
                    Navigation.findNavController(v).navigate(R.id.nav_main);
                }
            }
        });

        builder.create().show();
    }

    private void saveGameFragment() {
        Context c;
        if ((c = getContext()) == null) {
            return;
        }
        final EditText input = new EditText(c);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        new AlertDialog.Builder(c)
                .setTitle("Save Game")
                .setMessage("Type the file name:")
                .setView(input)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    String fileOutput;
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fileOutput = input.getText().toString();
                        saveGameState(fileOutput);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create()
                .show();
    }

    private void saveGameState(String fileOutput) {
        Context c;
        if ((c = getContext()) == null) {
            return;
        }
        try {
            FileOutputStream fos = c.openFileOutput(fileOutput, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(game);
            fos.close();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void pawnPromotionDialog(final int row, final int col, final Move lastMove) {
        AlertDialog.Builder builder;

        if (getActivity() != null) {
            builder = new AlertDialog.Builder(getActivity());
        } else {
            return;
        }

        builder.setTitle("Pick a piece to promote:")
                .setItems(R.array.promotablePieces, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Piece promotablePiece = null;
                        boolean currentPlayer = !game.isWhiteTurn();

                        switch (which) {
                            case 0:
                                promotablePiece = new Queen(currentPlayer);
                                break;
                            case 1:
                                promotablePiece = new Knight(currentPlayer);
                                break;
                            case 2:
                                promotablePiece = new Rook(currentPlayer);
                                break;
                            case 3:
                                promotablePiece = new Bishop(currentPlayer);
                                break;
                        }

                        if (promotablePiece != null) {
                            game.promotePawn(row, col, promotablePiece);
                        }
                        drawGameBoard(null, lastMove, game.isCheck());
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void initializeDrawableTiles() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                int index = 8 * row + col;
                Square sq = game.getBoard().getSquare(row, col);
                View tempV = chessboard.getChildAt(index);
                if (tempV instanceof ImageView) {
                    tempV.setOnClickListener(this);
                    drawableTiles.put((ImageView) tempV, sq);
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