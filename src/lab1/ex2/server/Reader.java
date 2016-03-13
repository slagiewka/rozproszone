package lab1.ex2.server;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.Socket;

public class Reader {
    public static void read(Socket clientSocket) {
        while (!clientSocket.isClosed()) {
            try {
                final InputStream inputStream = clientSocket.getInputStream();

                final String fileName = getFileName(inputStream);
                final FileOutputStream fileOutputStream = new FileOutputStream(fileName);
                copyStream(inputStream, fileOutputStream);

                fileOutputStream.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void copyStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        IOUtils.copy(inputStream, outputStream);
    }

    private static String getFileName(InputStream inputStream) throws IOException {
        final DataInputStream dataInputStream = new DataInputStream(inputStream);

        final int fileNameLength = dataInputStream.readByte();
        final byte[] fileNameRawData = new byte[fileNameLength];
        dataInputStream.readFully(fileNameRawData, 0, fileNameLength);
        return IOUtils.toString(fileNameRawData, "UTF-8");
    }
}
