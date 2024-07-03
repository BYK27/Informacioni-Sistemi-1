package server;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("server")
public class Server 
{
    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager entityManager;
    
    @Resource(lookup = "serverConnFactory")
    private ConnectionFactory connectionFactory;
    
    @Resource(lookup  = "qSR1")
    private Queue queueServerRadnik1;
    
    @Resource(lookup  = "queueRadnik1Server")
    private Queue queueRadnik1Server;
    
    @Resource(lookup  = "queueServerRadnik2")
    private Queue queueServerRadnik2;
    
    @Resource(lookup  = "queueRadnik2Server")
    private Queue queueRadnik2Server;
    
    @POST
    @Path("query1/{Naziv}")
    public Response createMesto(@PathParam("Naziv") String Naziv)
    {
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(queueRadnik1Server);
        //return Response.ok("usao u ovo sranje", MediaType.TEXT_PLAIN).build();
        try
        {
            ObjectMessage objectMessage = context.createObjectMessage();
            objectMessage.setIntProperty("QueryNumber", 1);
            objectMessage.setStringProperty("Naziv", Naziv);
            producer.send(queueServerRadnik1, objectMessage);
            Message message = consumer.receive();
            if(message instanceof ObjectMessage && ((ObjectMessage)message).getObject() != null)
            {
                return Response.status(Response.Status.CREATED).entity("successful query 1 - Create Mesto.").build();
            }
        } 
        catch (JMSException ex)
        {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            consumer.close();
        }
        
        return Response.serverError().build();
    }
    
    @POST
    @Path("query2/{Ime}/{Email}/{Godiste}/{Pol}/{idMesta}")
    public Response createKorisnik(@PathParam("Ime") String Ime, @PathParam("Email") String Email, @PathParam("Godiste") int Godiste, @PathParam("Pol") String Pol, @PathParam("idMesta") String Mesto)
    {
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(queueRadnik1Server);
        try
        {
            ObjectMessage objectMessage = context.createObjectMessage();
            objectMessage.setIntProperty("QueryNumber", 2);
            objectMessage.setStringProperty("Ime", Ime);
            objectMessage.setStringProperty("Email", Email);
            objectMessage.setIntProperty("Godiste", Godiste);
            objectMessage.setStringProperty("Pol", Pol);
            objectMessage.setStringProperty("idMesta", Mesto);
            producer.send(queueServerRadnik1, objectMessage);
            Message message = consumer.receive();
            if(message instanceof ObjectMessage && ((ObjectMessage)message).getObject() != null)
            {
                return Response.status(Response.Status.CREATED).entity("successful query 2 - Create Korisnik.").build();
            }
        } 
        catch (JMSException ex)
        {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            consumer.close();
        }
        return Response.serverError().build();
    }
    
    @POST
    @Path("query3/{Korisnik}/{Email}")
    public Response updateEmail(@PathParam("Korisnik") String Korisnik, @PathParam("Email") String Email)
    {
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(queueRadnik1Server);
        try
        {
            ObjectMessage objectMessage = context.createObjectMessage();
            objectMessage.setIntProperty("QueryNumber", 3);
            objectMessage.setStringProperty("Korisnik", Korisnik);
            objectMessage.setStringProperty("Email", Email);
            producer.send(queueServerRadnik1, objectMessage);
            Message message = consumer.receive();
            if(message instanceof ObjectMessage && ((ObjectMessage)message).getObject() != null)
            {
                return Response.status(Response.Status.ACCEPTED).entity("successful query 3 - Update Email.").build();
            }
        } 
        catch (JMSException ex)
        {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            consumer.close();
        }
        return Response.serverError().build();
    }
    
    @POST
    @Path("query4/{Korisnik}/{Mesto}")
    public Response updateMesto(@PathParam("Korisnik") String Korisnik, @PathParam("Mesto") String Mesto)
    {
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(queueRadnik1Server);
        try
        {
            ObjectMessage objectMessage = context.createObjectMessage();
            objectMessage.setIntProperty("QueryNumber", 4);
            objectMessage.setStringProperty("Korisnik", Korisnik);
            objectMessage.setStringProperty("Mesto", Mesto);
            producer.send(queueServerRadnik1, objectMessage);
            Message message = consumer.receive();
            if(message instanceof ObjectMessage && ((ObjectMessage)message).getObject() != null)
            {
                return Response.status(Response.Status.ACCEPTED).entity("successful query 4 - Update Mesto.").build();
            }
        } 
        catch (JMSException ex)
        {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            consumer.close();
        }
        return Response.serverError().build();
    }
    
    @POST
    @Path("query5/{Naziv}")
    public Response createKategorija(@PathParam("Naziv") String Naziv)
    {
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(queueRadnik2Server);
        //return Response.ok("usao u ovo sranje", MediaType.TEXT_PLAIN).build();
        try
        {
            ObjectMessage objectMessage = context.createObjectMessage();
            objectMessage.setIntProperty("QueryNumber", 5);
            objectMessage.setStringProperty("Naziv", Naziv);
            producer.send(queueServerRadnik2, objectMessage);
            Message message = consumer.receive();
            if(message instanceof ObjectMessage && ((ObjectMessage)message).getObject() != null)
            {
                return Response.status(Response.Status.CREATED).entity("successful query 5 - Create Kategorija.").build();
            }
        } 
        catch (JMSException ex)
        {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            consumer.close();
        }
        
        return Response.serverError().build();
    }
    
    @POST
    @Path("query6/{Naziv}/{Trajanje}/{Korisnik}/{DatumPostavljanja}")
    public Response createVideo(@PathParam("Naziv") String Naziv, @PathParam("Trajanje") Double Trajanje, @PathParam("Korisnik") String Korisnik, @PathParam("DatumPostavljanja") String DatumPostavljanja )
    {
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(queueRadnik2Server);
        //return Response.ok("usao u ovo sranje", MediaType.TEXT_PLAIN).build();
        
        System.err.println("AAAAAAA");
        
        try
        {
            String naziv = Naziv;
            String datumString = DatumPostavljanja;
            
            naziv = URLDecoder.decode(Naziv, StandardCharsets.UTF_8.toString());
            datumString = URLDecoder.decode(DatumPostavljanja, StandardCharsets.UTF_8.toString());
            
            ObjectMessage objectMessage = context.createObjectMessage();
            objectMessage.setIntProperty("QueryNumber", 6);
            objectMessage.setStringProperty("Naziv", naziv);
            objectMessage.setDoubleProperty("Trajanje", Trajanje);
            objectMessage.setStringProperty("Korisnik", Korisnik);
            objectMessage.setStringProperty("DatumPostavljanja", datumString);
            producer.send(queueServerRadnik2, objectMessage);
            Message message = consumer.receive();
            if(message instanceof ObjectMessage && ((ObjectMessage)message).getObject() != null)
            {
                return Response.status(Response.Status.CREATED).entity("successful query 6 - Create Video.").build();
            }
        } 
        catch (JMSException | UnsupportedEncodingException ex)
        {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            consumer.close();
        }
        
        return Response.serverError().build();
    }
    
    @POST
    @Path("query7/{NazivOld}/{NazivNew}")
    public Response updateNaziv(@PathParam("NazivOld") String NazivOld, @PathParam("NazivNew") String NazivNew)
    {
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(queueRadnik2Server);
        //return Response.ok("usao u ovo sranje", MediaType.TEXT_PLAIN).build();
        try
        {
            ObjectMessage objectMessage = context.createObjectMessage();
            objectMessage.setIntProperty("QueryNumber", 7);
            objectMessage.setStringProperty("NazivOld", NazivOld);
            objectMessage.setStringProperty("NazivNew", NazivNew);
            producer.send(queueServerRadnik2, objectMessage);
            Message message = consumer.receive();
            if(message instanceof ObjectMessage && ((ObjectMessage)message).getObject() != null)
            {
                return Response.status(Response.Status.CREATED).entity("successful query 7 - Update Naziv.").build();
            }
        } 
        catch (JMSException ex)
        {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            consumer.close();
        }
        
        return Response.serverError().build();
    }
    
    @POST
    @Path("query8/{Video}/{Kategorija}")
    public Response updateKategorija(@PathParam("Video") String Video, @PathParam("Kategorija") String Kategorija)
    {
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(queueRadnik2Server);
        //return Response.ok("usao u ovo sranje", MediaType.TEXT_PLAIN).build();
        try
        {
            ObjectMessage objectMessage = context.createObjectMessage();
            objectMessage.setIntProperty("QueryNumber", 8);
            objectMessage.setStringProperty("Video", Video);
            objectMessage.setStringProperty("Kategorija", Kategorija);
            producer.send(queueServerRadnik2, objectMessage);
            Message message = consumer.receive();
            if(message instanceof ObjectMessage && ((ObjectMessage)message).getObject() != null)
            {
                return Response.status(Response.Status.CREATED).entity("successful query 8 - Update Kategorija.").build();
            }
        } 
        catch (JMSException ex)
        {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            consumer.close();
        }
        
        return Response.serverError().build();
    }
    
    @POST
    @Path("query16/{Video}/{Korisnik}")
    public Response deleteVideo(@PathParam("Video") String Video, @PathParam("Korisnik") String Korisnik)
    {
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(queueRadnik2Server);
        //return Response.ok("usao u ovo sranje", MediaType.TEXT_PLAIN).build();
        try
        {
            ObjectMessage objectMessage = context.createObjectMessage();
            objectMessage.setIntProperty("QueryNumber", 16);
            objectMessage.setStringProperty("Video", Video);
            objectMessage.setStringProperty("Korisnik", Korisnik);
            producer.send(queueServerRadnik2, objectMessage);
            Message message = consumer.receive();
            if(message instanceof ObjectMessage && ((ObjectMessage)message).getObject() != null)
            {
                return Response.status(Response.Status.CREATED).entity("successful query 16 - Delete Video.").build();
            }
        } 
        catch (JMSException ex)
        {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            consumer.close();
        }
        
        return Response.serverError().build();
    }
    
    @GET
    @Path("query17")
    public Response getAllKorisnik()
    {
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(queueRadnik1Server);
        try
        {
            ObjectMessage objectMessage = context.createObjectMessage();
            objectMessage.setIntProperty("QueryNumber", 17);
            producer.send(queueServerRadnik1, objectMessage);
            Message message = consumer.receive();
            if(message instanceof ObjectMessage && ((ObjectMessage)message).getObject() != null)
            {
                return Response.status(Response.Status.OK).entity(((ObjectMessage)message).getObject()).build();
            }
        } 
        catch (JMSException ex)
        {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            consumer.close();
        }
        return Response.serverError().build();
    }
    
    @GET
    @Path("query18")
    public Response getAllMesto()
    {
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(queueRadnik1Server);
        try
        {
            ObjectMessage objectMessage = context.createObjectMessage();
            objectMessage.setIntProperty("QueryNumber", 18);
            producer.send(queueServerRadnik1, objectMessage);
            Message message = consumer.receive();
            if(message instanceof ObjectMessage && ((ObjectMessage)message).getObject() != null)
            {
                return Response.status(Response.Status.OK).entity(((ObjectMessage)message).getObject()).build();
            }
        } 
        catch (JMSException ex)
        {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            consumer.close();
        }
        return Response.serverError().build();
    }
    
    @GET
    @Path("query19")
    public Response getAllKategorija()
    {
        System.err.println("USO U KAT");
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(queueRadnik2Server);
        try
        {
            ObjectMessage objectMessage = context.createObjectMessage();
            objectMessage.setIntProperty("QueryNumber", 19);
            producer.send(queueServerRadnik2, objectMessage);
            Message message = consumer.receive();
            if(message instanceof ObjectMessage && ((ObjectMessage)message).getObject() != null)
            {
                return Response.status(Response.Status.OK).entity(((ObjectMessage)message).getObject()).build();
            }
        } 
        catch (JMSException ex)
        {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            consumer.close();
        }
        return Response.serverError().build();
    }
    
    @GET
    @Path("query20")
    public Response getAllVideo()
    {
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(queueRadnik2Server);
        try
        {
            ObjectMessage objectMessage = context.createObjectMessage();
            objectMessage.setIntProperty("QueryNumber", 20);
            producer.send(queueServerRadnik2, objectMessage);
            Message message = consumer.receive();
            if(message instanceof ObjectMessage && ((ObjectMessage)message).getObject() != null)
            {
                return Response.status(Response.Status.OK).entity(((ObjectMessage)message).getObject()).build();
            }
        } 
        catch (JMSException ex)
        {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            consumer.close();
        }
        return Response.serverError().build();
    }
    
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("test")
    public Response test()
    {
        return Response.ok("sve je okej", MediaType.TEXT_PLAIN).build();
    }
    
    @GET
    @Path("empty")
    public Response empty()
    {
        Message message = null;
        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(queueServerRadnik1);
        do
        {
            message = consumer.receiveNoWait();
        }
        while(message != null);
        
        return Response.status(Response.Status.GONE).entity("successful empty queueServerRadnik1").build();
    }

}
