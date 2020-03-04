package com.example.shakki.nappulat;

import java.util.ArrayList;

public abstract class Ylänappula {
    private boolean onValkoinen;

    public abstract ArrayList<Siirto> laillisetSiirrot(Pelilauta lauta, int x, int y);

}
