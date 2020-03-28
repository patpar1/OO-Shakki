package com.example.chess;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.chess.game.Game;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public static void main(String[] args) {
        int maxTurns = 100;

        Game game = new Game();
        int result = game.gameLoop(maxTurns);
        System.out.println(result);
    }
}
