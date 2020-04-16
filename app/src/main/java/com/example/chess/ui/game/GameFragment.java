package com.example.chess.ui.game;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
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
import com.google.android.material.bottomappbar.BottomAppBar;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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

        setHasOptionsMenu(true);

        BottomAppBar bottomAppBar = v.findViewById(R.id.bar);
        bottomAppBar.replaceMenu(R.menu.bottom_navigation);
        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int moveIndex = game.getMoveIndex();
                switch (item.getItemId()) {
                    case R.id.button_back:
                        if (moveIndex > 0) {
                            game.getMoves().get(moveIndex - 1).undoMove(game.getBoard());
                            game.setMoveIndex(moveIndex - 1);
                            game.switchPlayerTurn();
                        } else {
                            Toast.makeText(getContext(), "First Move!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.button_forward:
                        if (moveIndex < game.getMoves().size()) {
                            game.getMoves().get(moveIndex).makeMove(game.getBoard());
                            game.setMoveIndex(moveIndex + 1);
                            game.switchPlayerTurn();
                        } else {
                            Toast.makeText(getContext(), "Last Move!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        break;
                }
                drawGameBoard();
                return true;
            }
        });

        chessboard = v.findViewById(R.id.chessboard);
        drawableTiles = new HashMap<>();
        game = new Game();

        initializeDrawableTiles();

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        drawGameBoard();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_game, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_game:
                newGame();
                return true;
            case R.id.save_game:
                saveGameDialog();
                return true;
            case R.id.load_game:
                loadGameDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        int gameState = game.handleSquareClickEvent(drawableTiles.get(v));
        ArrayList<Move> moves = game.getMoves();
        Move lastMove = null;

        if (moves.size() > 0) {
            lastMove = moves.get(moves.size() - 1);
        }

        drawGameBoard();

        if (gameState > 0) {
            gameEndingDialog(gameState);
        }

        // Handle pawn promotion
        if (lastMove != null
                && game.getBoard().getSquare(lastMove.getRowEnd(), lastMove.getColEnd()).getPiece() instanceof Pawn
                && (lastMove.getRowEnd() == 7 || lastMove.getRowEnd() == 0)) {
            pawnPromotionDialog(lastMove.getRowEnd(), lastMove.getColEnd());
        }

    }

    private void newGame() {
        View v;
        if ((v = getView()) != null) {
            Navigation.findNavController(v).popBackStack();
            Navigation.findNavController(v).navigate(R.id.nav_game);
        }
    }

    private void gameEndingDialog(final int gameState) {
        final Context c;
        if ((c = getContext()) == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(c);

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
                newGame();
            }
        });

        builder.setNeutralButton("Save Game", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveGameDialog();
            }
        });

        builder.setNegativeButton("Main Menu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                View v;
                if ((v = getView()) != null) {
                    Navigation.findNavController(v).navigateUp();
                }
            }
        });

        builder.create().show();
    }

    private void saveGameDialog() {
        final Context c;
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
                        try {
                            FileOutputStream fos = c.openFileOutput(fileOutput, Context.MODE_PRIVATE);
                            ObjectOutputStream oos = new ObjectOutputStream(fos);
                            oos.writeObject(game);
                            fos.close();
                            oos.close();
                            Toast.makeText(c, "Game Saved!", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Toast.makeText(c, "Error occured: " + e, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
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

    private void loadGameDialog() {
        final Context c;
        if ((c = getContext()) == null) {
            return;
        }
        final String[] fileList = c.fileList();
        new AlertDialog.Builder(c)
                .setTitle("Pick a game to load")
                .setItems(fileList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String filePath = fileList[which];
                        try {
                            FileInputStream fis = c.openFileInput(filePath);
                            ObjectInputStream ois = new ObjectInputStream(fis);
                            game = (Game) ois.readObject();
                            ois.close();
                            fis.close();
                            initializeDrawableTiles();
                            drawGameBoard();
                            Toast.makeText(c, "Game successfully loaded!", Toast.LENGTH_SHORT).show();
                        } catch (ClassNotFoundException e) {
                            game = new Game();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .create()
                .show();
    }

    private void pawnPromotionDialog(final int row, final int col) {
        final Context c;
        if ((c = getContext()) == null) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(c);

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
                        drawGameBoard();
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

    private void drawGameBoard() {
        Square s;
        ImageView iv;
        LayerDrawable ld;
        ArrayList<Square> moveSquares = new ArrayList<>();
        Square chosenSquare;
        Square kingSquare = null;
        ArrayList<Square> moveHints = null;
        Move lastMove = null;
        ArrayList<Move> moves = game.getMoves();
        boolean isCheck = game.isCheck();

        if (game.getChosenSquare() != null) {
            moveHints = game.getCurrentMoveSquares();
        }

        if (moves.size() > 0) {
            lastMove = moves.get(moves.size() - 1);
        }

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