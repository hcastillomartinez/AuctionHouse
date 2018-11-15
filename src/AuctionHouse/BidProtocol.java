package AuctionHouse;

import java.util.*;

public class BidProtocol {
    private int lastBid=0;

    /**
     * If bid is not more than the last bid it
     * will return current bid placed
     * @param bid An int
     * @return An int
     */
    public int processBid(int bid){
        if(lastBid>bid) return lastBid;
        else return bid;
    }

}
