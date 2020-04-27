package com.example.chess.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.chess.R;
import com.example.chess.ai.AlphaBetaPlayer;
import com.example.chess.game.Game;
import com.example.chess.game.Move;
import com.example.chess.game.Square;
import com.example.chess.game.pieces.Bishop;
import com.example.chess.game.pieces.Knight;
import com.example.chess.game.pieces.Pawn;
import com.example.chess.game.pieces.Piece;
import com.example.chess.game.pieces.Queen;
import com.example.chess.game.pieces.Rook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GameFragment extends Fragment implements View.OnClickListener {

    private GridLayout chessboard;
    private Map<ImageView, Square> drawableTiles;
    private Game game;

    private long startTime;
    private Handler timerHandler;
    private Runnable timerRunnable;

    private boolean humanSelected;
    private ImageView player1;
    private ImageView player2;
    private TextView turnText;

    public GameFragment() {
        // Required empty public constructor
    }

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

        boolean isHumanPlayer = true;
        int aiLevel = 3;

        if (getArguments() != null) {
            isHumanPlayer = getArguments().getBoolean("isHumanPlayer");
            if (!isHumanPlayer) {
                aiLevel = getArguments().getInt("aiLevel");
            }
        }

        // Game class for the game logic
        game = new Game(isHumanPlayer, aiLevel);

        // Main chessboard view
        chessboard = v.findViewById(R.id.chessboard);

        // Initializes the map which maps all Squares of the Board to corresponding ImageViews
        drawableTiles = new HashMap<>();
        initializeDrawableTiles();

        player1 = v.findViewById(R.id.image_human);
        player2 = v.findViewById(R.id.image_bot);
        if (isHumanPlayer) {
            player2.setBackgroundResource(R.drawable.human_player);
        }

        humanSelected = true;
        turnText = v.findViewById(R.id.turn_text);

        v.findViewById(R.id.new_game_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGame();
            }
        });

        v.findViewById(R.id.save_game_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveGameDialog();
            }
        });


        v.findViewById(R.id.load_game_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadGameDialog();
            }
        });


        v.findViewById(R.id.undo_move_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (game.getMoveIndex() > 0) {
                    checkGameState(game.undoPreviousMove());
                } else {
                    Toast.makeText(getContext(), "First Move!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        v.findViewById(R.id.redo_move_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (game.getMoveIndex() < game.getMoves().size()) {
                    checkGameState(game.makeNextMove());
                } else {
                    Toast.makeText(getContext(), "Last Move!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final TextView timerTextView = v.findViewById(R.id.game_clock);

        startTime = 0;
        timerHandler = new Handler();
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                long millis = System.currentTimeMillis() - startTime;
                int seconds = (int) (millis/1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;

                timerTextView.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
                timerHandler.postDelayed(this, 500);
            }
        };

        return v;
    }

    private void switchSelected() {
        if (!humanSelected) {
            humanSelected = true;

            ViewGroup.LayoutParams humanParams = player1.getLayoutParams();
            ViewGroup.LayoutParams botParams = player2.getLayoutParams();

            humanParams.height = getResources().getDimensionPixelSize(R.dimen.player_selected_game);
            humanParams.width = getResources().getDimensionPixelSize(R.dimen.player_selected_game);
            player1.setLayoutParams(humanParams);

            botParams.height = getResources().getDimensionPixelSize(R.dimen.player_unselected_game);
            botParams.width = getResources().getDimensionPixelSize(R.dimen.player_unselected_game);
            player2.setLayoutParams(botParams);

            turnText.setText("White's turn!");
        } else {
            humanSelected = false;

            ViewGroup.LayoutParams humanParams = player1.getLayoutParams();
            ViewGroup.LayoutParams botParams = player2.getLayoutParams();

            humanParams.height = getResources().getDimensionPixelSize(R.dimen.player_unselected_game);
            humanParams.width = getResources().getDimensionPixelSize(R.dimen.player_unselected_game);
            player1.setLayoutParams(humanParams);

            botParams.height = getResources().getDimensionPixelSize(R.dimen.player_selected_game);
            botParams.width = getResources().getDimensionPixelSize(R.dimen.player_selected_game);
            player2.setLayoutParams(botParams);

            turnText.setText("Black's turn!");
        }
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
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);
    }

    /**
     * Method which is called after View.OnClickListener detects a click on UI board squares.
     *
     * @param v The view which was clicked.
     */
    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public void onClick(View v) {
        /* First onClick method searches the square in Game classes board that corresponds to the clicked
         * square. After that the Game class handles the click event and makes the move on it's board.
         * Game class returns an integer which represents the game's state after the move is done. */
        game.handleSquareClickEvent(drawableTiles.get(v));
        checkPawnPromotionDialog();
    }

    /**
     * Checks if pawn promotion dialog is needed to show. If it is, calls the pawn promotion dialog
     * function. If it isn't ends the player turn or draws the game board.
     */
    private void checkPawnPromotionDialog() {
        // Get the latest made move for pawn promotion dialog.
        Move currentMove = game.getCurrentPlayer().getCurrentMove();

        // Handle pawn promotion.
        if (currentMove != null
                && currentMove.getMovingPiece() instanceof Pawn
                && (currentMove.getRowEnd() == 7 || currentMove.getRowEnd() == 0)) {
            // If the pawn is in first or last rank, promote the pawn.
            pawnPromotionDialog(currentMove, true);
        } else {
            if (game.getCurrentPlayer().canEndTurn()) {
                endTurn();
            } else {
                drawGameBoard();
            }
        }
    }

    /**
     * Method which ends the current players turn. Used to make sure user can do all actions
     * (eg. promote a pawn) before AI does it's turn.
     */
    private void endTurn() {
        // End the turn.
        int gameState = game.endTurn();

        // Draw the game board to the UI after the move is done.
        drawGameBoard();
        switchSelected();

        // If game does not continue, shows the game ending dialog.
        if (gameState > 0) {
            gameEndingDialog(gameState);
            return;
        }

        if (game.getCurrentPlayer() instanceof AlphaBetaPlayer) {
            ((AlphaBetaPlayer) game.getCurrentPlayer()).calculateMove(game);
            checkPawnPromotionDialog();
        }
    }

    /**
     * Method for checking the game state, after user has manually undone or redone moves.
     *
     * @param gameState integer value representing the current state of the game.
     */
    private void checkGameState(int gameState) {
        Move currentMove = null;
        ArrayList<Move> moves = game.getMoves();

        if (moves.size() > 0 && game.getMoveIndex() > 0) {
            currentMove = game.getMoves().get(game.getMoveIndex() - 1);
        }

        // Handle pawn promotion.
        if (currentMove != null
                && currentMove.getMovingPiece() instanceof Pawn
                && (currentMove.getRowEnd() == 7 || currentMove.getRowEnd() == 0)) {
            pawnPromotionDialog(currentMove, false);
        }

        drawGameBoard();
        switchSelected();

        if (gameState > 0) {
            gameEndingDialog(gameState);
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
                            Toast.makeText(c, "Error occurred: " + e, Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(c, "Error occurred: " + e, Toast.LENGTH_SHORT).show();
                            View v;
                            if ((v = getView()) != null) {
                                Navigation.findNavController(v).navigateUp();
                            }
                        } catch (IOException e) {
                            // File was not found
                            Toast.makeText(c, "Error occurred: " + e, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                })
                .create()
                .show();
    }


    /**
     * Creates a dialog for pawn promotion. User can choose the piece to promote to from a list and
     * the method replaces the pawn with the piece chosen by the user.
     *
     * @param currentMove Current pawn move.
     * @param endTurn     Boolean value, true if this is at the end of turn.
     */
    private void pawnPromotionDialog(final Move currentMove, final boolean endTurn) {
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
                        boolean currentPlayer = game.isWhiteTurn();

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
                            currentMove.setPromotion(promotablePiece);
                        }
                        if (endTurn) {
                            endTurn();
                        }
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
        Activity a = getActivity();

        if (a == null) {
            return;
        }

        Square s;
        ImageView iv;
        LayerDrawable ld;

        // Calculate the move hints for chosen piece for drawing the move hints.
        Square chosenSquare = game.getCurrentPlayer().getChosenSquare();
        ArrayList<Square> moveHints = null;
        if (chosenSquare != null) {
            moveHints = game.getCurrentMoveSquares();
        }

        // Get the latest move made for drawing the latest move.
        Move lastMove = null;
        ArrayList<Move> moves = game.getMoves();
        if (moves.size() > 0 && game.getMoveIndex() > 0) {
            lastMove = moves.get(game.getMoveIndex() - 1);
        }

        // Add the squares of the latest move made to an ArrayList.
        ArrayList<Square> moveSquares = new ArrayList<>();
        if (lastMove != null) {
            moveSquares.add(game.getBoard().getSquare(lastMove.getRowStart(), lastMove.getColStart()));
            moveSquares.add(game.getBoard().getSquare(lastMove.getRowEnd(), lastMove.getColEnd()));
        }

        // If there is a check on the board, visualize it on the king's square.
        Square kingSquare = null;
        boolean isCheck = game.getCurrentPlayer().isCheck();
        if (isCheck) {
            kingSquare = game.getBoard().findPlayerKing(game.getCurrentPlayer());
        }

        // Loop through and draw all squares.
        for (Map.Entry entry : drawableTiles.entrySet()) {
            s = (Square) entry.getValue();
            iv = (ImageView) entry.getKey();
            ld = new LayerDrawable(new Drawable[]{});

            // If player has chosen a square, visualize the chosen square.
            if (s.equals(chosenSquare)) {
                ld.addLayer(getResources().getDrawable(R.drawable.last_moved, a.getTheme()));
            }

            // Visualize the latest move made by the player.
            if (lastMove != null) {
                if (moveSquares.contains(s)) {
                    ld.addLayer(getResources().getDrawable(R.drawable.last_moved, a.getTheme()));
                }
            }

            // Visualize the legal moves of chosen square.
            if (moveHints != null && moveHints.contains(s)) {
                // If the move is an attacking move, show it as red.
                if (s.getPiece() != null || s.equals(Game.getEnPassantTarget())) {
                    ld.addLayer(getResources().getDrawable(R.drawable.legal_attack_hint, a.getTheme()));
                }

                // Otherwise show it as a black circle.
                else {
                    ld.addLayer(getResources().getDrawable(R.drawable.legal_move_hint, a.getTheme()));
                }
            }

            // If there is check on the board, show it under the player king.
            if (isCheck && s.equals(kingSquare)) {
                ld.addLayer(getResources().getDrawable(R.drawable.legal_attack_hint, a.getTheme()));
            }

            // If there is a piece on the square, get it's drawable resource.
            if (s.getPiece() != null) {
                ld.addLayer(getResources().getDrawable(s.getPiece().getDrawable(), a.getTheme()));
            }

            // Set the ImageView's drawable to the LayerDrawable.
            iv.setImageDrawable(ld);
        }
    }
}
