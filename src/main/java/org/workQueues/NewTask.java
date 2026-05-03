package org.workQueues;
import com.rabbitmq.client.amqp.Connection;
import com.rabbitmq.client.amqp.Environment;
import com.rabbitmq.client.amqp.Publisher;
import com.rabbitmq.client.amqp.impl.AmqpEnvironmentBuilder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

// Message Sender demonstrating Round-robin dispatching using a Task Queue.
public class NewTask {
    private static final String QUEUE_NAME = "testQueue";

    public static void main(String[] argv){
        try {
            Environment environment = new AmqpEnvironmentBuilder()
                    .connectionSettings()
                    .uri("amqp://guest:guest@localhost:5672/%2f")
                    .environmentBuilder()
                    .build();

            Connection connection = environment.connectionBuilder().build();
            connection.management().queue(QUEUE_NAME).quorum().queue().declare();

            Publisher publisher = connection.publisherBuilder().queue(QUEUE_NAME).build();
            String messageOut = String.join(" ", argv);
            CountDownLatch latch = new CountDownLatch(1);

            publisher.publish(
                    publisher.message(messageOut.getBytes(StandardCharsets.UTF_8)),
                    context -> {
                        if (context.status() == Publisher.Status.ACCEPTED) {
                            System.out.println(" [x] Sent '" + messageOut + "'");
                        }
                        latch.countDown();
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}