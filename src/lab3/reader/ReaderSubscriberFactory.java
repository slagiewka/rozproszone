package lab3.reader;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class ReaderSubscriberFactory {
    private static final String DEFAULT_JMS_PROVIDER_URL = "tcp://localhost:3035/";
    private static final String JNDI_CONTEXT_FACTORY_CLASS_NAME = "org.exolab.jms.jndi.InitialContextFactory";

    // Application JNDI context
    private Context jndiContext;

    // JMS Administrative objects
    private TopicConnectionFactory topicConnectionFactory;
    private Topic incomingTopic;

    // JMS Client objects
    private TopicConnection topicConnection;
    private TopicSession topicSession;
    private TopicSubscriber topicSubscriber;

    public TopicSubscriber getTopicSubscriber(String topicName) throws JMSException, NamingException {
        initializeJndiContext(DEFAULT_JMS_PROVIDER_URL);
        initializeAdministrativeObjects(topicName);
        initializeJmsClientObjects();
        topicConnection.start();
        return topicSubscriber;
    }

    private void initializeJndiContext(String providerUrl) throws NamingException {
        // JNDI Context
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, JNDI_CONTEXT_FACTORY_CLASS_NAME);
        props.put(Context.PROVIDER_URL, providerUrl);
        jndiContext = new InitialContext(props);
    }

    private void initializeAdministrativeObjects(String incomingTopicName) throws NamingException {
        // ConnectionFactory
        topicConnectionFactory = (TopicConnectionFactory) jndiContext.lookup("ConnectionFactory");
        // Destination
        incomingTopic = (Topic) jndiContext.lookup(incomingTopicName);
    }

    private void initializeJmsClientObjects() throws JMSException {
        topicConnection = topicConnectionFactory.createTopicConnection();
        topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        topicSubscriber = topicSession.createSubscriber(incomingTopic);
    }
}
