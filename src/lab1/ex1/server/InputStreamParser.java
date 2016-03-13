package lab1.ex1.server;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class InputStreamParser {
    public static long fromInputStream(InputStream stream) throws IOException {
        final int bytes = stream.read();
        final byte[] rawData = new byte[bytes];

        IOUtils.readFully(stream, rawData, 0, bytes);
        ByteBuffer buffer = ByteBuffer.allocate(rawData.length);
        buffer.put(rawData);
        buffer.flip();

        return parseFromBuffer(buffer, bytes);
    }

    private static long parseFromBuffer(ByteBuffer buffer, int packetLength) {
        long result;
        switch (packetLength) {
            case 1:
                result = buffer.get();
                break;
            case 2:
                result = buffer.getShort();
                break;
            case 4:
                result = buffer.getInt();
                break;
            case 8:
                result = buffer.getLong();
                break;
            default:
                throw new IllegalArgumentException("Unrecognized number format");
        }

        return result;
    }
}
