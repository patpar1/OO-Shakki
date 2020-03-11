package com.example.shakki;

public class Pelaaja {

    private boolean onValkoinen;
    private boolean onShakissa;

    private Ruutu valittuRuutu;

    public Pelaaja(boolean onValkoinen) {
        this.onValkoinen = onValkoinen;
    }

    public boolean onShakissa() {
        return onShakissa;
    }

    public void setOnShakissa(boolean onShakissa) {
        this.onShakissa = onShakissa;
    }

    public boolean onValkoinen() {
        return onValkoinen;
    }

}
