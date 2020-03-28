package com.example.shakki.game;

import java.util.ArrayList;

public class Game {

    private Board lauta;
    // private ArrayList<Siirto> siirrot;
    private boolean valkoisenVuoro;

    private Player valkoinenPelaaja;
    private Player mustaPelaaja;

    public Game() {
        lauta = new Board();
        // siirrot = new ArrayList<Siirto>();
        valkoisenVuoro = true;

        valkoinenPelaaja = new Player(true); // Valkoinen pelaaja
        mustaPelaaja = new Player(false); // Musta pelaaja
    }

    private Player haeNykyinenPelaaja() {
        return haePelaaja(valkoisenVuoro);
    }

    private Player haePelaaja(boolean vuoro) {
        if (vuoro) {
            return valkoinenPelaaja;
        } else {
            return mustaPelaaja;
        }
    }

    public int peliSilmukka(int maksimiSiirrot) {

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
            Move pelaajanSiirto = haeNykyinenPelaaja().muodostaSiirto(lauta);
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
        // Hakee vuorossa olevan pelaajan kaikki mahdolliset siirrot
        ArrayList<Square> pRuudut = new ArrayList<>();
        for (Square r : lauta.haePelaajanRuudut(valkoisenVuoro)) {
            pRuudut.addAll(haeNykyinenPelaaja().haeLaillisetSiirrot(lauta, r));
        }

        // Pelin lopun ehdot
        boolean shakki = lauta.onShakki(haeNykyinenPelaaja());
        boolean patti = pRuudut.size() == 0;

        // Shakkimatti
        if (shakki && patti) {
            return !valkoisenVuoro ? 1 : 2;
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
