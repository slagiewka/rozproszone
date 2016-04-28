package lab4.Server;

import Ice.ObjectAdapter;
import Ice.Util;
import lab4.Impl.DemoAppImpl;
import lab4.Locators.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerThread implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerThread.class);
    private final String[] args;

    public ServerThread(String[] args) {
        this.args = args;
    }

    @Override
    public void run() {
        int status = 0;
        Ice.Communicator communicator = null;

        try {
            communicator = Util.initialize(args);

            ObjectAdapter adapter = communicator.createObjectAdapter("Adapter");

            //Lazy initialized data
            adapter.addServantLocator(new K1ServantLocator(adapter), "K1");

            //Separate state object
            adapter.addServantLocator(new K2ServantLocator(), "K2");

            //Pool of threads that can execute properly with shared data
            adapter.addServantLocator(new K3ServantLocator(4), "K3");

            //Object with small responsibility - high availability for sequential use
            adapter.addDefaultServant(new DemoAppImpl(), "K4");

            //Objects with obligatory persistence of the state
            //i.e. session objects with concurrent access
            adapter.addServantLocator(new K5ServantLocator(4), "K5");

            adapter.activate();
            LOGGER.info("Entering event processing loop...");
            communicator.waitForShutdown();
        } catch (Exception e) {
            LOGGER.error(e.toString(), e);
            status = 1;
        }
        if (communicator != null) {
            try {
                communicator.destroy();
            } catch (Exception e) {
                LOGGER.error(e.toString(), e);
                status = 1;
            }
        }
        System.exit(status);
    }
}
