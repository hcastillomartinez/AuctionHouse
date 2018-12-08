package AuctionHouse;

import Agent.Bid;
import Bank.Account;
import Bank.AuctionInfo;
import MessageHandling.Message;
import MessageHandling.MessageTypes;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * When created it takes in one IP address, which should be the one it will
 * exist on, and initially does nothing until agents connect to it and start
 * placing bids.
 */
public class AuctionHouse implements Runnable {
    private int agentCount;
    private String type;
    private HouseMessageAnalyzer messageAnalyzer;
    private List<Item> itemList;
    private List<Auction> auctions;
    private List<Server> serverThreads;
    private boolean safeToClose;
    private boolean updateGUI;
    private MakeItems makeItems;
    private ServerSocket serverSocket;
    private Socket bankClient;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private int port;
    private String serverName;
    private BlockingQueue<Message> messages;
    private boolean auctionOpen;
    private Account account;
    static AuctionHouseGUI auctionHouseGUI;


    /**
     * Expects an int that represents what type
     * auction house will be. 1 for furniture, 2 for tech
     * and any other number for car.
     * @param type An int
     */
    public  AuctionHouse(String type,String port,String serverName,
                         String myServerName){

        agentCount = 0;
        auctionOpen = true;
        safeToClose=true;
        account=new Account(type,10,0,0);
        messageAnalyzer=new HouseMessageAnalyzer();
        makeItems = new MakeItems();
        messages = new LinkedBlockingQueue<>();
        itemList = makeItems.getItems(type);
        Collections.shuffle(itemList);
        serverThreads = new ArrayList<>();
        auctions = new ArrayList<>();
        this.type = makeItems.getListType();
        this.port = Integer.parseInt(port);
        this.serverName = serverName;
        try {
            bankClient=new Socket(serverName,4444);
            objectOutputStream=
                    new ObjectOutputStream(bankClient.getOutputStream());
            objectInputStream=
                    new ObjectInputStream(bankClient.getInputStream());
            sendToBank(new Message("auction house",
                    MessageTypes.CREATE_ACCOUNT,new AuctionInfo(this.type,
                    myServerName,0,Integer.parseInt(port))));
            handleMessagesFromBank();
            serverSocket = new ServerSocket(this.port);
        }catch(IOException i){
            i.printStackTrace();
        }
    }


    /******************************************************************/
    /*                                                                */
    /*                Getters and Setters                             */
    /*                                                                */
    /******************************************************************/

    /**
     * Used to update as a flag to update the GUI.
     * @param updateGUI
     */
    public void setUpdateGUI(boolean updateGUI) {
        this.updateGUI = updateGUI;
    }

    /**
     * Used to check if gui needs to be updated
     * @return A boolean
     */
    public boolean isUpdateGUI() {
        return updateGUI;
    }

    /**
     * Checks if auction house taking in anything else.
     * @return boolean, whether or not AH is open.
     */
    public boolean getAuctionStatus(){
        return auctionOpen;
    }

    /**
     * Gets the auctions created
     * @return A list of auctions
     */
    public List<Auction> getAuctions() {
        return auctions;
    }

    /**
     * Used to set the auction to be able to be closed by the GUI. Done when
     * safe or not safe.
     * @param safeToClose boolean that is state safe to close.
     */
    public void setSafeToClose(boolean safeToClose) {
        this.safeToClose = safeToClose;
    }

    /**
     * Used to ask whether connection is safe to close.
     * @return A boolean whether auction house can be closed.
     */
    public boolean isSafeToClose() {
        return safeToClose;
    }

    /**
     * Gets the account of the AH.
     * @return Account, holds info for bank.
     */
    public Account getAccount(){
        return account;
    }

    /**
     * Gets the amount of money a house has.
     * @return Amount of money the house has, double.
     */
    public double getHouseFunds() {
        return account.getBalance();
    }

    /**
     * Gets the ID of the auction house.
     * @return An int, is the ID
     */
    public int getHouseID() {
        return account.getAccountNumber();
    }


    /**
     * Used to get port number that server is on.
     * @return An int, is the port number.
     */
    public int getPort(){
        return port;
    }

    /**
     * Used to remove a specific item from its item list if match with item
     * name is found. Then begins process to update the GUI.
     * @param item Item to be removed
     * @return boolean to whether or not item was removed.
     */
    public boolean removeItem(Item item){
        for(Item i:itemList){
            if(item.getItemName().equals(i.getItemName())){
                itemList.remove(i);
                setUpdateGUI(true);
                return true;
            }
        }
        return false;
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



    /******************************************************************/
    /*                                                                */
    /*              Possible actions from auction house               */
    /*                                                                */
    /******************************************************************/

    /**
     * Where based off incoming message, sends back the appropriate response.
     * @param m Message that will be analyzed and responded to.
     */
    private synchronized void doAction(Message m){
        int action = messageAnalyzer.analyzeMessage(m);
        if(action == 1){
           int id= (int)m.getMessageList().get(m.getMessageList().size()-1);
           sendToServer(id,new Message("auction house",
                   MessageTypes.GET_ITEMS,itemList));
        }else if(action == 2){
            int id= (int)m.getMessageList().get(m.getMessageList().size()-1);
            tryBid((Bid) m.getMessageList().get(2),id,
                    (int)m.getMessageList().get(3));
        }else if(action == 3){
            updateAccount((double) m.getMessageList().get(2),
                          (double) m.getMessageList().get(3),
                          (int) m.getMessageList().get(4));
            setUpdateGUI(true);
        }else if(action==4){
               setUpdateGUI(true);
        }else if(action==5){
            updateItemList((Item)m.getMessageList().get(2));
            setUpdateGUI(true);
        }else if(action==6){
            setSafeToClose(true);
        }else if(action==7){
            setSafeToClose(false);
        }

    }

    /**
     * Used to add money to the funds of house from bank.
     * @param acntNum double that is funds to be added
     */
    private synchronized void updateAccount(double bal, double penBal, int acntNum) {
        this.account.setBalance(bal);
        this.account.setPendingBalance(penBal);
        this.account.setAccountNumber(acntNum);
    }

    /**
     * Updates the internal item to have updated price.
     * @param item
     */
    private void updateItemList(Item item){
        for(Item i: itemList){
            if(item.getItemName().equals(i.getItemName())){
                i.updatePrice(item.getPrice());
            }
        }
    }

    /**
     * Creates an auction for an item if one does not already
     * exist.
     * @param b, A Bid
     */
    private synchronized void createAuction(Bid b,int serverThreadID,
                                            int accountNumber){
        if(!b.getItem().isInBid()){
            Auction a = new Auction(this,b.getItem(),b,serverThreadID,accountNumber);
            auctions.add(a);
            setUpdateGUI(true);
            Thread t = new Thread(a);
            t.start();
        }
        else{
            System.out.println(b.getItem()+" sold already.");
        }
    }

    /**
     * Finds the correct auction to pass bid to. If auction not found a new
     * one is created for the item they are trying to bid on.
     * @param b, A Bid
     */
    private synchronized void tryBid(Bid b,int serverThreadID,
                                     int accountNumber){
        for(Auction a: auctions){
            if(a.getItem().getItemName().equals(b.getItem().getItemName())){
                a.placeBid(b,serverThreadID,accountNumber);
                setUpdateGUI(true);
                return;
            }
        }
        if(b.getItem().getPrice()>b.getAmount()){
            sendToServer(serverThreadID,new Message("auction house",
                    MessageTypes.BID_REJECTED,b));
            return;
        }
        else createAuction(b,serverThreadID,accountNumber);
    }

    /******************************************************************/
    /*                                                                */
    /*              Local Message Handling                            */
    /*                                                                */
    /******************************************************************/

    /**
     * Used to get messages from the server threads and the auction.
     */
    private void messageWait(){
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        doAction(messages.take());
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



    /******************************************************************/
    /*                                                                */
    /*     Socket code and handling of messages for socket            */
    /*                                                                */
    /******************************************************************/

    /**
     * Handles the connections of agents and the messages coming through
     * their sockets.
     */
    private class Server implements Runnable {
        private Socket client;
        private int ID;
        private AuctionHouse auctionHouse;
        private boolean connected;
        private ObjectInputStream in;
        private ObjectOutputStream out;

        public Server(Socket client,
                      int id
                ,AuctionHouse a) throws IOException {
            this.client = client;
            connected=true;
            auctionHouse = a;
            this.ID = id;
            out = new ObjectOutputStream(client.getOutputStream());
            in = new ObjectInputStream(client.getInputStream());
        }

        /**
         * Writes to the client
         * @param m Message that client will receive.
         */
        public synchronized void placeMessage(Message m){
            try{
                out.writeObject(m);
                out.reset();
            }catch(IOException i){
                i.printStackTrace();
            }
        }

        /**
         * Adds ID of server thread that way when passes through the auction
         * house it knows which of the server threads to send response to.
         * @param m Message that ID will be appended to.
         */
        private void addID(Message m){
            m.getMessageList().add(ID);
        }

        /**
         * Used for closing cleanly, used to check if client is still connected.
         * @return boolean whether still connected or not.
         */
        public boolean isConnected(){
            return connected;
        }


        /**
         * Gets the ID of the server thread.
         * @return ID of the serve thread.
         */
        public int getID() {
            return ID;
        }

        /**
         * Goes through all socket connection and closes them.
         */
        private void closeClient(){
            try{
                in.close();
                out.close();
                client.close();
            }catch(IOException i){
                i.printStackTrace();
            }
        }

        @Override
        public void run()  {
            while(connected){
                try
                {
                    Message m = (Message) in.readObject();
                    if (m != null)
                    {
                        addID(m);
                        auctionHouse.placeMessageForAnalyzing(m);
                    }
                }catch (EOFException se) {
                    connected = !connected;
                    closeClient();
                }catch(SocketException i){
                    connected = !connected;
                    closeClient();
                    i.printStackTrace();
                }catch(ClassNotFoundException i){
                    i.printStackTrace();
                }catch(IOException i){
                    System.out.println(i);
                }
            }
        }
    }



    /**
     * Closes all the server thread's clients and also the server socket.
     */
    private void closeAllSockets(){
        for(Server s: serverThreads){
            s.closeClient();
        }
        try {
            serverSocket.close();
        }catch(IOException i){
            i.printStackTrace();
        }
    }

    /**
     * Based off the id, passes message to correct server thread.
     * @param ID int that is ID of server thread.
     * @param m Message that needs to be sent to server thread.
     */
    public void sendToServer(int ID,Message m){
        for(Server server:serverThreads){
            if(ID==server.getID()){
                server.placeMessage(m);
            }
        }
    }

    /**
     * Message passed through will send be sent to all agents currently
     * connected.
     * @param m Message that will be relayed to all agents connected.
     */
    public void sendToAllClient(Message m){
        for(Server s: serverThreads){
            if(s.isConnected()){
                s.placeMessage(m);
            }
        }
    }

    /**
     * Thread that listens for incoming messages from the bank, these
     * messages are then placed into queue to be analyzed.
     */
    private void handleMessagesFromBank(){
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                boolean connected =true;
                try {
                    while(connected) {
                        Message m = (Message) objectInputStream.readObject();
                        if (m != null) {
                            placeMessageForAnalyzing(m);
                        }
                    }
                }catch(EOFException i){
                    connected=false;
                    i.printStackTrace();
                } catch(IOException i){
                    i.printStackTrace();
                }catch(ClassNotFoundException i){
                    i.printStackTrace();
                }
            }
        });
        t.start();
    }

    /**
     * Writes out messages to the bank.
     * @param m Message to be passed to bank.
     */
    public synchronized void sendToBank(Object m){
        try{
            objectOutputStream.writeObject(m);
        }catch(IOException i){
            i.printStackTrace();
        }
    }


    /**
     * Listens for incoming socket connections and places them into threads
     * to handle each one separately  while connected.
     */
    @Override
    public void run(){
        messageWait();
        while(auctionOpen){
            try {
                Socket agent = serverSocket.accept();
                agentCount++;
                Server server = new Server(agent,agentCount,this);
                serverThreads.add(server);
                Thread t =new Thread(server);
                t.start();
            }catch(IOException i){
                i.printStackTrace();
            }
        }
        closeAllSockets();
    }


    public static void main(String[] args){
        AuctionHouseGUI.launch(args);
    }
}
