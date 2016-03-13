package lab1.ex1.server;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {
    public static void main(String[] args) throws IOException {
        errorHandler(args.length != 1, "Server expects only one argument: ./server [port]");
        final int port = Integer.parseInt(args[0]);

        final ServerSocket serverSocket = new ServerSocket(port);

        new Thread(
                new Server(serverSocket)
        ).start();
    }

    private static void errorHandler(Boolean statement, String error) {
        if (statement) {
            System.out.println(error);
            System.exit(-1);
        }
    }
}
