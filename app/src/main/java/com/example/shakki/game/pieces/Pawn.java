package com.example.shakki.game.pieces;

import com.example.shakki.game.Board;
import com.example.shakki.game.Square;

import java.util.ArrayList;

public class Pawn extends Piece {

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

    public Pawn(boolean onValkoinen) {
        super(onValkoinen);
    }


    @Override
    public ArrayList<Square> laillisetSiirrot(Board lauta, int x, int y) {
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
