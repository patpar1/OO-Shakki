package com.example.shakki.nappulat;

import com.example.shakki.Pelilauta;
import com.example.shakki.Ruutu;

import java.util.ArrayList;

public class Kuningas extends Nappula {

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
    public Kuningas(boolean onValkoinen) {
        super(onValkoinen);
    }

    @Override
    public ArrayList<Ruutu> laillisetSiirrot(Pelilauta lauta, int y, int x) {
        ArrayList<Ruutu> siirtoLista = new ArrayList<Ruutu>();
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
            if (!lauta.getRuutu(siirtoEhdokas[0], siirtoEhdokas[1]).onTyhjä()) {
                if (this.onValkoinen() == lauta.getRuutu(siirtoEhdokas[0], siirtoEhdokas[1]).getNappula().onValkoinen()) {
                    continue;
                }

            }
            //palauttaa "lauta" objektiin for lauseen läpi menneet siirtoehdokkaat, jotka ovat laillisia
            siirtoLista.add(lauta.getRuutu(siirtoEhdokas[0], siirtoEhdokas[1]));

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
