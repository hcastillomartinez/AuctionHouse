package Agent;

import AuctionHouse.*;
import Bank.*;
import MessageHandling.Message;
import MessageHandling.MessageAnalyzer;
import MessageHandling.MessageTypes;
import Proxies.AuctionHouseProxy;
import Proxies.BankProxy;

import java.io.*;
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
    private ArrayList<Item> wonItems;
    private ArrayList<Bid> bids;
    private BlockingQueue<Message> messageQueue;
    private HashMap<String, Integer> auctionHouseMap;
    private HashMap<AuctionInfo, AuctionHouseProxy> houseProxyMap;
    private HashMap<Integer, Integer> auctionHouseKeys;
    private boolean connected = true;
    private String hostName;
    private int portNumber;
    private boolean itemListChange = false, aucHouseChange = false,
        bidChange = false, itemsWonChange = false;
    
    // tester value for the auction house info
    private HashMap<AuctionHouseProxy, ArrayList<Item>> auctionHouseItems;
    private AuctionInfo auctionInfo;
    private boolean accountChange = false;
    
    
    /**
     * Constructor for the Agent.
     */
    public Agent(String hostName, int portNumber) {
        itemList = new ArrayList<>();
        wonItems = new ArrayList<>();
        messageQueue = new LinkedBlockingQueue<>();
        auctionHouseMap = new HashMap<>();
        houseList = new ArrayList<>();
        houseProxyMap = new HashMap<>();
        auctionHouseItems = new HashMap<>();
        auctionHouseKeys = new HashMap<>();
        bids = new ArrayList<>();
        
        this.bank = new BankProxy(hostName,
                                  portNumber,
                                  this);
    }
    
    /**
     * Getting account change.
     * @return account change
     */
    public boolean getAccountChange() {
        return accountChange;
    }
    
    /**
     * Setting account change.
     * @param accountChange change
     */
    public void setAccountChange(boolean accountChange){
        this.accountChange = accountChange;
    }
    
    /**
     * Getting the auction info that holds the status of the house.
     * @return auctionInfo object
     */
    public AuctionInfo getAuctionInfo() { return auctionInfo; }
    
    /**
     * Getting if there has been a change in the item list.
     * @return true if change, false otherwise
     */
    public boolean isItemListChange() {
        return itemListChange;
    }
    
    /**
     * Setting if there has been a change in the item list.
     * @param itemListChange, setting to false
     */
    public void setItemListChange(boolean itemListChange) {
        this.itemListChange = itemListChange;
    }
    
    /**
     * Getting the auction house info keys
     * @return auctionHouseKeys
     */
    public HashMap<Integer, Integer> getAuctionHouseKeys() {
        return auctionHouseKeys;
    }
    
    /**
     * Getting if there has been a change in the auction house list.
     * @return true if change, false otherwise
     */
    public boolean isAucHouseChange() {
        return aucHouseChange;
    }
    
    /**
     * Getting if there has been a change in the auction house list.
     * @param aucHouseChange, setting change to false
     */
    public void setAucHouseChange(boolean aucHouseChange) {
        this.aucHouseChange = aucHouseChange;
    }
    
    /**
     * Getting the account number for sending to the bank.
     */
    public int getAccountNumber() {
        return accountNumber;
    }
    
    /**
     * Getting if there has been a change in the item list.
     * @return true if change, false otherwise
     */
    public boolean isBidChange() {
        return bidChange;
    }
    
    /**
     * Setting if there has been a change in the bid list.
     * @param bidChange, setting bid change to false
     */
    public void setBidChange(boolean bidChange) {
        this.bidChange = bidChange;
    }
    
    /**
     * Getting if there has been a change in the items won list.
     * @return true if change, false otherwise
     */
    public boolean isItemsWonChange() {
        return itemsWonChange;
    }
    
    /**
     * Setting if there has been a change in the items won list.
     * @param itemsWonChange, changing to no change
     */
    public void setItemsWonChange(boolean itemsWonChange) {
        this.itemsWonChange = itemsWonChange;
    }
    
    /**
     * Getting the current item.
     * @return currentItem selected.
     */
    public Item getItem() { return item; }
    
    /**
     * Setting the item for the agent.
     * @param item for the agent
     */
    public void setItem(Item item) {
        this.item = item;
    }
    
    /**
     * Getting the item list for the specific auction house.
     * @return itemList for the ah
     */
    private ArrayList<Item> getItemList(AuctionInfo ai) {
        return auctionHouseItems.get(houseProxyMap.get(ai));
    }
    
    /**
     * Getting the item list for the specific auction house.
     * @return itemList for the ah
     */
    public ArrayList<Item> getItemList() {
        return itemList;
    }
    
    /**
     * Getting the item list for the specific auction house.
     * @return itemList for the ah
     */
    public ArrayList<Item> getWonItems() {
        return wonItems;
    }
    
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
     * Function to get the pending balance of the agent account.
     * @return pending balance
     */
    public Double getPendingBalance() {
        synchronized (account) {
            return account.getPendingBalance();
        }
    }
    
    /**
     * Function to return the agent account.
     * @return account for the agent.
     */
    public Account getAccount() {
        return account;
    }
    
    /**
     * Function to get the bids.
     */
    public ArrayList<Bid> getBids() {
        synchronized (bids) {
            return bids;
        }
    }
    
    /**
     * Getting the host name.
     * @return host name for the client.
     */
    public String getHostName() { return hostName; }
    
    /**
     * Setting the auction house for the agent.
     * @param auctionInfo house for functionality
     */
    public synchronized boolean setAuctionHouse(AuctionInfo auctionInfo) {
        if (!houseProxyMap.containsKey(auctionInfo)) {
            AuctionHouseProxy proxy = new AuctionHouseProxy(auctionInfo
                                                                .getIPAddress(),
                                                            auctionInfo
                                                                .getPortNumber(),
                                                            this);
            houseProxyMap.put(auctionInfo, proxy);
            auctionHouseItems.put(proxy, new ArrayList<Item>());
        }
        this.auctionInfo = auctionInfo;
        aHProxy = houseProxyMap.get(auctionInfo);
//        aHProxy.sendMessage(new Message(NAME, MessageTypes.GET_ITEMS));
        return true;
    }
    
    /**
     * Getting the house list from the agent.
     * @return list of the houses
     */
    public synchronized ArrayList<AuctionInfo> getHouseList() {
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
    
    /**
     * Returning the list of auction house info.
     * @return list of the auction house info
     */
    public Integer getCurrentAuctionID() {
        if (auctionInfo != null) {
            return auctionInfo.getAuctionID();
        }
        return null;
    }

    /**
     * Setting the id to match the account number
     * @param id int
     */
    private void setID(int id) {
        this.id = id;
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
    public Integer getKeyForHouse() {
        return auctionHouseKeys.get(this.auctionInfo.getPortNumber());
    }
    
    /**
     * Get items from the auction house that can be bid on.
     */
    @SuppressWarnings("unchecked")
    private void getItems() {
    //        itemList = (ArrayList) aHProxy.getItemList();
    }
    
    /**
     * Setting the account of the agent.
     * @param account for the agent
     */
    public boolean setAccount(Account account) {
        synchronized(account) {
            if (this.account == null) {
                this.account = account;
                return true;
            }
            return false;
        }
    }
    
    /**
     * Setting the connected status to not connected.
     */
    public void setConnected() { connected = !connected; }
    
    /***********************************************************   updateShowBidList(list);******/
    /*                                                               */
    /*               Functions For Actions Based On Input            */
    /*                                                               */
    /*****************************************************************/
    
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
     * Updating the list of bids.
     */
    private void updateShowBidList(ArrayList<Object> messList) {
        ArrayList<Bid> removeList = new ArrayList<>();
        Bid b = (Bid) messList.get(2);
        for (Bid b1: bids) {
            if (b1.getItem().getItemName().equals(b.getItem().getItemName())) {
                if (b1.getAmount() == b.getAmount()) {
                    removeList.add(b1);
                }
            }
        }
        for (Bid b2: removeList) {
            System.out.println(b2.getItem());
            bids.remove(b2);
        }
    }

    /**
     * Updating the item list from the ah.
     */
    private void updateItemListForView() {
        for (Item i: wonItems) {
            itemList.remove(i);
        }
    }

    /**
     * Function to respond after message analysis
     */
    @SuppressWarnings("unchecked")
    private void response(Message message,
                          MessageTypes type,
                          int sender) {
        System.out.println(message + " = message");
        Message response;
        ArrayList<Object> list = message.getMessageList();
        
        switch (type) {
            case TRANSFER_ITEM:
                Bid bid = (Bid) list.get(2);
                Item bidItem = bid.getItem();
                bidItem.updatePrice(bid.getAmount());
                updateShowBidList(list);
                updateItemListForView();
                wonItems.add(bidItem);
                itemList.remove(bidItem);
                itemListChange = true;
                break;
            case ID_FOR_HOUSE:
                AuctionInfo ai = (AuctionInfo) message.getMessageList().get(2);
                int keyForHouse = (int) message.getMessageList().get(3);
                if (!auctionHouseKeys.containsKey(ai)) {
                    auctionHouseKeys.put(ai.getPortNumber(),
                                         keyForHouse);
                }
                break;
            case HOUSES:
                houseList = (ArrayList<AuctionInfo>) list.get(2);
                break;
            case BID_REJECTED:
                break;
            case BID_ACCEPTED:
                updateShowBidList(list);
                Bid b = (Bid) list.get(2);
                bids.add(b);
                break;
            case OUT_BID:
                updateShowBidList(list);
                break;
            case GET_ITEMS:
                ArrayList<Item> temp = (ArrayList<Item>) list.get(2);
                itemList.clear();
                itemList.addAll(temp);
                updateItemListForView();
                itemListChange = true;
                break;
            case ACCOUNT_INFO:
                double bal = (double) list.get(2);
                double pend = (double) list.get(3);
                int accntNum = (int) list.get(4);
                account.setBalance(bal);
                account.setPendingBalance(pend);
                account.setAccountNumber(accntNum);
                setID(accntNum);
                accountChange = true;
                break;
            case UNBLOCK_FUNDS:
                double price = (double) list.get(3);
                bank.sendAgentMessage(new Message(NAME,
                                                  MessageTypes.TRANSFER_FUNDS,
                                                  auctionInfo.getAccountNumber(),
                                                  price));
                break;
            case UPDATE_ITEM:
                String nameOfItem = (String) list.get(2);
                double newPrice = (double) list.get(3);
                for (Item i: itemList) {
                    if (i.getItemName().equalsIgnoreCase(nameOfItem)) {
                        i.updatePrice(newPrice);
                    }
                }
                updateItemListForView();
                itemListChange = true;
                break;
        }
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
     * Overriding toString() to print out the class.
     * @return string for the class
     */
    @Override
    public String toString() {
        return "Agent{" + "NAME='" + NAME +
            '\'' +
            ", id=" + id +
            ", accountNumber=" + accountNumber +
            ", account=" + account +
            ", aHProxy=" + aHProxy +
            ", bank=" + bank + '}';
    }
    
    /**
     * Overrides run to perform specific tasks.
     */
    @Override
    public void run() {
        Message in;
        MessageAnalyzer analyzer = new MessageAnalyzer();
        long pastTime = System.currentTimeMillis(), presentTime;
        
        try {
            while (connected) {
                in = messageQueue.take();
                response(in,
                         (MessageTypes) in.getMessageList().get(1),
                         analyzer.analyze(in));

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
