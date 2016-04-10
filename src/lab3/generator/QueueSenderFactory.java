package lab3.generator;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class QueueSenderFactory {
    private static final String DEFAULT_JMS_PROVIDER_URL = "tcp://localhost:3035/";
    private static final String JNDI_CONTEXT_FACTORY_CLASS_NAME = "org.exolab.jms.jndi.InitialContextFactory";

    // Application JNDI context
    private Context jndiContext;

    // JMS Administrative objects
    private QueueConnectionFactory queueConnectionFactory;
    private Queue outgoingMessagesQueue;

    // JMS Client objects
    private QueueConnection connection;
    private QueueSession session;
    private QueueSender queueSender;

    public QueueSender getQueueSender(String queueName) throws JMSException, NamingException {
//        if (queueSender != null) {
//            return queueSender;
//        }

        initializeJndiContext(DEFAULT_JMS_PROVIDER_URL);
        initializeAdministrativeObjects(queueName);
        initializeJmsClientObjects();
        connection.start();

        return queueSender;
    }

    private void initializeJndiContext(String providerUrl) throws NamingException {
        // JNDI Context
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, JNDI_CONTEXT_FACTORY_CLASS_NAME);
        props.put(Context.PROVIDER_URL, providerUrl);
        jndiContext = new InitialContext(props);
    }

    private void initializeAdministrativeObjects(String outgoingMessagesQueueName) throws NamingException {
        // ConnectionFactory
        queueConnectionFactory = (QueueConnectionFactory) jndiContext.lookup("ConnectionFactory");
        // Destination
        outgoingMessagesQueue = (Queue) jndiContext.lookup(outgoingMessagesQueueName);
    }

    private void initializeJmsClientObjects() throws JMSException {
        connection = queueConnectionFactory.createQueueConnection();
        session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        queueSender = session.createSender(outgoingMessagesQueue);
    }
}
