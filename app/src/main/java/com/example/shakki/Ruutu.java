package com.example.shakki;

import com.example.shakki.nappulat.Ylänappula;

public class Ruutu {

    private Ylänappula nappula;

    public Ruutu() {
        nappula = null;
    }

    public Ruutu(Ylänappula nappula) {
        this.nappula = nappula;
    }

    public Ylänappula getNappula() {
        return nappula;
    }

    public boolean onTyhja() {
        return (nappula == null);
    }
}
