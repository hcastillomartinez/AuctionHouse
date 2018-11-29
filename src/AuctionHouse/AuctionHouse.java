package AuctionHouse;

import Agent.Bid;
import Agent.Message;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AuctionHouse implements Runnable {
    private int agentCount;
    private String type;
    private double houseFunds;
    private int houseID;
    private List<Item> itemList;
    private List<Auction> auctions;
    private List<Server> serverThreads;
    private MakeItems makeItems;
    private ServerSocket serverSocket;
    private int port;
    private String serverName;
    private BlockingQueue<Message> messages;


    /**
     * Expects an int that represents what type
     * auction house will be. 1 for furniture, 2 for tech
     * and any other number for car.
     * @param type An int
     */
    public AuctionHouse(String type,String port,String serverName){
        agentCount=0;
        houseFunds=0;
        houseID=((int)(Math.random()*60000)+30000)
                -((int)(Math.random()*30000)+1);
        makeItems = new MakeItems();
        messages=new LinkedBlockingQueue<>();
        itemList = makeItems.getItems(Integer.parseInt(type));
        serverThreads=new ArrayList<>();
        auctions=new ArrayList<>();
        this.type = makeItems.getListType();
        this.port=Integer.parseInt(port);
        this.serverName=serverName;
        try {
            serverSocket = new ServerSocket(this.port);
        }catch(IOException i){
            i.printStackTrace();
        }
    }


    /**
     * Gets the amount of money a house has.
     * @return Amount of money the house has, double.
     */
    public double getHouseFunds() {
        return houseFunds;
    }

    /**
     * Gets the ID of the auction house.
     * @return An int, is the ID
     */
    public int getHouseID() {
        return houseID;
    }

    /**
     * Used to add money to the funds of house from bank.
     * @param houseFunds double that is funds to be added
     */
    public void setHouseFunds(double houseFunds) {
        this.houseFunds+=houseFunds;
    }

    /**
     * Used to get port number that server is on.
     * @return An int, is the port number.
     */
    public int getPort(){
        return port;
    }

    /**
     * Used to get the name of computer that server is running
     * on.
     * @return Name of server,String
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * Returns the items for sale in auction house.
     * @return a list.
     */
    public synchronized List<Item> getItemList(){
        return itemList;
    }

    /**
     * Finds the most expensive item
     * in auction is and gets the price.
     * @return An int that is max price
     */
    public double maxPrice(){
        double max=0;
        for(Item t:itemList){
            if(t.getPrice()>max)max=t.getPrice();
        }
        return max;
    }

    /**
     * Gets the type of items sold at an
     * auction.
     * @return A string that is type.
     */
    public String getType() {
        return type;
    }

    /**
     * Finds the cheapest item in auction
     * and gets its price.
     * @return An int that is the lowest price.
     */
    public double lowestPrice(){
        double min=10000;
        for(Item t:itemList){
            if(t.getPrice()<min)min=t.getPrice();
        }
        return min;
    }


    /******************************************************************/
    /*                                                                */
    /*              Possible actions from auction house               */
    /*                                                                */
    /******************************************************************/


    private void doAction(Message m){

    }

    /**
     * Creates an auction for an item if one does not already
     * exist.
     * @param b, A Bid
     */
    private void createAuction(Bid b){
        if(!b.getItem().isInBid()){
            Auction a=new Auction(b);
            auctions.add(a);
            Thread t=new Thread(a);
            t.start();
        }
    }

    /**
     * Finds the correct auction to pass bid to. If auction not found a new
     * one is created for the item they are trying to bid on.
     * @param b, A Bid
     */
    private void tryBid(Bid b){
        for(Auction a: auctions){
            if(a.getItem().equals(b.getItem())){
                a.placeBid(b);
                break;
            }
        }
        createAuction(b);
    }

    /**
     * Handles the connections of agents and the messages coming through
     * their sockets.
     */
    private class Server implements Runnable {
        private Socket client;
        private int ID;
        private AuctionHouse auctionHouse;
        private BufferedReader stdIn;
        private ObjectInputStream in;
        private ObjectOutputStream out;

        public Server(Socket client,
                      int id
                ,AuctionHouse a) throws IOException {
            this.client = client;
            auctionHouse=a;
            this.ID=id;
            in=new ObjectInputStream(client.getInputStream());
            out=new ObjectOutputStream(client.getOutputStream());
            out.writeObject("connected to "+ID);
        }

        /**
         * Gets the ID of the server thread.
         * @return
         */
        public int getID() {
            return ID;
        }

        @Override
        public void run()  {
            while(true){
                try{
                    Message m = (Message) in.readObject();
                    if(m!=null){
                        auctionHouse.placeMessageForAnalyzing(m);
                    }
                }catch(IOException i){
                    i.printStackTrace();
                }catch(ClassNotFoundException i){
                    i.printStackTrace();
                }
            }
        }
    }

    /**
     * Used to get messages from the server and the auction.
     *
     */
    private void messageWait(){
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        System.out.println("waiting for message");
                        //todo
                        //here is where auction house decides what to do
                        //and upon completion should say it has finished.
                        messages.take();
                    } catch (InterruptedException i) {
                        i.printStackTrace();
                    }
                }
            }
        });
        t.start();
    }

    /**
     * Puts message in queue so that AH can take
     * appropriate action.
     * @param m, Message to be analyzed.
     */
    public void placeMessageForAnalyzing(Message m){
        try{
            messages.put(m);
        }catch(InterruptedException i){
            i.printStackTrace();
        }
    }


    @Override
    public void run(){
        messageWait();
        while(true){
            try {
                System.out.println("waiting for agents");
                Socket agent = serverSocket.accept();
                agentCount++;
                Server server=new Server(agent,agentCount,this);
                serverThreads.add(server);
            }catch(IOException i){
                i.printStackTrace();
            }
        }
    }


    public static void main(String[] args){
        AuctionHouse auctionHouse=new AuctionHouse(args[0],args[1],args[3]);
        Thread t=new Thread(auctionHouse);
        t.start();
    }
}
