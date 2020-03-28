package com.example.shakki.game.pieces;

import com.example.shakki.game.Board;
import com.example.shakki.game.Square;

import java.util.ArrayList;

public class King extends Piece {

    private static final int[][] siirtoEhdokkaat = {
            {1, 0}, // Alas
            {1, 1}, // Alas oikealle
            {1, -1}, // Alas vasemmalle
            {-1, 0}, // Ylös
            {-1, 1}, // Ylös oikealle
            {-1, -1}, // Ylös vasemmalle
            {0, 1}, // Oikealle
            {0, -1} // Vasemmalle
    };
    public King(boolean onValkoinen) {
        super(onValkoinen);
    }

    @Override
    public ArrayList<Square> laillisetSiirrot(Board lauta, int y, int x) {
        ArrayList<Square> siirtoLista = new ArrayList<Square>();
        for (int[] siirto : siirtoEhdokkaat) {
            int[] siirtoEhdokas = {(y + siirto[0]), (x + siirto[1])};
            //tarkastaa onko siirto laudalla
            if (!onLaudalla(siirtoEhdokas[0], siirtoEhdokas[1])) {
                continue;
            }
            /* haetaan ruutu luokasta ruutu, josta selviää onko
            ruutuun siirrettävä siirtoehdokas mahdollinen siten, että
            ruudussa ei ole pelinappulaa.
            */
            if (!lauta.haeRuutu(siirtoEhdokas[0], siirtoEhdokas[1]).onTyhjä()) {
                if (this.onValkoinen() == lauta.haeRuutu(siirtoEhdokas[0], siirtoEhdokas[1]).haeNappula().onValkoinen()) {
                    continue;
                }

            }
            //palauttaa "lauta" objektiin for lauseen läpi menneet siirtoehdokkaat, jotka ovat laillisia
            siirtoLista.add(lauta.haeRuutu(siirtoEhdokas[0], siirtoEhdokas[1]));

        }
            return siirtoLista;
    }
    @Override
    public String toString() {
        if (this.onValkoinen()) {
            return "K";
        } else {
            return "k";
        }
    }
}
