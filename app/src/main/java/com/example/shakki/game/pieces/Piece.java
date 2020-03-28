package com.example.shakki.game.pieces;

import com.example.shakki.game.Board;
import com.example.shakki.game.Square;

import java.util.ArrayList;

public abstract class Piece {
    private boolean onValkoinen;

    public Piece(boolean onValkoinen) {
        this.onValkoinen = onValkoinen;
    }

    // Laskee lailliset siirrot. Palauttaa listan laillisista siirroista
    public abstract ArrayList<Square> laillisetSiirrot(Board lauta, int y, int x);

    public ArrayList<Square> laillisetSiirrot(Board lauta, Square ruutu) {
        return laillisetSiirrot(lauta, ruutu.haeY(), ruutu.haeX());
    }

    public boolean onValkoinen() {
        return onValkoinen;
    }

    public static boolean onLaudalla(int y, int x) {
        if ((x > 7) || (y > 7)) {
            return false;
        }
        if ((x < 0) || (y < 0)) {
            return false;
        }
        return true;
    }

}
