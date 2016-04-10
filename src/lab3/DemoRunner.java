package lab3;

import lab3.generator.GeneratorThread;
import lab3.generator.QueueSenderFactory;
import lab3.reader.ReaderSubscriberFactory;
import lab3.reader.ReaderThread;
import lab3.solver.SolverThread;
import lab3.solver.SolverUtilFactory;

import javax.jms.*;
import javax.naming.NamingException;

public class DemoRunner {
    private final static String ADDITION_QUEUE = "addition";
    private final static String ADDITION_OPERATOR = "+";
    private final static String ADDITION_SOLVED_TOPIC = "addition_solved";

    private final static String SUBTRACTION_QUEUE = "subtraction";
    private final static String SUBTRACTION_OPERATOR = "-";
    private final static String SUBTRACTION_SOLVED_TOPIC = "subtraction_solved";

    private final static String DIVISION_QUEUE = "division";
    private final static String DIVISION_OPERATOR = "/";
    private final static String DIVISION_SOLVED_TOPIC = "division_solved";

    private final static String MULTIPLICATION_QUEUE = "multiplication";
    private final static String MULTIPLICATION_OPERATOR = "*";
    private final static String MULTIPLICATION_SOLVED_TOPIC = "multiplication_solved";

    public static void main(String[] args) {
        //Start readers
        ReaderSubscriberFactory readerSubscriberFactory = new ReaderSubscriberFactory();
        TopicSubscriber additionTopicSubscriber;
        TopicSubscriber subtractionTopicSubscriber;
        TopicSubscriber divisionTopicSubscriber;
        TopicSubscriber multiplicationTopicSubscriber;

        try {
            additionTopicSubscriber = readerSubscriberFactory.getTopicSubscriber(ADDITION_SOLVED_TOPIC);
            subtractionTopicSubscriber = readerSubscriberFactory.getTopicSubscriber(SUBTRACTION_SOLVED_TOPIC);
            divisionTopicSubscriber = readerSubscriberFactory.getTopicSubscriber(DIVISION_SOLVED_TOPIC);
            multiplicationTopicSubscriber = readerSubscriberFactory.getTopicSubscriber(MULTIPLICATION_SOLVED_TOPIC);
        } catch (JMSException | NamingException e) {
            e.printStackTrace();
            return;
        }

        new Thread(new ReaderThread(additionTopicSubscriber)).start();
        new Thread(new ReaderThread(additionTopicSubscriber)).start();

        new Thread(new ReaderThread(subtractionTopicSubscriber)).start();

        new Thread(new ReaderThread(divisionTopicSubscriber)).start();

        new Thread(new ReaderThread(multiplicationTopicSubscriber)).start();
        new Thread(new ReaderThread(multiplicationTopicSubscriber)).start();

        //Start solvers
        QueueReceiver additionQueueReceiver;
        QueueReceiver subtractionQueueReceiver;
        QueueReceiver divisionQueueReceiver;
        QueueReceiver multiplicationQueueReceiver;
        TopicPublisher additionPublisher;
        TopicPublisher subtractionPublisher;
        TopicPublisher divisionPublisher;
        TopicPublisher multiplicationPublisher;
        try {
            SolverUtilFactory solverUtilFactory = new SolverUtilFactory();
            additionQueueReceiver = solverUtilFactory.getQueueReceiver(ADDITION_QUEUE);
            subtractionQueueReceiver = solverUtilFactory.getQueueReceiver(SUBTRACTION_QUEUE);
            divisionQueueReceiver = solverUtilFactory.getQueueReceiver(DIVISION_QUEUE);
            multiplicationQueueReceiver = solverUtilFactory.getQueueReceiver(MULTIPLICATION_QUEUE);

            additionPublisher = solverUtilFactory.getTopicPublisher(ADDITION_SOLVED_TOPIC);
            subtractionPublisher = solverUtilFactory.getTopicPublisher(SUBTRACTION_SOLVED_TOPIC);
            divisionPublisher = solverUtilFactory.getTopicPublisher(DIVISION_SOLVED_TOPIC);
            multiplicationPublisher = solverUtilFactory.getTopicPublisher(MULTIPLICATION_SOLVED_TOPIC);

        } catch (JMSException | NamingException e) {
            e.printStackTrace();
            return;
        }

        new Thread(new SolverThread(additionQueueReceiver, ADDITION_OPERATOR, additionPublisher)).start();
        new Thread(new SolverThread(additionQueueReceiver, ADDITION_OPERATOR, additionPublisher)).start();

        new Thread(new SolverThread(subtractionQueueReceiver, SUBTRACTION_OPERATOR, subtractionPublisher)).start();
        new Thread(new SolverThread(subtractionQueueReceiver, SUBTRACTION_OPERATOR, subtractionPublisher)).start();

        new Thread(new SolverThread(divisionQueueReceiver, DIVISION_OPERATOR, divisionPublisher)).start();
        new Thread(new SolverThread(divisionQueueReceiver, DIVISION_OPERATOR, divisionPublisher)).start();

        new Thread(new SolverThread(multiplicationQueueReceiver, MULTIPLICATION_OPERATOR, multiplicationPublisher)).start();

        //Start generators
        QueueSenderFactory queueSenderFactory = new QueueSenderFactory();
        QueueSender additionQueueSender;
        QueueSender subtractionQueueSender;
        QueueSender multiplicationQueueSender;
        QueueSender divisionQueueSender;

        try {
            additionQueueSender = queueSenderFactory.getQueueSender(ADDITION_QUEUE);
            subtractionQueueSender = queueSenderFactory.getQueueSender(SUBTRACTION_QUEUE);
            divisionQueueSender = queueSenderFactory.getQueueSender(DIVISION_QUEUE);
            multiplicationQueueSender = queueSenderFactory.getQueueSender(MULTIPLICATION_QUEUE);
        } catch (JMSException | NamingException e) {
            e.printStackTrace();
            return;
        }

        new Thread(new GeneratorThread(additionQueueSender, ADDITION_OPERATOR)).start();
        new Thread(new GeneratorThread(additionQueueSender, ADDITION_OPERATOR)).start();
        new Thread(new GeneratorThread(additionQueueSender, ADDITION_OPERATOR)).start();

        new Thread(new GeneratorThread(subtractionQueueSender, SUBTRACTION_OPERATOR)).start();
        new Thread(new GeneratorThread(subtractionQueueSender, SUBTRACTION_OPERATOR)).start();

        new Thread(new GeneratorThread(divisionQueueSender, DIVISION_OPERATOR)).start();

        new Thread(new GeneratorThread(multiplicationQueueSender, MULTIPLICATION_OPERATOR)).start();
        new Thread(new GeneratorThread(multiplicationQueueSender, MULTIPLICATION_OPERATOR)).start();
    }
}
