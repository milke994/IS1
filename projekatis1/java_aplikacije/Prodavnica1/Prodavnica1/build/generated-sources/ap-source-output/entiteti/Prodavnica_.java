package entiteti;

import entiteti.Menadzer;
import entiteti.Promet;
import entiteti.Rezervacije;
import entiteti.Stanje;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-01-25T23:09:37")
@StaticMetamodel(Prodavnica.class)
public class Prodavnica_ { 

    public static volatile ListAttribute<Prodavnica, Stanje> stanjeList;
    public static volatile SingularAttribute<Prodavnica, Promet> promet;
    public static volatile SingularAttribute<Prodavnica, String> naziv;
    public static volatile SingularAttribute<Prodavnica, String> sifra;
    public static volatile ListAttribute<Prodavnica, Rezervacije> rezervacijeList;
    public static volatile SingularAttribute<Prodavnica, Menadzer> menadzer;

}