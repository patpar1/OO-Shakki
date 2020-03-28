package com.example.shakki.game;

import com.example.shakki.game.pieces.Piece;

public class Move {

    private int xAlku;
    private int yAlku;
    private int xLoppu;
    private int yLoppu;

    private Piece nappula;
    private Piece tuhottuNappula;

    /*
    private boolean onShakki;
    private boolean onMatti;
    private boolean linnoitus;
     */

    public Move(Square aloitusRuutu, Square lopetusRuutu) {
        xAlku = aloitusRuutu.haeX();
        yAlku = aloitusRuutu.haeY();
        nappula = aloitusRuutu.haeNappula();
        xLoppu = lopetusRuutu.haeX();
        yLoppu = lopetusRuutu.haeY();
        tuhottuNappula = lopetusRuutu.haeNappula();
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

    public Piece haeNappula() {
        return nappula;
    }
    public Piece haeTuhottuNappula() {
        return tuhottuNappula;
    }

    /* Tarvitaan muissa toiminnallisuuksissa

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
     */

}
