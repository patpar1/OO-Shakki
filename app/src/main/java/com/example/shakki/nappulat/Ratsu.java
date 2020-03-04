package com.example.shakki.nappulat;

import java.util.ArrayList;

public class Ratsu extends Ylänappula {

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
    public ArrayList<Siirto> laillisetSiirrot(Pelilauta lauta, int x, int y) {
        ArrayList<Siirto> siirtoLista = new ArrayList<Siirto>();

        for (int[] siirto : siirtoEhdokkaat) {
            int[] siirtoEhdokas = {(x + siirto[0]), (y + siirto[1])};

            //TODO if, elseif, else lausekkeet tähän

            siirtoLista.add(new Siirto());
        }



        return siirtoLista;

    }
}
