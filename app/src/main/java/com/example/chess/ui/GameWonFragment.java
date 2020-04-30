package com.example.chess.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.chess.R;
import com.example.chess.game.Game;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class GameWonFragment extends Fragment {

    public GameWonFragment() {
        // Required empty public constructor
    }

    /**
     * Instantiates the GameWonFragment to the user interface view.
     *
     * @param inflater           Inflater object which can inflate any views in the fragment.
     * @param container          Parent view that the fragment's UI should be attached to.
     * @param savedInstanceState Previous state of this fragment.
     * @return the View for the fragment's UI.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_game_won, container, false);

        // Set the title text to match the game outcome.
        TextView gameWonText = v.findViewById(R.id.player_won_title);
        if (getArguments() != null) {
            int gameState = getArguments().getInt("gameState");
            switch (gameState) {
                case 1:
                    // White won
                    gameWonText.setText(R.string.won_white);
                    break;
                case 2:
                    // Black won
                    gameWonText.setText(R.string.won_black);
                    break;
                case 3:
                    // Stalemate
                    gameWonText.setText(R.string.won_stalemate);
                    break;
            }
        }

        // Button for starting a new game.
        Button newGameButton = v.findViewById(R.id.new_game_button);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.nav_new_game);
            }
        });

        // Button for saving the ended game.
        Button saveGameButton = v.findViewById(R.id.save_game_button);
        saveGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getArguments() != null) {
                    Game endedGame = (Game) getArguments().getSerializable("game");
                    saveGameDialog(endedGame);
                } else {
                    Toast.makeText(v.getContext(), "ERROR: Game object not found!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Button for returning to main menu.
        Button mainMenuButton = v.findViewById(R.id.main_menu_button);
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack(R.id.nav_main, false);
            }
        });

        return v;
    }

    /**
     * Dialog for asking the user file name.
     *
     * @param game Current game object.
     */
    private void saveGameDialog(final Game game) {
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
                        if (fileOutput.contains(".")) {
                            Toast.makeText(c, "Name can't contain '.' !", Toast.LENGTH_SHORT).show();
                            input.setText("");
                            fileOutput = "";
                        } else {
                            try {
                                FileOutputStream fos = c.openFileOutput(fileOutput + ".txt", Context.MODE_PRIVATE);
                                ObjectOutputStream oos = new ObjectOutputStream(fos);
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
}
