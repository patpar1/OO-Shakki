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
        Peli peli = new Peli();
        //System.out.println(peli.getPelilauta().getRuutu(0, 0).getNappula().toString());
        //System.out.println(peli.getPelilauta().toString());
        peli.peliSilmukka();
    }
}
