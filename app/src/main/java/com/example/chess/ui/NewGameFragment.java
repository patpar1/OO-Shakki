package com.example.chess.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.chess.R;

import java.util.Locale;

public class NewGameFragment extends Fragment {

    private boolean humanSelected = true;

    public NewGameFragment() {
        // Required empty public constructor
    }

    /**
     * Instantiates the NewGameFragment to the user interface view.
     *
     * @param inflater           Inflater object which can inflate any views in the fragment.
     * @param container          Parent view that the fragment's UI should be attached to.
     * @param savedInstanceState Previous state of this fragment.
     * @return the View for the fragment's UI.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment.
        View v = inflater.inflate(R.layout.fragment_new_game, container, false);

        // Switch for selecting opposing player.
        final ImageView humanButton = v.findViewById(R.id.image_human);
        final ImageView botButton = v.findViewById(R.id.image_bot);

        final ViewGroup.LayoutParams humanParams = humanButton.getLayoutParams();
        final ViewGroup.LayoutParams botParams = botButton.getLayoutParams();

        // TextView for showing the AI level.
        final TextView aiText = v.findViewById(R.id.ai_text);

        // SeekBar for choosing the AI level.
        final SeekBar aiLevel = v.findViewById(R.id.ai_level);
        aiLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                aiText.setText(String.format(Locale.getDefault(), "AI Level: %d", progress + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // Button for starting a game.
        v.findViewById(R.id.game_start_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isHumanPlayer", humanSelected);
                if (!humanSelected) {
                    bundle.putInt("aiLevel", aiLevel.getProgress());
                }
                Navigation.findNavController(v).navigate(R.id.action_nav_new_game_to_nav_game, bundle);
            }
        });

        // Button for selecting human player.
        humanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isHumanSelected()) {
                    humanSelected = true;
                    humanParams.height = getResources().getDimensionPixelSize(R.dimen.player_selected);
                    humanParams.width = getResources().getDimensionPixelSize(R.dimen.player_selected);
                    humanButton.setLayoutParams(humanParams);
                    botParams.height = getResources().getDimensionPixelSize(R.dimen.player_unselected);
                    botParams.width = getResources().getDimensionPixelSize(R.dimen.player_unselected);
                    botButton.setLayoutParams(botParams);
                    aiText.setVisibility(View.INVISIBLE);
                    aiLevel.setVisibility(View.INVISIBLE);
                }
            }
        });

        // Button for selecting bot player.
        botButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHumanSelected()) {
                    humanSelected = false;
                    humanParams.height = getResources().getDimensionPixelSize(R.dimen.player_unselected);
                    humanParams.width = getResources().getDimensionPixelSize(R.dimen.player_unselected);
                    humanButton.setLayoutParams(humanParams);
                    botParams.height = getResources().getDimensionPixelSize(R.dimen.player_selected);
                    botParams.width = getResources().getDimensionPixelSize(R.dimen.player_selected);
                    botButton.setLayoutParams(botParams);
                    aiText.setVisibility(View.VISIBLE);
                    aiLevel.setVisibility(View.VISIBLE);
                }
            }
        });

        return v;
    }

    private boolean isHumanSelected() {
        return humanSelected;
    }
}
