package com.example.shakki.peli;

import com.example.shakki.peli.nappulat.Nappula;

public class Ruutu {

    private Nappula nappula;
    private int x;
    private int y;

    public Ruutu(int y, int x) {
        nappula = null;
        this.x = x;
        this.y = y;
    }

    public Ruutu(Nappula nappula, int y, int x) {
        this.nappula = nappula;
        this.x = x;
        this.y = y;
    }

    Ruutu kopioi() {
        return new Ruutu(nappula, y, x);
    }

    public Nappula haeNappula() {
        return nappula;
    }

    public void asetaNappula(Nappula nappula) {
        this.nappula = nappula;
    }

    public int haeX() {
        return x;
    }

    public int haeY() {
        return y;
    }

    public boolean onTyhjä() {
        return (nappula == null);
    }
}
