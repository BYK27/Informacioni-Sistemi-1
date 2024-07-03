/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package radnik2;

import entities.Kategorija;
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
    private static JMSContext context;
    private static Connection connection;
    
    public static void main(String[] args)
    {
        entityManagerFactory = Persistence.createEntityManagerFactory("Radnik1PU");
        entityManager  = entityManagerFactory.createEntityManager();
        context = connectionFactory.createContext();    
        
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
                case 1:
                    return createKategorija(objectMessage);
                    
        }
        
        return null;
    }
    
    private static ObjectMessage createKategorija(ObjectMessage objectMessage)
    {
        ObjectMessage message = context.createObjectMessage();
        try
        {
            String Naziv = objectMessage.getStringProperty("Naziv");
            Kategorija mesto = new Kategorija();
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
    
    private static ObjectMessage createVideo(ObjectMessage objectMessage)
    {
        ObjectMessage message = context.createObjectMessage();
        try
        {
            String Naziv = objectMessage.getStringProperty("Naziv");
            String Trajanje = objectMessage.getStringProperty("Trajanje");
            String Korisnik = objectMessage.getStringProperty("Korisnik");
            String DatumPostavljanja = objectMessage.getStringProperty("DatumPostavljanja");
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
}
