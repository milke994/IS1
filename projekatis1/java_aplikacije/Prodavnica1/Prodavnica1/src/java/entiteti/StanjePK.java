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
    @Column(name = "sifraprodavnice")
    private String sifraprodavnice;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "sifraartikla")
    private String sifraartikla;

    public StanjePK() {
    }

    public StanjePK(String sifraprodavnice, String sifraartikla) {
        this.sifraprodavnice = sifraprodavnice;
        this.sifraartikla = sifraartikla;
    }

    public String getSifraprodavnice() {
        return sifraprodavnice;
    }

    public void setSifraprodavnice(String sifraprodavnice) {
        this.sifraprodavnice = sifraprodavnice;
    }

    public String getSifraartikla() {
        return sifraartikla;
    }

    public void setSifraartikla(String sifraartikla) {
        this.sifraartikla = sifraartikla;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sifraprodavnice != null ? sifraprodavnice.hashCode() : 0);
        hash += (sifraartikla != null ? sifraartikla.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StanjePK)) {
            return false;
        }
        StanjePK other = (StanjePK) object;
        if ((this.sifraprodavnice == null && other.sifraprodavnice != null) || (this.sifraprodavnice != null && !this.sifraprodavnice.equals(other.sifraprodavnice))) {
            return false;
        }
        if ((this.sifraartikla == null && other.sifraartikla != null) || (this.sifraartikla != null && !this.sifraartikla.equals(other.sifraartikla))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.StanjePK[ sifraprodavnice=" + sifraprodavnice + ", sifraartikla=" + sifraartikla + " ]";
    }
    
}
