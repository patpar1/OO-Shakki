package com.example.shakki;

import java.util.Scanner;

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

    public Ruutu valitseRuutu(Pelilauta lauta) {

        String sRuutu = null;
        int[] koordinaatit = new int[0];

        Scanner sc = new Scanner(System.in);
        if (sc.hasNextLine()) {
            sRuutu = sc.nextLine();
        }

        if (sRuutu != null) {
            koordinaatit = muunnaTekstiKoordinaatiksi(sRuutu);
        }

        // System.out.println("Valittu ruutu: " + koordinaatit[0] + koordinaatit[1]);

        return lauta.getRuutu(koordinaatit[0], koordinaatit[1]);
    }

    private int[] muunnaTekstiKoordinaatiksi(String s) {
        String ats = "abcdefgh";
        int i0 = ats.indexOf(s.toLowerCase().charAt(0));
        int i1 = 8 - Character.getNumericValue(s.charAt(1));
        int[] temp = {i1, i0};
        return temp;
    }

}
