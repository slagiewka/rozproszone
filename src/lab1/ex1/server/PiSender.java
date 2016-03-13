package lab1.ex1.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class PiSender {
    public static void handle(Socket clientSocket) {
        while (!clientSocket.isClosed()) {
            try {
                final long extractedInput = extractDataFrom(
                        clientSocket.getInputStream()
                );

                final int piDigit = (int) extractedInput % 10;

                sendResponse(piDigit, clientSocket);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static void sendResponse(int piDigit, Socket clientSocket) throws IOException {
        final ByteBuffer buffer = ByteBuffer.allocate(1);
        buffer.put((byte) piDigit);

        final OutputStream stream = clientSocket.getOutputStream();
        stream.write(
                buffer.array()
        );
    }

    private static long extractDataFrom(InputStream stream) throws IOException {
        return InputStreamParser.fromInputStream(stream);
    }
}
