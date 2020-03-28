package com.example.shakki.game.pieces;

import com.example.shakki.game.Board;
import com.example.shakki.game.Square;

import java.util.ArrayList;

public class Knight extends Piece {

    private static final int[][] siirtoEhdokkaat = {
            {-2, -1},
            {-1, -2},
            {1, -2},
            {2, -1},
            {2, 1},
            {1, 2},
            {-1, 2},
            {-2, 1}
    };

    public Knight(boolean onValkoinen) {
        super(onValkoinen);
    }

    @Override
    public ArrayList<Square> laillisetSiirrot(Board lauta, int y, int x) {
        //Pelilauta luokasta saadaan kaikkien nappuloiden nykyiset sijainnit
        ArrayList<Square> siirtoLista = new ArrayList<Square>();
        //siirto on muuttuja joka for loopin alussa on siirtoehdokkaat listan ensimmäinen kohta
        for (int[] siirto : siirtoEhdokkaat) {
            //siirtoEhdokkaassa y koordinaatti on 0 ja x koordinaatti on 1
            int[] siirtoEhdokas = {(y + siirto[0]), (x + siirto[1])};
            //tarkastaa onko siirto laudalla
            if (!onLaudalla(siirtoEhdokas[0], siirtoEhdokas[1])) {
                continue;
            }
            /* 1. if lause: haetaan ruutu luokasta ruutu, josta selviää onko
            ruutuun siirrettävä siirtoehdokas mahdollinen siten, että
            ruudussa ei ole pelinappulaa.
            */
            if (!lauta.haeRuutu(siirtoEhdokas[0] , siirtoEhdokas[1]).onTyhjä()){
                /*2. if lause: vertaa Ratsun ja pelilaudan ruudussa olevan nappulan väriä.
                Jos väri on sama (esim. valkoinen, for looppi lähtee alusta,
                        koska siirto ei ole mahdollinen).
             */
                if (this.onValkoinen() == lauta.haeRuutu(siirtoEhdokas[0], siirtoEhdokas[1]).haeNappula().onValkoinen()) {
                    continue;
                }

            }
            //palauttaa "lauta" objektiin for lauseen läpi menneet siirtoehdokkaat, jotka ovat laillisia
            siirtoLista.add(lauta.haeRuutu(siirtoEhdokas[0] , siirtoEhdokas[1]));

        }

        return siirtoLista;
    }
    @Override
    public String toString() {
        if (this.onValkoinen()) {
            return "R";
        } else {
            return "r";
        }
    }
}
