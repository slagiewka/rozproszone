package lab1.ex2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
    final private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        while(true) {
            try {
                final Socket clientSocket = serverSocket.accept();
                Reader.read(clientSocket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
