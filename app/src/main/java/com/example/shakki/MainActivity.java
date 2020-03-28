package com.example.shakki;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.shakki.peli.Peli;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public static void main(String[] args) {
        int maksimiKierrokset = 100;

        Peli peli = new Peli();
        int tulos = peli.peliSilmukka(maksimiKierrokset);
        System.out.println(tulos);
    }
}
