package lab3.reader;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.concurrent.ThreadLocalRandom;

class TopicMessageListener implements MessageListener{
    private final String topicName;

    TopicMessageListener(String topicName) {
        this.topicName = topicName;
    }

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            String text;
            try {
                text = ((TextMessage) message).getText();
            } catch (JMSException e) {
                e.printStackTrace();
                return;
            }

            System.out.println(topicName + ": " +text);

            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(500, 3000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
