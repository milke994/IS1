/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Aleksandar
 */
@Entity
@Table(name = "stanje")
@NamedQueries({
    @NamedQuery(name = "Stanje.findAll", query = "SELECT s FROM Stanje s")
    , @NamedQuery(name = "Stanje.findBySifraprodavnice", query = "SELECT s FROM Stanje s WHERE s.stanjePK.sifraprodavnice = :sifraprodavnice")
    , @NamedQuery(name = "Stanje.findBySifraartikla", query = "SELECT s FROM Stanje s WHERE s.stanjePK.sifraartikla = :sifraartikla")
    , @NamedQuery(name = "Stanje.findByKolicina", query = "SELECT s FROM Stanje s WHERE s.kolicina = :kolicina")})
public class Stanje implements Serializable {

    @JoinColumn(name = "sifraArtikla", referencedColumnName = "sifra", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Artikal artikal;
    @JoinColumn(name = "sifraProdavnice", referencedColumnName = "sifra", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Prodavnica prodavnica;

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected StanjePK stanjePK;
    @Column(name = "kolicina")
    private Integer kolicina;
    

    public Stanje() {
    }

    public Stanje(StanjePK stanjePK) {
        this.stanjePK = stanjePK;
    }

    public Stanje(String sifraprodavnice, String sifraartikla) {
        this.stanjePK = new StanjePK(sifraprodavnice, sifraartikla);
    }

    public StanjePK getStanjePK() {
        return stanjePK;
    }

    public void setStanjePK(StanjePK stanjePK) {
        this.stanjePK = stanjePK;
    }

    public Integer getKolicina() {
        return kolicina;
    }

    public void setKolicina(Integer kolicina) {
        this.kolicina = kolicina;
    }

    public Artikal getArtikal() {
        return artikal;
    }

    public void setArtikal(Artikal artikal) {
        this.artikal = artikal;
    }

    public Prodavnica getProdavnica() {
        return prodavnica;
    }

    public void setProdavnica(Prodavnica prodavnica) {
        this.prodavnica = prodavnica;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stanjePK != null ? stanjePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Stanje)) {
            return false;
        }
        Stanje other = (Stanje) object;
        if ((this.stanjePK == null && other.stanjePK != null) || (this.stanjePK != null && !this.stanjePK.equals(other.stanjePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Stanje[ stanjePK=" + stanjePK + " ]";
    }

    
    
}
