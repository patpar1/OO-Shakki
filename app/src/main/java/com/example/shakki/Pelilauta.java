package com.example.shakki;

import com.example.shakki.nappulat.*;

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
            ruudut[1][i] = new Ruutu(new Sotilas(false), 1, i);       // Mustan sotilaat
        }

        // Tyhjien ruutujen alustus

        for (int j = 2; j < (PELILAUDAN_KOKO - 2); j++) {
            for (int i = 0; i < PELILAUDAN_KOKO; i++) {
                ruudut[j][i] = new Ruutu(j, i);
            }
        }
    }

    // Kopiorakentaja
    public Pelilauta(Pelilauta pLauta) {
        this.ruudut = pLauta.ruudut;
    }

    public Ruutu getRuutu(int rivi, int sarake) {
        return ruudut[rivi][sarake];
    }

    public ArrayList<Nappula> haeNappulat(boolean onValkoinen) {
        ArrayList<Nappula> nappulat = new ArrayList<>();

        for (int i = 0; i < PELILAUDAN_KOKO; i++) {
            for (int j = 0; j < PELILAUDAN_KOKO; j++) {
                if (ruudut[i][j].getNappula() != null) {
                    if (ruudut[i][j].getNappula().onValkoinen() == onValkoinen) {
                        nappulat.add(ruudut[i][j].getNappula());
                    }
                }
            }
        }

        return nappulat;
    }

    public ArrayList<Ruutu> haePelaajanSiirrot(boolean onValkoinen) {
        /* Hakee pelaajan kaikkien nappuloiden mahdolliset siirrot.
        *  Käytetään shakki-tilanteen tarkastamiseen vastapelaajalta.*/

        ArrayList<Ruutu> pelaajanRuudut = new ArrayList<Ruutu>();

        for (int i = 0; i < PELILAUDAN_KOKO; i++) {
            for (int j = 0; j < PELILAUDAN_KOKO; j++) {
                if (ruudut[i][j].getNappula().onValkoinen() == onValkoinen) {
                    pelaajanRuudut.addAll(ruudut[i][j].getNappula().laillisetSiirrot(this, i, j));
                }
            }
        }
        return pelaajanRuudut;
    }

    public boolean onShakki(boolean onValkoinen) {
        int kuningasX = -1;
        int kuningasY = -1;

        // Hakee pelilaudalta kuninkaan sijainnin
        for (int i = 0; i < PELILAUDAN_KOKO; i++) {
            for (int j = 0; j < PELILAUDAN_KOKO; j++) {
                if (ruudut[i][j].getNappula() instanceof Kuningas &&
                        ruudut[i][j].getNappula().onValkoinen() == onValkoinen) {
                    kuningasY = i;
                    kuningasX = j;
                    break;
                }
            }
        }

        // Tarkistaa, vastaako vastustajan mahdollisista siirroista mikään kuninkaan ruutua
        return haePelaajanSiirrot(!onValkoinen).contains(getRuutu(kuningasY, kuningasX));
    }

    public boolean onPatti(boolean onValkoinen) {
        return haePelaajanSiirrot(onValkoinen).isEmpty();
    }

    public boolean onShakkiMatti(boolean onValkoinen) {
        return onShakki(onValkoinen) && onPatti(onValkoinen);
    }

    public void teeSiirto(Siirto s) {
        /* Tekee annetun siirron pelilaudalla */

        ruudut[s.getyLoppu()][s.getxLoppu()].setNappula(s.haeNappula());
        ruudut[s.getyAlku()][s.getxAlku()].setNappula(null);

    }

    public void kumoaSiirto(Siirto s) {
        // Kumoaa annetun siirron pelilaudalla.

        ruudut[s.getyAlku()][s.getxAlku()].setNappula(s.haeNappula());
        ruudut[s.getyLoppu()][s.getxLoppu()].setNappula(s.haeTuhottuNappula());

    }

    public String tulostaPelilauta(ArrayList<Ruutu> laillisetRuudut) {
        StringBuilder sb = new StringBuilder();

        sb.append("\n");

        for (int i = 0; i < PELILAUDAN_KOKO; i++) {
            // Rivin numero
            sb.append(8 - i).append("\t");

            // Nappulat
            for (int j = 0; j < PELILAUDAN_KOKO; j++) {
                if (laillisetRuudut != null) {
                    if (laillisetRuudut.contains(this.getRuutu(i, j))) {
                        sb.append('*').append(" ");
                        continue;
                    }
                }
                if (this.getRuutu(i, j).getNappula() == null) {
                    sb.append('-');
                } else {
                    sb.append(this.getRuutu(i, j).getNappula().toString());
                }
                sb.append(" ");
            }
            sb.append("\n");
        }
        // Alin rivi
        sb.append("\n \ta b c d e f g h");

        return sb.toString();
    }

    public String tulostaPelilauta() {
        return tulostaPelilauta(null);
    }

}
