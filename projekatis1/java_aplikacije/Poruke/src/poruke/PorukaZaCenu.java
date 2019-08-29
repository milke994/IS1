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
public class PorukaZaCenu implements Serializable{
    private String nazivArtikla;
    private int cena;

    public PorukaZaCenu(String nazivArtikla, int cena) {
        this.nazivArtikla = nazivArtikla;
        this.cena = cena;
    }

    public String getNazivArtikla() {
        return nazivArtikla;
    }

    public void setNazivArtikla(String nazivArtikla) {
        this.nazivArtikla = nazivArtikla;
    }

    public int getCena() {
        return cena;
    }

    public void setCena(int cena) {
        this.cena = cena;
    }
    
    
}
