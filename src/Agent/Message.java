package Agent;

import AuctionHouse.AuctionHouse;
import Proxies.AuctionHouseProxy;
import Proxies.BankProxy;

import java.io.Serializable;

/**
 * Message.java contains the information to be sent between the server and the
 * client to make certain requests. The message contains a sender, receiver and
 * a message from the sender.
 */
public class Message implements Serializable {

    private AuctionHouse auctionHouse = null;
    private BankProxy bankProxy = null;
    private AuctionHouseProxy auctionHouseProxy = null;
    private Agent agent = null;
    private TestBank testBank = null;
    private String detailedMessage = null;
    private Bid bid = null;

    /**
     * Constructor for the message class.
     * @param detailedMessage for the message
     */
    public Message(String detailedMessage) {
        this.detailedMessage = detailedMessage;
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
    public String getDetailedMessage() {
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
