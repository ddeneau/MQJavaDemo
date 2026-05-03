package org.workQueues;

import com.rabbitmq.client.amqp.Connection;
import com.rabbitmq.client.amqp.Consumer;
import com.rabbitmq.client.amqp.Environment;
import com.rabbitmq.client.amqp.impl.AmqpEnvironmentBuilder;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

// Message receiver demonstrating Round-robin dispatching using a Task Queue.
public class Worker {

    private static final String QUEUE_NAME = "testQueue";
    public static void main(String[] argv) {
        try {
            Environment environment = new AmqpEnvironmentBuilder()
                    .connectionSettings()
                    .uri("amqp://guest:guest@localhost:5672/%2f")
                    .environmentBuilder()
                    .build();
            Connection connection = environment.connectionBuilder().build();

            connection.management().queue(QUEUE_NAME).quorum().queue().declare();
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            // The tutorial says to write it like this even though the variable is unused for now...probably for later.
            Consumer consumer = connection.consumerBuilder()
                    .queue(QUEUE_NAME)
                    .messageHandler((context, message) -> {
                        String text = new String(message.body(), StandardCharsets.UTF_8);
                        System.out.println(" [x] Received '" + text + "'");
                        try {
                            doWork(text);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        finally {
                            System.out.println(" [x] Done");
                            context.accept();
                        }
                    })
                    .build();

            new CountDownLatch(1).await();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void doWork(String task) throws InterruptedException {
        for (char ch: task.toCharArray()) {
            if (ch == '.') Thread.sleep(1000);
        }
    }


}
