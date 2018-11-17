package Agent;

import AuctionHouse.AuctionHouse;

/**
 * Message.java contains the information to be sent between the server and the
 * client to make certain requests. The message contains a sender, receiver and
 * a message from the sender.
 */
public class Message {

    private AuctionHouse auctionHouse;
    private Agent agent;
    private TestBank testBank;
    private String detailedMessage;

    /**
     * Constructor for the message class.
     * @param auctionHouse for the message
     * @param agent for the message
     * @param testBank for the message
     * @param detailedMessage for the message
     */
    public Message(AuctionHouse auctionHouse,
                   Agent agent,
                   TestBank testBank,
                   String detailedMessage) {
        this.auctionHouse = auctionHouse;
        this.agent = agent;
        this.testBank = testBank;
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
}
