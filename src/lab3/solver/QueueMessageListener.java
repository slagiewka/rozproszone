package lab3.solver;

import org.exolab.jms.message.TextMessageImpl;

import javax.jms.*;
import java.util.StringTokenizer;
import java.util.concurrent.ThreadLocalRandom;

class QueueMessageListener implements MessageListener{

    private String operator = null;
    private TopicPublisher topicPublisher = null;

    private QueueMessageListener() {}

    QueueMessageListener(String operator, TopicPublisher topicPublisher) {
        this.operator = operator;
        this.topicPublisher = topicPublisher;
    }

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            String textMessage;
            try {
                textMessage = ((TextMessage) message).getText();
            } catch (JMSException e) {
                e.printStackTrace();
                return;
            }

            StringTokenizer stringTokenizer = new StringTokenizer(textMessage, operator);
            String token;
            if (!stringTokenizer.hasMoreTokens()) {
                return;
            }

            token = stringTokenizer.nextToken();
            double result = Double.parseDouble(token);
            double arg2;
            while (stringTokenizer.hasMoreTokens()) {
                token = stringTokenizer.nextToken();
                arg2 = Double.parseDouble(token);
                switch (operator) {
                    case "+":
                        result = result + arg2;
                        break;
                    case "-":
                        result = result - arg2;
                        break;
                    case "/":
                        if (arg2 == 0) {
                            throw new InvalidDivisionArgumentException("Invalid division argument given");
                        }

                        result = result / arg2;
                        break;
                    case "*":
                        result = result * arg2;
                        break;
                }

            }

            try {
                TextMessage sendMessage = new TextMessageImpl();
                sendMessage.setText(String.valueOf(result));
                topicPublisher.publish(sendMessage);
            } catch (JMSException e) {
                e.printStackTrace();
                return;
            }

            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(500, 3000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class InvalidDivisionArgumentException extends RuntimeException {
        InvalidDivisionArgumentException(String s) {
        }
    }
}
