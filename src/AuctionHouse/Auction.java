package AuctionHouse;

import Agent.Agent;
import Agent.Bid;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class Auction implements Runnable{

    private final int duration;
    private double bidAmount;
    private int currentBidderID;
    private BlockingQueue<Bid> bids;
    private BidProtocol bidProtocol;
    private int currentWinner;
    private Item item;
    private int time;
    private double bidToBeat;
    private Timer t;
    private boolean auctionActive;

    //todo
    //have way to send winner
    //relay out once  agent taken over, to release hold on pending.

    /**
     * Takes an item and sets auction up for the
     * item.
     * @param b An Item
     */
    public Auction(Bid b){
        duration=30;
        time=0;
        breakDownBid(b);
        item.setInBid(true);
        bids=new LinkedBlockingQueue<>();
        bidProtocol=new BidProtocol(item.getPrice());
        auctionActive=true;
        bidToBeat=item.getPrice();
        t=new Timer();
    }

    /**
     * Deconstructs the bid and gets the necessary component of a bid.
     */
    private void breakDownBid(Bid b){
        item=b.getItem();
        bidAmount=b.getAmount();
        currentBidderID=b.getBidder();
    }

    /**
     * Begins timer and runs until 30 seconds is reached.
     */
    private void setTime(){
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                if(time>=duration){
                    auctionActive=false;
                    t.cancel();
                    placeBid(new Bid(null,9,0));
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
     * @param bid
     * @return
     */
    private void analyzeBid(double bid){
        if(!auctionActive){
            System.out.println("Auction Over");
            return;
        }
        System.out.println(currentBidderID+" tries "+bid);
        if(bidProtocol.processBid(bid)!=0){
            currentWinner=currentBidderID;
            bidToBeat=bid;
            System.out.println("current winner "+currentWinner);
        }
        else{
            System.out.println(currentBidderID+" need to beat "+bidToBeat
                    +" ,you bid "+bid);
        }
    }

    /**
     * Puts bid onto queue.
     * @param b An int
     */
    public void placeBid(Bid b){
        try {
            if(b.getItem()!=null) breakDownBid(b);
            bids.put(b);
        }catch(InterruptedException i){
            System.out.println(i);
        }
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
        System.out.println("winner: "+currentWinner);
    }

    public static void main(String[] args)throws Exception {
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
    }
}
