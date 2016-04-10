package lab3.generator;

import org.exolab.jms.message.TextMessageImpl;

import javax.jms.*;
import java.util.concurrent.ThreadLocalRandom;

public class GeneratorThread implements Runnable {
    private String operator = null;
    private QueueSender queueSender;

    private GeneratorThread() {}

    public GeneratorThread(QueueSender queueSender, String operator) {
        this.queueSender = queueSender;
        this.operator = operator;
    }

    @Override
    public void run() {
        String task;
        TextMessage message;
        try {
            message = new TextMessageImpl();
        } catch (JMSException e) {
            e.printStackTrace();
            return;
        }
        while (true) {
            task = produceTask();
            try {
                message.setText(task);
                queueSender.send(message);
            } catch (JMSException e) {
                e.printStackTrace();
                break;
            }

            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private String produceTask() {
        String first = String.valueOf(ThreadLocalRandom.current().nextDouble(1, 1000));
        String second = String.valueOf(ThreadLocalRandom.current().nextDouble(1, 1000));
        return first + operator + second;
    }
}
