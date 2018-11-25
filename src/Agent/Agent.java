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
    private LinkedBlockingQueue<TestMessage<Object, Object>> messages
        = new LinkedBlockingQueue<>();
    private boolean connected = true;
    
    private static String hostName;
    private static int portNumber;

    /**
     * Constructor for the Agent.
     */
    public Agent() {
        auctionHouseProxy = new AuctionHouseProxy();
        bank = new BankProxy(hostName, portNumber, this);
    }

    /**
     * Making a bid to an auction house.
     * @param bidAmount amount to bid on the item.
     */
    private void placeBid(int bidAmount) {
        Bid bid = new Bid(item, this, bidAmount);
        Message message = new Message("bid");
//        auctionHouseProxy.placeBid(message);
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
    
    /**
     * Setting the connected status to not connected.
     */
    public void setConnected() { connected = !connected; }

    /**
     * Getting and setting the new account information from the bank.
     */
    private Account openNewBankAccount() {
        synchronized (account) {
            Scanner scanner = new Scanner(System.in);
            if (account != null) {
                if (account != null) {
                    // getting the account name
                    System.out.print("name: ");
                    String name = scanner.next();
                    System.out.println();
        
                    // getting the account starting amount
                    System.out.print("staring balance: ");
                    Double amount = scanner.nextDouble();
                    System.out.println();
        
                    return bank.openAccount(name, amount);
                }
            }
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
        account.toString();
    }

    /**
     * Function to handle the analyzing of the messages.
     */
    @SuppressWarnings("unchecked")
    private void analyzeMessages(TestMessage<Object, Object>
                                     message) {
        if (message.getSender()
                   .getClass()
                   .equals(AuctionHouseProxy.class)) {
            if (message.getDetailedMessage()
                       .getClass()
                       .equals(Item.class)) {
                itemList.add((Item) message.getDetailedMessage());
            } else if (message.getDetailedMessage()
                              .getClass()
                              .equals(String.class)) {
                String mail = (String) message.getDetailedMessage();
            }
        } else if (message.getSender()
                          .getClass()
                          .equals(BankProxy.class)) {
            if (message.getDetailedMessage()
                       .getClass()
                       .equals(Account.class)) {
                if (account != null) {
                    account = (Account) message.getDetailedMessage();
                }
            } else if (message.getDetailedMessage()
                              .getClass()
                              .equals(String.class)) {
                String mail = (String) message.getDetailedMessage();
                if (mail.equalsIgnoreCase("name and amount")) {
                    bank.sendMessage(this,
                                     new TestMessage<Agent, Account>(this,
                                                                     openNewBankAccount()));
                }
            }
        }
    }
    
    /**
     * Function to add to the list.
     */
    public void addMessage(TestMessage<Object, Object> testMessage) {
        messages.add(testMessage);
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
        try {
            String out = null;
            AuctionHouse holder;
            BufferedReader userIn =
                    new BufferedReader(new InputStreamReader(System.in));
            TestMessage<Object, Object> in = null;
            
            connectToBank();
            
            while (connected) {
                if (messages.size() > 0) {
                    in = messages.take();
                    
                    analyzeMessages(in);
                }

                out = userIn.readLine();
                if (out != null) {
                    if (out.equalsIgnoreCase("bye")) {
                        connected = false;
                    }
                    bank.sendMessage(out, this);
                    out = null;
                }
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
    
    /**
     * Overriding toString to print out the class.
     */
    @Override
    public String toString() { return id + " has balance: "; }

    /**
     * Main method to start the program for the user/agent.
     */
    public static void main(String[] args) throws IOException {
        hostName = args[0];
        portNumber = Integer.parseInt(args[1]);
        Agent agent = new Agent();
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























