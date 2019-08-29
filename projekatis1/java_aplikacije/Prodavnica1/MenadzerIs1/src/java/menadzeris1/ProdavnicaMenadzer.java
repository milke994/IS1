/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menadzeris1;

import entiteti.Artikal;
import entiteti.Korisnik;
import entiteti.Prodavnica;
import entiteti.Rezervacije;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import poruke.PorukaZaRezervaciju;
import poruke.PorukaZaStanje;

/**
 *
 * @author Aleksandar
 */
public class ProdavnicaMenadzer extends Thread {
    public void run(){
        System.out.println("Startovana je prva nit");
        System.out.println(Main.menadzer);
        
        JMSContext context = Main.connectionFactory.createContext();
        context.setClientID(Main.idString);
        JMSConsumer consumer = context.createDurableConsumer(Main.topic, Main.idString, "menadzer = '" + Main.menadzer + "'", false);
        JMSProducer producer = context.createProducer();
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MenadzerIs1PU");
        EntityManager em = emf.createEntityManager();
        
        while(true){
            System.out.println("Ceka se zahtev...");
            Message message = consumer.receive();
            if (message instanceof ObjectMessage){
                try {
                    if (((ObjectMessage) message).getObject() instanceof PorukaZaStanje){
                        ObjectMessage objectMessage = (ObjectMessage) message;
                        int br = (int) (Math.random()*1 + 100);
                        PorukaZaStanje porukaZaStanje = (PorukaZaStanje) objectMessage.getObject();
                        if (br <= 70) porukaZaStanje.setStanje("Neotpakovan");
                        else porukaZaStanje.setStanje("Otpakovan");
                        System.out.println("Poslato je stanje za artikal " + porukaZaStanje.getNazivArtikla() + " a stanje je " + porukaZaStanje.getStanje());
                        ObjectMessage objectMessage1 = context.createObjectMessage();
                        objectMessage1.setObject(porukaZaStanje);
                        objectMessage1.setStringProperty("NazivProdavnice", porukaZaStanje.getNazivProdavnice());
                        producer.send(Main.topic, objectMessage1);
                    }
                    
                } catch (JMSException ex) {
                    Logger.getLogger(ProdavnicaMenadzer.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            
            if (message instanceof ObjectMessage){
                try {
                    if (((ObjectMessage) message).getObject() instanceof PorukaZaRezervaciju){
                        ObjectMessage objectMessage = (ObjectMessage) message;
                        PorukaZaRezervaciju porukaZaRezervaciju = (PorukaZaRezervaciju) objectMessage.getObject();
                        Query query = em.createQuery("select korisnik from Korisnik as korisnik where korisnik.jmbg = :jmbg", Korisnik.class);
                        List<Korisnik> korisnici = query.setParameter("jmbg", porukaZaRezervaciju.getJMBG()).getResultList();
                        int bool = 0;
                        if (korisnici.isEmpty()){
                            em.getTransaction().begin();
                            Korisnik korisnik = new Korisnik();
                            korisnik.setIme(porukaZaRezervaciju.getIme());
                            korisnik.setJmbg(porukaZaRezervaciju.getJMBG());
                            korisnik.setPrezime(porukaZaRezervaciju.getPrezime());
                            em.persist(korisnik);
                            em.getTransaction().commit();
                        }
                        
                        Query q1 = em.createQuery("select korisnik from Korisnik as korisnik where korisnik.jmbg = :jmbg", Korisnik.class);
                        List<Korisnik> korisnicii = query.setParameter("jmbg", porukaZaRezervaciju.getJMBG()).getResultList();
                        
                        em.getTransaction().begin();
                        Rezervacije rezervacija = new Rezervacije();
                        rezervacija.setKolicina(porukaZaRezervaciju.getKolicina());
                        rezervacija.setSifrakorinsik(korisnicii.get(0));
                        Query q = em.createQuery("select artikal from Artikal as artikal where artikal.naziv = :naziv", Artikal.class);
                        List<Artikal> artikli = q.setParameter("naziv", porukaZaRezervaciju.getNazivArtikla()).getResultList();
                        rezervacija.setSifartikla(artikli.get(0));
                        Prodavnica prodavnica = em.find(Prodavnica.class, Main.StoreName);
                        rezervacija.setSifprodavnice(prodavnica);
                        Date date = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy' 'HH:mm");
                        String datum = dateFormat.format(date);
                        rezervacija.setDatum(datum);
                        em.persist(rezervacija);
                        em.getTransaction().commit();
                        TextMessage textMessage = context.createTextMessage("Rezervacija obradjena");
                        textMessage.setStringProperty("NazivProdavnice", porukaZaRezervaciju.getPosiljalac());
                        producer.send(Main.topic, textMessage);
                    }
                } catch (JMSException ex) {
                    Logger.getLogger(ProdavnicaMenadzer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
