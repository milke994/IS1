/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package radnik;

import entiteti.Artikal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import poruke.PorukaZaArtikal;
import poruke.PorukaZaCenu;

/**
 *
 * @author Aleksandar
 */
public class Utility {
    public static String OdrediProdavnicu(){
        String prodavnica = "";
            int br = (int) (Math.random() * 3 + 1);
            switch(br){
                case 1:
                    prodavnica = "WinWin";
                    break;
                case 2:
                    prodavnica = "Gigatron";
                    break;
                case 3:
                    prodavnica = "Tehnomanija";
                    break;
            }
        return prodavnica;
    }
    
    public static void NapraviArtikal(String naziv, int kolicina, EntityManager em, JMSProducer producer, JMSContext context){
        Query query = em.createQuery("select artikal from Artikal as artikal where artikal.naziv = :naziv", Artikal.class);
        List<Artikal> artikli = query.setParameter("naziv", naziv).getResultList();
        if(!artikli.isEmpty()){
            try {
                PorukaZaArtikal porukaZaArtikal = new PorukaZaArtikal(naziv, kolicina);
                String prodavnica = OdrediProdavnicu();
                ObjectMessage message = context.createObjectMessage(porukaZaArtikal);
                String id = "menadzer" + prodavnica;
                message.setStringProperty("menadzer", id);
                System.out.println("Artikal se pravi..");
                long timetosleep = artikli.get(0).getVreme() * 1000;
                Thread.sleep(timetosleep);
                producer.send(Main.topic, message);
                System.out.println("Poslat artikal " + naziv + " u kolicini " + kolicina + " u prodavnicu " + prodavnica);
            } catch (JMSException ex) {
                Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            System.out.println("Artikal tog imena se ne moze napraviti!");
        }
    }
    
    public static void PromeniCenu(String naziv, int cena, EntityManager em, JMSContext context, JMSProducer producer){
        Query query = em.createQuery("select artikal from Artikal as artikal where artikal.naziv = :naziv", Artikal.class);
        List<Artikal> artikli = query.setParameter("naziv", naziv).getResultList();
        if(!artikli.isEmpty()){
            em.getTransaction().begin();
                artikli.get(0).setCena(cena);
            em.getTransaction().commit();
            PorukaZaCenu porukaZaCenu = new PorukaZaCenu(naziv, cena);
            ObjectMessage objectMessage = context.createObjectMessage(porukaZaCenu);
            producer.send(Main.topic1, objectMessage);
            System.out.println("Cena je promenjena za artikal " + naziv + " i poslata svim prodavnicama");
        }
        else{
            System.out.println("Artikal sa tim imenom ne postoji u bazi!");
        }
    }
    
    
}
