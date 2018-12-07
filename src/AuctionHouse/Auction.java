package AuctionHouse;

import Agent.Bid;
import MessageHandling.Message;
import MessageHandling.MessageTypes;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Auction implements Runnable{

    private final int DURATION=30;
    private AuctionHouse auctionHouse;
    private double bidAmount;
    private HashMap<Integer, Double> bidMaps;
    private int currentClientID;
    private int winningClientID;
    private int currentBidderID;
    private Bid winningBid;
    private BlockingQueue<Bid> bids;
    private BidProtocol bidProtocol;
    private int currentWinnerID;
    private Item item;
    private int time;
    private double bidToBeat;
    private Timer t;
    private boolean auctionActive;
    private Bid currentBid;



    /**
     * Takes an item and sets auction up for the
     * item.
     * @param a An Item
     */
    public Auction(AuctionHouse a,Item i, Bid currentBid,int currentClientID){
        this.currentClientID=currentClientID;
        bidMaps=new HashMap<>();
        winningClientID=-1;
        winningBid=null;
        this.currentBid = currentBid;
        auctionHouse=a;
        item=i;
        time = 0;
        item.setInBid(true);
        i.setInBid(true);
        bids = new LinkedBlockingQueue<>();
        bidProtocol = new BidProtocol(item.getPrice());
        auctionActive = true;
        bidToBeat = item.getPrice();
        t = new Timer();
        placeBid(currentBid,currentClientID);
    }

    /**
     * Deconstructs the bid and gets the necessary component of a bid.
     */
    private void breakDownBid(Bid b){
        bidAmount = b.getAmount();
        currentBidderID = b.getBidder();
    }

    /**
     * Begins timer and runs until 30 seconds is reached.
     */
    private void setTime(){
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                if(time>= DURATION){
                    auctionActive = false;
//                    System.out.println(time);
                    t.cancel();
                    placeBid(new Bid(null,9,0),1);
                }
                else {
                    time++;
                }
            }
        },0,1000);
    }


    /**
     * If money does not go through will reset timer.
     * dont think will need
     */
    private void resetTime(){
        auctionActive=true;
        time=0;
    }

    /**
     * Takes from queue and passes it to be
     * analyzed.
     */
    private void getBid(){
        try{
            analyzeBid(bids.take().getAmount());
        }catch(InterruptedException i){
            System.out.println(i);
        }
    }

    /**
     * Checks bid if is valid, returns 0 if it is,
     * else returns 1 and sets winning bid.
     * @param bid Double that is the bid amount.
     */

    //todo rethink this auction methods
    private synchronized void analyzeBid(double bid){
        if(!auctionActive){
            System.out.println("Auction Over");
            return;
        }
        System.out.println(currentBidderID+" tries "+bid);
        if(bidProtocol.processBid(bid)!=0){
            //bid is accepted
            if(winningClientID==-1){
                //first bid that passes threshold
                System.out.println("first bid ");
                auctionHouse.sendToServer(currentClientID,new Message(
                        "auction house", MessageTypes.BID_ACCEPTED,
                        currentBid));
                auctionHouse.sendToBank(new Message("auction house",
                        MessageTypes.BLOCK_FUNDS,currentBidderID,bid));
                bidMaps.put(currentBidderID,bid);
            }
            else {
                System.out.println("overtaking old winner");
                auctionHouse.sendToServer(currentClientID,new Message(
                        "auction house",MessageTypes.BID_ACCEPTED,
                        currentBid));

                if(bidMaps.get(currentBidderID)!=null){
                    //returning bidder
                    if(currentBidderID==currentWinnerID){
                        auctionHouse.sendToBank(new Message(
                                "auction " +
                                        "house",
                                MessageTypes.BLOCK_FUNDS,currentBidderID,
                                bid-bidToBeat));
                    }
                    else{
                        auctionHouse.sendToBank(new Message("auction house",
                                MessageTypes.BLOCK_FUNDS,currentBidderID,bid));
                        auctionHouse.sendToBank(new Message("auction house"
                                ,MessageTypes.UNBLOCK_FUNDS,currentWinnerID,
                                bidToBeat));
                        auctionHouse.sendToServer(winningClientID,new Message(
                                "auction house",MessageTypes.OUT_BID,
                                winningBid));
                    }
                }
                else{
                    //someone new entering bidding
                    auctionHouse.sendToBank(new Message("auction house",
                            MessageTypes.BLOCK_FUNDS,currentBidderID, bid));
                    auctionHouse.sendToBank(new Message("auction house",
                            MessageTypes.UNBLOCK_FUNDS,currentWinnerID,bidToBeat));
                }

                bidMaps.put(currentBidderID,bid);

//                auctionHouse.sendToBank(new Message("auction house",
//                        MessageTypes.UNBLOCK_FUNDS,currentWinnerID,bidToBeat));
            }
            winningClientID = currentClientID;
            currentWinnerID = currentBidderID;
            winningBid=currentBid;
            item.updatePrice(bid);
            auctionHouse.placeMessageForAnalyzing(new Message("auction"
                    ,MessageTypes.UPDATE_ITEM,item));
            bidToBeat = bid;

            System.out.println("current winner " + currentWinnerID);

            auctionHouse.sendToAllClient(new Message("auction house",
                    MessageTypes.UPDATE_ITEM,item.getItemName(),bidToBeat));
        }
        else{
            auctionHouse.sendToServer(currentClientID,new Message("auction " +
                    "house",MessageTypes.BID_REJECTED, currentBid));
            //bid is rejected
        }
    }

    /**
     * Puts bid onto queue.
     * @param b An int
     */
    public void placeBid(Bid b,int currentClientID){
        try {
            if(b.getItem()!=null) breakDownBid(b);
            this.currentClientID=currentClientID;
//            breakDownBid(b);
            bids.put(b);
        }catch(InterruptedException i){
            i.printStackTrace();
        }
    }



    /**
     * Gets the current winner of the auction
     * @return An int that is key of bidder that is winning.
     */
    public int getCurrentWinnerID(){
        return currentBidderID;
    }

    /**
     * Gets the item in the auction.
     * @return An Item.
     */
    public Item getItem(){
        return item;
    }

    @Override
    public String toString(){
        return "Auction Status: "+auctionActive+" Item: "+item.getItemName()+
                " Bid: "+bidToBeat;
    }

    /**
     * Waits for bids to come through.
     */
    @Override
    public void run(){
        auctionHouse.placeMessageForAnalyzing(new Message("auction",
                MessageTypes.UNSAFE_TO_CLOSE));
        while(auctionActive){
            getBid();
            if(time==0)setTime();
        }

        auctionHouse.sendToServer(winningClientID,
                new Message("auction house",MessageTypes.UNBLOCK_FUNDS,
                        currentWinnerID,item.getPrice()));
        //wait for conformation

        auctionHouse.sendToServer(winningClientID,new Message("auction house",
                MessageTypes.TRANSFER_ITEM, winningBid));

        //removes item from its list
        System.out.println(auctionHouse.removeItem(item));

//        auctionHouse.sendToAllClient(new Message("auction house",
//                MessageTypes.UPDATE_ITEM,item.getItemName(),winningBid));

        auctionHouse.placeMessageForAnalyzing(new Message("auction",
                MessageTypes.UPDATE));
        auctionHouse.placeMessageForAnalyzing(new Message("auction",
                MessageTypes.SAFE_TO_CLOSE));

        System.out.println("Agent winner: "+ currentWinnerID);
    }

}
