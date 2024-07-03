package entities;

import entities.Kategorija;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-07-03T13:21:21")
@StaticMetamodel(Video.class)
public class Video_ { 

    public static volatile SingularAttribute<Video, Integer> idVideo;
    public static volatile ListAttribute<Video, Kategorija> kategorijaList;
    public static volatile SingularAttribute<Video, Date> datumPostavljanja;
    public static volatile SingularAttribute<Video, BigDecimal> trajanje;
    public static volatile SingularAttribute<Video, String> naziv;
    public static volatile SingularAttribute<Video, Integer> idKorisnik;

}