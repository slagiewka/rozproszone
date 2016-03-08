package lab1.ex3.Util;

import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;

import java.io.*;
import java.nio.ByteBuffer;

public class Message {

    private static final int BUFFER_SIZE = 2048;
    private final MessageCore messageCore;
    private final int checkSum;

    public Message(String nick, String messageString) throws IOException {
        this.messageCore = new MessageCore(nick, messageString);
        this.checkSum = messageCore.hashCode();
    }

    public Message(MessageCore messageCore) {
        this.messageCore = messageCore;
        checkSum = messageCore.hashCode();
    }

    public byte[] toBytes() {
        final ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

        return buffer
                .putInt(checkSum)
                .put(SerializationUtils.serialize(messageCore))
                .array();
    }

    public static Message fromBytes(byte[] bytes) throws InvalidChecksumException, InvalidSerializedMessageException {
        final DataInputStream dataInputStream = new DataInputStream(
                new ByteArrayInputStream(bytes)
        );

        int checkSum;
        MessageCore deserializedMessageData;

        try {
            checkSum = dataInputStream.readInt();
            deserializedMessageData = SerializationUtils.deserialize(dataInputStream);
        } catch (IOException | SerializationException e) {
            throw new InvalidSerializedMessageException();
        }

        if (checkSum != deserializedMessageData.hashCode()) {
            throw new InvalidChecksumException();
        }
        return new Message(deserializedMessageData);

    }

    public String getNick() {
        return messageCore.getNick();
    }

    @Override
    public String toString() {
        return messageCore.getTime()
                + "<"
                + messageCore.getNick()
                + "> "
                + messageCore.getMessageString()
                ;
    }

    @Override
    public int hashCode() {
        int result = messageCore != null ? messageCore.hashCode() : 0;
        result = 31 * result + checkSum;
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (checkSum != message.checkSum) return false;
        return messageCore != null ? messageCore.equals(message.messageCore) : message.messageCore == null;
    }

    public static class InvalidChecksumException extends Throwable {
    }

    public static class InvalidSerializedMessageException extends Throwable {
    }
}
