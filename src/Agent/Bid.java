package Agent;

import AuctionHouse.Item;

/**
 * Bid.java is the class that handles bids on items from agents.
 * Danan high, 11/18/2018
 */
public class Bid {
    
    private Agent bidder;
    private Item item;
    private int amount;
    
    /**
     * Constructor for the bid object.
     * @param item item to be bid on.
     * @param bidder agent placing the bid.
     * @param amount the amount of the bid.
     */
    public Bid(Item item, Agent bidder, int amount) {
        this.item = item;
        this.bidder = bidder;
        this.amount = amount;
    }
    
    /**
     * Function to get the item being bid on.
     * @return item for bid.
     */
    public Item getItem() {
        return item;
    }
    
    /**
     * Getting the agent that has placed the bid.
     * @return bidder making the bid.
     */
    public Agent getBidder() {
        return bidder;
    }
    
    /**
     * Getting the amount that has been placed on the bid.
     * @return amount being bid on the item.
     */
    public int getAmount() {
        return amount;
    }
}
