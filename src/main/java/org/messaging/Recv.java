package org.messaging;

import com.rabbitmq.client.amqp.Connection;
import com.rabbitmq.client.amqp.Consumer;
import com.rabbitmq.client.amqp.Environment;
import com.rabbitmq.client.amqp.impl.AmqpEnvironmentBuilder;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

import java.time.Instant;

public class Recv implements Runnable{

    private static final String QUEUE_NAME = "testQueue";
    private static final long SIMULATED_RECIEVE_TIME = 500L;

    public Recv(){

    }

    @Override
    public void run()  {
        try {
            Environment environment = new AmqpEnvironmentBuilder()
                    .connectionSettings()
                    .uri("amqp://guest:guest@localhost:5672/%2f")
                    .environmentBuilder()
                    .build();
            Connection connection = environment.connectionBuilder().build();

            connection.management().queue(QUEUE_NAME).quorum().queue().declare();
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            Consumer consumer = connection.consumerBuilder()
                    .queue(QUEUE_NAME)
                    .messageHandler((context, message) -> {
                        String text = new String(message.body(), StandardCharsets.UTF_8);

                        System.out.println("Receiving..." + Instant.now().toString());
                        try {
                            Thread.sleep(SIMULATED_RECIEVE_TIME);
                        } catch (Exception e){
                            e.printStackTrace();
                        }

                        System.out.println(" [x] Received '" + text + "'" + Instant.now().toString());
                        context.accept();
                    })
                    .build();

            new CountDownLatch(1).await();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}