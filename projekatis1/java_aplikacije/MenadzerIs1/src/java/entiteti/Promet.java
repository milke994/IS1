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
@Table(name = "promet")
@NamedQueries({
    @NamedQuery(name = "Promet.findAll", query = "SELECT p FROM Promet p")
    , @NamedQuery(name = "Promet.findByIdpromet", query = "SELECT p FROM Promet p WHERE p.idpromet = :idpromet")
    , @NamedQuery(name = "Promet.findByVrednost", query = "SELECT p FROM Promet p WHERE p.vrednost = :vrednost")
    , @NamedQuery(name = "Promet.findByDatum", query = "SELECT p FROM Promet p WHERE p.datum = :datum")})
public class Promet implements Serializable {

    @Size(max = 45)
    @Column(name = "datum")
    private String datum;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "idpromet")
    private String idpromet;
    @Column(name = "vrednost")
    private Integer vrednost;
    @JoinColumn(name = "idpromet", referencedColumnName = "sifra", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Prodavnica prodavnica;

    public Promet() {
    }

    public Promet(String idpromet) {
        this.idpromet = idpromet;
    }

    public String getIdpromet() {
        return idpromet;
    }

    public void setIdpromet(String idpromet) {
        this.idpromet = idpromet;
    }

    public Integer getVrednost() {
        return vrednost;
    }

    public void setVrednost(Integer vrednost) {
        this.vrednost = vrednost;
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
        hash += (idpromet != null ? idpromet.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Promet)) {
            return false;
        }
        Promet other = (Promet) object;
        if ((this.idpromet == null && other.idpromet != null) || (this.idpromet != null && !this.idpromet.equals(other.idpromet))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Promet[ idpromet=" + idpromet + " ]";
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }
    
}
