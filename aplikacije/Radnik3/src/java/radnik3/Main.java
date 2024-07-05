package radnik3;

import entities.Gledanje;
import entities.Ocena;
import entities.Paket;
import entities.Pretplata;
import entitiesForeign.Korisnik;
import entitiesForeign2.Video;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;


public class Main
{

    @Resource(lookup  = "queueServerRadnik3")
    private static Queue queueServerRadnik3;
    
    @Resource(lookup  = "queueRadnik3Server")
    private static Queue queueRadnik3Server;
    
    @Resource(lookup = "serverConnFactory")
    private static ConnectionFactory connectionFactory;

    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;
    private static EntityManagerFactory entityManagerFactoryForeign;
    private static EntityManager entityManagerForeign;
    private static EntityManagerFactory entityManagerFactoryForeign2;
    private static EntityManager entityManagerForeign2;
    private static JMSContext context;
    private static Connection connection;
    
    public static void main(String[] args)
    {
        entityManagerFactory = Persistence.createEntityManagerFactory("Radnik3PU");
        entityManager  = entityManagerFactory.createEntityManager();
        
        context = connectionFactory.createContext();    
        
        entityManagerFactoryForeign = Persistence.createEntityManagerFactory("Radnik1PU");
        entityManagerForeign = entityManagerFactoryForeign.createEntityManager();
        
        entityManagerFactoryForeign2 = Persistence.createEntityManagerFactory("Radnik2PU");
        entityManagerForeign2 = entityManagerFactoryForeign2.createEntityManager();
        
        
        JMSConsumer consumer = context.createConsumer(queueServerRadnik3);
        JMSProducer producer = context.createProducer();
        try
        {
            connection = connectionFactory.createConnection();
            connection.start();
            while(true)
            {
                System.err.println(" while");
                ObjectMessage objectMessage = (ObjectMessage) consumer.receive();
                int choice = objectMessage.getIntProperty("QueryNumber");
                ObjectMessage message = performChoice(choice, objectMessage);
                
                producer.send(queueRadnik3Server, message);
            }
            
        } catch (JMSException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            consumer.close();
        }
    }
    
    private static ObjectMessage performChoice(int choice, ObjectMessage objectMessage)
    {
        switch(choice)
        {
            case 9:
                return createPaket(objectMessage);
            case 10:
                return updateCena(objectMessage);
            case 11:
                return createPretplata(objectMessage);
            case 12:
                return createGledanje(objectMessage);
            case 13:
                return createOcena(objectMessage);
            case 14:
                return updateOcena(objectMessage);
            case 15:
                return deleteOcena(objectMessage);
            case 22:
                return getAllPaket(objectMessage);
            case 23:
                return getAllPretplataByKorisnik(objectMessage);
            case 24:
                return getAllGledanjeByVideo(objectMessage);
            case 25:
                return getAllOcenaByVideo(objectMessage);
        }
        
        return null;
    }
    
    private static ObjectMessage createPaket(ObjectMessage objectMessage)
    {
        ObjectMessage message = context.createObjectMessage();
        try
        {
            String Naziv = objectMessage.getStringProperty("Naziv");
            BigDecimal Cena = BigDecimal.valueOf(objectMessage.getDoubleProperty("Cena"));
            Paket paket = new Paket();
            paket.setNaziv(Naziv);
            paket.setCena(Cena);
            
            entityManager.getTransaction().begin();
            entityManager.persist(paket);
            entityManager.getTransaction().commit();
            
            message.setIntProperty("Status", 1);
            return message;
            
        } catch (JMSException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private static ObjectMessage updateCena(ObjectMessage objectMessage)
    {
        ObjectMessage message = context.createObjectMessage();
        try
        {
            String Naziv = objectMessage.getStringProperty("Naziv");
            BigDecimal Cena = BigDecimal.valueOf(objectMessage.getDoubleProperty("Cena"));
            
            entityManager.getTransaction().begin();
            
            Paket paket = entityManager.createNamedQuery("Paket.findByNaziv", Paket.class).setParameter("naziv", Naziv).getSingleResult();
            
            if(paket != null)
            {
                paket.setCena(Cena);
                entityManager.getTransaction().commit();
                message.setIntProperty("Status", 1);
            }
            else
            {
                entityManager.getTransaction().rollback();
                message.setIntProperty("Status", 0);
            }
            
            message.setIntProperty("Status", 1);
            return message;
            
        } catch (JMSException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static ObjectMessage createPretplata(ObjectMessage objectMessage)
    {
        ObjectMessage message = context.createObjectMessage();
        try
        {
            String Korisnik = objectMessage.getStringProperty("Korisnik");
            String Paket = objectMessage.getStringProperty("Paket");
            BigDecimal Cena = BigDecimal.valueOf(objectMessage.getDoubleProperty("Cena"));
            
            String datumString = objectMessage.getStringProperty("DatumPocetka");
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date datumPocetka = null;
            try 
            {
                datumPocetka = dateFormat.parse(datumString);
            } catch (ParseException e) 
            {
                System.out.println("Datum nije u ispravnom formatu. Pokušajte ponovo.");
                message.setIntProperty("Status", 0); // Failure
                return message;
            }
            
            entityManager.getTransaction().begin();
            
            Korisnik korisnik = entityManagerForeign.createNamedQuery("Korisnik.findByIme", Korisnik.class).setParameter("ime", Korisnik).getSingleResult();
            Paket paket = entityManager.createNamedQuery("Paket.findByNaziv", Paket.class).setParameter("naziv", Paket).getSingleResult();
            
            if(korisnik != null && paket != null)
            {
                Pretplata pretplata = new Pretplata();
                pretplata.setIdKorisnik(korisnik.getIdKorisnik());
                pretplata.setIdPaket(paket);
                pretplata.setDatumPocetka(datumPocetka);
                pretplata.setCena(Cena);
                entityManager.persist(pretplata);
                entityManager.getTransaction().commit();
                message.setIntProperty("Status", 1);
            }
            else
            {
                entityManager.getTransaction().rollback();
                message.setIntProperty("Status", 0);
            }
            
            message.setIntProperty("Status", 1);
            return message;
            
        } catch (JMSException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private static ObjectMessage createGledanje(ObjectMessage objectMessage)
    {
        ObjectMessage message = context.createObjectMessage();
        try
        {
            String Korisnik = objectMessage.getStringProperty("Korisnik");
            String Video = objectMessage.getStringProperty("Video");
            String datumString = objectMessage.getStringProperty("DatumPocetka");
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date datumPocetka = null;
            try 
            {
                datumPocetka = dateFormat.parse(datumString);
            } catch (ParseException e) 
            {
                System.out.println("Datum nije u ispravnom formatu. Pokušajte ponovo.");
                message.setIntProperty("Status", 0); // Failure
                return message;
            }
            
            Integer SekundPocetka = objectMessage.getIntProperty("SekundPocetka");
            Integer SekundOdgledano = objectMessage.getIntProperty("SekundOdgledano");
            
            entityManager.getTransaction().begin();
            
            
            Korisnik korisnik = entityManagerForeign.createNamedQuery("Korisnik.findByIme", Korisnik.class).setParameter("ime", Korisnik).getSingleResult();
            Video video = entityManagerForeign2.createNamedQuery("Video.findByNaziv", Video.class).setParameter("naziv", Video).getSingleResult();
            
            if(korisnik != null && video != null)
            {
                Gledanje gledanje = new Gledanje();
                gledanje.setIdKorisnik(korisnik.getIdKorisnik());
                gledanje.setIdVideo(video.getIdVideo());
                gledanje.setDatumPocetka(datumPocetka);
                gledanje.setSekundPocetka(SekundPocetka);
                gledanje.setSekundOdgledano(SekundOdgledano);
                
                entityManager.persist(gledanje);
                entityManager.getTransaction().commit();
                message.setIntProperty("Status", 1);
            }
            else
            {
                entityManager.getTransaction().rollback();
                message.setIntProperty("Status", 0);
            }
            
            return message;
            
        } catch (JMSException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private static ObjectMessage createOcena(ObjectMessage objectMessage)
    {
        ObjectMessage message = context.createObjectMessage();
        try
        {
            String Korisnik = objectMessage.getStringProperty("Korisnik");
            String Video = objectMessage.getStringProperty("Video");
            Integer Ocena = objectMessage.getIntProperty("Ocena");
            String datumString = objectMessage.getStringProperty("Datum");
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date datumPocetka = null;
            try 
            {
                datumPocetka = dateFormat.parse(datumString);
            } catch (ParseException e) 
            {
                System.out.println("Datum nije u ispravnom formatu. Pokušajte ponovo.");
                message.setIntProperty("Status", 0); // Failure
                return message;
            }
            
            
            entityManager.getTransaction().begin();
            
            Korisnik korisnik = entityManagerForeign.createNamedQuery("Korisnik.findByIme", Korisnik.class).setParameter("ime", Korisnik).getSingleResult();
            Video video = entityManagerForeign2.createNamedQuery("Video.findByNaziv", Video.class).setParameter("naziv", Video).getSingleResult();
            
            if(korisnik != null && video != null && !existsOcena(korisnik, video))
            {
                Ocena ocena = new Ocena();
                ocena.setIdKorisnik(korisnik.getIdKorisnik());
                ocena.setIdVideo(video.getIdVideo());
                ocena.setOcena(Ocena);
                ocena.setDatum(datumPocetka);
                
                entityManager.persist(ocena);
                entityManager.getTransaction().commit();
                message.setIntProperty("Status", 1);
            }
            else
            {
                entityManager.getTransaction().rollback();
                message.setIntProperty("Status", 0);
            }
            
            return message;
            
        } catch (JMSException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private static ObjectMessage updateOcena(ObjectMessage objectMessage)
    {
        ObjectMessage message = context.createObjectMessage();
        try
        {
            String Korisnik = objectMessage.getStringProperty("Korisnik");
            String Video = objectMessage.getStringProperty("Video");
            Integer Ocena = objectMessage.getIntProperty("Ocena");

            entityManager.getTransaction().begin();
            
            Korisnik korisnik = entityManagerForeign.createNamedQuery("Korisnik.findByIme", Korisnik.class).setParameter("ime", Korisnik).getSingleResult();
            Video video = entityManagerForeign2.createNamedQuery("Video.findByNaziv", Video.class).setParameter("naziv", Video).getSingleResult();
            
            if(korisnik != null && video != null && existsOcena(korisnik, video))
            {
                Ocena ocena = getOcena(korisnik, video);
                ocena.setOcena(Ocena);
                entityManager.getTransaction().commit();
                message.setIntProperty("Status", 1);
            }
            else
            {
                entityManager.getTransaction().rollback();
                message.setIntProperty("Status", 0);
            }
            
            return message;
            
        } catch (JMSException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private static ObjectMessage deleteOcena(ObjectMessage objectMessage)
    {
        ObjectMessage message = context.createObjectMessage();
        try
        {
            String Korisnik = objectMessage.getStringProperty("Korisnik");
            String Video = objectMessage.getStringProperty("Video");

            entityManager.getTransaction().begin();
            
            Korisnik korisnik = entityManagerForeign.createNamedQuery("Korisnik.findByIme", Korisnik.class).setParameter("ime", Korisnik).getSingleResult();
            Video video = entityManagerForeign2.createNamedQuery("Video.findByNaziv", Video.class).setParameter("naziv", Video).getSingleResult();
            
            
            if(korisnik != null && video != null && existsOcena(korisnik, video))
            {
                Ocena ocena = getOcena(korisnik, video);
                entityManager.remove(ocena);
                entityManager.getTransaction().commit();
                message.setIntProperty("Status", 1);
            }
            else
            {
                entityManager.getTransaction().rollback();
                message.setIntProperty("Status", 0);
            }
            
            return message;
            
        } catch (JMSException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private static ObjectMessage getAllPaket(ObjectMessage objectMessage)
    {
        ObjectMessage message = context.createObjectMessage();
        try
        {
            List<Paket> paketList = entityManager.createNamedQuery("Paket.findAll", Paket.class).getResultList();
            List<Paket> paketListReturn = new ArrayList<>();
            
            paketList.forEach(paket -> 
            {
                Paket p = new Paket();
                p.setIdPaket(paket.getIdPaket());
                p.setNaziv(paket.getNaziv());
                p.setCena(paket.getCena());
                
                paketListReturn.add(p);
            });
            
            message.setObject((Serializable) paketListReturn);
            message.setIntProperty("Status", 1);

        } catch (JMSException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            entityManager.getTransaction().rollback();
            try {
                message.setIntProperty("Status", 0); // Failure
            } catch (JMSException e) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return message;
    }
    
    private static ObjectMessage getAllPretplataByKorisnik(ObjectMessage objectMessage)
    {
        ObjectMessage message = context.createObjectMessage();
        try
        {
            String Korisnik = objectMessage.getStringProperty("Korisnik");
            Korisnik korisnik = entityManagerForeign.createNamedQuery("Korisnik.findByIme", Korisnik.class).setParameter("ime", Korisnik).getSingleResult();
            
            if(korisnik != null)
            {
                List<Pretplata> pretplataList = getPretplataForKorisnik(korisnik);
                List<Pretplata> pretplataListReturn = new ArrayList<>();

                pretplataList.forEach(pretplata -> 
                {
                    Pretplata p = new Pretplata();
                    p.setIdPretplata(pretplata.getIdPretplata());
                    p.setIdKorisnik(pretplata.getIdKorisnik());
                    
                    Paket pak = new Paket();
                    pak.setIdPaket(pretplata.getIdPaket().getIdPaket());
                    
                    p.setIdPaket(pak);
                    p.setDatumPocetka(pretplata.getDatumPocetka());
                    p.setCena(pretplata.getCena());
                    
                    pretplataListReturn.add(p);
                });

                message.setObject((Serializable) pretplataListReturn);
                message.setIntProperty("Status", 1);
            }
        } catch (JMSException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            entityManager.getTransaction().rollback();
            try {
                message.setIntProperty("Status", 0); // Failure
            } catch (JMSException e) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return message;
    }
    
    private static ObjectMessage getAllGledanjeByVideo(ObjectMessage objectMessage)
    {
        ObjectMessage message = context.createObjectMessage();
        try
        {
            String Video = objectMessage.getStringProperty("Video");
            Video video = entityManagerForeign2.createNamedQuery("Video.findByNaziv", Video.class).setParameter("naziv", Video).getSingleResult();
            
            if(video != null)
            {
                List<Gledanje> gledanjeList = getGledanjeForVideo(video);
                List<Gledanje> gledanjeListReturn = new ArrayList<>();

                gledanjeList.forEach(gledanje -> 
                {
                    Gledanje g = new Gledanje();
                    g.setIdGledanje(gledanje.getIdGledanje());
                    g.setIdKorisnik(gledanje.getIdKorisnik());
                    g.setIdVideo(gledanje.getIdVideo());
                    g.setDatumPocetka(gledanje.getDatumPocetka());
                    g.setSekundPocetka(gledanje.getSekundPocetka());
                    g.setSekundOdgledano(gledanje.getSekundOdgledano());
                    
                    gledanjeListReturn.add(g);
                });

                message.setObject((Serializable) gledanjeListReturn);
                message.setIntProperty("Status", 1);
            }
        } catch (JMSException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            entityManager.getTransaction().rollback();
            try {
                message.setIntProperty("Status", 0); // Failure
            } catch (JMSException e) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return message;
    }
    
    private static ObjectMessage getAllOcenaByVideo(ObjectMessage objectMessage)
    {
        ObjectMessage message = context.createObjectMessage();
        try
        {
            String Video = objectMessage.getStringProperty("Video");
            Video video = entityManagerForeign2.createNamedQuery("Video.findByNaziv", Video.class).setParameter("naziv", Video).getSingleResult();
            
            if(video != null)
            {
                List<Ocena> ocenaList = getOcenaForVideo(video);
                List<Ocena> ocenaListReturn = new ArrayList<>();

                ocenaList.forEach(ocena -> 
                {
                    Ocena o = new Ocena();
                    o.setIdOcena(ocena.getIdOcena());
                    o.setIdKorisnik(ocena.getIdKorisnik());
                    o.setIdVideo(ocena.getIdVideo());
                    o.setOcena(ocena.getOcena());
                    o.setDatum(ocena.getDatum());
                    
                    ocenaListReturn.add(o);
                });

                message.setObject((Serializable) ocenaListReturn);
                message.setIntProperty("Status", 1);
            }
        } catch (JMSException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            entityManager.getTransaction().rollback();
            try {
                message.setIntProperty("Status", 0); // Failure
            } catch (JMSException e) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return message;
    }
    
    //returns true if Ocena exists
    private static boolean existsOcena(Korisnik korisnik, Video video) 
    {
        TypedQuery<Ocena> query = entityManager.createQuery(
                "SELECT o FROM Ocena o WHERE o.idKorisnik = :idKorisnik AND o.idVideo = :idVideo", Ocena.class);
        query.setParameter("idKorisnik", korisnik.getIdKorisnik());
        query.setParameter("idVideo", video.getIdVideo());

        List<Ocena> results = query.getResultList();
        return !results.isEmpty();
    }
    
    private static Ocena getOcena(Korisnik korisnik, Video video)
    {
        TypedQuery<Ocena> query = entityManager.createQuery(
                "SELECT o FROM Ocena o WHERE o.idKorisnik = :idKorisnik AND o.idVideo = :idVideo", Ocena.class);
            query.setParameter("idKorisnik", korisnik.getIdKorisnik());
            query.setParameter("idVideo", video.getIdVideo());
            
        Ocena ocena = query.getSingleResult();
        
        return ocena;
    }

    private static List<Pretplata> getPretplataForKorisnik(Korisnik korisnik) 
    {
        TypedQuery<Pretplata> query = entityManager.createQuery(
            "SELECT p FROM Pretplata p WHERE p.idKorisnik = :idKorisnik", Pretplata.class);
        query.setParameter("idKorisnik", korisnik.getIdKorisnik());

        List<Pretplata> pretplataList = query.getResultList();

        return pretplataList;
    }
    
    private static List<Gledanje> getGledanjeForVideo(Video video) 
    {
        TypedQuery<Gledanje> query = entityManager.createQuery(
            "SELECT g FROM Gledanje g WHERE g.idVideo = :idVideo", Gledanje.class);
        query.setParameter("idVideo", video.getIdVideo());

        List<Gledanje> geldanjeList = query.getResultList();

        return geldanjeList;
    }

    private static List<Ocena> getOcenaForVideo(Video video) 
    {
        TypedQuery<Ocena> query = entityManager.createQuery(
            "SELECT o FROM Ocena o WHERE o.idVideo = :idVideo", Ocena.class);
        query.setParameter("idVideo", video.getIdVideo());

        List<Ocena> ocenaList = query.getResultList();

        return ocenaList;
    }
}
