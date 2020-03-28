package com.example.shakki.game;

import com.example.shakki.game.pieces.Piece;

public class Square {

    private Piece nappula;
    private int x;
    private int y;

    public Square(int y, int x) {
        nappula = null;
        this.x = x;
        this.y = y;
    }

    public Square(Piece nappula, int y, int x) {
        this.nappula = nappula;
        this.x = x;
        this.y = y;
    }

    Square kopioi() {
        return new Square(nappula, y, x);
    }

    public Piece haeNappula() {
        return nappula;
    }

    public void asetaNappula(Piece nappula) {
        this.nappula = nappula;
    }

    public int haeX() {
        return x;
    }

    public int haeY() {
        return y;
    }

    public boolean onTyhjä() {
        return (nappula == null);
    }
}
