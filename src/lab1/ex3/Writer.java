package lab1.ex3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Writer implements Runnable {
    private static final String QUIT_CMD = "/quit";
    private static final String MESSAGE_TOO_LONG = "Message should not be longer than 20 characters!";
    private static final int MESSAGE_SIZE_LIMIT = 20;

    private final String nick;
    private final DatagramSocket socket;
    private final InetAddress address;
    private final int port;

    private final BufferedReader reader;

    public Writer(String nick,
                  DatagramSocket socket,
                  InetAddress address,
                  int port
    ) {
        this.nick = nick;
        this.socket = socket;
        this.address = address;
        this.port = port;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run() {
        try {
            String messageContent = reader.readLine();
            while (!QUIT_CMD.equals(messageContent)) {
                if (messageContent.length() > MESSAGE_SIZE_LIMIT) {
                    System.out.println(MESSAGE_TOO_LONG);
                } else {
                    socket.send(
                        new DatagramPacket(
                            messageContent.getBytes(),
                            messageContent.length(),
                            address,
                            port
                        )
                    );
                }
                messageContent = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
}
