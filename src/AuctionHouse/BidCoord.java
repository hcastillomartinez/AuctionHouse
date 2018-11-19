package AuctionHouse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class BidCoord implements Runnable{

    private final int duration;
    private BlockingQueue<Integer> bids;
    private BidProtocol bidProtocol;
    private long winnerID;
    private long currentWinner;
    private Item item;
    private int time;
    private int winningBid;
    private Timer t;
    private boolean auctionActive;

    /**
     * Takes an item and sets auction up for the
     * item.
     * @param i An Item
     */
    public BidCoord(Item i){
        duration=30;
        item=i;
        item.setInBid(true);
        bids=new LinkedBlockingDeque<>();
        bidProtocol=new BidProtocol(item.getPrice());
        time=0;
        auctionActive=true;
        winningBid=0;
        t=new Timer();
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
                    placeBid(-1);
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
        time=0;
    }

    /**
     * Takes from queue and passes it to be
     * analyzed.
     */
    private void getBid(){
        try{
            analyzeBid(bids.take());
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
    private void analyzeBid(int bid){
        if(!auctionActive){
            System.out.println("Auction Over");
            return;
        }
        System.out.println("try "+bid);
        if(bidProtocol.processBid(bid)!=0){
            winningBid=bid;
            System.out.println("current winner "+winningBid);
        }
        else{
            System.out.println("Need to beat "+winningBid+" ,you bid "+bid);
        }
    }

    /**
     * Puts bid onto queue.
     * @param bid An int
     */
    public void placeBid(int bid){
        try {
            bids.put(bid);
        }catch(InterruptedException i){
            System.out.println(i+" taking");
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
        System.out.println("winner: "+winningBid);
    }

    public static void main(String[] args)throws Exception{
        Furniture f=Furniture.desk;
        BidCoord bidCoord=new BidCoord(new Item("desk",20,f.getIDType()));
        Thread t=new Thread(bidCoord);
        t.start();
        BufferedReader bf=new BufferedReader(new InputStreamReader(System.in));
        String i;
        while(!(i=bf.readLine()).equals("44")){
            System.out.println("enter "+i);
            bidCoord.placeBid(Integer.parseInt(i));
        }
        System.out.println("No longer taking input");
    }
}
