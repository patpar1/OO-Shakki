package com.example.shakki.nappulat;

import java.util.ArrayList;

public abstract class Nappula {
    private boolean onValkoinen;

    public Nappula(boolean onValkoinen) {
        self.onValkoinen = onValkoinen;
    }
//Laskee lailliset siirrot. Palauttaa listan laillisista siirroista
    public abstract ArrayList<Siirto> laillisetSiirrot(Pelilauta lauta, int x, int y);

    public static boolean onLaudalla(int x, int y) {
        if ((x > 7) || (y > 7)) {
            return false;
        }
        if ((x < 0) || (y < 0)) {
            return false;
        }
        return true;
    }

}
