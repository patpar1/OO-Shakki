package com.example.shakki;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public static void main(String[] args) {
        int maksimiKierrokset = 100;

        Peli peli = new Peli();
        peli.peliSilmukka(maksimiKierrokset);
    }
}
