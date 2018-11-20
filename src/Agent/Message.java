package Agent;

import AuctionHouse.AuctionHouse;

import java.io.Serializable;

/**
 * Message.java contains the information to be sent between the server and the
 * client to make certain requests. The message contains a sender, receiver and
 * a message from the sender.
 */
public class Message implements Serializable {

    private AuctionHouse auctionHouse = null;
    private Agent agent = null;
    private TestBank testBank = null;
    private String detailedMessage = null;
    private Bid bid = null;

    /**
     * Constructor for the message class.
     * @param sender for the message
     * @param receiver for the message
     * @param detailedMessage for the message
     */
    public Message(Object sender, Object receiver, Object detailedMessage) {
        assignClasses(sender);
        assignClasses(receiver);
        assignClasses(detailedMessage);
    }
    
    /**
     * Function to assign the sender and receiver to their respective classes.
     * @param object to assign.
     */
    private void assignClasses(Object object) {
        if (object.getClass().equals(AuctionHouse.class)) {
            auctionHouse = (AuctionHouse) object;
        } else if (object.getClass().equals(Agent.class)) {
            agent = (Agent) object;
        } else if (object.getClass().equals(TestBank.class)) {
            testBank = (TestBank) object;
        } else if (object.getClass().equals(Bid.class)) {
            bid = (Bid) object;
        } else if (object.getClass().equals(String.class)) {
            this.detailedMessage = (String) object;
        }
        
    }

    /**
     * Returning the AuctionHouse from the message.
     * @return auctionHouse from the message.
     */
    public AuctionHouse getAuctionHouse() {
        return auctionHouse;
    }

    /**
     * Returning the Agent from the message.
     * @return agent from the message.
     */
    public Agent getAgent() {
        return agent;
    }

    /**
     * Returning the TestBank from the message.
     * @return testBank from the message.
     */
    public TestBank getTestBank() {
        return testBank;
    }

    /**
     * Returning the detailedMessage from the message.
     * @return detailedMessage giving directions/requests.
     */
    public Object getDetailedMessage() {
        return detailedMessage;
    }

    @Override
    public String toString() {
        return "Message{" + "auctionHouse=" + auctionHouse +
            ", agent=" + agent +
            ", testBank=" + testBank +
            ", detailedMessage=" + detailedMessage +
            ", bid=" + bid + '}';
    }
}
