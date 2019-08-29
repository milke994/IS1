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
import java.text.SimpleDateFormat;
import java.util.Date;
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
    
    public static void ProveriOpseg(int i, int max, Scanner scanner){
        while (i < 0 || i >= max){
            System.out.println("Greska, unesite ponovo!");
            i = scanner.nextInt();
        }
    }
    
    public static void ProveriPromete(EntityManager em){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy' 'HH:mm");
        date.setDate(date.getDate() - 1);
        String datum = dateFormat.format(date);
        Date today = new Date();
        String danas = dateFormat.format(today);
        
        Query query = em.createQuery("select promet from Promet as promet where promet.prodavnica.naziv = :naziv and promet.datum <= :datee", Promet.class);
        List<Promet> prometi = query.setParameter("naziv", Main.StoreName).setParameter("datee", datum).getResultList();
        
        if (!prometi.isEmpty()){
            em.getTransaction().begin();
                for (Promet promet : prometi){
                    System.out.println("Resetovan promet");
                    promet.setVrednost(0);
                    promet.setDatum(danas);
                }
            em.getTransaction().commit();
        }
    }
    
    public static void IstekleRegistracije(EntityManager em){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy' 'HH:mm");
        date.setDate(date.getDate() - 2);
        String datum = dateFormat.format(date);
        
        Query query = em.createQuery("select rezervacija from Rezervacije as rezervacija where rezervacija.datum <= :datee and rezervacija.sifprodavnice.naziv = :prodavnica", Rezervacije.class);
        List<Rezervacije> rezervacije = query.setParameter("datee", datum).setParameter("prodavnica", Main.StoreName).getResultList();
        
        if (!rezervacije.isEmpty()){
            em.getTransaction().begin();
                for (Rezervacije rezervacija : rezervacije){
                    System.out.println("Obrisana rezervacija za artikal po imenu " + rezervacija.getSifartikla().getNaziv() + " za korisnika " + rezervacija.getSifrakorinsik().getIme());
                    em.remove(rezervacija);
                }
            em.getTransaction().commit();
        }
    }
    
    public static void RezervisanArtikal(String ime, String prezime, String jmbg, String nazivArtikla, EntityManager em){
        Query query = em.createQuery("select rezervacija from Rezervacije as rezervacija where rezervacija.sifrakorinsik.jmbg = :jmbg" , Rezervacije.class);
        List<Rezervacije> rezervacije = query.setParameter("jmbg", jmbg).getResultList();
        if (rezervacije.isEmpty()){
            System.out.println("Ne postoji rezervacija na to ime!");
        }
        else{
            int i = 0;
            for (Rezervacije rezervacija : rezervacije){
                Query query1 = em.createQuery("select rezervacija from Rezervacije as rezervacija where rezervacija.sifrakorinsik.jmbg = :jmbg and rezervacija.sifartikla.naziv = :naziv or rezervacija.sifartikla.tip = :tip", Rezervacije.class); 
                List<Rezervacije> rezervacije1 = query1.setParameter("jmbg", jmbg).setParameter("naziv", nazivArtikla).setParameter("tip", rezervacija.getSifartikla().getTip()).getResultList();
                if (rezervacije1.isEmpty()){
                    System.out.println("Rezervacija tog artikla ne postoji!");
                }
                else{
                    Rezervacije rezervacijaa = rezervacije1.get(i);
                    em.getTransaction().begin();
                        Query q = em.createQuery("select promet from Promet as promet where promet.prodavnica = :prodavnica", Promet.class);
                        List<Promet> prometi = q.setParameter("prodavnica", rezervacijaa.getSifprodavnice()).getResultList();
                        if (prometi.isEmpty()){
                            Promet promet = new Promet(rezervacijaa.getSifprodavnice().getNaziv());
                            promet.setVrednost(0);
                            Date date = new Date();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy ' 'HH:mm");
                            String datum = dateFormat.format(date);
                            promet.setDatum(datum);
                            em.persist(promet);
                            rezervacijaa.getSifprodavnice().setPromet(promet);
                        }
                        Integer promet = rezervacijaa.getSifprodavnice().getPromet().getVrednost() + rezervacijaa.getKolicina() * rezervacijaa.getSifartikla().getCena();
                        rezervacijaa.getSifprodavnice().getPromet().setVrednost(promet);
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
            ProveriOpseg(index, i, scanner);
            String prodavnica =  stanja.get(index).getProdavnica().getNaziv();
            ProveriStanje(nazivArtikla, context, producer, consumer, prodavnica);
        }
            System.out.println("Zelite li da rezervisete artikal:");
            odgovor = scanner.next();
            if("da".equals(odgovor)){
                System.out.println("Unesite broj artikla koji zelite da rezervisete:");
                int index = scanner.nextInt();
                ProveriOpseg(index, i, scanner);
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
    
    public static void PretraziPoImenu(String nazivArtikla, EntityManager em, JMSConsumer consumer, JMSProducer producer, JMSContext context, Scanner scanner){
        Query query = em.createQuery("select stanje from Stanje as stanje where stanje.artikal.naziv = :nazivArtikla and stanje.prodavnica.naziv = :naziv and stanje.kolicina > 0", Stanje.class);
        List<Stanje> stanja = query.setParameter("nazivArtikla", nazivArtikla).setParameter("naziv", Main.StoreName).getResultList();
        if(stanja.isEmpty()){
            System.out.println("Trenutno nemamo na stanju artikal sa tim nazivom! Zelite li da proverimo u drugim prodavnicama?");
            String odgovor = scanner.next();
            if("da".equals(odgovor)){
                PretraziUDrugimProdavnicama(nazivArtikla, em, context, consumer, producer, scanner);
            }
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
                ProveriOpseg(br, i, scanner);
                ProveriStanje(stanja.get(br).getArtikal().getNaziv(), context, producer, consumer, Main.StoreName);
            }
            
            System.out.println("Zelite li da kupite artikal?");
            odgovor = scanner.next();
            if("da".equals(odgovor)){
                System.out.println("Unesite broj artikla iz liste koji zelite da kupite");
                br = scanner.nextInt();
                ProveriOpseg(br, i, scanner);
                System.out.println("Unesite kolicinu koju zelite da kupite");
                int kolicina = scanner.nextInt();
                if (kolicina > stanja.get(br).getKolicina()) {
                    System.out.println("Nemamo dovoljno na stanju! Zelite li da proverimo u drugim prodavnicama?");
                    odgovor = scanner.next();
                    if ("da".equals(odgovor)){
                        PretraziUDrugimProdavnicama(nazivArtikla, em, context, consumer, producer, scanner);
                    }
                    return;
                }
                Query query1 = em.createQuery("select promet from Promet as promet where promet.prodavnica.naziv = :naziv", Promet.class);
                List<Promet> prometi = query1.setParameter("naziv", Main.StoreName).getResultList();
                if (prometi.isEmpty()){
                    Promet promet1 = new Promet(Main.StoreName);
                    promet1.setVrednost(0);
                    Date date = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy ' 'HH:mm");
                    String datum = dateFormat.format(date);
                    promet1.setDatum(datum);
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
                ProveriOpseg(br, i, scanner);
                ProveriStanje(stanja.get(br).getArtikal().getNaziv(), context, producer, consumer, Main.StoreName);
            }
            
            System.out.println("Zelite li da kupite odredjeni artikal?");
            odgovor = scanner.next();
            if ("da".equals(odgovor)){
                System.out.println("Unesite broj artikla iz liste koji zelite da kupite");
                int br = scanner.nextInt();
                ProveriOpseg(br, i, scanner);
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
                    Date date = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy ' 'HH:mm");
                    String datum = dateFormat.format(date);
                    promet1.setDatum(datum);
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
