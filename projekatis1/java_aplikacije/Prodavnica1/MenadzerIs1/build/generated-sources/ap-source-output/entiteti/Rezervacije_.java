package entiteti;

import entiteti.Artikal;
import entiteti.Korisnik;
import entiteti.Prodavnica;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-01-26T11:04:57")
@StaticMetamodel(Rezervacije.class)
public class Rezervacije_ { 

    public static volatile SingularAttribute<Rezervacije, String> datum;
    public static volatile SingularAttribute<Rezervacije, Prodavnica> sifprodavnice;
    public static volatile SingularAttribute<Rezervacije, Integer> kolicina;
    public static volatile SingularAttribute<Rezervacije, Artikal> sifartikla;
    public static volatile SingularAttribute<Rezervacije, Integer> idrezervacije;
    public static volatile SingularAttribute<Rezervacije, Korisnik> sifrakorinsik;

}