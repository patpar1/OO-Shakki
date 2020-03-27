package com.example.shakki;

import com.example.shakki.nappulat.Nappula;

public class Siirto {

    private int xAlku;
    private int yAlku;
    private int xLoppu;
    private int yLoppu;

    private Nappula nappula;
    private Nappula tuhottuNappula;

    private boolean onShakki;
    private boolean onMatti;
    private boolean linnoitus;

    public Siirto(Ruutu aloitusRuutu, Ruutu lopetusRuutu) {
        xAlku = aloitusRuutu.getX();
        yAlku = aloitusRuutu.getY();
        nappula = aloitusRuutu.getNappula();
        xLoppu = lopetusRuutu.getX();
        yLoppu = lopetusRuutu.getY();
        tuhottuNappula = lopetusRuutu.getNappula();
    }

    public int getxAlku() {
        return xAlku;
    }
    public int getyAlku() {
        return yAlku;
    }
    public int getxLoppu() {
        return xLoppu;
    }
    public int getyLoppu() {
        return yLoppu;
    }

    public Nappula haeNappula() {
        return nappula;
    }
    public Nappula haeTuhottuNappula() {
        return tuhottuNappula;
    }

    public void asetaTuhottuNappula(Nappula tuhottuNappula) {
        this.tuhottuNappula = tuhottuNappula;
    }

    public boolean onShakki() {
        return onShakki;
    }
    public void asetaShakki(boolean onShakki) {
        this.onShakki = onShakki;
    }

    public boolean onMatti() {
        return onMatti;
    }
    public void asetaMatti(boolean onMatti) {
        this.onMatti = onMatti;
    }

    public boolean isLinnoitus() {
        return linnoitus;
    }
    public void asetaLinnoitus(boolean linnoitus) {
        this.linnoitus = linnoitus;
    }
}
