/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package radnik;

import java.util.Scanner;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
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
    
    @Resource (lookup = "MyTopic1")
    static Topic topic1;
    
    @Resource (lookup = "MyTopic")
    static Topic topic;
    
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("RadnikPU");
        EntityManager em = emf.createEntityManager();
        
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        
        System.out.println("Radnik je pokrenut");
        int x = 1;
        Scanner scanner = new Scanner(System.in);
        while(x != 0){
            System.out.println("1. Napravi artikal");
            System.out.println("2. Promeni cenu");
            System.out.println("0.Kraj");
            
            x = scanner.nextInt();
            String odgovor = "";
            switch(x){
                case 1:
                    System.out.println("Unesite ime artikla koji zelite da napravite");
                    odgovor = scanner.next() + " " + scanner.next();
                    System.out.println("Unesite kolicinu koju zelite da napravite");
                    int kolicina = scanner.nextInt();
                    Utility.NapraviArtikal(odgovor, kolicina, em, producer, context);
                    break;
                    
                case 2:
                    System.out.println("Unesite ime artikla koji zelite da napravite");
                    odgovor = scanner.next() + " " + scanner.next();
                    System.out.println("Unesite cenu koju zelite da postavite za trazeni artikal");
                    int cena = scanner.nextInt();
                    Utility.PromeniCenu(odgovor, cena, em, context, producer);
                    break;
                    
                case 0:
                    break;
                default: break;
            }
        }
    }
    
}
