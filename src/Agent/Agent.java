package Agent;

import AuctionHouse.*;
import Bank.*;
import MessageHandling.Message;
import MessageHandling.MessageAnalyzer;
import MessageHandling.MessageTypes;
import Proxies.AuctionHouseProxy;
import Proxies.BankProxy;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Agent.java is the class that bids on objects inside the auction house.
 * @author Danan High, 11/13/2018
 */
public class Agent implements Runnable {
    
    private final String NAME = "agent";
 
    private int id, key, accountNumber;
    private Account account = null;
    private AuctionHouseProxy aHProxy;
    private BankProxy bank;
    private Item item = null;
    private AuctionHouse auctionHouse = null;
    private ArrayList<AuctionInfo> houseList;
    private ArrayList<Item> itemList;
    private BlockingQueue<Message> messageQueue;
    private HashMap<String, Integer> auctionHouseMap;
    private HashMap<AuctionInfo, AuctionHouseProxy> houseProxyMap;
    private boolean connected = true;
    private Scanner scanner = new Scanner(System.in);
    private String hostName;
    private int portNumber;

    // tester value for the auction house info
    private HashMap<Item, Bid> itemBidMap;
    private AuctionInfo auctionInfo;

    
    /**
     * Constructor for the Agent.
     */
    public Agent(String hostName, int portNumber) {
        itemList = new ArrayList<>();
        messageQueue = new LinkedBlockingQueue<>();
        auctionHouseMap = new HashMap<>();
        houseList = new ArrayList<>();
        houseProxyMap = new HashMap<>();
        itemBidMap = new HashMap<>();

        this.bank = new BankProxy(hostName,
                                  portNumber,
                                  this);
    }

    /**
     * Getting the auction info that holds the status of the house.
     * @return auctionInfo object
     */
    public AuctionInfo getAuctionInfo() { return auctionInfo; }

    /**
     * Getting the current item.
     * @return currentItem selected.
     */
    public Item getItem() { return item; }

    /**
     * Getting the correct auction house proxy to send the message.
     * @param info of the auction house
     * @return auctionHouseProxy to send the messages
     */
    public AuctionHouseProxy getAHProxy(AuctionInfo info) {
        return houseProxyMap.get(info);
    }

    /**
     * Getting the port number.
     * @return portNumber for the client
     */
    public int getPortNumber() { return portNumber; }

    /**
     * Getting the host name.
     * @return host name for the client.
     */
    public String getHostName() { return hostName; }

    /**
     * Setting the auction house for the agent.
     * @param auctionInfo house for functionality
     */
    public void setAuctionHouse(AuctionInfo auctionInfo) {
        this.auctionInfo = auctionInfo;
    }

    /**
     * Getting the house list from the agent.
     * @return list of the houses
     */
    public ArrayList<AuctionInfo> getHouseList() {
        return houseList;
    }

    /**
     * Returning the name of the class for messages.
     * @return NAME of the class object
     */
    public String getNAME() { return NAME; }

    /**
     * Returning the bank from the Agent.
     * @return bank to send messages to
     */
    public BankProxy getBank() {
        return bank;
    }
    
//    /**
//     * Returning the list of auction house infos.
//     * @return list of the auction house infos
//     */
//    public Integer getCurrentAuctionID() {
//        if (auctionInfo != null) {
//            return auctionInfo.getAuctionID();
//        }
//        return null;
//    }

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
//        itemList = (ArrayList) aHProxy.getItemList();
    }

    /**
     * Setting the key from the bank at a specific auction house.
     */
    private void setKey() {
//        key = bank.setKey();
    }

    /**
     * Setting the account of the agent.
     * @param account for the agent
     */
    public void setAccount(Account account) { this.account = account; }

    /**
     * Setting the connected status to not connected.
     */
    public void setConnected() { connected = !connected; }

    /*****************************************************************/
    /*                                                               */
    /*               Functions For Actions Based On Input            */
    /*                                                               */
    /*****************************************************************/

    /**
     * Getting and setting the new account information from the bank.
     */
    private synchronized Account openNewBankAccount() {
        if (account == null) {
            // getting the account first name
            System.out.print("First Name: ");
            String firstName = scanner.next();
    
            // getting the account last name
            System.out.print("Last Name: ");
            String lastName = scanner.next();

            // getting the account starting amount
            System.out.print("staring balance: ");
            double amount = scanner.nextDouble();
            System.out.println();
            
            return new Account(firstName + " " + lastName,
                               getId(),
                               amount,
                               amount);
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
     * Function to handle rebidding.
     */
    private void reBid(Message m) {
        MessageTypes reject = (MessageTypes) m.getMessageList().get(1);
        System.out.println(reject.getMessage()+ ". New bid?(y/n)");
        String answer = scanner.next();
        
        if (answer.contains("y")) {
            aHProxy.sendMessage(new Message(NAME, MessageTypes.BID, makeBid()));
        }
    }
    
    /**
     * Assigning the id for the AuctionHouse.
     */
    private void assignAHID(Message m) {
        String house = (String) m.getMessageList().get(2);
        int houseID = (int) m.getMessageList().get(3);
        if (auctionHouseMap.containsKey(house)) {
            auctionHouseMap.replace(house, houseID);
        } else {
            auctionHouseMap.put(house, houseID);
        }
    }
    
    /**
     * Responding to the sender of the message.
     */
    private void respondToSender(int sender,
                                 Message outMessage,
                                 AuctionHouseProxy proxy) {
        if (sender == 2) {
            proxy.sendMessage(outMessage);
        } else if (sender == 3) {
            bank.sendAgentMessage(outMessage);
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
    @SuppressWarnings("unchecked")
    private synchronized void response(Message message,
                                       MessageTypes type,
                                       int sender) {
        Message response;
        ArrayList<Object> list = message.getMessageList();
        
        switch (type) {
            case CONFIRMATION:
                response = new Message(NAME, MessageTypes.THANKS);
                respondToSender(sender, response, getAHProxy(auctionInfo));
                break;
            case TRANSFER_ITEM:
                Bid bid = (Bid) list.get(2);
                response = new Message(NAME,
                                       MessageTypes.REMOVE_FUNDS,
                                       getId(),
                                       bid.getAmount());
                respondToSender(sender, response, getAHProxy(auctionInfo));
                break;
            case BANK_ACCOUNT:
                accountNumber = (int) list.get(2);
                break;
            case ID_FOR_HOUSE:
                assignAHID(message);
                break;
            case HOUSES:
                houseList = (ArrayList<AuctionInfo>) list.get(2);
                System.out.println(houseList);
                break;
            case BID_REJECTED:
                reBid(message);
                break;
            case BID_ACCEPTED:
                response = new Message(NAME, MessageTypes.THANKS);
                respondToSender(sender, response, getAHProxy(auctionInfo));
                break;
            case OUT_BID:
                reBid(message);
                break;
            case ITEMS:
                if (list.get(2).equals(ArrayList.class)) {
                    itemList = (ArrayList) list.get(2);
                }
                break;
            case ACCOUNT_INFO:
                account = (Account) list.get(2);
                break;
            case UNBLOCK_FUNDS:
                int aucID = (int) list.get(2);
                double price = (double) list.get(3);
                bank.sendAgentMessage(new Message(NAME,
                                                  MessageTypes.UNBLOCK_FUNDS,
                                                  aucID));
                break;
        }
    }

    /**
     * Function to send messages to get updates for the auction houses and the
     * bank account information.
     */
    private void sendMessageForUpdates() {
        bank.sendAgentMessage(new Message(NAME,
                                          MessageTypes.GET_HOUSES));
        bank.sendAgentMessage(new Message(NAME,
                                          MessageTypes.ACCOUNT_INFO));
    }

    /**
     * Function to close the connection.
     */
    public void closeApplicationConnection() {
        connected = !connected;
    }

    /*****************************************************************/
    /*                                                               */
    /*                         Override Functions                    */
    /*                                                               */
    /*****************************************************************/

    /**
     * Overrides run to perform specific tasks.
     */
    @Override
    public void run() {
        Message in;
        MessageAnalyzer analyzer = new MessageAnalyzer();

        try {
            while (connected) {
                in = messageQueue.take();
                if (in != null) {
                    response(in,
                             (MessageTypes) in.getMessageList().get(1),
                             analyzer.analyze(in));
                }
                in = null;
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        bank.closeConnections();
    }
    
    /**
     * Main method to start the program for the user/agent.
     */
    public static void main(String[] args) throws IOException {
        AgentGUI.launch(args);
    }
}




/*
computers for checking offline
b146-34
b146-22
b146-19
*/























