package lab1.ex3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Reader implements Runnable{
    private final int BUFFER_SIZE = 2048;

    private final DatagramSocket socket;
    private final String nick;

    public Reader(String nick, DatagramSocket socket) {
        this.nick = nick;
        this.socket = socket;
    }

    @Override
    public void run() {
        while (!socket.isClosed()) {
            final DatagramPacket packet = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
            try {
                socket.receive(packet);
                byte[] data = packet.getData();
                String string = new String(data);
                System.out.println("Received: " + string);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
