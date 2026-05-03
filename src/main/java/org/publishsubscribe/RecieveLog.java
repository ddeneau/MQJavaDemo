package org.publishsubscribe;

import com.rabbitmq.client.*;
import java.time.Clock;

public class RecieveLog {
 private static final String EXCHANGE_NAME = "logs";
 private static  String exchange_type = "fanout";

 public static void main(String[] argv) {
     try {
         ConnectionFactory factory = new ConnectionFactory();
         factory.setHost("localhost");
         Connection connection = factory.newConnection();
         Channel channel = connection.createChannel();

         channel.exchangeDeclare(EXCHANGE_NAME, exchange_type);
         String queueName = channel.queueDeclare().getQueue();
         channel.queueBind(queueName, EXCHANGE_NAME, "");

         System.out.println(" [*] Waiting for messages. ");

         DeliverCallback deliverCallback = (consumerTag, delivery) -> {
             String message = new String(delivery.getBody(), "UTF-8");
             System.out.println(" Received " + message + " " + Clock.systemUTC().toString());
         };
         channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
     } catch (Exception e) {
         e.printStackTrace();
         throw new RuntimeException(e);
     }

 }

}
