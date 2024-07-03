package entities;

import entities.Mesto;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-07-01T12:30:52")
@StaticMetamodel(Korisnik.class)
public class Korisnik_ { 

    public static volatile SingularAttribute<Korisnik, String> ime;
    public static volatile SingularAttribute<Korisnik, Mesto> idMesto;
    public static volatile SingularAttribute<Korisnik, Integer> godiste;
    public static volatile SingularAttribute<Korisnik, Character> pol;
    public static volatile SingularAttribute<Korisnik, String> email;
    public static volatile SingularAttribute<Korisnik, Integer> idKorisnik;

}