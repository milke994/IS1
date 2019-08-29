/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Aleksandar
 */
@Entity
@Table(name = "rezervacije")
@NamedQueries({
    @NamedQuery(name = "Rezervacije.findAll", query = "SELECT r FROM Rezervacije r")
    , @NamedQuery(name = "Rezervacije.findByIdrezervacije", query = "SELECT r FROM Rezervacije r WHERE r.idrezervacije = :idrezervacije")
    , @NamedQuery(name = "Rezervacije.findByKolicina", query = "SELECT r FROM Rezervacije r WHERE r.kolicina = :kolicina")
    , @NamedQuery(name = "Rezervacije.findByDatum", query = "SELECT r FROM Rezervacije r WHERE r.datum = :datum")})
public class Rezervacije implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "kolicina")
    private int kolicina;
    @Size(max = 45)
    @Column(name = "datum")
    private String datum;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idrezervacije")
    private Integer idrezervacije;
    @JoinColumn(name = "sifartikla", referencedColumnName = "sifra")
    @ManyToOne(optional = false)
    private Artikal sifartikla;
    @JoinColumn(name = "sifrakorinsik", referencedColumnName = "idkorisnik")
    @ManyToOne(optional = false)
    private Korisnik sifrakorinsik;
    @JoinColumn(name = "sifprodavnice", referencedColumnName = "sifra")
    @ManyToOne(optional = false)
    private Prodavnica sifprodavnice;

    public Rezervacije() {
    }

    public Rezervacije(Integer idrezervacije) {
        this.idrezervacije = idrezervacije;
    }

    public Rezervacije(Integer idrezervacije, int kolicina) {
        this.idrezervacije = idrezervacije;
        this.kolicina = kolicina;
    }

    public Integer getIdrezervacije() {
        return idrezervacije;
    }

    public void setIdrezervacije(Integer idrezervacije) {
        this.idrezervacije = idrezervacije;
    }


    public Artikal getSifartikla() {
        return sifartikla;
    }

    public void setSifartikla(Artikal sifartikla) {
        this.sifartikla = sifartikla;
    }

    public Korisnik getSifrakorinsik() {
        return sifrakorinsik;
    }

    public void setSifrakorinsik(Korisnik sifrakorinsik) {
        this.sifrakorinsik = sifrakorinsik;
    }

    public Prodavnica getSifprodavnice() {
        return sifprodavnice;
    }

    public void setSifprodavnice(Prodavnica sifprodavnice) {
        this.sifprodavnice = sifprodavnice;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idrezervacije != null ? idrezervacije.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rezervacije)) {
            return false;
        }
        Rezervacije other = (Rezervacije) object;
        if ((this.idrezervacije == null && other.idrezervacije != null) || (this.idrezervacije != null && !this.idrezervacije.equals(other.idrezervacije))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Rezervacije[ idrezervacije=" + idrezervacije + " ]";
    }

    public int getKolicina() {
        return kolicina;
    }

    public void setKolicina(int kolicina) {
        this.kolicina = kolicina;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }
    
}
