package lab1.ex3;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Main {

    private static final int MAX_NICK_LENGTH = 6;

    public static void main(String[] args) throws Exception {
        errorHandler(args.length < 3, "Wrong call. Arguments: <address> <port> <nick>");

        InetAddress address = Inet4Address.getByName(args[0]);
        errorHandler(!address.isMulticastAddress(), "Address is not multicast");

        int port = Integer.parseInt(args[1]);

        String nick = args[2];
        errorHandler(nick.length() > MAX_NICK_LENGTH, "Nickname is too long");

        final MulticastSocket multicastSocket = new MulticastSocket(port);
        multicastSocket.joinGroup(address);
        multicastSocket.setLoopbackMode(false);

        new Thread(new Writer(nick, multicastSocket, address, port)).start();
        new Thread(new Reader(nick, multicastSocket)).start();
    }

    private static void errorHandler(Boolean statement, String error) {
        if (statement) {
            System.out.println(error);
            System.exit(-1);
        }
    }
}
