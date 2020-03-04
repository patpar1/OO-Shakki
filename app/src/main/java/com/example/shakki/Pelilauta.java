package com.example.shakki;

import com.example.shakki.nappulat.Ratsu;

import java.util.ArrayList;

public class Pelilauta {

    public static final int PELILAUDAN_KOKO = 8;

    private Ruutu[][] ruudut;

    /*  Pelilaudan rakenne pelin aloituksessa:

            8       t r l d k l r t
            7       s s s s s s s s
            6       - - - - - - - -
            5       - - - - - - - -
            4       - - - - - - - -
            3       - - - - - - - -
            2       S S S S S S S S
            1       T R L D K L R T

                    a b c d e f g h

            S = Sotilas
            R = Ratsu
            L = Lähetti
            T = Torni
            D = Kuningatar (Daami)
            K = Kuningas

            Valkoinen pelaaja on isoilla kirjaimilla.

            Ruutu[i][j]:
                i = Rivin indeksi (välillä 0-7, indeksi 0 tarkoittaa pelilaudalla riviä '8')
                j = Sarakkeen indeksi (välillä 0-7, indeksi 0 tarkoittaa pelilaudalla saraketta 'a')
     */

    public Pelilauta() {
        /* Alustaa pelilaudan eli asettaa nappulat pelilaudalle oikeaan järjestykseen */

        ruudut = new Ruutu[PELILAUDAN_KOKO][PELILAUDAN_KOKO];

        // Valkoisen pelinappuloiden asetus

        ruudut[0][0] = new Ruutu(new Torni(true));                  // Torni h1
        ruudut[1][0] = new Ruutu(new Ratsu(true));      // Ratsu g1
        ruudut[2][0] = new Ruutu(new Lähetti(true));                // Lähetti f1
        ruudut[3][0] = new Ruutu(new Kuningas(true));               // Kuningas e1
        ruudut[4][0] = new Ruutu(new Kuningatar(true));             // Kuningatar d1
        ruudut[5][0] = new Ruutu(new Lähetti(true));                // Lähetti c1
        ruudut[6][0] = new Ruutu(new Ratsu(true));      // Ratsu b1
        ruudut[7][0] = new Ruutu(new Torni(true));                  // Torni a1

        for (int i = 0; i < PELILAUDAN_KOKO; i++) {
            ruudut[i][1] = new Ruutu(new Sotilas(true));            // Valkoisen sotilaat
        }

        // Mustan pelinappuloiden asetus

        ruudut[0][7] = new Ruutu(new Torni(false));                  // Torni h8
        ruudut[1][7] = new Ruutu(new Ratsu(false));      // Ratsu g8
        ruudut[2][7] = new Ruutu(new Lähetti(false));                // Lähetti f8
        ruudut[3][7] = new Ruutu(new Kuningas(false));               // Kuningas e8
        ruudut[4][7] = new Ruutu(new Kuningatar(false));             // Kuningatar d8
        ruudut[5][7] = new Ruutu(new Lähetti(false));                // Lähetti c8
        ruudut[6][7] = new Ruutu(new Ratsu(false));      // Ratsu b8
        ruudut[7][7] = new Ruutu(new Torni(false));                  // Torni a8

        for (int i = 0; i < PELILAUDAN_KOKO; i++) {
            ruudut[i][6] = new Ruutu(new Sotilas(false));            // Mustan sotilaat
        }

        // Tyhjien ruutujen alustus

        for (int j = 2; j < (PELILAUDAN_KOKO - 2); j++) {
            for (int i = 0; i < PELILAUDAN_KOKO; i++) {
                ruudut[i][j] = new Ruutu();
            }
        }
    }

    public ArrayList<Siirto> haePelaajanSiirrot(boolean onValkoinen) {
        /* Hakee pelaajan kaikkien nappuloiden mahdolliset siirrot.
        *  Käytetään shakki-tilanteen tarkastamiseen vastapelaajalta.*/

        ArrayList<Siirto> pelaajanSiirrot = new ArrayList<Siirto>();

        for (int i = 0; i < PELILAUDAN_KOKO; i++) {
            for (int j = 0; j < PELILAUDAN_KOKO; j++) {
                if (ruudut[i][j].getNappula().onValkoinen() == onValkoinen) {
                    pelaajanSiirrot.addAll(ruudut[i][j].getNappula().laillisetSiirrot(this, i, j));
                }
            }
        }

    }

}
