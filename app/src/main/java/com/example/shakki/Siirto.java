package com.example.shakki;

import com.example.shakki.nappulat.Nappula;

public class Siirto {

    private Nappula nappula;
    private Ruutu aloitusRuutu;
    private Ruutu lopetusRuutu;

    private boolean onLyönti;
    private boolean onShakki;
    private boolean onMatti;
    private boolean linnoitus;

    public Siirto(Nappula nappula, Ruutu aloitusRuutu, Ruutu lopetusRuutu) {
        this.nappula = nappula;
        this.aloitusRuutu = aloitusRuutu;
        this.lopetusRuutu = lopetusRuutu;
    }

}
