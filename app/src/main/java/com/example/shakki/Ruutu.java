package com.example.shakki;

import com.example.shakki.nappulat.Nappula;

public class Ruutu {

    private Nappula nappula;

    public Ruutu() {
        nappula = null;
    }

    public Ruutu(Nappula nappula) {
        this.nappula = nappula;
    }

    public Nappula getNappula() {
        return nappula;
    }

    public boolean onTyhjä() {
        return (nappula == null);
    }
}
