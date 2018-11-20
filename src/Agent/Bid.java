package Agent;

import AuctionHouse.Item;

import java.io.Serializable;

/**
 * Bid.java is the class that handles bids on items from agents.
 * Danan high, 11/18/2018
 */
public class Bid implements Serializable {
    
    private Agent bidder;
    private Item item;
    private double amount;
    
    /**
     * Constructor for the bid object.
     * @param item item to be bid on.
     * @param bidder agent placing the bid.
     * @param amount the amount of the bid.
     */
    public Bid(Item item, Agent bidder, double amount) {
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
    public double getAmount() {
        return amount;
    }
}
