package Agent;

import AuctionHouse.AuctionHouse;

/**
 * AuctionHouseProxy is the proxy for the auction house class. The proxy
 * provides high level functionality as a mediary between the actual auction
 * house and the agent.
 * Danan High, 11/15/2018
 */
public class AuctionHouseProxy {
    private AuctionHouse auctionHouse;
    
    /**
     * Constructor for the AuctionHouseProxy, builds a reference to the
     * auction house.
     */
    public AuctionHouseProxy() {
        this.auctionHouse = new AuctionHouse();
    }
}
