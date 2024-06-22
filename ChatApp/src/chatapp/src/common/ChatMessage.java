package chatapp.src.common;

import java.io.Serializable;

public class ChatMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum MessageType {
        LOGIN, LOGOUT, MESSAGE, LIST, PING
    }

    private final MessageType type;
    private String sender;
    private final String content;

    public ChatMessage(MessageType type, String sender, String content) {
        this.type = type;
        this.sender = sender;
        this.content = content;
    }

    public ChatMessage(MessageType type, String content) {
        this.type = type;
        this.content = content;
    }

    public MessageType getType() {
        return type;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }
    
    @Override
   public String toString() {
       return "ChatMessage{" +
               "type=" + type +
               ", sender=" + sender +
               ", content=" + content +
               '}';
   }
}

