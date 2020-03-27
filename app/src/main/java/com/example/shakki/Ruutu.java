package com.example.shakki;

import com.example.shakki.nappulat.Nappula;

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

    public Nappula getNappula() {
        return nappula;
    }

    public void setNappula(Nappula nappula) {
        this.nappula = nappula;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean onTyhjä() {
        return (nappula == null);
    }
}
