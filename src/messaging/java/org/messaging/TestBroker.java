package org.messaging;

public class TestBroker {
    static Recv recv;
    static Send sender;
    static final int MAX_MESSAGES = 10;
    static final long WAIT_TIME = 1000L;

    public static void main(String[] argv) {
        int messagesSent = 0;
        recv = new Recv();
        sender = new Send();
        Thread recvThread = new Thread(recv);

        recvThread.start();

        while(messagesSent <= MAX_MESSAGES) {

            StringBuffer sb = new StringBuffer("Message" + Integer.toString(messagesSent));
            Thread senderThread = new Thread(new Send(sb));
            senderThread.start();

            try {
                Thread.sleep(WAIT_TIME);
            } catch (Exception e) {
                e.printStackTrace();
            }
            messagesSent++;
        }

        System.out.println("Demo Over");
    }
}
