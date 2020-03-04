package com.example.shakki;

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
        alustaPelilauta(ruudut);
    }

    private void alustaPelilauta(Ruutu[][] ruudut) {
        /* Alustaa pelilaudan eli asettaa nappulat pelilaudalle oikeaan järjestykseen */

        ruudut = new Ruutu[PELILAUDAN_KOKO][PELILAUDAN_KOKO];

        // Valkoisen pelinappuloiden asetus

        // Mustan pelinappuloiden asetus

    }

}
