package com.example.shakki.nappulat;

import com.example.shakki.Pelilauta;
import com.example.shakki.Ruutu;

import java.util.ArrayList;

public class Lähetti extends Nappula {

    private static final int[][] siirtoVektorit = {
            {-1, 1}, // oikeelle ylös
            {1, 1}, // oikeelle alas
            {-1, -1}, // vasemmalle ylös
            {1, -1} // vasemmalle alas
    };

    public Lähetti(boolean onValkoinen) {
        super(onValkoinen);
    }

    @Override
    public ArrayList<Ruutu> laillisetSiirrot(Pelilauta lauta, int y, int x) {
        ArrayList<Ruutu> siirtoLista = new ArrayList<Ruutu>();
        for (int[] suunta : siirtoVektorit) {
            while (true) {
                int[] siirtoEhdokas = {(y += suunta[0]), (x += suunta[1])};
                // siirtää kerralla x ja y koordinaatteja siirtovektorien mukaiseen suuntaan
                if (!onLaudalla(siirtoEhdokas[0], siirtoEhdokas[1])) {
                    break;
                }
                if (!lauta.haeRuutu(siirtoEhdokas[0], siirtoEhdokas[1]).onTyhjä()){
                    if (this.onValkoinen() == lauta.haeRuutu(siirtoEhdokas[0], siirtoEhdokas[1]).haeNappula().onValkoinen()) {
                        break;
                    } else {
                        siirtoLista.add(lauta.haeRuutu(siirtoEhdokas[0] , siirtoEhdokas[1]));
                        break;
                    }
                }
                siirtoLista.add(lauta.haeRuutu(siirtoEhdokas[0] , siirtoEhdokas[1]));
            }
        }
        return siirtoLista;
    }
    @Override
    public String toString() {
        if (this.onValkoinen()) {
            return "L";
        } else {
            return "l";
        }
    }
}
