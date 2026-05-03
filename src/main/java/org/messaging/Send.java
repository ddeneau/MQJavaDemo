package org.messaging;
import com.rabbitmq.client.amqp.Connection;
import com.rabbitmq.client.amqp.Environment;
import com.rabbitmq.client.amqp.Publisher;
import com.rabbitmq.client.amqp.impl.AmqpEnvironmentBuilder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

public class Send implements Runnable {
    private static final String QUEUE_NAME = "testQueue";
    private StringBuffer messageSB;

    public Send() {
    }

    public Send(StringBuffer messageSB) {
        this.messageSB = messageSB;
    }

    @Override
    public void run() {
        try {
            Environment environment = new AmqpEnvironmentBuilder()
                    .connectionSettings()
                    .uri("amqp://guest:guest@localhost:5672/%2f")
                    .environmentBuilder()
                    .build();

            Connection connection = environment.connectionBuilder().build();
            connection.management().queue(QUEUE_NAME).quorum().queue().declare();

            Publisher publisher = connection.publisherBuilder().queue(QUEUE_NAME).build();
            CountDownLatch latch = new CountDownLatch(1);

            String message = String.valueOf(messageSB);

            publisher.publish(
                    publisher.message(message.getBytes(StandardCharsets.UTF_8)),
                    context -> {
                        if (context.status() == Publisher.Status.ACCEPTED) {
                            System.out.println(" [x] Sent '" + message + "'");
                        }
                        latch.countDown();
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}