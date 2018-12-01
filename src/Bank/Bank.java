package Bank;

import Agent.*;
import AuctionHouse.*;
import MessageHandling.Message;
import MessageHandling.MessageAnalyzer;
import MessageHandling.MessageTypes;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The Bank class.
 * @author Daniel Miller
 * @version 11-13-18
 */
public class Bank implements Runnable {
    private final String NAME = "bank";
    private LinkedBlockingQueue<Message> messages = new LinkedBlockingQueue<>();
    private ArrayList<AgentInfo> agents; //list of agent accounts
    private ArrayList<AuctionInfo> auctionHouses; //list of auction house accounts
    private ArrayList<Account> accounts;
    private HashMap<Integer,ServerThread> clients;
    private HashMap<Agent,HashMap<AuctionHouse, Integer>> secretKeys; //todo
    private int clientNumber = 0;
    private int currentAccountNumber = 0;
    static private String address;
    static private int portNumber;

    
    /**
     It is static and at a known address (IP address and port number)
     It hosts
     a list of agent accounts
     a list of auction house accounts
     It shares the list of auction houses with agents having bank accounts
     It provides agents with secret keys for use in the bidding process
     It transfers funds from agent to auction accounts, under agent control
     It blocks and unblocks funds in agent accounts, at the request of action houses
     
     */
    
    public static void main(String[] args) throws Exception {
        if(args.length >= 1){
            portNumber = Integer.parseInt(args[0]);
        }
        else{
            System.out.println("Error: Invalid program arguments. The first argument must be the bank port number.");
            return;
        }
        
        Bank bank = new Bank(address, portNumber);
        Thread bankThread = new Thread(bank);
        bankThread.start();
    }
    
    /**
     * Constructor for Bank
     *
     */
    public Bank(String address, int portNumber){
        agents = new ArrayList<AgentInfo>();
        auctionHouses = new ArrayList<AuctionInfo>();
        accounts = new ArrayList<Account>();
        clients = new HashMap<>();
    }


    /**
     * Pseudocode
     * while(true)
     *      message = messageQueue.take()
     *      analyze message
     *      carry out action specified by message
     */
    @Override
    public void run(){

        try{
            ServerSocket server = new ServerSocket(portNumber);

            while (true) {
                Socket client = server.accept();
                ServerThread bankClient = new ServerThread(client,clientNumber++,this);
                getClients().put(bankClient.idNumber,bankClient);
                (new Thread(bankClient)).start();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void handleMessage(Message message){
//        MessageAnalyzer analyzer = new MessageAnalyzer();
//
//        String sender = (String) message.getMessageList().get(0);
//        sender.toLowerCase();
//        MessageTypes type = (MessageTypes) message.getMessageList().get(1);
//        String
//
//        switch(sender){
//            //all of the casess
//            case "auction house":
//                //switch();
//                break;
//
//            case "agent":
//                break;
//
//            default:
//                System.out.println("Error in handleMessage() case statement in Bank.java");
//        }


    }

    public synchronized Message response(Message message,
                                         MessageTypes type,
                                         int sender) {
        Message response = null;
        ArrayList<Object> messageList = message.getMessageList();
        int agentAccountNumber;
        int auctionHouseAccountNumber;
        double amount;

        switch (type) {
            case REMOVE_FUNDS:
                agentAccountNumber = (int) messageList.get(1);
                amount = (double) messageList.get(2);

                blockFunds(agentAccountNumber,amount);
                return new Message(NAME,
                                   MessageTypes.ACCOUNT_INFO,
                                   accounts.get(agentAccountNumber));

            case CREATE_ACCOUNT:
                //todo
                break;

            case ACCOUNT_INFO:
                //todo
                break;

            //return list of HouseInfo objects
            case GET_HOUSES:
                return new Message(NAME,MessageTypes.HOUSES,this.auctionHouses);

            case TRANSFER_FUNDS:
                if(messageList.size() > 3){
                    auctionHouseAccountNumber = (int) messageList.get(0);
                    agentAccountNumber = (int) messageList.get(1);
                    amount = (double) messageList.get(2);
                    boolean success = transferFunds(auctionHouseAccountNumber,agentAccountNumber,amount);
                    if(success){
                        //todo send new accounts back to houses and agents
                    }
                }
                else{ /*throw error*/}
                //todo
                break;

             //todo make default

        }

            return new Message(NAME,MessageTypes.CONFIRMATION);
    }

    /**
     * Creates and returns an account.
     */
    public synchronized Account makeAccount(String name, int startingBalance) {
        Account account = new Account(name,
                                      assignAccountNumber(),
                                      startingBalance,
                                      startingBalance);

        if (!this.getAccounts().contains(account)) {
            this.getAccounts().add(account);
        }

        return account;
    }

    /**
     * Adds an auction house to the list of auction houses.
     */
    public synchronized void addAuctionHouse(AuctionInfo house){
        this.auctionHouses.add(house);
    }

    /**
     * Assigns an account number to an agent and increments the current account number
     */
    private synchronized int assignAccountNumber() {
        int number = this.currentAccountNumber;
        this.currentAccountNumber++;
        return number;
    }

    /**
     * Gets the list of bank accounts
     * @return
     */
    public synchronized ArrayList<Account> getAccounts(){
        return accounts;
    }

    /**
     * Gets the map of integer id's to clients.
     * @return
     */
    public synchronized HashMap<Integer, ServerThread> getClients() {
        return clients;
    }

    /**
     * Gets list of agents for a auction house.
     */
    public synchronized ArrayList<AgentInfo> getAgents() {
        return agents;
    }
    
    /**
     * Gets list of auction houses for a agent.
     */
    public synchronized ArrayList<AuctionInfo> getAuctionHouses() {
        return auctionHouses;
    }
    
    
    /**
     * Transfers funds from an Agent account to an AuctionHouse account.
     */
    public synchronized boolean transferFunds(int auctionHouseAccountNumber,
                                           int agentAccountNumber,
                                           double amount){
        
        Account houseAccount = accounts.get(auctionHouseAccountNumber);
        Account agentAccount = accounts.get(agentAccountNumber);
        
        synchronized (houseAccount){
            synchronized (agentAccount){
                
                if(agentAccount.getPendingBalance() >= amount){
                    //transfer funds from agent to auction house
                    agentAccount.setPendingBalance(agentAccount.getPendingBalance() - amount);
                    agentAccount.setBalance(agentAccount.getBalance() - amount);
                    houseAccount.setBalance(houseAccount.getBalance() + amount);
                    houseAccount.setPendingBalance(houseAccount.getPendingBalance() + amount);
                    return true;
                }
                else{
                    return false;
                    //todo sent message back to agent; //"Unable to transfer funds. The agent's pending balance is less than the specified amount."
                }
            }
        }
    }

    /**
     * Blocks the funds in an agent account by removing them from the pending balance
     * @param agentAccountNumber
     * @param amount
     */
    public synchronized void blockFunds(int agentAccountNumber, double amount){
        Account agentAccount = accounts.get(agentAccountNumber);
        agentAccount.setPendingBalance(agentAccount.getPendingBalance() - amount);
    }

    /**
     * Unblocks funds in an agent account by adding them back to the pending balance.
     */
    public synchronized  void unblockFunds(int agentAccountNumber, double amount){
        Account agentAccount = accounts.get(agentAccountNumber);
        agentAccount.setPendingBalance(agentAccount.getPendingBalance() - amount);
    }




    // private nested class
    private static class ServerThread implements Runnable {

        private Bank bank;
        private Socket client;
        private BufferedReader stdIn;
        private ObjectInputStream inputStream;
        private ObjectOutputStream outputStream;
        private int idNumber;
        private LinkedBlockingQueue<Message> messages = new LinkedBlockingQueue<>();

        // constructor
        public ServerThread(Socket client, int idNumber, Bank bank) {
            this.bank = bank;
            this.client = client;
            this.idNumber = idNumber;

            try {
                stdIn = new BufferedReader(new InputStreamReader(System.in));
                outputStream = new ObjectOutputStream(client.getOutputStream());
                outputStream.flush();
                inputStream = new ObjectInputStream(client.getInputStream());
            } catch (IOException io) {
                io.printStackTrace();
            }
        }
        
        /**
         * Function to close the client from the server.
         */
        private void closeClient() {
            try {
                outputStream.writeObject("Server " + client.getLocalAddress() + " has closed.");
                inputStream.close();
                outputStream.close();
                stdIn.close();
            } catch (IOException io) {
                io.printStackTrace();
            }
        }
        
        @Override
        public void run() {
            MessageAnalyzer analyzer = new MessageAnalyzer();

                while(true) {
                    try {
                        Message msg = (Message) inputStream.readObject();
                        messages.add(msg);
                        Message message = messages.take();
                        System.out.println(message);

                        Message response = bank.response(message,
                                                        (MessageTypes) message.getMessageList().get(1),
                                                         analyzer.analyze(message));

                        outputStream.writeObject(response);


                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }catch(IOException e){
                        e.printStackTrace();
                    }catch(ClassNotFoundException e){
                        e.printStackTrace();
                    }
                }
        }
    }
    
    
    
    
    
    //TODO
    
    
    /**
     * Handles messages received from Houses and Agents.
     */
}
