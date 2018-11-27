package Agent;

import AuctionHouse.*;
import Bank.*;
import Proxies.AuctionHouseProxy;
import Proxies.BankProxy;

import java.io.*;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Agent.java is the class that bids on objects inside the auction house.
 * Danan High, 11/13/2018
 */
public class Agent implements Runnable {

    private int id, key;
    private Account account = null;
    private AuctionHouseProxy auctionHouseProxy;
    private BankProxy bank;
    private Item item = null;
    private AuctionHouse auctionHouse = null;
    private ArrayList<AuctionHouse> houseList;
    private ArrayList<Item> itemList;
    private LinkedBlockingQueue<TestMessage<Object, Object>> messages = new LinkedBlockingQueue<>();
    private boolean connected = true;
    
    private static String hostName;
    private static int portNumber;

    /**
     * Constructor for the Agent.
     */
    public Agent(String hostName, int portNumber) {
        this.hostName = hostName;
        this.portNumber = portNumber;


        this.bank = new BankProxy(this.hostName,
                                  this.portNumber,
                                  this,
                                  null);
    }

    /**
     * Making a bid to an auction house.
     * @param bidAmount amount to bid on the item.
     */
    private void placeBid(int bidAmount) {
        Bid bid = new Bid(item, this, bidAmount);
        Message message = new Message("bid");
    }

    /**
     * Set chosen auction house from the bank.
     */
    private void setAuctionHouse() {
        // using a random house for testing
        auctionHouse = houseList.get((new Random()).nextInt(houseList.size()));
    }
    
    /**
     * Get Auction houses from the bank list
     */
    private void getAuctionHouses() {
        houseList = bank.getAuctionHouses();
        setAuctionHouse();
    }

    /**
     * Returning the id of the Agent.
     * @return id of the agent.
     */
    public int getId() { return id; }

    /**
     * Returning the key of the Agent.
     * @return key of the agent at the specific house.
     */
    public int getKey() { return key; }

    /**
     * Get items from the auction house that can be bid on.
     */
    @SuppressWarnings("unchecked")
    private void getItems() {
        itemList = (ArrayList) auctionHouseProxy.getItemList();
    }

    /**
     * Setting the key from the bank at a specific auction house.
     */
    private void setKey() {
//        key = bank.setKey();
    }

    public void setBank(BankProxy bank) { this.bank = bank; }
    
    /**
     * Setting the connected status to not connected.
     */
    public void setConnected() { connected = !connected; }

    /**
     * Getting and setting the new account information from the bank.
     */
    private synchronized Account openNewBankAccount() {
        Scanner scanner = new Scanner(System.in);
        if (account == null) {
            // getting the account name
            System.out.print("name: ");
            String name = scanner.next();

            // getting the account starting amount
            System.out.print("staring balance: ");
            double amount = scanner.nextDouble();
            System.out.println();
        
            return new Account(name, getId(), amount, amount);
        }
        return null;
    }

    /**
     * Connecting to an auction house.
     */
    private void connectToAuctionHouse() {
        String auctionHouseHostName = auctionHouse.getServerName();
        int auctionHousePortNumber = auctionHouse.getPort();

        auctionHouseProxy.connectToAuctionHouse(auctionHouseHostName,
                                                auctionHousePortNumber);
    }

    /**
     * Connecting to the bank.
     */
    private void connectToBank() {
        bank.connectToServer();
    }
    
    /**
     * Function to add to the list of messages.
     */
    public void addMessage(TestMessage<Object, Object> testMessage) {
        try {
            System.out.println(messages);
            messages.put(testMessage);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    /**
     * Function to respond after message analysis
     */
    private TestMessage<Object, Object> response(int analysis) {
        if (analysis == 14) {
            // update the account from the bank
        } else if (analysis == 3) {
            // confirmation that the bank has created an account
            System.out.println("here");
        } else if (analysis == 15) {
            // set the auction house id for the specific auction house int
            // make sure to have a current auction house
        } else if (analysis == 4) {
            // set the list of auction houses from the bank
        } else if (analysis == 10) {
            // bid has been denied
        } else if (analysis == 16) {
            // been outbid
        } else if (analysis == 9) {
            // bid has been accepted
        } else if (analysis == 17) {
            // the bid statuse
        } else if (analysis == 11) {
            // setting the item won (adding to a list of items won?)
        }
        return null;
    }

    /*****************************************************************/
    /*                                                               */
    /*                         Override Functions                    */
    /*                                                               */
    /*****************************************************************/

    /**
     * Overrides run to perform certain tasks.
     */
    @Override
    public void run() {
        TestMessage<Object, Object> in = null;
        MessageAnalyzer analyzer = new MessageAnalyzer();

        try {
            // look back here
            bank.sendAgentMessage(this, openNewBankAccount());
            connectToBank();

            while (connected) {
                if (messages.size() > 0) {
                    in = messages.take();
                    bank.sendAgentMessage(this, response(analyzer.analyze(in)));
                }
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }
    
    /**
     * Overriding toString to print out the class.
     */
//    @Override
//    public String toString() { return id + " has balance: "; }

    /**
     * Main method to start the program for the user/agent.
     */
    public static void main(String[] args) throws IOException {
        hostName = args[0];
        portNumber = Integer.parseInt(args[1]);
        Agent agent = new Agent(hostName, portNumber);
        (new Thread(agent)).start();
    }
}

/*
    Agent is dynamically created

    opens a bank account by providing a name and an initial balance

    receives a unique account number

    gets from the bank a list of active auctions

    asks an auction house for a list of items being auctioned

    gets from the bank a secret key to be used when interacting with a specific
    auction house

    Gets replies from the auction house:
        - acceptance
        - rejection
        - pass (higher bid in place)
        - winner

    notifies the bank to transfer the blocked funds to the auction house when
    winning a bid

    terminates and closes the account when no bidding action is in progress

    Will have proxies to bank and auction house
        - proxy for house will communicate with specific auction houses
        - proxy for the bank will communicate with the bank
             - bank will contain list of all of the auction houses

    Will have to check if the bank is running before trying to connect,
    connecting the the bank is the first thing done.
        - best way to prevent crashing
        - once you discover the server is running then you can connect to the
          bank.

    Make sure you are not trying to reference an empty/dead auction house
        - if the house is dead get rid of the reference


    -------------------Design---------------------
    For the bank get the server of the machine running on and use that
    for the server constructor arg

    Think about how protocols should be designed and implemented

    User commands
        - bids
        - withdraws
        - check items in the houses
    Active bids
    User Display
    User Account
        - add account
        - create account
    Will need to have host names passed around in the proper way
    Auction House proxy
        - to connect to the auction house:
            - need port number
            - need host name
        -> Notification Server (has own thread, will block on reads)
            - will store the messages and wait for the auction house proxy to
              obtain messages
        -> Communication Server
    Bank Proxy
        -> Communication Server (will hide all the socket communication and will
                                 send the message to the desired location)


    { -> = reference to object }

 */























