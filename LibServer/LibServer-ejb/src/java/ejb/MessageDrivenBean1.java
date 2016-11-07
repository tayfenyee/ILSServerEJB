/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import java.util.Date;
import java.util.Random;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author USER
 */
@MessageDriven(mappedName = "jms/Topic", activationConfig = {
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
    @ActivationConfigProperty(propertyName = "subscriptionName", propertyValue = "MessageDrivenBean1"),
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/Topic"),
    @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable"),
    @ActivationConfigProperty(propertyName = "subscriptionName", propertyValue = "jms/Topic"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic")
})
public class MessageDrivenBean1 implements MessageListener {
    @PersistenceContext()
    EntityManager em;
    LibMemberEntity libmember;
    RequestEntity request;
    @Resource(mappedName = "jms/TopicConnectionFactory")
    private ConnectionFactory topicConnectionFactory;
    private Random processingTime = new Random();
    
    public MessageDrivenBean1() {
    }
    
    public void createRequest(String memberName, Date requestTime, String requestMsg, String status, String comment) {
        request = new RequestEntity(requestTime, requestMsg, status, comment);
        libmember = em.find(LibMemberEntity.class, memberName);
        request.setLibmember(libmember);
        libmember.getRequests().add(request);
        em.merge(request);
        em.merge(libmember);
    }
    
    @Override
    public void onMessage(Message inMessage) {
        MapMessage msg = null;
        try {
            if (inMessage instanceof MapMessage) {
                msg = (MapMessage) inMessage;
                Thread.sleep(processingTime.nextInt(5) * 1000);
                setUpEntities(msg);
            } 
        } catch (InterruptedException ie) {
            System.out.println(ie.toString());
        } catch (Throwable te) {
            System.out.println(te.toString());
        }
    }
    
    void setUpEntities(MapMessage msg) {
        String userName = null;
        String userMessage = null;
        Connection connection = null;
        try {
            userName = msg.getString("UserName");
            userMessage = msg.getString("UserMessage");
            
            libmember = em.find(LibMemberEntity.class, userName);
            if (libmember != null) {
                //UserExist
                Date nowTime = new Date();
                createRequest(userName, nowTime, userMessage, "unread", "");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: No entities found");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        
        try {
           connection = topicConnectionFactory.createConnection();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
