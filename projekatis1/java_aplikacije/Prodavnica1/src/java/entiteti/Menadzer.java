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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Aleksandar
 */
@Entity
@Table(name = "menadzer")
@NamedQueries({
    @NamedQuery(name = "Menadzer.findAll", query = "SELECT m FROM Menadzer m")
    , @NamedQuery(name = "Menadzer.findByIdmenadzer", query = "SELECT m FROM Menadzer m WHERE m.idmenadzer = :idmenadzer")
    , @NamedQuery(name = "Menadzer.findByIme", query = "SELECT m FROM Menadzer m WHERE m.ime = :ime")
    , @NamedQuery(name = "Menadzer.findByPrezime", query = "SELECT m FROM Menadzer m WHERE m.prezime = :prezime")})
public class Menadzer implements Serializable {

    @Size(max = 45)
    @Column(name = "ime")
    private String ime;
    @Size(max = 45)
    @Column(name = "prezime")
    private String prezime;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "idmenadzer")
    private String idmenadzer;
    @JoinColumn(name = "idmenadzer", referencedColumnName = "sifra", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Prodavnica prodavnica;

    public Menadzer() {
    }

    public Menadzer(String idmenadzer) {
        this.idmenadzer = idmenadzer;
    }

    public String getIdmenadzer() {
        return idmenadzer;
    }

    public void setIdmenadzer(String idmenadzer) {
        this.idmenadzer = idmenadzer;
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
        hash += (idmenadzer != null ? idmenadzer.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Menadzer)) {
            return false;
        }
        Menadzer other = (Menadzer) object;
        if ((this.idmenadzer == null && other.idmenadzer != null) || (this.idmenadzer != null && !this.idmenadzer.equals(other.idmenadzer))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Menadzer[ idmenadzer=" + idmenadzer + " ]";
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
    
}
