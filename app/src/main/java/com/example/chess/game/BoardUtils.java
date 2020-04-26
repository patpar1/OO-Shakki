package com.example.chess.game;

public class BoardUtils {
    public static double[][] reverseArray(double[][] array) {
        double[][] rArray = new double[8][8];
        for (int i = 0; i < array.length / 2; i++) {
            rArray[i] = array[array.length - i - 1];
            rArray[array.length - i - 1] = array[i];
        }
        return rArray;
    }
}
