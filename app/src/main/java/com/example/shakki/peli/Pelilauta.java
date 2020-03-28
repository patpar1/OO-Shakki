package com.example.shakki.peli;

import com.example.shakki.peli.nappulat.*;

import java.util.ArrayList;

public class Pelilauta {

    private static final int PELILAUDAN_KOKO = 8;

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

            Ruutu[y][x]:
                y = Rivin indeksi (välillä 0-7, indeksi 0 tarkoittaa pelilaudalla riviä '8')
                x = Sarakkeen indeksi (välillä 0-7, indeksi 0 tarkoittaa pelilaudalla saraketta 'a')
     */

    public Pelilauta() {
        /* Alustaa pelilaudan eli asettaa nappulat pelilaudalle oikeaan järjestykseen */

        ruudut = new Ruutu[PELILAUDAN_KOKO][PELILAUDAN_KOKO];

        // Valkoisen pelinappuloiden asetus

        ruudut[7][0] = new Ruutu(new Torni(true), 7, 0);        // Torni a1
        ruudut[7][1] = new Ruutu(new Ratsu(true), 7, 1);        // Ratsu b1
        ruudut[7][2] = new Ruutu(new Lähetti(true), 7, 2);      // Lähetti c1
        ruudut[7][3] = new Ruutu(new Kuningatar(true), 7, 3);   // Kuningatar d1
        ruudut[7][4] = new Ruutu(new Kuningas(true), 7, 4);     // Kuningas e1
        ruudut[7][5] = new Ruutu(new Lähetti(true), 7, 5);      // Lähetti f1
        ruudut[7][6] = new Ruutu(new Ratsu(true), 7, 6);        // Ratsu g1
        ruudut[7][7] = new Ruutu(new Torni(true), 7, 7);        // Torni h1


        for (int i = 0; i < PELILAUDAN_KOKO; i++) {
            // ruudut[6][i] = new Ruutu(new Sotilas(true), 6, i);        // Valkoisen sotilaat

            // Väliaikaisesti poistettu sotilaat
            ruudut[6][i] = new Ruutu(6, i);
        }

        // Mustan pelinappuloiden asetus

        ruudut[0][0] = new Ruutu(new Torni(false), 0, 0);             // Torni a8
        ruudut[0][1] = new Ruutu(new Ratsu(false), 0, 1);             // Ratsu b8
        ruudut[0][2] = new Ruutu(new Lähetti(false), 0, 2);           // Lähetti c8
        ruudut[0][3] = new Ruutu(new Kuningatar(false), 0, 3);        // Kuningatar d8
        ruudut[0][4] = new Ruutu(new Kuningas(false), 0, 4);          // Kuningas e8
        ruudut[0][5] = new Ruutu(new Lähetti(false), 0, 5);           // Lähetti f8
        ruudut[0][6] = new Ruutu(new Ratsu(false), 0, 6);             // Ratsu g8
        ruudut[0][7] = new Ruutu(new Torni(false), 0, 7);             // Torni h8

        for (int i = 0; i < PELILAUDAN_KOKO; i++) {
            //ruudut[1][i] = new Ruutu(new Sotilas(false), 1, i);       // Mustan sotilaat

            // Väliaikaisesti poistettu sotilaat
            ruudut[1][i] = new Ruutu(1, i);
        }

        // Tyhjien ruutujen alustus

        for (int j = 2; j < (PELILAUDAN_KOKO - 2); j++) {
            for (int i = 0; i < PELILAUDAN_KOKO; i++) {
                ruudut[j][i] = new Ruutu(j, i);
            }
        }
    }

    Pelilauta kopioi() {
        Pelilauta pl = new Pelilauta();
        for (int i = 0; i < PELILAUDAN_KOKO; i++) {
            for (int j = 0; j < PELILAUDAN_KOKO; j++) {
                pl.ruudut[j][i] = ruudut[j][i].kopioi();
            }
        }
        return pl;
    }

    public Ruutu haeRuutu(int rivi, int sarake) {
        return ruudut[rivi][sarake];
    }

    ArrayList<Ruutu> haePelaajanRuudut(boolean onValkoinen) {
        ArrayList<Ruutu> r = new ArrayList<>();

        for (int i = 0; i < PELILAUDAN_KOKO; i++) {
            for (int j = 0; j < PELILAUDAN_KOKO; j++) {
                if (ruudut[i][j].haeNappula() != null) {
                    if (ruudut[i][j].haeNappula().onValkoinen() == onValkoinen) {
                        r.add(ruudut[i][j]);
                    }
                }
            }
        }
        return r;
    }

    ArrayList<Nappula> haeNappulat(boolean onValkoinen) {
        ArrayList<Nappula> nappulat = new ArrayList<>();
        for (Ruutu r : haePelaajanRuudut(onValkoinen)) {
            nappulat.add(r.haeNappula());
        }
        return nappulat;
    }

    boolean onShakki(Pelaaja pelaaja) {
        int kuningasX = -1;
        int kuningasY = -1;

        // Hakee pelilaudalta kuninkaan sijainnin
        for (int i = 0; i < PELILAUDAN_KOKO; i++) {
            for (int j = 0; j < PELILAUDAN_KOKO; j++) {
                if (ruudut[i][j].haeNappula() instanceof Kuningas &&
                        ruudut[i][j].haeNappula().onValkoinen() == pelaaja.onValkoinen()) {
                    kuningasY = i;
                    kuningasX = j;
                    break;
                }
            }
        }

        ArrayList<Ruutu> pRuutu = new ArrayList<>();
        ArrayList<Ruutu> nappulanRuutuEhdokkaat;
        for (Ruutu r : haePelaajanRuudut(!pelaaja.onValkoinen())) {
            if ((nappulanRuutuEhdokkaat = r.haeNappula().laillisetSiirrot(this, r)) != null) {
                pRuutu.addAll(nappulanRuutuEhdokkaat);
            }
        }

        return pRuutu.contains(haeRuutu(kuningasY, kuningasX));
    }

    void teeSiirto(Siirto s) {
        // Tekee annetun siirron pelilaudalla

        ruudut[s.getyLoppu()][s.getxLoppu()].asetaNappula(s.haeNappula());
        ruudut[s.getyAlku()][s.getxAlku()].asetaNappula(null);

    }

    void kumoaSiirto(Siirto s) {
        // Kumoaa annetun siirron pelilaudalla.

        ruudut[s.getyAlku()][s.getxAlku()].asetaNappula(s.haeNappula());
        ruudut[s.getyLoppu()][s.getxLoppu()].asetaNappula(s.haeTuhottuNappula());

    }

    String tulostaPelilauta(ArrayList<Ruutu> laillisetRuudut) {
        StringBuilder sb = new StringBuilder();

        sb.append("\n");

        for (int i = 0; i < PELILAUDAN_KOKO; i++) {
            // Rivin numero
            sb.append(8 - i).append("\t");

            // Nappulat
            for (int j = 0; j < PELILAUDAN_KOKO; j++) {
                if (laillisetRuudut != null) {
                    if (laillisetRuudut.contains(this.haeRuutu(i, j))) {
                        sb.append('*').append(" ");
                        continue;
                    }
                }
                if (this.haeRuutu(i, j).haeNappula() == null) {
                    sb.append('-');
                } else {
                    sb.append(this.haeRuutu(i, j).haeNappula().toString());
                }
                sb.append(" ");
            }
            sb.append("\n");
        }
        // Alin rivi
        sb.append("\n \ta b c d e f g h");

        return sb.toString();
    }

    String tulostaPelilauta() {
        return tulostaPelilauta(null);
    }

}
