package lab1.ex3.Util;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;

public class MessageCore implements Serializable{
    private final String messageString;
    private final String nick;
    private final LocalDateTime time;

    public MessageCore(String nick, String messageString) throws IOException {
        this.nick = nick;
        this.messageString = messageString;
        this.time = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageCore that = (MessageCore) o;

        if (messageString != null ? !messageString.equals(that.messageString) : that.messageString != null)
            return false;
        if (nick != null ? !nick.equals(that.nick) : that.nick != null) return false;
        return time != null ? time.equals(that.time) : that.time == null;

    }

    @Override
    public int hashCode() {
        int result = messageString != null ? messageString.hashCode() : 0;
        result = 31 * result + (nick != null ? nick.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        return result;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getNick() {
        return nick;
    }

    public String getMessageString() {
        return messageString;
    }
}
