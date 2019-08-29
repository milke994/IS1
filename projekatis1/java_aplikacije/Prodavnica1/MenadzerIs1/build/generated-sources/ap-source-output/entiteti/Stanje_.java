package entiteti;

import entiteti.Artikal;
import entiteti.Prodavnica;
import entiteti.StanjePK;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-01-26T00:46:48")
@StaticMetamodel(Stanje.class)
public class Stanje_ { 

    public static volatile SingularAttribute<Stanje, Prodavnica> prodavnica;
    public static volatile SingularAttribute<Stanje, Artikal> artikal;
    public static volatile SingularAttribute<Stanje, Integer> kolicina;
    public static volatile SingularAttribute<Stanje, StanjePK> stanjePK;

}