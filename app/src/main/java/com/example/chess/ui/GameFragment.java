package com.example.chess.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
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

import androidx.activity.OnBackPressedCallback;
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

import java.io.FileOutputStream;
import java.io.IOException;
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
     * Creates an instance of this fragment.
     *
     * @param savedInstanceState Previous state of this fragment.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Override the default functionality on back press. Pop up a dialog for making sure user's
        // intention and return to main menu.
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Builder class for building the dialog.
                new AlertDialog.Builder(requireActivity())
                        .setTitle("Are you sure?")
                        .setMessage("Unsaved progress will be lost!")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Navigation.findNavController(requireView()).popBackStack(R.id.nav_main, false);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            // Button for canceling the saving.
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .create()
                        .show();

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
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

        boolean isHumanPlayer = false;
        int aiLevel = 3;

        if (getArguments() != null) {
            if (getArguments().getSerializable("game") != null) {
                // Load game
                game = (Game) getArguments().getSerializable("game");
                Toast.makeText(v.getContext(), "Game loaded successfully!", Toast.LENGTH_SHORT).show();
            } else {
                // New game
                isHumanPlayer = getArguments().getBoolean("isHumanPlayer");
                if (!isHumanPlayer) {
                    aiLevel = getArguments().getInt("aiLevel");
                }
                // Game class for the game logic
                game = new Game(isHumanPlayer, aiLevel);
            }
        }

        // Main chessboard view
        chessboard = v.findViewById(R.id.chessboard);
        chessboard.setDrawingCacheEnabled(true);

        // Initializes the map which maps all Squares of the Board to corresponding ImageViews
        drawableTiles = new HashMap<>();
        initializeDrawableTiles();

        // Player images
        player1 = v.findViewById(R.id.image_human);
        player2 = v.findViewById(R.id.image_bot);
        if (isHumanPlayer) {
            player2.setBackgroundResource(R.drawable.human_player);
        }
        humanSelected = true;

        // Text which shows whose turn it is currently.
        turnText = v.findViewById(R.id.turn_text);

        // Bottom app bar buttons

        // New game
        v.findViewById(R.id.new_game_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeSureDialog(R.id.nav_new_game);
            }
        });

        // Save game
        v.findViewById(R.id.save_game_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveGameDialog();
            }
        });

        // Load game
        v.findViewById(R.id.load_game_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeSureDialog(R.id.nav_load_game);
            }
        });

        // Undo move
        v.findViewById(R.id.undo_move_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (game.getMoveIndex() > 0) {
                    game.undoPreviousMove();
                    checkGameState();
                } else {
                    Toast.makeText(getContext(), "First Move!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Redo move
        v.findViewById(R.id.redo_move_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (game.getMoveIndex() < game.getMoves().size()) {
                    game.makeNextMove();
                    checkGameState();
                } else {
                    Toast.makeText(getContext(), "Last Move!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Timer text on the bottom app bar
        final TextView timerTextView = v.findViewById(R.id.game_clock);
        startTime = 0;
        timerHandler = new Handler();
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                long millis = System.currentTimeMillis() - startTime;
                int seconds = (int) (millis / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;

                timerTextView.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
                timerHandler.postDelayed(this, 500);
            }
        };

        return v;
    }

    /**
     * Method for switching player currently in turn visually on the UI.
     */
    private void switchSelected() {
        ViewGroup.LayoutParams humanParams = player1.getLayoutParams();
        ViewGroup.LayoutParams botParams = player2.getLayoutParams();
        if (!humanSelected) {
            humanSelected = true;
            humanParams.height = getResources().getDimensionPixelSize(R.dimen.player_selected_game);
            humanParams.width = getResources().getDimensionPixelSize(R.dimen.player_selected_game);
            botParams.height = getResources().getDimensionPixelSize(R.dimen.player_unselected_game);
            botParams.width = getResources().getDimensionPixelSize(R.dimen.player_unselected_game);
            turnText.setText(R.string.turn_white);
        } else {
            humanSelected = false;
            humanParams.height = getResources().getDimensionPixelSize(R.dimen.player_unselected_game);
            humanParams.width = getResources().getDimensionPixelSize(R.dimen.player_unselected_game);
            botParams.height = getResources().getDimensionPixelSize(R.dimen.player_selected_game);
            botParams.width = getResources().getDimensionPixelSize(R.dimen.player_selected_game);
            turnText.setText(R.string.turn_black);
        }
        player1.setLayoutParams(humanParams);
        player2.setLayoutParams(botParams);
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
        if (!game.isFinished()) {
            game.handleSquareClickEvent(drawableTiles.get(v));
            checkPawnPromotionDialog();
        }
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
            if (game.getCurrentPlayer() instanceof AlphaBetaPlayer) {
                // If bot promotes a piece, it chooses always queen.
                currentMove.setPromotion(new Queen(game.getCurrentPlayer().isWhite()));
            }
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
            game.setFinished(true);
            updateGameImage(game);
            Bundle bundle = new Bundle();
            bundle.putInt("gameState", gameState);
            bundle.putSerializable("game", game);
            Navigation.findNavController(requireView()).navigate(R.id.action_nav_game_to_nav_game_won, bundle);
            return;
        }

        // If opposing player is a bot, tell him to make a turn.
        if (game.getCurrentPlayer() instanceof AlphaBetaPlayer) {
            ((AlphaBetaPlayer) game.getCurrentPlayer()).calculateMove(game);
            checkPawnPromotionDialog();
        }
    }

    /**
     * Method for checking the game state, after user has manually undone or redone moves.
     */
    private void checkGameState() {
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

        // Update game board.
        drawGameBoard();
        switchSelected();
    }

    /**
     * Method for asking a user, if he is sure for his intentions.
     *
     * @param destination Resource ID of the destination.
     */
    private void makeSureDialog(final int destination) {
        // Builder class for building the dialog.
        new AlertDialog.Builder(requireContext())
                .setTitle("Are you sure?")
                .setMessage("Unsaved progress will be lost!")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Navigation.findNavController(requireView()).navigate(destination);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
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
     * Creates a dialog for game saving. User can input a file name and the method saves the game
     * object to a file named by user.
     */
    private void saveGameDialog() {
        // Find the application context for the builder.
        final Context c = requireContext();

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
                        if (fileOutput.contains(".")) {
                            Toast.makeText(c, "Name can't contain '.' !", Toast.LENGTH_SHORT).show();
                            input.setText("");
                            fileOutput = "";
                        } else {
                            try {
                                FileOutputStream fos = c.openFileOutput(fileOutput + ".txt", Context.MODE_PRIVATE);
                                ObjectOutputStream oos = new ObjectOutputStream(fos);
                                updateGameImage(game);
                                game.getGameInformation().setGameInformation(fileOutput, game.isFinished());
                                game.getGameInformation().updateTimeStamp();
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
     * Creates a dialog for pawn promotion. User can choose the piece to promote to from a list and
     * the method replaces the pawn with the piece chosen by the user.
     *
     * @param currentMove Current pawn move.
     * @param endTurn     Boolean value, true if this is at the end of turn.
     */
    private void pawnPromotionDialog(final Move currentMove, final boolean endTurn) {
        // Builder class for building the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

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
     * Saves the board snapshot image on the GameInformation object.
     *
     * @param game Current game.
     */
    private void updateGameImage(Game game) {
        chessboard.buildDrawingCache();
        Bitmap bitmap = chessboard.getDrawingCache();
        game.getGameInformation().setImageByteArray(bitmap);
    }

    /**
     * Main method for drawing the UI game board. Layers the different drawables so all drawables
     * can be shown on one ImageView.
     */
    private void drawGameBoard() {
        Activity a = requireActivity();
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
