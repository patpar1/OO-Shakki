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

    public Siirto(int xAlku, int yAlku, int xLoppu, int yLoppu, Nappula siirrettäväNappula) {
        this.xAlku = xAlku;
        this.yAlku = yAlku;
        this.xLoppu = xLoppu;
        this.yLoppu = yLoppu;
        this.siirrettäväNappula = siirrettäväNappula;
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

    public boolean isOnShakki() {
        return onShakki;
    }

    public void setOnShakki(boolean onShakki) {
        this.onShakki = onShakki;
    }

    public boolean isOnMatti() {
        return onMatti;
    }

    public void setOnMatti(boolean onMatti) {
        this.onMatti = onMatti;
    }

    public boolean isLinnoitus() {
        return linnoitus;
    }

    public void setLinnoitus(boolean linnoitus) {
        this.linnoitus = linnoitus;
    }
}
