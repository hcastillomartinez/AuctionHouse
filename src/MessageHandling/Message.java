package MessageHandling;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Message.java contains the information to be sent between the server and the
 * client to make certain requests. The message contains a sender, MessageType
 * and a message from the sender.
 * @author Danan High, 11/27/2018
 */
public class Message implements Serializable {

    private static final long serialVersionUID = 4L;
    
    private ArrayList<Object> messageList;

    /**
     * Constructor for the message class.
     * @param objects for the message
     */
    public Message(Object...objects) {
        messageList = new ArrayList<>();
        Collections.addAll(messageList, objects);
    }

    /**
     * Returning the objects from the message.
     * @return messageList for checking the objects.
     */
    public ArrayList<Object> getMessageList() {
        return messageList;
    }
    
    /**
     * Overriding the toString() to print the message passed in.
     */
    @Override
    public String toString() {
        return "Message{" + "messageList=" + messageList + '}';
    }
}
