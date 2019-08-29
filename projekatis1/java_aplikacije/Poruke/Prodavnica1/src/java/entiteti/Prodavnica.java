/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Aleksandar
 */
@Entity
@Table(name = "prodavnica")
@NamedQueries({
    @NamedQuery(name = "Prodavnica.findAll", query = "SELECT p FROM Prodavnica p")
    , @NamedQuery(name = "Prodavnica.findBySifra", query = "SELECT p FROM Prodavnica p WHERE p.sifra = :sifra")
    , @NamedQuery(name = "Prodavnica.findByNaziv", query = "SELECT p FROM Prodavnica p WHERE p.naziv = :naziv")})
public class Prodavnica implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "naziv")
    private String naziv;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "sifra")
    private String sifra;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "prodavnica")
    private List<Stanje> stanjeList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "prodavnica")
    private Promet promet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sifprodavnice")
    private List<Rezervacije> rezervacijeList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "prodavnica")
    private Menadzer menadzer;

    public Prodavnica() {
    }

    public Prodavnica(String sifra) {
        this.sifra = sifra;
    }

    public Prodavnica(String sifra, String naziv) {
        this.sifra = sifra;
        this.naziv = naziv;
    }

    public String getSifra() {
        return sifra;
    }

    public void setSifra(String sifra) {
        this.sifra = sifra;
    }


    public List<Stanje> getStanjeList() {
        return stanjeList;
    }

    public void setStanjeList(List<Stanje> stanjeList) {
        this.stanjeList = stanjeList;
    }

    public Promet getPromet() {
        return promet;
    }

    public void setPromet(Promet promet) {
        this.promet = promet;
    }

    public List<Rezervacije> getRezervacijeList() {
        return rezervacijeList;
    }

    public void setRezervacijeList(List<Rezervacije> rezervacijeList) {
        this.rezervacijeList = rezervacijeList;
    }

    public Menadzer getMenadzer() {
        return menadzer;
    }

    public void setMenadzer(Menadzer menadzer) {
        this.menadzer = menadzer;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sifra != null ? sifra.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Prodavnica)) {
            return false;
        }
        Prodavnica other = (Prodavnica) object;
        if ((this.sifra == null && other.sifra != null) || (this.sifra != null && !this.sifra.equals(other.sifra))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Prodavnica[ sifra=" + sifra + " ]";
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }
    
}
