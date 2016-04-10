package lab3.solver;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class SolverUtilFactory {
    private static final String DEFAULT_JMS_PROVIDER_URL = "tcp://localhost:3035/";
    private static final String JNDI_CONTEXT_FACTORY_CLASS_NAME = "org.exolab.jms.jndi.InitialContextFactory";

    // Application JNDI context
    private Context jndiContext;

    // JMS Administrative objects
    private TopicConnectionFactory topicConnectionFactory;
    private QueueConnectionFactory queueConnectionFactory;
    private Queue incomingQueue;
    private Topic outgoingTopic;

    // JMS Client objects
    private TopicConnection topicConnection;
    private QueueConnection queueConnection;
    private TopicSession topicSession;
    private QueueSession queueSession;
    private QueueReceiver queueReceiver;
    private TopicPublisher topicPublisher;

    public SolverUtilFactory() throws NamingException {
        initialize();
    }

    public QueueReceiver getQueueReceiver(String queueName) throws JMSException, NamingException {
//        if (queueReceiver != null) {
//            return queueReceiver;
//        }

        initializeAdministrativeQueueObjects(queueName);
        initializeJmsClientQueueObjects();
        queueConnection.start();

        return queueReceiver;
    }

    public TopicPublisher getTopicPublisher(String publishTopicName) throws NamingException, JMSException {
//        if (topicPublisher != null) {
//            return topicPublisher;
//        }

        initializeAdministrativeTopicObjects(publishTopicName);
        initializeJmsClientTopicObjects();
        topicConnection.start();

        return topicPublisher;
    }

    private void initialize() throws NamingException {
        initializeJndiContext(DEFAULT_JMS_PROVIDER_URL);
    }

    private void initializeJndiContext(String providerUrl) throws NamingException {
        // JNDI Context
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, JNDI_CONTEXT_FACTORY_CLASS_NAME);
        props.put(Context.PROVIDER_URL, providerUrl);
        jndiContext = new InitialContext(props);
    }

    private void initializeAdministrativeQueueObjects(String incomingQueueName) throws NamingException {
        queueConnectionFactory = (QueueConnectionFactory) jndiContext.lookup("ConnectionFactory");
        incomingQueue = (Queue) jndiContext.lookup(incomingQueueName);
    }

    private void initializeAdministrativeTopicObjects(String publishTopicName) throws NamingException {
        topicConnectionFactory = (TopicConnectionFactory) jndiContext.lookup("ConnectionFactory");
        outgoingTopic = (Topic) jndiContext.lookup(publishTopicName);
    }

    private void initializeJmsClientQueueObjects() throws JMSException {
        queueConnection = queueConnectionFactory.createQueueConnection();
        queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        queueReceiver = queueSession.createReceiver(incomingQueue);
    }

    private void initializeJmsClientTopicObjects() throws JMSException {
        topicConnection = topicConnectionFactory.createTopicConnection();
        topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        topicPublisher = topicSession.createPublisher(outgoingTopic);
    }
}
