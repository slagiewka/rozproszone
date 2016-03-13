package lab1.ex1.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
    private final ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        while(true) {
            try {
                final Socket clientSocket = serverSocket.accept();
                PiSender.handle(clientSocket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
