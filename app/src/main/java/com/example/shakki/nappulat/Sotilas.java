package com.example.shakki.nappulat;

import com.example.shakki.Pelilauta;
import com.example.shakki.Ruutu;
import com.example.shakki.Siirto;

import java.util.ArrayList;

public class Sotilas extends Nappula {

    private static final int[][] siirtoEhdokkaat = {
            {2, 0}, // jos y,x koordinaatisto
            {-2, 0},
            {1, 0},
            {-1, 0},
            {1, 1}, // Ohestalyönti ja syönti
            {1, -1}, // Ohestalyönti ja syönti
            {-1, 1}, // Ohestalyönti ja syönti
            {-1, -1} // Ohestalyönti ja syönti

    };

    public Sotilas(boolean onValkoinen) {
        super(onValkoinen);
    }


    @Override
    public ArrayList<Ruutu> laillisetSiirrot(Pelilauta lauta, int x, int y) {
        return null;
    }

    @Override
    public String toString() {
        if (this.onValkoinen()) {
            return "S";
        } else {
            return "s";
        }
    }

}
