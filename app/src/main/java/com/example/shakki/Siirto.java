package com.example.shakki;

import com.example.shakki.nappulat.Nappula;

public class Siirto {

    private int xAlku;
    private int yAlku;
    private int xLoppu;
    private int yLoppu;

    private Nappula siirrettäväNappula;
    private Nappula syötäväNappula;

    private boolean onShakki;
    private boolean onMatti;
    private boolean linnoitus;

    public Siirto(Ruutu aloitusRuutu, Ruutu lopetusRuutu) {
        xAlku = aloitusRuutu.getX();
        yAlku = aloitusRuutu.getY();
        siirrettäväNappula = aloitusRuutu.getNappula();
        xLoppu = lopetusRuutu.getX();
        yLoppu = lopetusRuutu.getY();
        syötäväNappula = lopetusRuutu.getNappula();
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

    public Nappula getSiirrettäväNappula() {
        return siirrettäväNappula;
    }
    public Nappula getSyötäväNappula() {
        return syötäväNappula;
    }

    public void setSyötäväNappula(Nappula syötäväNappula) {
        this.syötäväNappula = syötäväNappula;
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
