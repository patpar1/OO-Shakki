package com.example.chess.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chess.R;
import com.example.chess.game.Game;
import com.example.chess.game.GameInformation;

import java.util.ArrayList;
import java.util.Date;

public class LoadAdapter extends RecyclerView.Adapter<LoadAdapter.LoadViewHolder> {

    private ArrayList<String> fileName = new ArrayList<>();
    private ArrayList<Boolean> isFinished = new ArrayList<>();
    private ArrayList<Date> dates = new ArrayList<>();
    private ArrayList<Bitmap> images = new ArrayList<>();

    private ArrayList<Game> games;

    LoadAdapter(ArrayList<Game> games, ArrayList<GameInformation> gameInformationList) {
        this.games = games;
        for (GameInformation gi : gameInformationList) {
            fileName.add(gi.getFileName());
            isFinished.add(gi.isFinished());
            dates.add(gi.getDate());
            images.add(gi.getBitmap());
        }
    }

    @NonNull
    @Override
    public LoadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_item, parent, false);
        return new LoadViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LoadViewHolder holder, final int position) {
        holder.fileName.setText(fileName.get(position));
        holder.isFinished.setText(isFinished.get(position) ? "Finished" : "Not Finished");
        holder.date.setText(dates.get(position).toString());
        holder.picture.setImageBitmap(images.get(position));

        holder.parentPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("game", games.get(position));
                Navigation.findNavController(v).navigate(R.id.action_nav_load_game_to_nav_game, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fileName.size();
    }

    static class LoadViewHolder extends RecyclerView.ViewHolder {

        TextView fileName;
        TextView isFinished;
        TextView date;
        ImageView picture;
        ConstraintLayout parentPanel;

        LoadViewHolder(View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.file_name_text);
            isFinished = itemView.findViewById(R.id.game_status);
            date = itemView.findViewById(R.id.game_date);
            picture = itemView.findViewById(R.id.game_image);
            parentPanel = itemView.findViewById(R.id.parentPanel);
        }
    }
}
