package com.example.shakki;

import com.example.shakki.nappulat.Nappula;

import java.util.ArrayList;
import java.util.Scanner;

public class Pelaaja {

    private boolean onValkoinen;
    private boolean onShakissa;

    private Ruutu valittuRuutu;

    private ArrayList<Ruutu> laillisetRuudut;

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

        // Käyttäjä valitsee ruudun
        System.out.print("Valitse nappula: ");

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
        return new int[] {i1, i0};
    }

    private Ruutu haeValittuRuutu(Pelilauta lauta) {

        ArrayList<Nappula> nappulat;

        // Lasketaan siirrettävät nappulat
        nappulat = lauta.haeNappulat(onValkoinen);

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

        // Lasketaan ruudun nappulan mahdolliset siirrot
        laillisetRuudut = valittuRuutu.getNappula().laillisetSiirrot(lauta, valittuRuutu);

        if (onShakissa) {
            // Poista laittomat siirrot listasta, jos pelilaudalla on shakki
            for (Ruutu kohdeRuutuEhdokas : laillisetRuudut) {
                Pelilauta kopioLauta = new Pelilauta(lauta);
                kopioLauta.teeSiirto(new Siirto(valittuRuutu, kohdeRuutuEhdokas));
                if (kopioLauta.onShakki(onValkoinen)) {
                    laillisetRuudut.remove(kohdeRuutuEhdokas);
                }
            }
        }

        if (laillisetRuudut == null) {
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
