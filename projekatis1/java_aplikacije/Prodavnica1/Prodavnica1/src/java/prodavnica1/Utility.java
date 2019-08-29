/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prodavnica1;

import entiteti.Korisnik;
import entiteti.Prodavnica;
import entiteti.Promet;
import entiteti.Rezervacije;
import entiteti.Stanje;
import entiteti.StanjePK;
import java.util.List;
import java.util.Scanner;
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
import javax.persistence.LockModeType;
import javax.persistence.Query;
import poruke.PorukaZaRezervaciju;
import poruke.PorukaZaStanje;

/**
 *
 * @author Aleksandar
 */
public class Utility {
    public static void RezervisanArtikal(String ime, String prezime, String jmbg, String nazivArtikla, EntityManager em){
        Query query = em.createQuery("select rezervacija from Rezervacije as rezervacija where rezervacija.sifrakorinsik.jmbg = :jmbg" , Rezervacije.class);
        List<Rezervacije> rezervacije = query.setParameter("jmbg", jmbg).getResultList();
        if (rezervacije.isEmpty()){
            System.out.println("Ne postoji rezervacija na to ime!");
        }
        else{
            int i = 0;
            for (Rezervacije rezervacija : rezervacije){
                Query query1 = em.createQuery("select rezervacija from Rezervacije as rezervacija where rezervacija.sifrakorinsik.jmbg = :jmbg and rezervacija.sifartikla.naziv = :naziv", Rezervacije.class); 
                List<Rezervacije> rezervacije1 = query.setParameter("jmbg", jmbg).setParameter("naziv", nazivArtikla).getResultList();
                if (rezervacije1.isEmpty()){
                    System.out.println("Rezervacija tog artikla ne postoji!");
                }
                else{
                    Rezervacije rezervacijaa = rezervacije1.get(i);
                    em.getTransaction().begin();
                        Prodavnica prodavnica = em.find(Prodavnica.class, Main.StoreName);
                        Integer promet = prodavnica.getPromet().getVrednost() + rezervacijaa.getKolicina() * rezervacijaa.getSifartikla().getCena();
                        prodavnica.getPromet().setVrednost(promet);
                        em.remove(rezervacijaa);
                    em.getTransaction().commit();
                    System.out.println("Rezervacija evidentirana");
                }
            }
        }
    }
    
    public static void ProveriStanje(String naziv, JMSContext context, JMSProducer producer, JMSConsumer consumer, String prodavnica){
        try {
                    PorukaZaStanje porukaZaStanje = new PorukaZaStanje(naziv, Main.StoreName);
                    ObjectMessage objectMessage = context.createObjectMessage(porukaZaStanje);
                    String menadzer = "menadzer" + prodavnica;
                    objectMessage.setStringProperty("menadzer", menadzer);
                    producer.send(Main.topic, objectMessage);
                    System.out.println("Ceka se odgovor...");
                    Message message = consumer.receive();
                    if (message instanceof ObjectMessage){
                        ObjectMessage received = (ObjectMessage) message;
                        PorukaZaStanje stanje = (PorukaZaStanje) received.getObject();
                        System.out.println("Stanje Vaseg objekta je " + stanje.getStanje());
                    }
                } catch (JMSException ex) {
                    Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, ex);
                }
    }
    
    public static void PosaljiRezervaciju(PorukaZaRezervaciju porukaZaRezervaciju, String menadzer, JMSContext context, JMSConsumer consumer, JMSProducer producer){
        try {
            ObjectMessage objectMessage = context.createObjectMessage(porukaZaRezervaciju);
            objectMessage.setStringProperty("menadzer", menadzer);
            producer.send(Main.topic, objectMessage);
            System.out.println("Ceka se odgovor...");
            Message message = consumer.receive();
            if (message instanceof TextMessage){
               TextMessage textMessage = (TextMessage) message;
               System.out.println(textMessage.getText());
            }
        } catch (JMSException ex) {
            Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void PretraziUDrugimProdavnicama(String nazivArtikla, EntityManager em, JMSContext context, JMSConsumer consumer, JMSProducer producer, Scanner scanner){
        Query query = em.createQuery("select stanje from Stanje as stanje where stanje.artikal.naziv = :nazivArtikla or stanje.artikal.tip = :nazivArtikla and stanje.kolicina > 0", Stanje.class);
        List<Stanje> stanja = query.setParameter("nazivArtikla", nazivArtikla).getResultList();
        int i = 0;
        for (Stanje stanje : stanja){
            System.out.println(i + ". Prodavnica " + stanje.getProdavnica().getNaziv() + " ima artikal po imenu " + stanje.getArtikal().getNaziv() + " u kolicini " + stanje.getKolicina());
            i++;
        }
        System.out.println("Zelite li da proverite stanje nekog artikla?");
        String odgovor = scanner.next();
        if ("da".equals(odgovor)){
            System.out.println("Unesite broj artikla za koji zelite da proverite stanje:");
            int index = scanner.nextInt();
            String prodavnica =  stanja.get(index).getProdavnica().getNaziv();
            ProveriStanje(nazivArtikla, context, producer, consumer, prodavnica);
            System.out.println("Zelite li da rezervisete artikal:");
            odgovor = scanner.next();
            if("da".equals(odgovor)){
                System.out.println("Unesite broj artikla koji zelite da rezervisete:");
                index = scanner.nextInt();
                System.out.println("Unesite kolicinu koju zelite da rezervisete:");
                int kolicina = scanner.nextInt();
                if (kolicina < stanja.get(index).getKolicina()){
                    System.out.println("Vase ime :");
                    String ime = scanner.next();
                    System.out.println("Vase prezime:");
                    String prezime = scanner.next();
                    System.out.println("Vas jmbg:");
                    String jmbg = scanner.next();
                    String naziv = stanja.get(index).getArtikal().getNaziv();
                    PorukaZaRezervaciju porukaZaRezervaciju = new PorukaZaRezervaciju(naziv, kolicina, ime, prezime, jmbg, Main.StoreName);
                    String menadzer = "menadzer" + stanja.get(index).getProdavnica().getNaziv();
                    PosaljiRezervaciju(porukaZaRezervaciju, menadzer, context, consumer, producer);
                }
                else{
                    System.out.println("Nema dovoljne kolicine!");
                }
            }
        }
        
    }
    
    public static void PretraziPoImenu(String nazivArtikla, EntityManager em, JMSConsumer consumer, JMSProducer producer, JMSContext context, Scanner scanner){
        Query query = em.createQuery("select stanje from Stanje as stanje where stanje.artikal.naziv = :nazivArtikla and stanje.prodavnica.naziv = :naziv and stanje.kolicina > 0", Stanje.class);
        List<Stanje> stanja = query.setParameter("nazivArtikla", nazivArtikla).setParameter("naziv", Main.StoreName).getResultList();
        if(stanja.isEmpty()){
            System.out.println("Trenutno nemamo na stanju artikal sa tim nazivom!");
        }
        else{
            System.out.println("Trenutno na stanju imamo:");
            int i = 0;
            for(Stanje stanje : stanja){
                System.out.println(i + ". " + stanje.getArtikal().getNaziv() + " u kolicini : " + stanje.getKolicina());
                i++;
            }
            System.out.println("Zelite li da proverite stanje odredjenog artikla? ");
            String odgovor = scanner.next();
            int br;
            if("da".equals(odgovor)){
                System.out.println("Unesite broj artikla iz liste za koji zelite da proverite stanje");
                br = scanner.nextInt();
                ProveriStanje(stanja.get(br).getArtikal().getNaziv(), context, producer, consumer, Main.StoreName);
            }
            
            System.out.println("Zelite li da kupite artikal?");
            odgovor = scanner.next();
            if("da".equals(odgovor)){
                System.out.println("Unesite broj artikla iz liste koji zelite da kupite");
                br = scanner.nextInt();
                System.out.println("Unesite kolicinu koju zelite da kupite");
                int kolicina = scanner.nextInt();
                if (kolicina > stanja.get(br).getKolicina()) {
                    System.out.println("Nemamo dovoljno na stanju!");
                    return;
                }
                Query query1 = em.createQuery("select promet from Promet as promet where promet.prodavnica.naziv = :naziv", Promet.class);
                List<Promet> prometi = query1.setParameter("naziv", Main.StoreName).getResultList();
                if (prometi.isEmpty()){
                    Promet promet1 = new Promet(Main.StoreName);
                    promet1.setVrednost(0);
                    stanja.get(br).getProdavnica().setPromet(promet1);
                    em.persist(promet1);
                    
                }
                em.getTransaction().begin();
                    em.lock(stanja.get(br), LockModeType.PESSIMISTIC_WRITE);
                    stanja.get(br).setKolicina(stanja.get(br).getKolicina() - kolicina);
                    stanja.get(br).getProdavnica().getPromet().setVrednost(stanja.get(br).getProdavnica().getPromet().getVrednost() + kolicina * stanja.get(br).getArtikal().getCena());
                em.getTransaction().commit();
            }
        }
    }
    
    public static void PretraziPoTipu(String tip, EntityManager em, Scanner scanner, JMSContext context, JMSProducer producer, JMSConsumer consumer){
        Query query = em.createQuery("select stanje from Stanje as stanje where stanje.artikal.tip = :tip and stanje.prodavnica.naziv = :naziv and stanje.kolicina > 0", Stanje.class);
        List<Stanje> stanja = query.setParameter("tip", tip).setParameter("naziv", Main.StoreName).getResultList();
        if (stanja.isEmpty()){
            System.out.println("Trenutno na stanju nema artikala trazenog tipa, zelite li da proverimo u drugim prodavnicama?");
            String s = scanner.next();
            if ("da".equals(s)){
                PretraziUDrugimProdavnicama(tip, em, context, consumer, producer ,scanner);
            }
        }
        else{
            System.out.println("Na stanju imamo:");
            int i = 0;
            for (Stanje stanje : stanja){
                System.out.println(i + ". " +stanje.getArtikal().getNaziv() +" u kolicini : " + stanje.getKolicina() );
                i++;
            }
            System.out.println("Zelite li da proverite stanje odredjenog artikla? ");
            String odgovor = scanner.next();
            if("da".equals(odgovor)){
                System.out.println("Unesite broj artikla iz liste za koji zelite da proverite stanje");
                int br = scanner.nextInt();
                ProveriStanje(stanja.get(br).getArtikal().getNaziv(), context, producer, consumer, Main.StoreName);
            }
            
            System.out.println("Zelite li da kupite odredjeni artikal?");
            odgovor = scanner.next();
            if ("da".equals(odgovor)){
                System.out.println("Unesite broj artikla iz liste koji zelite da kupite");
                int br = scanner.nextInt();
                System.out.println("Unesite kolicinu koju zelite da kupite");
                int kolicina = scanner.nextInt();
                if (kolicina > stanja.get(br).getKolicina()) {
                    System.out.println("Nemamo dovoljno na stanju!");
                    return;
                }
                Query query1 = em.createQuery("select promet from Promet as promet where promet.prodavnica.naziv = :naziv", Promet.class);
                List<Promet> prometi = query1.setParameter("naziv", Main.StoreName).getResultList();
                if (prometi.isEmpty()){
                    Promet promet1 = new Promet(Main.StoreName);
                    promet1.setVrednost(0);
                    stanja.get(br).getProdavnica().setPromet(promet1);
                    em.persist(promet1);
                }
                em.getTransaction().begin();
                    em.lock(stanja.get(br), LockModeType.PESSIMISTIC_WRITE);
                    stanja.get(br).setKolicina(stanja.get(br).getKolicina() - kolicina);
                    stanja.get(br).getProdavnica().getPromet().setVrednost(stanja.get(br).getProdavnica().getPromet().getVrednost() + kolicina * stanja.get(br).getArtikal().getCena());
                em.getTransaction().commit();
            }
        }
    }
}
