/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prodavnica1;

import entiteti.Prodavnica;
import entiteti.Promet;
import java.util.Scanner;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Aleksandar
 */
public class Main {

    @Resource (lookup = "jms/__defaultConnectionFactory")
    static ConnectionFactory connectionFactory;
    
    @Resource (lookup = "MyTopic")
    static Topic topic;
    
    static final String StoreName = "WinWin";
    
    static final String idString = "id" + StoreName;
    
    static final String menadzer = "menadzer" + StoreName;
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Prodavnica1PU");
        EntityManager em = emf.createEntityManager();
        
        JMSContext context = connectionFactory.createContext();
        context.setClientID(idString);
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createDurableConsumer(topic, StoreName, "NazivProdavnice = '" + StoreName + "'", false);
        
        Scanner scanner = new Scanner(System.in);
        int x = 1;
        
        while(x != 0){
            
            Utility.IstekleRegistracije(em);
            Utility.ProveriPromete(em);
            
            System.out.println("1. Rezervisan artikal");
            System.out.println("2. Pretrazi po imenu artikla");
            System.out.println("3. Pretrazi po tipu artikla");
            System.out.println("0. Zavrsi");
            x = scanner.nextInt();
            
            switch(x){
                case 1:
                    System.out.println("Unesite vase podatke i naziv artikla koji ste rezervisali:");
                    System.out.println("Vase ime: ");
                    String ime = scanner.next();
                    System.out.println("Vase prezime: ");
                    String prezime = scanner.next();
                    System.out.println("JMBG: ");
                    String jmbg = scanner.next();
                    System.out.println("Tip koji ste rezervisali: ");
                    String nazivArtikla = scanner.next();
                    
                    Utility.RezervisanArtikal(ime, prezime, jmbg, nazivArtikla, em);
                    break;
                    
                case 2:
                    System.out.println("Unesite naziv artikla koji zelite da pretrazite: ");
                    nazivArtikla = scanner.next() + " " + scanner.next();
                    
                    Utility.PretraziPoImenu(nazivArtikla, em, consumer, producer, context, scanner);
                    break;
                    
                case 3:
                    System.out.println("Unesite tip artikla koji zelite da pretrazite");
                    String tipArtikla = scanner.next();
                    
                    Utility.PretraziPoTipu(tipArtikla, em, scanner, context, producer, consumer);
                    break;
                case 0:
                    x = 0;
                    break;
                    
                default:
                    break;
            }
        }
    }
    
}
