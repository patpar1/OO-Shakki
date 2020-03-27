package com.example.shakki;

import com.example.shakki.nappulat.Nappula;

import java.util.ArrayList;
import java.util.Scanner;

public class Pelaaja {

    private boolean onValkoinen;
    private boolean onShakissa;

    private Ruutu valittuRuutu;

    Pelaaja(boolean onValkoinen) {
        this.onValkoinen = onValkoinen;
        this.onShakissa = false;
    }

    public boolean onShakissa() {
        return onShakissa;
    }

    void asetaShakki(boolean onShakissa) {
        this.onShakissa = onShakissa;
    }

    public boolean onValkoinen() {
        return onValkoinen;
    }

    private Ruutu haeRuutu(Pelilauta lauta) {
        String sRuutu = null;
        int[] koordinaatit = new int[0];

        Scanner sc = new Scanner(System.in);
        if (sc.hasNextLine()) {
            sRuutu = sc.nextLine();
        }

        if (sRuutu != null) {
            koordinaatit = muunnaTekstiKoordinaatiksi(sRuutu);
        }

        return lauta.getRuutu(koordinaatit[0], koordinaatit[1]);

    }

    private int[] muunnaTekstiKoordinaatiksi(String s) {
        String sarakkeet = "abcdefgh";
        int i0 = sarakkeet.indexOf(s.toLowerCase().charAt(0));
        int i1 = 8 - Character.getNumericValue(s.charAt(1));
        // System.out.println("TEKSTI KOORDINAATEIKSI: " + i1 + i0);
        return new int[] {i1, i0};
    }

    private Ruutu haeValittuRuutu(Pelilauta lauta) {

        ArrayList<Nappula> nappulat;

        // Lasketaan siirrettävät nappulat
        nappulat = lauta.haeNappulat(onValkoinen);

        // Käyttäjä valitsee ruudun
        System.out.print("Valitse nappula: ");

        valittuRuutu = haeRuutu(lauta);

        // Siirron tarkistus
        if (valittuRuutu.getNappula() == null) {
            System.out.println("Ruudussa ei ole nappulaa!");
            return null;
        } else if (!nappulat.contains(valittuRuutu.getNappula())) {
            System.out.println("Et voi siirtää tätä nappulaa!");
            return null;
        }

        return valittuRuutu;
    }

    private Ruutu haeKohdeRuutu(Pelilauta lauta) {

        Ruutu kohdeRuutu;
        ArrayList<Ruutu> laillisetRuudut = new ArrayList<>();

        if (onShakissa) {
            // Lisää siirrot ainoastaan jos siirron seurauksena pelilaudalla ei ole shakkia
            for (Ruutu kohdeRuutuEhdokas : valittuRuutu.getNappula().laillisetSiirrot(lauta, valittuRuutu)) {
                Pelilauta kopioLauta = lauta.kopioi();
                Ruutu valittuRuutuKopio = kopioLauta.getRuutu(valittuRuutu.getY(), valittuRuutu.getX());
                Ruutu kohdeRuutuEhdokasKopio = kopioLauta.getRuutu(kohdeRuutuEhdokas.getY(), kohdeRuutuEhdokas.getX());
                kopioLauta.teeSiirto(new Siirto(valittuRuutuKopio, kohdeRuutuEhdokasKopio));
                if (!kopioLauta.onShakki(onValkoinen)) {
                    laillisetRuudut.add(kohdeRuutuEhdokas);
                }
            }
        } else {
            laillisetRuudut.addAll(valittuRuutu.getNappula().laillisetSiirrot(lauta, valittuRuutu));
        }

        // System.out.println("Lailliset ruudut: " + laillisetRuudut.size());

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

    Siirto muodostaSiirto(Pelilauta lauta) {

        // Tulosta pelilauta
        System.out.println(onValkoinen ? "Valkoisen vuoro" : "Mustan vuoro");
        System.out.println(lauta.tulostaPelilauta());

        // Valitse siirrettävä nappula
        Ruutu valittuRuutu = haeValittuRuutu(lauta);
        if (valittuRuutu == null) {
            return null;
        }

        // Valitse kohderuutu
        Ruutu kohdeRuutu = haeKohdeRuutu(lauta);
        if (kohdeRuutu == null) {
            return null;
        }

        return new Siirto(valittuRuutu, kohdeRuutu);
    }
}
