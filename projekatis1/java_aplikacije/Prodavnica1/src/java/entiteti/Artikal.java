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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Aleksandar
 */
@Entity
@Table(name = "artikal")
@NamedQueries({
    @NamedQuery(name = "Artikal.findAll", query = "SELECT a FROM Artikal a")
    , @NamedQuery(name = "Artikal.findBySifra", query = "SELECT a FROM Artikal a WHERE a.sifra = :sifra")
    , @NamedQuery(name = "Artikal.findByNaziv", query = "SELECT a FROM Artikal a WHERE a.naziv = :naziv")
    , @NamedQuery(name = "Artikal.findByTip", query = "SELECT a FROM Artikal a WHERE a.tip = :tip")
    , @NamedQuery(name = "Artikal.findByCena", query = "SELECT a FROM Artikal a WHERE a.cena = :cena")})
public class Artikal implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "naziv")
    private String naziv;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "tip")
    private String tip;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cena")
    private int cena;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "sifra")
    private String sifra;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "artikal")
    private List<Stanje> stanjeList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sifartikla")
    private List<Rezervacije> rezervacijeList;

    public Artikal() {
    }

    public Artikal(String sifra) {
        this.sifra = sifra;
    }

    public Artikal(String sifra, String naziv, String tip, int cena) {
        this.sifra = sifra;
        this.naziv = naziv;
        this.tip = tip;
        this.cena = cena;
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

    public List<Rezervacije> getRezervacijeList() {
        return rezervacijeList;
    }

    public void setRezervacijeList(List<Rezervacije> rezervacijeList) {
        this.rezervacijeList = rezervacijeList;
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
        if (!(object instanceof Artikal)) {
            return false;
        }
        Artikal other = (Artikal) object;
        if ((this.sifra == null && other.sifra != null) || (this.sifra != null && !this.sifra.equals(other.sifra))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Artikal[ sifra=" + sifra + " ]";
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public int getCena() {
        return cena;
    }

    public void setCena(int cena) {
        this.cena = cena;
    }
    
}
