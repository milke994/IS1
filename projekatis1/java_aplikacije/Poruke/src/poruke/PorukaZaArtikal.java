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
public class PorukaZaArtikal implements Serializable{
    private String nazivArtikla;
    private int kolicina;

    public PorukaZaArtikal(String nazivArtikla, int kolicina) {
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
