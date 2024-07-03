package entities;

import entities.Video;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-07-03T13:21:21")
@StaticMetamodel(Kategorija.class)
public class Kategorija_ { 

    public static volatile ListAttribute<Kategorija, Video> videoList;
    public static volatile SingularAttribute<Kategorija, String> naziv;
    public static volatile SingularAttribute<Kategorija, Integer> idKategorija;

}