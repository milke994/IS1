/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menadzeris1;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.Topic;

/**
 *
 * @author Aleksandar
 */
public class Main {

    @Resource(lookup = "jms/__defaultConnectionFactory")
    static ConnectionFactory connectionFactory;
    
    @Resource(lookup = "MyTopic")
    static Topic topic;
    
    @Resource(lookup = "MyTopic1")
    static Topic topic1;
    
    static int id = 1;
    static int id2 = 10;
    
    static String idString = "id" + id;
    static String idString2 = "id" + id2;
    
    static String StoreName = "Tehnomanija";
    
    static String menadzer = "menadzer" + StoreName;
    static String menadzer2 = "menadzer2" + StoreName;
    
    
    public static void main(String[] args) {
        ProdavnicaMenadzer pm = new ProdavnicaMenadzer();
        pm.start();
        
        RadnikMenadzer rm = new RadnikMenadzer();
        rm.start();
    }
    
}
