/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package radnik2;

import entities.Kategorija;
import entities.Video;
import entitiesForeign.Korisnik;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
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

/**
 *
 * @author HP 735 G5
 */
public class Main
{
    @Resource(lookup  = "queueServerRadnik2")
    private static Queue queueServerRadnik2;
    
    @Resource(lookup  = "queueRadnik2Server")
    private static Queue queueRadnik2Server;
    
    @Resource(lookup = "serverConnFactory")
    private static ConnectionFactory connectionFactory;

    private static EntityManagerFactory entityManagerFactory ;
    private static EntityManager entityManager;
    private static EntityManagerFactory entityManagerFactoryForeign ;
    private static EntityManager entityManagerForeign;
    private static JMSContext context;
    private static Connection connection;
    
    public static void main(String[] args)
    {
        entityManagerFactory = Persistence.createEntityManagerFactory("Radnik2PU");
        entityManager  = entityManagerFactory.createEntityManager();
        
        context = connectionFactory.createContext();    
        
        entityManagerFactoryForeign = Persistence.createEntityManagerFactory("Radnik1PU");
        entityManagerForeign = entityManagerFactoryForeign.createEntityManager();
        
        
        JMSConsumer consumer = context.createConsumer(queueServerRadnik2);
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
                
                producer.send(queueRadnik2Server, message);
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
                case 5:
                    return createKategorija(objectMessage);
                case 6:
                    return createVideo(objectMessage); 
                case 7:
                    return updateNaziv(objectMessage);
                case 8:
                    return updateKategorija(objectMessage);
                case 16:
                    return deleteVideo(objectMessage);
                case 19:
                    return getAllKategorija(objectMessage);
                case 20:
                    return getAllVideo(objectMessage);
        }
        
        return null;
    }
    
    private static ObjectMessage createKategorija(ObjectMessage objectMessage)
    {
        ObjectMessage message = context.createObjectMessage();
        try
        {
            String Naziv = objectMessage.getStringProperty("Naziv");
            Kategorija kategorija = new Kategorija();
            kategorija.setNaziv(Naziv);
            
            entityManager.getTransaction().begin();
            entityManager.persist(kategorija);
            entityManager.getTransaction().commit();
            
            message.setIntProperty("Status", 1);
            return message;
            
        } catch (JMSException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private static ObjectMessage createVideo(ObjectMessage objectMessage)
    {
        ObjectMessage message = context.createObjectMessage();
        try 
        {
            String naziv = objectMessage.getStringProperty("Naziv");
            BigDecimal trajanje = BigDecimal.valueOf(objectMessage.getDoubleProperty("Trajanje"));
            
            String datumString = objectMessage.getStringProperty("DatumPostavljanja");
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date datumPostavljanja = null;
            try 
            {
                datumPostavljanja = dateFormat.parse(datumString);
            } catch (ParseException e) 
            {
                System.out.println("Datum nije u ispravnom formatu. Pokušajte ponovo.");
                message.setIntProperty("Status", 0); // Failure
                return message;
            }
            
            String Korisnik = objectMessage.getStringProperty("Korisnik");
            Korisnik korisnik = entityManagerForeign.createNamedQuery("Korisnik.findByIme", Korisnik.class).setParameter("ime", Korisnik).getSingleResult();
            
            entityManager.getTransaction().begin();
            
            if(korisnik != null)
            {
                int idKorisnik = korisnik.getIdKorisnik();
                Video video = new Video();
                video.setNaziv(naziv);
                video.setTrajanje(trajanje);
                video.setIdKorisnik(idKorisnik);
                video.setDatumPostavljanja(datumPostavljanja);
                entityManager.persist(video);
                entityManager.getTransaction().commit();
                message.setIntProperty("Status", 1);
            }
            else
            {
                entityManager.getTransaction().rollback();
                message.setIntProperty("Status", 0);
            }

        } catch (JMSException | RuntimeException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            if (entityManager.getTransaction().isActive()) 
            {
                entityManager.getTransaction().rollback();
            }
            try {
                message.setIntProperty("Status", 0); // Failure
            } catch (JMSException e) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return message;
    }

    private static ObjectMessage updateNaziv(ObjectMessage objectMessage)
    {
        ObjectMessage message = context.createObjectMessage();
        try
        {
            String NazivOld = objectMessage.getStringProperty("NazivOld");
            String NazivNew = objectMessage.getStringProperty("NazivNew");
            
            entityManager.getTransaction().begin();
            
            Video video = entityManager.createNamedQuery("Video.findByNaziv", Video.class).setParameter("naziv", NazivOld).getSingleResult();
            
            if(video != null)
            {
                video.setNaziv(NazivNew);
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

    private static ObjectMessage updateKategorija(ObjectMessage objectMessage)
    {
        ObjectMessage message = context.createObjectMessage();
        
        try
        {
            String Video = objectMessage.getStringProperty("Video");
            String Kategorija = objectMessage.getStringProperty("Kategorija");
            
            entityManager.getTransaction().begin();
            
            Video video = entityManager.createNamedQuery("Video.findByNaziv", Video.class).setParameter("naziv", Video).getSingleResult();
            Kategorija kategorija = entityManager.createNamedQuery("Kategorija.findByNaziv", Kategorija.class).setParameter("naziv", Kategorija).getSingleResult();
            
            
            if(kategorija != null && video != null)
            {
                List<Kategorija> kategorijaList = video.getKategorijaList();
                kategorijaList.add(kategorija);
                video.setKategorijaList(kategorijaList);
                
                List<Video> videoList = kategorija.getVideoList();
                videoList.add(video);
                kategorija.setVideoList(videoList);
                
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
        
        return message;
    }

    private static ObjectMessage deleteVideo(ObjectMessage objectMessage)
    {
        ObjectMessage message = context.createObjectMessage();
        try
        {
            String Video = objectMessage.getStringProperty("Video");
            String Korisnik = objectMessage.getStringProperty("Korisnik");
            
            entityManager.getTransaction().begin();
            
            Video video = entityManager.createNamedQuery("Video.findByNaziv", Video.class).setParameter("naziv", Video).getSingleResult();
            Korisnik korisnik = entityManagerForeign.createNamedQuery("Korisnik.findByIme", Korisnik.class).setParameter("ime", Korisnik).getSingleResult();
            
            if(video != null && korisnik != null && (video.getIdKorisnik().equals(korisnik.getIdKorisnik())))
            {
                entityManager.remove(video);
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

    private static ObjectMessage getAllKategorija(ObjectMessage objectMessage)
    {
        System.err.println("Usao u kat");
        ObjectMessage message = context.createObjectMessage();
        try
        {
            List<Kategorija> kategorijaList = entityManager.createNamedQuery("Kategorija.findAll", Kategorija.class).getResultList();
            List<Kategorija> kategorijaListReturn = new ArrayList<>();
            
            kategorijaList.forEach(kategorija -> 
            {
                Kategorija k = new Kategorija();
                k.setIdKategorija(kategorija.getIdKategorija());
                k.setNaziv(kategorija.getNaziv());
                List<Video> videoList = kategorija.getVideoList();
                List<Video> videoListReturn = new ArrayList<>();
                
                videoList.forEach(video ->
                {
                    Video v = new Video();
                    v.setIdVideo(video.getIdVideo());
                    v.setNaziv(video.getNaziv());
                    v.setTrajanje(video.getTrajanje());
                    v.setIdKorisnik(video.getIdKorisnik());
                    v.setDatumPostavljanja(video.getDatumPostavljanja());
                    
                    videoListReturn.add(v);
                });
                k.setVideoList(videoListReturn);
                
                kategorijaListReturn.add(k);
            });
            System.err.println("Usao u govno");
            message.setObject((Serializable) kategorijaListReturn);
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

    private static ObjectMessage getAllVideo(ObjectMessage objectMessage)
    {
        ObjectMessage message = context.createObjectMessage();
        try
        {
            List<Video> videoList = entityManager.createNamedQuery("Video.findAll", Video.class).getResultList();
            List<Video> videoListReturn = new ArrayList<>();
            
            videoList.forEach(video -> 
            {
                Video v = new Video();
                
                v.setIdVideo(video.getIdVideo());
                v.setNaziv(video.getNaziv());
                v.setTrajanje(video.getTrajanje());
                v.setIdKorisnik(video.getIdKorisnik());
                v.setDatumPostavljanja(video.getDatumPostavljanja());
                List<Kategorija> kategorijaList = video.getKategorijaList();
                List<Kategorija> kategorijaListReturn = new ArrayList<>();
                
                kategorijaList.forEach(kategorija -> 
                {
                    Kategorija k = new Kategorija();
                    k.setIdKategorija(kategorija.getIdKategorija());
                    k.setNaziv(kategorija.getNaziv());
                    
                    kategorijaListReturn.add(k);
                });
                
                v.setKategorijaList(kategorijaListReturn);
                
                videoListReturn.add(v);
            });
            
            message.setObject((Serializable) videoListReturn);
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

}
