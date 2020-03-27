package com.example.shakki;

import com.example.shakki.nappulat.Nappula;

import java.util.ArrayList;
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

    public void asetaShakki(boolean onShakissa) {
        this.onShakissa = onShakissa;
    }

    public boolean onValkoinen() {
        return onValkoinen;
    }

    public Ruutu haeRuutu(Pelilauta lauta) {
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

    public Ruutu haeValittuRuutu(Pelilauta lauta) {

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

    public Ruutu haeKohdeRuutu(Pelilauta lauta) {

        ArrayList<Ruutu> laillisetSiirrot;
        Ruutu kohdeRuutu;

        // Lasketaan ruudun nappulan mahdolliset siirrot
        laillisetSiirrot = valittuRuutu.getNappula().laillisetSiirrot(lauta, valittuRuutu);

        // Tulosta mahdolliset siirrot
        System.out.println(lauta.tulostaPelilauta(laillisetSiirrot));

        // Käyttäjä valitsee siirron kohderuudun
        System.out.print("Valitse kohderuutu: ");
        kohdeRuutu = haeRuutu(lauta);

        // Virheentarkistusta
        if (!laillisetSiirrot.contains(kohdeRuutu)) {
            System.out.println("Siirto ei ole laillinen!");
            return null;
        }

        return kohdeRuutu;
    }

    private int[] muunnaTekstiKoordinaatiksi(String s) {
        String ats = "abcdefgh";
        int i0 = ats.indexOf(s.toLowerCase().charAt(0));
        int i1 = 8 - Character.getNumericValue(s.charAt(1));
        return new int[] {i1, i0};
    }

}
