package com.example.shakki;

import com.example.shakki.nappulat.Nappula;

import java.util.ArrayList;
import java.util.Scanner;

public class Pelaaja {

    private boolean onValkoinen;
    private boolean onShakissa;

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

        return lauta.haeRuutu(koordinaatit[0], koordinaatit[1]);

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

        Ruutu valittuRuutu = haeRuutu(lauta);

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

    ArrayList<Ruutu> haeLaillisetSiirrot(Pelilauta lauta, Ruutu valittuRuutu) {

        ArrayList<Ruutu> laillisetRuudut = new ArrayList<>();
        ArrayList<Ruutu> nappulanRuutuEhdokkaat;

        if ((nappulanRuutuEhdokkaat = valittuRuutu.haeNappula().laillisetSiirrot(lauta, valittuRuutu)) == null) {
            return laillisetRuudut;
        }

        if (onShakissa) {
            // Lisää siirrot ainoastaan jos siirron seurauksena pelilaudalla ei ole shakkia
            for (Ruutu kohdeRuutuEhdokas : nappulanRuutuEhdokkaat) {
                Pelilauta kopioLauta = lauta.kopioi();
                Ruutu valittuRuutuKopio = kopioLauta.haeRuutu(valittuRuutu.haeY(), valittuRuutu.haeX());
                Ruutu kohdeRuutuEhdokasKopio = kopioLauta.haeRuutu(kohdeRuutuEhdokas.haeY(), kohdeRuutuEhdokas.haeX());
                kopioLauta.teeSiirto(new Siirto(valittuRuutuKopio, kohdeRuutuEhdokasKopio));
                if (!kopioLauta.onShakki(this)) {
                    laillisetRuudut.add(kohdeRuutuEhdokas);
                }
            }
        } else {
            laillisetRuudut.addAll(nappulanRuutuEhdokkaat);
        }

        // System.out.println("Lailliset ruudut: " + laillisetRuudut.size());

        return laillisetRuudut;
    }

    private Ruutu haeKohdeRuutu(Pelilauta lauta, Ruutu valittuRuutu) {

        Ruutu kohdeRuutu;
        ArrayList<Ruutu> laillisetRuudut = haeLaillisetSiirrot(lauta, valittuRuutu);

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
        Ruutu kohdeRuutu = haeKohdeRuutu(lauta, valittuRuutu);
        if (kohdeRuutu == null) {
            return null;
        }

        return new Siirto(valittuRuutu, kohdeRuutu);
    }
}
