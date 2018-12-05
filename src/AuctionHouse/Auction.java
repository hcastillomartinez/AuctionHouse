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
    public Auction(AuctionHouse a,Item i, Bid currentBid){
        currentClientID=0;
        bidMaps=new HashMap<>();
        winningClientID=-1;

        this.currentBid = currentBid;
        auctionHouse=a;
        item=i;
        time = 0;
        item.setInBid(true);
        bids = new LinkedBlockingQueue<>();
        bidProtocol = new BidProtocol(item.getPrice());
        auctionActive = true;
        bidToBeat = item.getPrice();
        t = new Timer();
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
    private synchronized void analyzeBid(double bid){
        if(!auctionActive){
//            auctionHouse.sendToServer(currentClientID,new Message("auction " +
//                    "house",MessageTypes.BID_REJECTED));
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
                    auctionHouse.sendToBank(new Message("auction house",
                            MessageTypes.BLOCK_FUNDS,currentBidderID,
                            bid-bidToBeat));
                }
                else{
                    //someone new entering bidding
                    auctionHouse.sendToBank(new Message("auction house",
                            MessageTypes.BLOCK_FUNDS,currentBidderID, bid));
                }

                bidMaps.put(currentBidderID,bid);
                auctionHouse.sendToServer(winningClientID,new Message(
                        "auction house",MessageTypes.OUT_BID));

                auctionHouse.sendToBank(new Message("auction house",
                        MessageTypes.UNBLOCK_FUNDS,currentWinnerID,bidToBeat));
            }
            winningClientID = currentClientID;
            currentWinnerID = currentBidderID;
            item.updatePrice(bid);
            bidToBeat = bid;
            System.out.println("current winner " + currentWinnerID);
        }
        else{
            auctionHouse.sendToServer(currentClientID,new Message("auction " +
                    "house",MessageTypes.BID_REJECTED, currentBid));
            //bid is rejected
            System.out.println(currentBidderID+" need to beat "+bidToBeat
                    +" ,you bid "+bid);
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
            breakDownBid(b);
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
        return "Auction Status: "+auctionActive+" Item: "+item+
                " Bid: "+bidToBeat;
    }

    /**
     * Waits for bids to come through.
     */
    @Override
    public void run(){
        while(auctionActive){
            getBid();
            if(time==0)setTime();
        }
        auctionHouse.sendToServer(winningClientID,
                new Message("auction house",MessageTypes.UNBLOCK_FUNDS,
                        auctionHouse.getHouseID(),item.getPrice()));
        //wait for conformation

        auctionHouse.sendToServer(winningClientID,new Message("auction house",
                MessageTypes.TRANSFER_ITEM, currentBid));

        //removes item from its list
        auctionHouse.getItemList().remove(item);
        auctionHouse.placeMessageForAnalyzing(new Message("auction",
                MessageTypes.UPDATE));
        System.out.println("Agent winner: "+ currentWinnerID);
    }

//    public static void main(String[] args)throws Exception {
//        Furniture f = Furniture.desk;
//        Auction bidCoord=new Auction(new Bid(new Item("desk",20,f.getIDType()),new Agent(1),20));
//        Thread t=new Thread(bidCoord);
//        t.start();
//        BufferedReader bf=new BufferedReader(new InputStreamReader(System.in));
//        String i;
//        while(!(i=bf.readLine()).equals("44")){
//            System.out.println("enter "+i);
//            bidCoord.placeBid(new Bid(new Item("desk",20,f.getIDType()),new Agent(Integer.parseInt(i)),Double.parseDouble(i)));
//        }
//        System.out.println("No longer taking input");
//    }
}
