package lab3.reader;

import javax.jms.*;

public class ReaderThread implements Runnable {
    private TopicSubscriber topicSubscriber;

    private ReaderThread() {}

    public ReaderThread(TopicSubscriber topicSubscriber) {
        this.topicSubscriber = topicSubscriber;
    }

    @Override
    public void run() {
        try {
            MessageListener messageListener = new TopicMessageListener(topicSubscriber.getTopic().getTopicName());
            topicSubscriber.setMessageListener(messageListener);
        } catch (JMSException e) {
            e.printStackTrace();
            return;
        }

        while (true) {}
    }
}
