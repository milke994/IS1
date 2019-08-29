/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menadzeris1;

import entiteti.Artikal;
import entiteti.Prodavnica;
import entiteti.Stanje;
import entiteti.StanjePK;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.persistence.Persistence;
import javax.persistence.Query;
import poruke.PorukaZaArtikal;
import poruke.PorukaZaCenu;

/**
 *
 * @author Aleksandar
 */
public class RadnikMenadzer extends Thread{
    public void run(){
        System.out.println("Startovana je druga nit");
        
        JMSContext context = Main.connectionFactory.createContext();
        context.setClientID(Main.idString2);
        JMSConsumer consumer = context.createDurableConsumer(Main.topic1, Main.idString2);
        JMSProducer producer = context.createProducer();
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MenadzerIs1PU");
        EntityManager em = emf.createEntityManager();
        
        while(true){
            System.out.println("Ceka se posiljka..");
            Message message = consumer.receive();
            
            if (message instanceof ObjectMessage){
                try {
                    if (((ObjectMessage) message).getObject() instanceof PorukaZaCenu){
                        ObjectMessage objectMessage = (ObjectMessage) message;
                        PorukaZaCenu porukaZaCenu = (PorukaZaCenu) objectMessage.getObject();
                        Query query = em.createQuery("select artikal from Artikal as artikal where artikal.naziv = :naziv", Artikal.class);
                        List<Artikal> artikli = query.setParameter("naziv", porukaZaCenu.getNazivArtikla()).getResultList();
                        if (!artikli.isEmpty()){
                            em.getTransaction().begin();
                                em.lock(artikli.get(0), LockModeType.PESSIMISTIC_WRITE);
                                artikli.get(0).setCena(porukaZaCenu.getCena());
                            em.getTransaction().commit();
                            System.out.println("Promenjena je cena za artikal " + porukaZaCenu.getNazivArtikla() + " na vrednost " + porukaZaCenu.getCena());
                        }
                    }
                } catch (JMSException ex) {
                    Logger.getLogger(RadnikMenadzer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
