package com.example.shakki.peli.nappulat;

import com.example.shakki.peli.Pelilauta;
import com.example.shakki.peli.Ruutu;

import java.util.ArrayList;

public class Kuningatar extends Nappula {

    public Kuningatar(boolean onValkoinen) {
        super(onValkoinen);
    }

    @Override
    public ArrayList<Ruutu> laillisetSiirrot(Pelilauta lauta, int y, int x) {
        return null;
    }

    @Override
    public String toString() {
        if (this.onValkoinen()) {
            return "D";
        } else {
            return "d";
        }
    }

}
