package lab3.solver;

import javax.jms.*;

public class SolverThread implements Runnable {
    private String operator = null;

    private QueueReceiver queueReceiver;
    private TopicPublisher topicPublisher;

    private SolverThread() {}

    public SolverThread(QueueReceiver queueReceiver, String operator, TopicPublisher topicPublisher) {
        this.queueReceiver = queueReceiver;
        this.operator = operator;
        this.topicPublisher = topicPublisher;
    }

    @Override
    public void run() {
        try {
            QueueMessageListener queueMessageListener = new QueueMessageListener(operator, topicPublisher);
            queueReceiver.setMessageListener(queueMessageListener);
        } catch (JMSException e) {
            e.printStackTrace();
            return;
        }
        while (true) {
        }
    }

//    private void stop() {
//        // close the context
//        if (jndiContext != null) {
//            try {
//                jndiContext.close();
//            } catch (NamingException exception) {
//                exception.printStackTrace();
//            }
//        }
//
//        // close the topicConnection
//        if (topicConnection != null) {
//            try {
//                topicConnection.close();
//            } catch (JMSException exception) {
//                exception.printStackTrace();
//            }
//        }
//    }
}
