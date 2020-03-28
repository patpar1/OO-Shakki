package com.example.shakki.game.pieces;

import com.example.shakki.game.Board;
import com.example.shakki.game.Square;

import java.util.ArrayList;

public class Queen extends Piece {

    public Queen(boolean onValkoinen) {
        super(onValkoinen);
    }

    @Override
    public ArrayList<Square> laillisetSiirrot(Board lauta, int y, int x) {
        return null;
    }

    @Override
    public String toString() {
        if (this.onValkoinen()) {
            return "D";
        } else {
            return "d";
        }
    }

}
