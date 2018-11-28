package Agent;

import AuctionHouse.*;
import Bank.*;
import Proxies.AuctionHouseProxy;
import Proxies.BankProxy;

import java.io.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Agent.java is the class that bids on objects inside the auction house.
 * Danan High, 11/13/2018
 */
public class Agent implements Runnable {
    
    private final String NAME = "agent";
 
    private int id, key;
    private Account account = null;
    private AuctionHouseProxy auctionHouseProxy;
    private BankProxy bank;
    private Item item = null;
    private AuctionHouse auctionHouse = null;
    private ArrayList<AuctionHouse> houseList;
    private ArrayList<Item> itemList;
    private BlockingQueue<Message> messageQueue = new LinkedBlockingQueue<>();
    private boolean connected = true;
    private Scanner scanner = new Scanner(System.in);


    private static String hostName;
    private static int portNumber;

    /**
     * Constructor for the Agent.
     */
    public Agent(String hostName, int portNumber) {
        this.bank = new BankProxy(hostName,
                                  portNumber,
                                  this,
                                  null);
        this.auctionHouseProxy = new AuctionHouseProxy(hostName,
                                                       portNumber,
                                                       this,
                                                       null);
    }

    /**
     * Making a bid to an auction house.
     * @param bidAmount amount to bid on the item.
     */
    private void placeBid(int bidAmount) {
        Bid bid = new Bid(item, id, bidAmount);
        Message message = new Message("Agent", MessageTypes.BID, bid);
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
//        itemList = (ArrayList) auctionHouseProxy.getItemList();
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

    /*****************************************************************/
    /*                                                               */
    /*          Analyzing Feedback and User Input Functions          */
    /*                                                               */
    /*****************************************************************/

    /**
     * Getting and setting the new account information from the bank.
     */
    private synchronized Account openNewBankAccount() {
        if (account == null) {
            // getting the account name
            System.out.print("name: ");
            String name = scanner.next();

            // getting the account starting amount
            System.out.print("staring balance: ");
            double amount = scanner.nextDouble();
            System.out.println();
            
            return new Account(name, getId(), amount, amount);
        } else {
            System.out.println("Account is already created");
        }
        return null;
    }
    
    /**
     * Getting the user input for making a bid.
     * @return bid from the user
     */
    private Bid makeBid() {
        System.out.println("amount to bid");
        double amount = scanner.nextDouble();
        System.out.println();
        
        return new Bid(item, getId(), amount);
    }
    
    /**
     * Function to add to the list of messageQueue.
     */
    public void addMessage(Message message) {
        try {
            messageQueue.put(message);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }
    
    /**
     * Function to pass along messages to the appropriate proxy.
     */
    private void passMessage(AuctionHouseProxy ahp,
                             BankProxy bp,
                             Message m) {
        if (ahp != null) {
            ahp.sendMessage(m);
        } else if (bp != null) {
            bp.sendAgentMessage(m);
        }
    }

    /*****************************************************************/
    /*                                                               */
    /*          Analyzing Feedback and User Input Functions          */
    /*                                                               */
    /*****************************************************************/

    /**
     * Function to respond after message analysis
     */
    private synchronized Message response(Message message,
                                          MessageTypes type,
                                          int sender) {
//        if (analysis == 14) {
//            // update the account from the bank
//        } else if (analysis == 3) {
//            // confirmation that the bank has created an account
//            System.out.println("here in the confirmation");
//        } else if (analysis == 15) {
//            // set the auction house id for the specific auction house int
//            // make sure to have a current auction house
//        } else if (analysis == 4) {
//            // set the list of auction houses from the bank
//        } else if (analysis == 10) {
//            // bid has been denied
//        } else if (analysis == 16) {
//            // been outbid
//        } else if (analysis == 9) {
//            // bid has been accepted
//        } else if (analysis == 17) {
//            // the bid statuse
//        } else if (analysis == 11) {
//            // setting the item won (adding to a list of items won?)
//        }
        
        Message response = null;
        ArrayList<Object> list = message.getMessageList();
        
        switch (type) {
            case CONFIRMATION:
                response = new Message(NAME, MessageTypes.THANKS);
                if (sender == 2) {
                    passMessage(auctionHouseProxy,
                                null,
                                response);
                } else if (sender == 3) {
                    passMessage(null,
                                bank,
                                response);
                }
                break;
            case ACCOUNT_EXISTS:
                response = new Message(NAME, MessageTypes.THANKS);
                if (sender == 2) {
                    passMessage(auctionHouseProxy,
                                null,
                                response);
                } else if (sender == 3) {
                    passMessage(null,
                                bank,
                                response);
                }
                break;
//            case:
//                break;
//            case:
//                break;
//            case:
//                break;
//            case:
//                break;
//            case:
//                break;
//            case:
//                break;
//            case:
//                break;
        }
        
        return null;
    }
    
    /**
     * Displaying the user options for the menu.
     */
    private synchronized int displayUserOptions() {
        System.out.print("1. Create Account\n" +
                         "2. Get Auction Houses\n" +
                         "3. Get Items from Auction House\n" +
                         "4. Make Bid\n" +
                         "5. Get Account Information\n" +
                         "6. Choose an Item for Bidding\n" +
                         "7. Check Bid Status\n");
        System.out.print("Enter Option: ");
        return scanner.nextInt();
    }
    
    /**
     * Handling the choice of the user.
     */
    private synchronized void handleChoice(int choice) {
        if (choice == 1 && account == null) {
            account = openNewBankAccount();
            bank.sendAgentMessage(new Message(NAME,
                                              MessageTypes.CREATE_ACCOUNT,
                                              getId(),
                                              account));
        } else if (choice == 2) {
            bank.sendAgentMessage(new Message(NAME,
                                              MessageTypes.GET_HOUSES));
        } else if (choice == 3 && auctionHouse != null) {
            // for testing getting item list
            bank.sendAgentMessage(new Message(NAME,
                                              MessageTypes.GET_ITEMS,
                                              auctionHouse.getType()));
        } else if (choice == 4) {
            if (item == null) {
                System.out.println("Please choose an item before making a bid");
            } else {
                Bid bid = makeBid();
                bank.sendAgentMessage(new Message(NAME,
                                                  MessageTypes.BID,
                                                  bid));
            }
        } else if (choice == 5) {
            bank.sendAgentMessage(new Message(NAME,
                                              MessageTypes.ACCOUNT_INFO));
        } else if (choice == 6) {
        
        } else if (choice == 7) {
        
        } else {
            System.out.println("Response not recognized!");
        }
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
        Message in, respond = null;
        MessageAnalyzer analyzer = new MessageAnalyzer();

        try {
            while (connected) {
                
                handleChoice(displayUserOptions());
                System.out.println();
                
                if (messageQueue.size() > 0) {
                    in = messageQueue.take();
                    respond = response(in,
                                       (MessageTypes) in.getMessageList().get(1),
                                       analyzer.analyze(in));
                    if (respond != null) {
                        bank.sendAgentMessage(respond);
                    }
                }
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

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























