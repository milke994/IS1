/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poruke;

import java.io.Serializable;

/**
 *
 * @author Aleksandar
 */
public class PorukaZaRezervaciju implements Serializable{
    private String nazivArtikla;
    private int kolicina;
    private String ime;
    private String prezime;
    private String JMBG;
    private String Posiljalac;

    public PorukaZaRezervaciju(String nazivArtikla, int kolicina, String ime, String prezime, String JMBG, String posiljalac) {
        this.nazivArtikla = nazivArtikla;
        this.kolicina = kolicina;
        this.ime = ime;
        this.prezime = prezime;
        this.JMBG = JMBG;
        this.Posiljalac = posiljalac;
    }

    public String getPosiljalac() {
        return Posiljalac;
    }

    public void setPosiljalac(String Posiljalac) {
        this.Posiljalac = Posiljalac;
    }
    

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getJMBG() {
        return JMBG;
    }

    public void setJMBG(String JMBG) {
        this.JMBG = JMBG;
    }
    
    
    public PorukaZaRezervaciju(String nazivArtikla, int kolicina) {
        this.nazivArtikla = nazivArtikla;
        this.kolicina = kolicina;
    }

    public String getNazivArtikla() {
        return nazivArtikla;
    }

    public void setNazivArtikla(String nazivArtikla) {
        this.nazivArtikla = nazivArtikla;
    }

    public int getKolicina() {
        return kolicina;
    }

    public void setKolicina(int kolicina) {
        this.kolicina = kolicina;
    }
    
    
    
}
