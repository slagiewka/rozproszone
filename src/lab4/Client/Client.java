package lab4.Client;

import Ice.*;
import lab4.app.DemoAppPrx;
import lab4.app.DemoAppPrxHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.Exception;

public class Client {
    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);
    private static String serverEndpoint
            = "tcp -h localhost -p 10000:udp -h localhost -p 10000:ssl -h localhost -p 10001";

    public static void main(String[] args) {
        int status = 0;
        Ice.Communicator communicator = null;

        try {
            communicator = Util.initialize(args);

            serverEndpoint = communicator.getProperties().getProperty("Server.Endpoints");

            String line;
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Write the object's id or q to quit");
            while (true) {
                line = input.readLine();
                if (line.equals("q"))
                    break;

                DemoAppPrx demoProxy = getProxyFor(line, communicator);
                if (demoProxy == null) {
                    System.err.println("Invalid object id: " + line);
                } else {
                    demoProxy.demo(12);
                }

                System.out.println("Next one...");
            }
        } catch (Ice.LocalException e) {
            e.printStackTrace();
            status = 1;
        } catch (Exception e) {
            LOGGER.error("Something went wrong", e);
            status = 1;
        }
        if (communicator != null) {
            try {
                communicator.destroy();
            } catch (Exception e) {
                LOGGER.error("Something went wrong", e);
                status = 1;
            }
        }
        System.exit(status);
    }

    private static DemoAppPrx getProxyFor(String id, Communicator comm) {
        ObjectPrx proxy;
        try {
            proxy = comm.stringToProxy(id + ":" + serverEndpoint);

            return DemoAppPrxHelper.checkedCast(proxy);
        } catch (ProxyParseException | ObjectNotExistException ex) {
            return null;
        }
    }
}
