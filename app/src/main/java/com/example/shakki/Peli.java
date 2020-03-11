package com.example.shakki;

import com.example.shakki.nappulat.Nappula;

import java.util.ArrayList;

public class Peli {

    private Pelilauta lauta;
    private ArrayList<Siirto> siirrot;
    private boolean valkoisenVuoro;

    private Ruutu valittuRuutu;
    private Ruutu kohdeRuutu;

    private ArrayList<Nappula> siirrettävätNappulat;
    private ArrayList<Siirto> laillisetSiirrot;

    private Pelaaja valkoinenPelaaja;
    private Pelaaja mustaPelaaja;

    public Peli() {
        lauta = new Pelilauta();
        siirrot = new ArrayList<Siirto>();
        valkoisenVuoro = true;

        valkoinenPelaaja = new Pelaaja(true); // Valkoinen pelaaja
        mustaPelaaja = new Pelaaja(false); // Musta pelaaja
    }

    public int peliSilmukka() {
        while (true) {

            // Lasketaan siirrettävät nappulat
            siirrettävätNappulat = lauta.haeSiirrettävätNappulat(valkoisenVuoro);

            // Käyttäjä valitsee ruudun
            valittuRuutu = null; //TODO Käyttäjän valitsema ruutu asetetaan tähän

            // Lasketaan ruudun nappulan mahdolliset siirrot
            laillisetSiirrot = valittuRuutu.getNappula().laillisetSiirrot(lauta, valittuRuutu);

            // Käyttäjä valitsee siirron kohderuudun
            kohdeRuutu = null; //TODO Käyttäjän valitsema kohderuutu asetetaan tähän


            // Lasketaan seuraavan pelaajan pelitilanne
            siirrot.add(new Siirto(valittuRuutu, kohdeRuutu));

            if (lauta.onShakkiMatti(!valkoisenVuoro)) {
                siirrot.get(-1).setOnMatti(true);
                lopetaPeli(valkoisenVuoro ? 1 : -1);
            }

            if (lauta.onShakki(!valkoisenVuoro)) {
                if (valkoisenVuoro) {
                    mustaPelaaja.setOnShakissa(true);
                } else {
                    valkoinenPelaaja.setOnShakissa(true);
                }
                siirrot.get(-1).setOnShakki(true);
            }

            if (lauta.onPatti(!valkoisenVuoro)) {
                lopetaPeli(0);
            }

            lauta.teeSiirto(siirrot.get(-1));

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
