package AuctionHouse;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class BidCoord implements Runnable{

    private BlockingQueue<Integer> bids;
    private BidProtocol bidProtocol;
    private long currentWinner;
    private Item item;
    private int time;
    private int winningBid;
    private Timeline timeLine;
    private Timer t;


    public BidCoord(Item i){
        item=i;
        item.setInBid(true);
        bids=new LinkedBlockingDeque<>();
        bidProtocol=new BidProtocol(item.getPrice());
        time=0;
        currentWinner=0;
        winningBid=0;
        t=new Timer();
    }

    private void setTime(){
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                if(time==30)t.cancel();
                time++;
                System.out.println(time);
            }
        },20,1000);
    }

    private void resetTime(){
        time=0;
    }

    private void getBid(){
        try{
            analyzeBid(bids.take());
        }catch(InterruptedException i){
            System.out.println(i);
        }
    }
    private int analyzeBid(int bid){
        if(time==30)return 1;
        if(bidProtocol.processBid(bid)==0)return 0;
        else{
            winningBid=bid;
            return 1;
        }
    }

    public void placeBid(int bid){
        try {
            bids.put(bid);
        }catch(InterruptedException i){
            System.out.println(i);
        }
    }


    @Override
    public void run(){
        while(time<=30){
            getBid();
            if(time==0)setTime();
        }
        System.out.println(winningBid);
    }

    public static void main(String[] args)throws Exception{
        Furniture f=Furniture.desk;
        BidCoord bidCoord=new BidCoord(new Item("desk",20,f.getIDType()));
        Thread t=new Thread(bidCoord);
        t.start();

        BufferedReader bf=new BufferedReader(new InputStreamReader(System.in));
        int i;
        while((i=bf.read())!=44){
            bidCoord.placeBid(i);
        }
    }
}
