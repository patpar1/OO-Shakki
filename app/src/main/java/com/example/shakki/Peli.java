package com.example.shakki;

import java.util.ArrayList;

public class Peli {

    private Pelilauta lauta;
    private ArrayList<Siirto> siirrot;
    private boolean valkoisenVuoro;

    private Pelaaja valkoinenPelaaja;
    private Pelaaja mustaPelaaja;

    public Peli() {
        lauta = new Pelilauta();
        siirrot = new ArrayList<Siirto>();
        valkoisenVuoro = true;

        valkoinenPelaaja = new Pelaaja(true); // Valkoinen pelaaja
        mustaPelaaja = new Pelaaja(false); // Musta pelaaja
    }

    private Pelaaja haeNykyinenPelaaja() {
        if (valkoisenVuoro) {
            return valkoinenPelaaja;
        } else {
            return mustaPelaaja;
        }
    }

    public void peliSilmukka(int maksimiSiirrot) {
        int iteraatio = 0;
        while (iteraatio++ < maksimiSiirrot) {

            // Tarkista shakkimatti
            if (lauta.onShakkiMatti(valkoisenVuoro)) {
                siirrot.get(-1).asetaMatti(true);
                lopetaPeli(valkoisenVuoro ? 1 : -1);
            }

            // Tarkista pattitilanne
            if (lauta.onPatti(valkoisenVuoro)) {
                lopetaPeli(0);
            }

            // Tarkista shakkitilanne
            if (lauta.onShakki(valkoisenVuoro)) {
                haeNykyinenPelaaja().asetaShakki(true);
                //siirrot.get(siirrot.size() - 1).asetaShakki(true);
            }

            Siirto pelaajanSiirto = haeNykyinenPelaaja().muodostaSiirto(lauta);
            if (pelaajanSiirto == null) {
                continue;
            }

            siirrot.add(pelaajanSiirto);
            lauta.teeSiirto(pelaajanSiirto);

            haeNykyinenPelaaja().asetaShakki(false);

            this.valkoisenVuoro = !valkoisenVuoro; // Anna vuoro toiselle pelaajalle
        }
    }

    private void lopetaPeli(int valkoisenVoitto) {
        if (valkoisenVoitto == 1) {
            // Valkoinen voitti
        } else if (valkoisenVoitto == -1) {
            // Musta voitti
        } else {
            // Tasapeli
        }
    }

    public void kumoaViimeisinSiirto() {
        lauta.kumoaSiirto(siirrot.get(-1));
        siirrot.remove(-1);
    }

}
