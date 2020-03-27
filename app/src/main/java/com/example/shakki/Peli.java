package com.example.shakki;

import com.example.shakki.nappulat.Kuningas;

import java.util.ArrayList;

public class Peli {

    private Pelilauta lauta;
    // private ArrayList<Siirto> siirrot;
    private boolean valkoisenVuoro;

    private Pelaaja valkoinenPelaaja;
    private Pelaaja mustaPelaaja;

    public Peli() {
        lauta = new Pelilauta();
        // siirrot = new ArrayList<Siirto>();
        valkoisenVuoro = true;

        valkoinenPelaaja = new Pelaaja(true); // Valkoinen pelaaja
        mustaPelaaja = new Pelaaja(false); // Musta pelaaja
    }

    private Pelaaja haeNykyinenPelaaja() {
        return haePelaaja(valkoisenVuoro);
    }

    private Pelaaja haePelaaja(boolean vuoro) {
        if (vuoro) {
            return valkoinenPelaaja;
        } else {
            return mustaPelaaja;
        }
    }

    int peliSilmukka(int maksimiSiirrot) {

        // 1 = Valkoinen pelaaja voittaa
        // 2 = Tasapeli
        // 3 = Musta pelaaja voittaa

        int iteraatio = 0;
        int l;

        while (iteraatio++ < maksimiSiirrot) {

            // Tarkistetaan pelin loppumisen ehdot
            if ((l = tarkistaPelitilanne()) != 0) {
                return l;
            }

            // Muodostetaan pelaajalle uusi siirto;
            Siirto pelaajanSiirto = haeNykyinenPelaaja().muodostaSiirto(lauta);
            if (pelaajanSiirto == null) {
                continue;
            }

            // Lisätään siirto siirtolistaan ja tehdään se pelilaudalla
            // siirrot.add(pelaajanSiirto);
            lauta.teeSiirto(pelaajanSiirto);

            // Jos tähän pisteeseen on päästy, shakkitilanteen voi ottaa pois
            haeNykyinenPelaaja().asetaShakki(false);

            // Anna vuoro toiselle pelaajalle
            this.valkoisenVuoro = !valkoisenVuoro;
        }

        // Palauttaa tasapelin, jos maksimimäärä siirtoja on tehty
        return 3;
    }

    private int tarkistaPelitilanne() {
        int kuningasX = -1;
        int kuningasY = -1;

        // Hakee pelilaudalta pelaajan kuninkaan sijainnin
        for (int i = 0; i < Pelilauta.PELILAUDAN_KOKO; i++) {
            for (int j = 0; j < Pelilauta.PELILAUDAN_KOKO; j++) {
                if (lauta.haeRuutu(j, i).haeNappula() instanceof Kuningas
                && lauta.haeRuutu(j, i).haeNappula().onValkoinen() == valkoisenVuoro) {
                    kuningasY = j;
                    kuningasX = i;
                }
            }
        }

        // Hakee vastapelaajan kaikki mahdolliset siirrot
        ArrayList<Ruutu> vRuudut = new ArrayList<>();
        for (Ruutu r : lauta.haePelaajanRuudut(!valkoisenVuoro)) {
            vRuudut.addAll(haePelaaja(!valkoisenVuoro).haeLaillisetSiirrot(lauta, r));
        }

        // Hakee vuorossa olevan pelaajan kaikki mahdolliset siirrot
        ArrayList<Ruutu> pRuudut = new ArrayList<>();
        for (Ruutu r : lauta.haePelaajanRuudut(valkoisenVuoro)) {
            pRuudut.addAll(haeNykyinenPelaaja().haeLaillisetSiirrot(lauta, r));
        }

        // Pelin lopun ehdot
        boolean shakki = vRuudut.contains(lauta.haeRuutu(kuningasY, kuningasX));
        boolean patti = pRuudut.size() == 0;

        // Shakkimatti
        if (shakki && patti) {
            return valkoisenVuoro ? 1 : 2;
        }

        // Pattitilanne
        if (patti) {
            return 3;
        }

        // Shakki
        if (shakki) {
            haeNykyinenPelaaja().asetaShakki(true);
        }
        return 0;
    }

    /*
    Tarvitaan, kun siirtoja halutaan mennä eteen- ja taaksepäin.
    public void kumoaViimeisinSiirto() {
        lauta.kumoaSiirto(siirrot.get(-1));
        siirrot.remove(-1);
    }
     */


}
