package com.example.shakki;

import com.example.shakki.nappulat.Ylänappula;

public class Siirto {

    private Ylänappula nappula;
    private Ruutu aloitusRuutu;
    private Ruutu lopetusRuutu;

    private boolean onLyönti;
    private boolean onShakki;
    private boolean onMatti;
    private boolean linnoitus;

    public Siirto(Ylänappula nappula, Ruutu aloitusRuutu, Ruutu lopetusRuutu) {
        this.nappula = nappula;
        this.aloitusRuutu = aloitusRuutu;
        this.lopetusRuutu = lopetusRuutu;
    }

}
