package Agent;

import java.io.Serializable;

public class TestMessage<T, U> implements Serializable {
    
    private T sender;
    private U detailedMessage;
    
    /**
     * Test constructor to test using generics.
     * @param sender sender of the message.
     * @param detailedMessage message from the sender.
     */
    public TestMessage(T sender, U detailedMessage) {
        this.sender = sender;
        this.detailedMessage = detailedMessage;
    }

    /**
     * Returning the sender of the message.
     * @return T sender of the message.
     */
    public T getSender() {
        return sender;
    }

    /**
     * Returning the detailed message from the sender.
     * @return U detailed message.
     */
    public U getDetailedMessage() {
        return detailedMessage;
    }
}
