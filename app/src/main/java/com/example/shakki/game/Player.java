package com.example.shakki.game;

import com.example.shakki.game.pieces.Piece;

import java.util.ArrayList;
import java.util.Scanner;

public class Player {

    private boolean onValkoinen;
    private boolean onShakissa;

    Player(boolean onValkoinen) {
        this.onValkoinen = onValkoinen;
        this.onShakissa = false;
    }

    void asetaShakki(boolean onShakissa) {
        this.onShakissa = onShakissa;
    }

    boolean onValkoinen() {
        return onValkoinen;
    }

    private Square haeRuutu(Board lauta) {
        String sRuutu = null;
        int[] koordinaatit = new int[0];

        Scanner sc = new Scanner(System.in);
        if (sc.hasNextLine()) {
            sRuutu = sc.nextLine();
        }

        if (sRuutu != null) {
            koordinaatit = muunnaTekstiKoordinaatiksi(sRuutu);
        }

        if (koordinaatit == null) {
            return null;
        }

        return lauta.haeRuutu(koordinaatit[0], koordinaatit[1]);

    }

    private int[] muunnaTekstiKoordinaatiksi(String s) {
        String sarakkeet = "abcdefgh";

        if (s.length() != 2) {
            System.out.println("Koordinaatti on väärä! Sen on oltava muotoa 'a5'.");
            return null;
        }

        int i0 = sarakkeet.indexOf(s.toLowerCase().charAt(0));
        if (i0 == -1) {
            System.out.println("Sarakkeen on oltava kirjain välillä a-h (esim. a5).");
            return null;
        }

        int i1 = 8 - Character.getNumericValue(s.charAt(1));
        if (i1 < 0) {
            System.out.println("Rivin on oltava numero välillä 1-8 (esim. a5).");
            return null;
        }

        // System.out.println("TEKSTI KOORDINAATEIKSI: " + i1 + i0);
        return new int[] {i1, i0};
    }

    private Square haeValittuRuutu(Board lauta) {

        ArrayList<Piece> nappulat;

        // Lasketaan siirrettävät nappulat
        nappulat = lauta.haeNappulat(onValkoinen);

        // Käyttäjä valitsee ruudun
        System.out.print("Valitse nappula: ");

        Square valittuRuutu = haeRuutu(lauta);
        if (valittuRuutu == null) {
            return null;
        }

        // Siirron tarkistus
        if (valittuRuutu.haeNappula() == null) {
            System.out.println("Ruudussa ei ole nappulaa!");
            return null;
        } else if (!nappulat.contains(valittuRuutu.haeNappula())) {
            System.out.println("Et voi siirtää tätä nappulaa!");
            return null;
        }

        return valittuRuutu;
    }

    ArrayList<Square> haeLaillisetSiirrot(Board lauta, Square valittuRuutu) {

        ArrayList<Square> laillisetRuudut = new ArrayList<>();
        ArrayList<Square> nappulanRuutuEhdokkaat = valittuRuutu.haeNappula().laillisetSiirrot(lauta, valittuRuutu);

        if (nappulanRuutuEhdokkaat == null) {
            return laillisetRuudut;
        }

        for (Square kohdeRuutuEhdokas : nappulanRuutuEhdokkaat) {
            Board kopioLauta = lauta.kopioi();
            Square valittuRuutuKopio = kopioLauta.haeRuutu(valittuRuutu.haeY(), valittuRuutu.haeX());
            Square kohdeRuutuEhdokasKopio = kopioLauta.haeRuutu(kohdeRuutuEhdokas.haeY(), kohdeRuutuEhdokas.haeX());
            kopioLauta.teeSiirto(new Move(valittuRuutuKopio, kohdeRuutuEhdokasKopio));
            if (!kopioLauta.onShakki(this)) {
                laillisetRuudut.add(kohdeRuutuEhdokas);
            }
        }

        // System.out.println("Lailliset ruudut: " + laillisetRuudut.size());

        return laillisetRuudut;
    }

    private Square haeKohdeRuutu(Board lauta, Square valittuRuutu) {

        Square kohdeRuutu;
        ArrayList<Square> laillisetRuudut = haeLaillisetSiirrot(lauta, valittuRuutu);

        if (laillisetRuudut.size() == 0) {
            System.out.println("Tällä nappulalla ei ole laillisia siirtoja!");
            return null;
        }

        // Tulosta mahdolliset siirrot
        System.out.println(lauta.tulostaPelilauta(laillisetRuudut));

        // Käyttäjä valitsee siirron kohderuudun
        System.out.print("Valitse kohderuutu: ");
        kohdeRuutu = haeRuutu(lauta);

        // Virheentarkistusta
        if (!laillisetRuudut.contains(kohdeRuutu)) {
            System.out.println("Siirto ei ole laillinen!");
            return null;
        }

        return kohdeRuutu;
    }

    Move muodostaSiirto(Board lauta) {

        // Tulosta pelilauta
        System.out.println(onValkoinen ? "Valkoisen vuoro" : "Mustan vuoro");
        System.out.println(lauta.tulostaPelilauta());

        // Valitse siirrettävä nappula
        Square valittuRuutu = haeValittuRuutu(lauta);
        if (valittuRuutu == null) {
            return null;
        }

        // Valitse kohderuutu
        Square kohdeRuutu = haeKohdeRuutu(lauta, valittuRuutu);
        if (kohdeRuutu == null) {
            return null;
        }

        return new Move(valittuRuutu, kohdeRuutu);
    }
}
