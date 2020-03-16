package com.example.shakki.nappulat;

import com.example.shakki.Pelilauta;
import com.example.shakki.Ruutu;

import java.util.ArrayList;

public class Torni extends Nappula {

    private static final int[][] siirtoVektorit = {
            {-1, 0},
            {1, 0},
            {0, -1},
            {0, 1}
    };

    public Torni(boolean onValkoinen) {
        super(onValkoinen);
    }

    @Override
    public ArrayList<Ruutu> laillisetSiirrot(Pelilauta lauta, int y, int x) {
        ArrayList<Ruutu> siirtoLista = new ArrayList<Ruutu>();
        for (int[] suunta : siirtoVektorit) {
            int[] siirtoEhdokas = {y, x};
            while (true) {
                siirtoEhdokas[0] += suunta[0];
                siirtoEhdokas[1] += suunta[1];
                if (!onLaudalla(siirtoEhdokas[0], siirtoEhdokas[1])) {
                    break;
                }
                if (!lauta.getRuutu(siirtoEhdokas[0] , siirtoEhdokas[1]).onTyhjä()){
                    if (this.onValkoinen() == lauta.getRuutu(siirtoEhdokas[0], siirtoEhdokas[1]).getNappula().onValkoinen()) {
                        break;
                    } else {
                        siirtoLista.add(lauta.getRuutu(siirtoEhdokas[0] , siirtoEhdokas[1]));
                        break;
                    }
                }
                siirtoLista.add(lauta.getRuutu(siirtoEhdokas[0] , siirtoEhdokas[1]));
            }

        }

        return siirtoLista;
    }

    @Override
    public String toString() {
       if (this.onValkoinen()) {
           return "T";
       } else {
           return "t";
       }
    }
}
