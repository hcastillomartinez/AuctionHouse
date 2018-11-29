package AuctionHouse;

import java.util.*;

/**
 * Used by auction.java to decide what to do
 * with the bid.
 */
public class BidProtocol {
    private double lastBid;
    private double price;

    public BidProtocol(double price){
        lastBid=0;
        this.price=price;
    }
    /**
     * If bid is not more than the last bid it
     * will return 0, else returns 1 if valid.
     * @param bid An int
     * @return An int
     */
    public double processBid(double bid){
        if(lastBid>bid || bid<price) return 0;
        else{
            lastBid=bid;
            return 1;
        }
    }
}
