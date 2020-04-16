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

    /**
     * Instantiates the GameFragment to the user interface view.
     *
     * @param inflater           Inflater object which can inflate any views in the fragment.
     * @param container          Parent view that the fragment's UI should be attached to.
     * @param savedInstanceState Previous state of this fragment.
     * @return the View for the fragment's UI.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game, container, false);

        // Setting for the options menu inflation
        setHasOptionsMenu(true);

        // Creates the bottom app bar for the undo and redo commands
        BottomAppBar bottomAppBar = v.findViewById(R.id.bar);
        bottomAppBar.replaceMenu(R.menu.bottom_navigation);
        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int moveIndex = game.getMoveIndex();
                switch (item.getItemId()) {
                    case R.id.button_back:
                        // Undo move
                        if (moveIndex > 0) {
                            game.getMoves().get(moveIndex - 1).undoMove(game.getBoard());
                            game.setMoveIndex(moveIndex - 1);
                            game.switchPlayerTurn();
                        } else {
                            Toast.makeText(getContext(), "First Move!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.button_forward:
                        // Redo move
                        if (moveIndex < game.getMoves().size()) {
                            game.getMoves().get(moveIndex).makeFinalMove(game.getBoard());
                            game.setMoveIndex(moveIndex + 1);
                            game.switchPlayerTurn();
                        } else {
                            Toast.makeText(getContext(), "Last Move!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        break;
                }
                // When board manipulation command is done, the board needs to be updated
                drawGameBoard();
                return true;
            }
        });

        // Initializes the map which maps all Squares of the Board to corresponding ImageViews
        drawableTiles = new HashMap<>();
        initializeDrawableTiles();

        // Main chessboard view
        chessboard = v.findViewById(R.id.chessboard);

        // Game class for the game logic
        game = new Game();

        return v;
    }

    /**
     * Called immediately after onCreateView() method. Used to draw the game board on the creation
     * of this fragment.
     *
     * @param view               View returned by onCreateView().
     * @param savedInstanceState Previous state of this fragment.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        drawGameBoard();
    }

    /**
     * Inflates the options menu on the toolbar.
     *
     * @param menu     contains the options menu to be created.
     * @param inflater Inflater object which inflates the menu XML.
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_game, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Called when options item is selected.
     *
     * @param item Selected Menu item.
     * @return boolean value to allow normal menu processing to proceed
     */
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

    /**
     * Method which is called after View.OnClickListener detects a click on UI board squares.
     *
     * @param v The view which was clicked.
     */
    @Override
    public void onClick(View v) {
        /*
         * First onClick method searches the square in Game classes board that corresponds to the clicked
         * square. After that the Game class handles the click event and makes the move on it's board.
         * Game class returns an integer which represents the game's state after the move is done.
         */
        int gameState = game.handleSquareClickEvent(drawableTiles.get(v));

        // If game does not continue, shows the game ending dialog.
        if (gameState > 0) {
            gameEndingDialog(gameState);
        }

        // Draw the game board to the UI after the move is done.
        drawGameBoard();

        // Get the latest made move for pawn promotion dialog.
        int moves;
        Move lastMove = null;
        if ((moves = game.getMoves().size()) > 0) {
            lastMove = game.getMoves().get(moves - 1);
        }

        // Handle pawn promotion.
        if (lastMove != null
                && game.getBoard().getSquare(lastMove.getRowEnd(), lastMove.getColEnd()).getPiece() instanceof Pawn
                && (lastMove.getRowEnd() == 7 || lastMove.getRowEnd() == 0)) {
            pawnPromotionDialog(lastMove.getRowEnd(), lastMove.getColEnd());
        }

    }

    /**
     * Method for instantiating a new game. Pops the current fragment and creates a new instance
     * of it.
     */
    private void newGame() {
        View v;
        if ((v = getView()) != null) {
            Navigation.findNavController(v).popBackStack();
            Navigation.findNavController(v).navigate(R.id.nav_game);
        }
    }

    /**
     * Creates a dialog for game ending. User can either start a new game, save the ended game or
     * return to the main menu.
     *
     * @param gameState integer representing the game ending state.
     */
    private void gameEndingDialog(final int gameState) {
        // Find the application context for the dialog builder.
        // If context isn't found, return to the upper method.
        final Context c;
        if ((c = getContext()) == null) {
            return;
        }

        // Builder class for building the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(c);

        // Determine the dialogs title based on game ending state.
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

        // Set button for starting a new game.
        builder.setPositiveButton("New Game", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newGame();
            }
        });

        // Set button for saving the game.
        builder.setNeutralButton("Save Game", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveGameDialog();
            }
        });

        // Set button for returning to the main menu.
        builder.setNegativeButton("Main Menu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                View v;
                if ((v = getView()) != null) {
                    Navigation.findNavController(v).navigateUp();
                }
            }
        });

        // Show the dialog.
        builder.create().show();
    }

    /**
     * Creates a dialog for game saving. User can input a file name and the method saves the game
     * object to a file named by user.
     */
    private void saveGameDialog() {
        // Find the application context for the builder.
        final Context c;
        if ((c = getContext()) == null) {
            return;
        }

        // Prepare text input fields for the builder.
        final EditText input = new EditText(c);
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        // Builder class for building the dialog.
        new AlertDialog.Builder(c)
                .setTitle("Save Game")
                .setMessage("Type the file name:")
                .setView(input)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    // Button for saving the Game class to a file.
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
                            // File was not found.
                            Toast.makeText(c, "Error occured: " + e, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    // Button for canceling the saving.
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create()
                .show();
    }

    /**
     * Creates a dialog for loading a game. User can choose a save file from a list and the method
     * loads the game which user chose to the UI.
     */
    private void loadGameDialog() {
        // Find the application context for the builder.
        final Context c;
        if ((c = getContext()) == null) {
            return;
        }

        // Find files saved to the system memory
        final String[] fileList = c.fileList();

        // Builder class for building the dialog.
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

                            // After the game has been loaded the UI views has to be reinitialized
                            // and the board has to be drawn.
                            initializeDrawableTiles();
                            drawGameBoard();

                            Toast.makeText(c, "Game successfully loaded!", Toast.LENGTH_SHORT).show();
                        } catch (ClassNotFoundException e) {
                            // If Game class was not found, return a new Game class
                            Toast.makeText(c, "Error occured: " + e, Toast.LENGTH_SHORT).show();
                            game = new Game();
                        } catch (IOException e) {
                            // File was not found
                            Toast.makeText(c, "Error occured: " + e, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                })
                .create()
                .show();
    }

    /**
     * Creates a dialog for pawn promotion. User can choose the piece to promote to from a list
     * and the method replaces the pawn with the piece chosen by the user.
     *
     * @param row Row of the promotable pawn.
     * @param col Column of the promotavble pawn.
     */
    private void pawnPromotionDialog(final int row, final int col) {
        // Find the application context for the builder.
        final Context c;
        if ((c = getContext()) == null) {
            return;
        }

        // Builder class for building the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(c);

        builder.setTitle("Pick a piece to promote:")
                .setItems(R.array.promotablePieces, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Piece promotablePiece = null;
                        boolean currentPlayer = !game.isWhiteTurn();

                        // Choose the piece to promote the pawn to.
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

                        // Promote the pawn on game board.
                        if (promotablePiece != null) {
                            game.promotePawn(row, col, promotablePiece);
                        }
                        // Update the UI board.
                        drawGameBoard();
                    }
                });

        // Show the promotion dialog.
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Initializes the drawables. Finds the Square ImageViews, sets onClickListeners to them and
     * adds the UI tiles to the drawableTiles Map.
     */
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

    /**
     * Main method for drawing the UI game board. Layers the different drawables so all drawables
     * can be shown on one ImageView.
     */
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

        // Calculate the move hints for chosen piece for drawing the move hints.
        if (game.getChosenSquare() != null) {
            moveHints = game.getCurrentMoveSquares();
        }

        // Get the latest move made for drawing the latest move.
        if (moves.size() > 0) {
            lastMove = moves.get(moves.size() - 1);
        }

        // Add the squares of the latest move made to an ArrayList.
        if (lastMove != null) {
            moveSquares.add(game.getBoard().getSquare(lastMove.getRowStart(), lastMove.getColStart()));
            moveSquares.add(game.getBoard().getSquare(lastMove.getRowEnd(), lastMove.getColEnd()));
        }

        // If there is a check on the board, visualize it on the king's square.
        if (isCheck) {
            kingSquare = game.getBoard().findPlayerKing(game.getCurrentPlayer());
        }

        // Loops and draws all squares.
        for (Map.Entry entry : drawableTiles.entrySet()) {
            s = (Square) entry.getValue();
            iv = (ImageView) entry.getKey();
            ld = new LayerDrawable(new Drawable[]{});
            chosenSquare = game.getChosenSquare();

            // If player has chosen a square, visualize the chosen square.
            if (s.equals(chosenSquare)) {
                ld.addLayer(getResources().getDrawable(R.drawable.last_moved));
            }

            // Visualize the latest move made by the player.
            if (lastMove != null) {
                if (moveSquares.contains(s)) {
                    ld.addLayer(getResources().getDrawable(R.drawable.last_moved));
                }
            }

            // Visualize the legal moves of chosen square.
            if (moveHints != null && moveHints.contains(s)) {
                // If the move is an attacking move, show it as red.
                if (s.getPiece() != null || s.equals(Game.getEnPassantTarget())) {
                    ld.addLayer(getResources().getDrawable(R.drawable.legal_attack_hint));
                }

                // Otherwise show it as a black circle.
                else {
                    ld.addLayer(getResources().getDrawable(R.drawable.legal_move_hint));
                }
            }

            // If there is check on the board, show it under the player king.
            if (isCheck && s.equals(kingSquare)) {
                ld.addLayer(getResources().getDrawable(R.drawable.legal_attack_hint));
            }

            // If there is a piece on the square, get it's drawable resource.
            if (s.getPiece() != null) {
                ld.addLayer(getResources().getDrawable(s.getPiece().getDrawable()));
            }

            // Set the ImageView's drawable to the LayerDrawable.
            iv.setImageDrawable(ld);
        }
    }
}