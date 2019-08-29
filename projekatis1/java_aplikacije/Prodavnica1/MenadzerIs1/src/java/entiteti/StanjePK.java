/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Aleksandar
 */
@Embeddable
public class StanjePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "sifraProdavnice")
    private String sifraProdavnice;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "sifraArtikla")
    private String sifraArtikla;

    public StanjePK() {
    }

    public StanjePK(String sifraProdavnice, String sifraArtikla) {
        this.sifraProdavnice = sifraProdavnice;
        this.sifraArtikla = sifraArtikla;
    }

    public String getSifraProdavnice() {
        return sifraProdavnice;
    }

    public void setSifraProdavnice(String sifraProdavnice) {
        this.sifraProdavnice = sifraProdavnice;
    }

    public String getSifraArtikla() {
        return sifraArtikla;
    }

    public void setSifraArtikla(String sifraArtikla) {
        this.sifraArtikla = sifraArtikla;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sifraProdavnice != null ? sifraProdavnice.hashCode() : 0);
        hash += (sifraArtikla != null ? sifraArtikla.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StanjePK)) {
            return false;
        }
        StanjePK other = (StanjePK) object;
        if ((this.sifraProdavnice == null && other.sifraProdavnice != null) || (this.sifraProdavnice != null && !this.sifraProdavnice.equals(other.sifraProdavnice))) {
            return false;
        }
        if ((this.sifraArtikla == null && other.sifraArtikla != null) || (this.sifraArtikla != null && !this.sifraArtikla.equals(other.sifraArtikla))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.StanjePK[ sifraProdavnice=" + sifraProdavnice + ", sifraArtikla=" + sifraArtikla + " ]";
    }
    
}
