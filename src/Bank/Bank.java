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
                ServerThread bankClient = new ServerThread(client,clientNumber,this);
                clientNumber++;
                getClients().put(bankClient.idNumber,bankClient);
                (new Thread(bankClient)).start();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    public synchronized  Message responseToAuctionHouse(Message message,
                                                        MessageTypes type){
        ArrayList<Object> messageList = message.getMessageList();
        int agentAccountNumber;
        int auctionHouseAccountNumber;
        double amount;

        switch(type) {
            case CREATE_ACCOUNT:
                AuctionInfo auctionInfo = (AuctionInfo) messageList.get(2);

                Account account = makeAccount(auctionInfo.getName(),0);

                addAuctionHouse(auctionInfo);

                return new Message(NAME, MessageTypes.ACCOUNT_INFO,account);

            case ACCOUNT_INFO:
                auctionHouseAccountNumber = (int) messageList.get(0);
                return new Message(NAME, MessageTypes.ACCOUNT_INFO,accounts.get(auctionHouseAccountNumber));

            case REMOVE_FUNDS:
                agentAccountNumber = (int) messageList.get(2);
                amount = (double) messageList.get(3);

                blockFunds(agentAccountNumber, amount);

                try{
                clients.get(agentAccountNumber).outputStream.writeObject(new Message(NAME,
                                                                         MessageTypes.ACCOUNT_INFO,
                                                                         accounts.get(agentAccountNumber)));

                }catch(Exception e){
                    e.printStackTrace();
                }
                return new Message(NAME,MessageTypes.CONFIRMATION);


            default: return new Message(NAME,MessageTypes.THANKS);

        }
    }

    public synchronized Message responseToAgent(Message message,
                                         MessageTypes type) {

        ArrayList<Object> messageList = message.getMessageList();
        int agentAccountNumber;
        int auctionHouseAccountNumber;
        double amount;

        switch (type) {

            case CREATE_ACCOUNT:
                //add agent to list

                Account account = (Account) messageList.get(2);


                AgentInfo agent = (AgentInfo) messageList.get(3);
                addAgent(agent);

                return new Message(NAME,MessageTypes.ACCOUNT_INFO,account);

            //return list of HouseInfo objects
            case GET_HOUSES:
                return new Message(NAME,MessageTypes.HOUSES,this.auctionHouses);

            case TRANSFER_FUNDS:
                auctionHouseAccountNumber = (int) messageList.get(0);
                agentAccountNumber = (int) messageList.get(1);
                amount = (double) messageList.get(2);
                boolean success = transferFunds(auctionHouseAccountNumber,agentAccountNumber,amount);
                if(success){
                    return new Message(NAME, MessageTypes.ACCOUNT_INFO,accounts.get(agentAccountNumber));
                }

                //todo send hector his account back to him
                break;

//            case UNBLOCK_FUNDS:
//                break;

            default: return new Message(NAME,MessageTypes.THANKS);
        }
        //todo remove
        return new Message(NAME,MessageTypes.CONFIRMATION);
    }

    /**
     * Creates and returns an account.
     */
    public synchronized Account makeAccount(String name, double startingBalance) {
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

    public synchronized  void addAgent(AgentInfo agent){
        this.agents.add(agent);
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

            while(!client.isClosed()) {
                try {
                    Message msg = (Message) inputStream.readObject();
                    messages.add(msg);
                    Message message = messages.take();
                    System.out.println(message);

                    Message response;
                    if(analyzer.analyze(message) == 1){
                        response = bank.responseToAgent(message,
                                                       (MessageTypes) message.getMessageList().get(1));
                    }
                    else{
                        response = bank.responseToAuctionHouse(message,
                                                              (MessageTypes) message.getMessageList().get(1));

                    }

                    outputStream.writeObject(response);

                }catch(InterruptedException e){
                    e.printStackTrace();
                }catch(IOException e){
                    e.printStackTrace();
                }catch(ClassNotFoundException e){
                    e.printStackTrace();
                }
            }
            closeClient();
            System.out.println("ServerThread is stopping");
        }
    }

}
