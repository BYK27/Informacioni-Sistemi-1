package radnik1;

import entities.Korisnik;
import entities.Mesto;
import java.io.Serializable;
import java.util.ArrayList;
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

public class Main
{

    @Resource(lookup  = "qSR1")
    private static Queue queueServerRadnik1;
    
    @Resource(lookup  = "queueRadnik1Server")
    private static Queue queueRadnik1Server;
    
    @Resource(lookup = "serverConnFactory")
    private static ConnectionFactory connectionFactory;

    private static EntityManagerFactory entityManagerFactory ;
    private static EntityManager entityManager;
    private static JMSContext context;
    private static Connection connection;
    
    public static void main(String[] args)
    {
        entityManagerFactory = Persistence.createEntityManagerFactory("Radnik1PU");
        entityManager  = entityManagerFactory.createEntityManager();
        context = connectionFactory.createContext();    
        
        System.err.println("USAO");
        JMSConsumer consumer = context.createConsumer(queueServerRadnik1);
        System.err.println("Napravio consumer");
        JMSProducer producer = context.createProducer();
        System.err.println("Napravio producer");
        try
        {
            connection = connectionFactory.createConnection();
            connection.start();
            System.err.println(" try");
            while(true)
            {
                System.err.println(" while");
                ObjectMessage objectMessage = (ObjectMessage) consumer.receive();
                System.out.println("PRIMIO IZBOR");
                int choice = objectMessage.getIntProperty("QueryNumber");
                ObjectMessage message = performChoice(choice, objectMessage);
                
                producer.send(queueRadnik1Server, message);
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
            case 1:
                return createMesto(objectMessage);
            case 2:
                return createKorisnik(objectMessage);
            case 3:
                return updateEmail(objectMessage);
            case 4:
                return updateMesto(objectMessage);
            case 17:
                return getAllKorisnik(objectMessage);
            case 18:
                return getAllMesto(objectMessage);
        }
        
        return null;
    }
     
    private static ObjectMessage createMesto(ObjectMessage objectMessage)
    {
        ObjectMessage message = context.createObjectMessage();
        try
        {
            String Naziv = objectMessage.getStringProperty("Naziv");
            Mesto mesto = new Mesto();
            mesto.setNaziv(Naziv);
            
            entityManager.getTransaction().begin();
            entityManager.persist(mesto);
            entityManager.getTransaction().commit();
            
            message.setIntProperty("Status", 1);
            return message;
            
        } catch (JMSException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private static ObjectMessage createKorisnik(ObjectMessage objectMessage)
    {
        ObjectMessage message = context.createObjectMessage();
        try
        {
            String Ime = objectMessage.getStringProperty("Ime");
            String Email = objectMessage.getStringProperty("Email");
            int Godiste = objectMessage.getIntProperty("Godiste");
            String Pol = objectMessage.getStringProperty("Pol");
            String Mesto = objectMessage.getStringProperty("idMesta");
            
            
            entityManager.getTransaction().begin();
            
            Mesto mesto = entityManager.createNamedQuery("Mesto.findByNaziv", Mesto.class).setParameter("naziv", Mesto).getSingleResult();
            
            if(mesto != null)
            {
                Korisnik korisnik = new Korisnik();
                korisnik.setIme(Ime);
                korisnik.setEmail(Email);
                korisnik.setGodiste(Godiste);
                korisnik.setPol(Pol.charAt(0));
                korisnik.setIdMesto(mesto);


                entityManager.persist(korisnik);
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
    
    private static ObjectMessage updateMesto (ObjectMessage objectMessage)
    {
        ObjectMessage message = context.createObjectMessage();
        try
        {
            String Korisnik = objectMessage.getStringProperty("Korisnik");
            String Mesto = objectMessage.getStringProperty("Mesto");

            entityManager.getTransaction().begin();

            Korisnik korisnik = entityManager.createNamedQuery("Korisnik.findByIme", Korisnik.class).setParameter("ime", Korisnik).getSingleResult();
            Mesto mesto = entityManager.createNamedQuery("Mesto.findByNaziv", Mesto.class).setParameter("naziv", Mesto).getSingleResult();
            if (korisnik != null && mesto != null)
            {
                korisnik.setIdMesto(mesto);
                entityManager.getTransaction().commit();
                message.setIntProperty("Status", 1);
            }
            else
            {
                entityManager.getTransaction().rollback();
                message.setIntProperty("Status", 0);
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
    
    private static ObjectMessage updateEmail (ObjectMessage objectMessage)
    {
        ObjectMessage message = context.createObjectMessage();
        try
        {
            String Korisnik = objectMessage.getStringProperty("Korisnik");
            String Email = objectMessage.getStringProperty("Email");

            entityManager.getTransaction().begin();

            Korisnik korisnik = entityManager.createNamedQuery("Korisnik.findByIme", Korisnik.class).setParameter("ime", Korisnik).getSingleResult();
            if (korisnik != null)
            {
                korisnik.setEmail(Email);
                entityManager.getTransaction().commit();
                message.setIntProperty("Status", 1);
            }
            else
            {
                entityManager.getTransaction().rollback();
                message.setIntProperty("Status", 0);
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

    private static ObjectMessage getAllKorisnik(ObjectMessage objectMessage)
    {
        ObjectMessage message = context.createObjectMessage();
        try
        {
            List<Korisnik> korisnikList = entityManager.createNamedQuery("Korisnik.findAll", Korisnik.class).getResultList();
            List<Korisnik> korisnikListReturn = new ArrayList<>();
            
            korisnikList.forEach(korisnik ->
            {
                Korisnik k = new Korisnik();
                Mesto m = new Mesto();
                
                m.setIdMesto(korisnik.getIdMesto().getIdMesto());
                
                k.setIdKorisnik(korisnik.getIdKorisnik());
                k.setIme(korisnik.getIme());
                k.setEmail(korisnik.getEmail());
                k.setGodiste(korisnik.getGodiste());
                k.setPol(korisnik.getPol());
                k.setIdMesto(m);
                korisnikListReturn.add(k);
            });
            
            message.setObject((Serializable) korisnikListReturn);
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
    
    private static ObjectMessage getAllMesto(ObjectMessage objectMessage)
    {
        ObjectMessage message = context.createObjectMessage();
        try
        {
            List<Mesto> mestoList = entityManager.createNamedQuery("Mesto.findAll", Mesto.class).getResultList();
            List<Mesto> mestoListReturn = new ArrayList<>();
            
            mestoList.forEach(mesto -> 
            {
                Mesto m = new Mesto();
                m.setIdMesto(mesto.getIdMesto());
                m.setNaziv(mesto.getNaziv());
                
                mestoListReturn.add(m);
            });
            
            message.setObject((Serializable) mestoListReturn);
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
