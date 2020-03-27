package com.example.shakki.nappulat;

import com.example.shakki.Pelilauta;
import com.example.shakki.Ruutu;

import java.util.ArrayList;

public class Ratsu extends Nappula {

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

    public Ratsu(boolean onValkoinen) {
        super(onValkoinen);
    }

    @Override
    public ArrayList<Ruutu> laillisetSiirrot(Pelilauta lauta, int y, int x) {
        //Pelilauta luokasta saadaan kaikkien nappuloiden nykyiset sijainnit
        ArrayList<Ruutu> siirtoLista = new ArrayList<Ruutu>();
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
