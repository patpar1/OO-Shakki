package com.example.chess.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chess.R;
import com.example.chess.game.Game;
import com.example.chess.game.GameInformation;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoadGameFragment extends Fragment {

    private ArrayList<Game> games = new ArrayList<>();
    private ArrayList<GameInformation> gameInformation = new ArrayList<>();

    public LoadGameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_load_game, container, false);

        RecyclerView recyclerView = v.findViewById(R.id.game_load_list);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        getGameInformation();

        RecyclerView.Adapter mAdapter = new LoadAdapter(games ,gameInformation);
        recyclerView.setAdapter(mAdapter);

        return v;
    }

    private void getGameInformation() {
        Context c;
        if ((c = getContext()) == null) {
            return;
        }
        String[] fileList = c.fileList();
        for (String fileName : fileList) {
            try {
                FileInputStream fis = c.openFileInput(fileName);
                ObjectInputStream ois = new ObjectInputStream(fis);
                Game game = (Game) ois.readObject();
                games.add(game);
                gameInformation.add(game.getGameInformation());
                ois.close();
                fis.close();
            } catch (ClassNotFoundException | IOException e) {
                // If Game class was not found, return a new Game class
                Toast.makeText(c, "Error occurred: " + e, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
}
