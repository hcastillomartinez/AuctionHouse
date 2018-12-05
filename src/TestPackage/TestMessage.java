package TestPackage;

import java.io.Serializable;

/**
 * TestMessage is a class to test the message sending between classes.
 * @author Danan High
 */
public class TestMessage implements Serializable {
    
    private static final long serialVersionUID = 3L;
    
    private Object detailedMessage;
    
    /**
     * Test constructor to test using generics.
     * @param detailedMessage message from the sender.
     */
    public TestMessage(Object detailedMessage) {
        this.detailedMessage = detailedMessage;
    }

    /**
     * Returning the detailed message from the sender.
     * @return U detailed message.
     */
    public Object getDetailedMessage() {
        return detailedMessage;
    }

    public String toString() { return "TM = {" + getDetailedMessage() + "}"; }

}
