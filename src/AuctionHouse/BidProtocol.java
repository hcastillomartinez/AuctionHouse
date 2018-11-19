package AuctionHouse;

import java.util.*;

public class BidProtocol {
    private int lastBid;
    private int price;
    public BidProtocol(int price){
        lastBid=0;
        this.price=price;
    }
    /**
     * If bid is not more than the last bid it
     * will return 0, else returns 1 if valid.
     * @param bid An int
     * @return An int
     */
    public int processBid(int bid){
        if(lastBid>bid || bid<price) return 0;
        else{
            lastBid=bid;
            return 1;
        }
    }
}
