# Messaging Demo

Using Java to learn JMS and RabbitMQ.

# Demo Steps:
1. Make sure Java is installed:
2. Make sure Docker is installed/running:
3. Make sure Maven is installed:
4. Clone the repository.
5. Follow this part:
   docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:4-management
of the RabbitMQ tutorial (run in powershell on windows)
6. Run: mvn -q compile exec:java "-Dexec.mainClass=org.workQueues.Worker" in the root folder.

   
# Sources, Tutorials, and Docs:
-  https://www.rabbitmq.com/tutorials
- https://www.rabbitmq.com/docs
