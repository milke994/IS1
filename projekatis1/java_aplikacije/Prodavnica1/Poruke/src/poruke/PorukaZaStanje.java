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
public class PorukaZaStanje implements Serializable{
    private String nazivArtikla;
    private String stanje;
    private String nazivProdavnice;

    public PorukaZaStanje(String nazivArtikla) {
        this.nazivArtikla = nazivArtikla;
    }

    public PorukaZaStanje(String nazivArtikla, String nazivProdavnice) {
        this.nazivArtikla = nazivArtikla;
        this.nazivProdavnice = nazivProdavnice;
    }


    public String getNazivProdavnice() {
        return nazivProdavnice;
    }

    public void setNazivProdavnice(String nazivProdavnice) {
        this.nazivProdavnice = nazivProdavnice;
    }
    
    

    public String getStanje() {
        return stanje;
    }

    public void setStanje(String stanje) {
        this.stanje = stanje;
    }

        
    public String getNazivArtikla() {
        return nazivArtikla;
    }

    public void setNazivArtikla(String nazivArtikla) {
        this.nazivArtikla = nazivArtikla;
    }
    
    
}
