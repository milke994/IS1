package entiteti;

import entiteti.Rezervacije;
import entiteti.Stanje;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-01-26T11:04:57")
@StaticMetamodel(Artikal.class)
public class Artikal_ { 

    public static volatile ListAttribute<Artikal, Stanje> stanjeList;
    public static volatile SingularAttribute<Artikal, String> naziv;
    public static volatile SingularAttribute<Artikal, String> tip;
    public static volatile SingularAttribute<Artikal, String> sifra;
    public static volatile ListAttribute<Artikal, Rezervacije> rezervacijeList;
    public static volatile SingularAttribute<Artikal, Integer> cena;

}