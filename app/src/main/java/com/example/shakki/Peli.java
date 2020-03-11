package com.example.shakki;

import java.util.ArrayList;

public class Peli {

    private Pelilauta lauta;
    private ArrayList<Siirto> siirrot;
    private boolean valkoisenVuoro;

    public Peli() {
        lauta = new Pelilauta();
        siirrot = new ArrayList<Siirto>();
        valkoisenVuoro = true;
    }

    public int peliSilmukka() {
        while (true) {
            // Tarkista siirrot
            // Tee siirrto
            this.valkoisenVuoro = !valkoisenVuoro; // Anna vuoro toiselle pelaajalle
        }
    }

}
