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
 *
 *   * It is static and at a known address (IP address and port number)
 *   * It hosts
 *      * a list of agent accounts
 *      * a list of auction house accounts
 *   * It shares the list of auction houses with agents having bank accounts
 *   * It provides agents with secret keys for use in the bidding process
 *   * It transfers funds from agent to auction accounts, under agent control
 *   * It blocks and unblocks funds in agent accounts, at the request of action houses
 * @author Daniel Miller 11-13-18
 */
public class Bank implements Runnable {
    private final String NAME = "bank";
    private LinkedBlockingQueue<Message> messages = new LinkedBlockingQueue<>(); //message queue
    private HashMap<Integer,AgentInfo> agents; //mapping of agent account numbers accounts
    private HashMap<Integer,AuctionInfo> auctionHouses; //mapping of auction house account numbers accounts
    private HashMap<Integer,Account> accounts; //mapping of account numbers to bank accounts
    private HashMap<Integer,ServerThread> clients; //mapping of client number to client threads
    private HashMap<Agent,HashMap<AuctionHouse, Integer>> secretKeys; //todo
    private int clientNumber = 0;
    static private String address; //IP address
    static private int portNumber; //port number
    static BankGUI gui; //gui for bank

    /**
     * The main method for the Bank application.
     * @param args the IP Address and port number that the bank will have socket connections on
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        BankGUI.launch(args);
    }
    
    /**
     * Constructor for Bank
     */
    public Bank(String address, int portNumber){
        this.address = address;
        this.portNumber = portNumber;
        agents = new HashMap<Integer,AgentInfo>();
        auctionHouses = new HashMap<Integer,AuctionInfo>();
        accounts = new HashMap<Integer,Account>();
        clients = new HashMap<>();
    }


    /**
     * Constantly searches for new socket connections.
     * When a new connection is found, a ServerThread to monitor the new connection is started.
     */
    @Override
    public void run(){
        try{
            ServerSocket server = new ServerSocket(portNumber);

            while (true) {
                System.out.println("bank is running");

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


    /**
     * Handles and responds to a message sent from an auction house.
     * @param message the message the bank is handling
     * @param type the type of the message
     * @return
     */
    public synchronized  Message responseToAuctionHouse(int clientID,Message message,
                                                        MessageTypes type){
        ArrayList<Object> messageList = message.getMessageList();
        int agentAccountNumber;
        int auctionHouseAccountNumber;
        double amount;

        switch(type) {

            //creates a bank account for an auction house
            case CREATE_ACCOUNT:
                AuctionInfo auctionInfo = (AuctionInfo) messageList.get(2);

                Account account = makeAccount(auctionInfo.getName(),0,clientID); //todo might be a bug if an agent starts a thread and doesn't create an account right away

                addAuctionHouse(auctionInfo,account);
                gui.refreshAccountInformation();
                return new Message(NAME, MessageTypes.ACCOUNT_INFO,account);

            //sends current account information back to the auction house
            case ACCOUNT_INFO:
                auctionHouseAccountNumber = (int) messageList.get(0);
                return new Message(NAME, MessageTypes.ACCOUNT_INFO,accounts.get(auctionHouseAccountNumber));

            //blocks funds in an agent's account per auction house's request and sends updated account back to the agent
            case REMOVE_FUNDS:
                agentAccountNumber = (int) messageList.get(2);
                amount = (double) messageList.get(3);

                blockFunds(agentAccountNumber, amount);

                try{
                clients.get(agentAccountNumber).outputStream.writeObject(new Message(NAME,
                                                                         MessageTypes.ACCOUNT_INFO,
                                                                         accounts.get(agentAccountNumber)));
                }catch(Exception e){ e.printStackTrace(); }

                gui.refreshAccountInformation();
                return new Message(NAME,MessageTypes.CONFIRMATION);

            case UNBLOCK_FUNDS:
                agentAccountNumber = (int) messageList.get(2);
                amount = (double) messageList.get(3);
                unblockFunds(agentAccountNumber,amount);

                try{
                    clients.get(agentAccountNumber).outputStream.writeObject(new Message(NAME,
                            MessageTypes.ACCOUNT_INFO,
                            accounts.get(agentAccountNumber)));
                }catch(Exception e){ e.printStackTrace(); }

                gui.refreshAccountInformation();
                return new Message(NAME,MessageTypes.CONFIRMATION);

            default: return new Message(NAME,MessageTypes.THANKS);
        }
    }

    public synchronized Message responseToAgent(int clientID, Message message,
                                         MessageTypes type) {
        ArrayList<Object> messageList = message.getMessageList();
        int agentAccountNumber;
        int auctionHouseAccountNumber;
        double amount;

        switch (type) {

            //creates a bank account for an auction house
            case CREATE_ACCOUNT:
                Account account = (Account) messageList.get(2);
                account.setAccountNumber(clientID); //todo think of a way to do this differently
                accounts.put(account.getAccountNumber(),account);

                System.out.println(accounts);


                AgentInfo agent = (AgentInfo) messageList.get(3);
                agent.setAccountNumber(account.getAccountNumber());
                addAgent(agent,account);

                gui.refreshAccountInformation();
                System.out.println(this.accounts);
                return new Message(NAME,MessageTypes.ACCOUNT_INFO,account);

            //return list of HouseInfo objects
            case GET_HOUSES:
                return new Message(NAME,MessageTypes.HOUSES,this.getAuctionHousesAsList());

            //transfers funds from an agent account to an auction house account
            case TRANSFER_FUNDS:
                auctionHouseAccountNumber = (int) messageList.get(0);
                agentAccountNumber = (int) messageList.get(1);
                amount = (double) messageList.get(2);
                boolean success = transferFunds(auctionHouseAccountNumber,agentAccountNumber,amount);

                try{
                    clients.get(auctionHouseAccountNumber).outputStream.writeObject(new Message(NAME,
                            MessageTypes.ACCOUNT_INFO,
                            accounts.get(agentAccountNumber)));
                }catch(Exception e){ e.printStackTrace();}

                gui.refreshAccountInformation();
                return new Message(NAME, MessageTypes.ACCOUNT_INFO,accounts.get(agentAccountNumber));

            default: return new Message(NAME,MessageTypes.THANKS);
        }
    }

    /**
     * Creates an account and adds it to the accounts map.
     */
    public synchronized Account makeAccount(String name, double startingBalance, int accountNumber) {
        Account account = new Account(name,
                                      assignAccountNumber(accountNumber),
                                      startingBalance,
                                      startingBalance);

        this.accounts.put(account.getAccountNumber(), account);
        return account;
    }

    /**
     * Adds an auction house to the map of auction houses.
     */
    public synchronized void addAuctionHouse(AuctionInfo house,Account account){
        this.auctionHouses.put(account.getAccountNumber(),house);
    }

    /**
     * Adds an agent to the mapping of agents
     */
    public synchronized  void addAgent(AgentInfo agent, Account account){
        this.agents.put(account.getAccountNumber(),agent);
    }

    /**
     * Assigns an account number to an agent
     */
    private synchronized int assignAccountNumber(int number) {
        return number;
    }

    /**
     * Gets the list of bank accounts
     * @return
     */
    public synchronized ArrayList<Account> getAccountsAsList(){
        return new ArrayList<Account>(accounts.values());
    }

    /**
     * Gets the map of integer id's to clients.
     * @return
     */
    public synchronized HashMap<Integer, ServerThread> getClients() {
        return clients;
    }

    /**
     * Gets list of auction houses.
     */
    public synchronized ArrayList<AuctionInfo> getAuctionHousesAsList() {
        return new ArrayList<AuctionInfo>(auctionHouses.values());
    }

    /**
     * Gets list of agents.
     */
    public synchronized ArrayList<AgentInfo> getAgentsAsList() {
        return new ArrayList<AgentInfo>(agents.values());
    }

    /**
     * Transfers funds from an Agent account to an AuctionHouse account.
     */
    public synchronized boolean transferFunds(int auctionHouseAccountNumber,
                                           int agentAccountNumber,
                                           double amount){
        
        Account houseAccount = accounts.get(auctionHouseAccountNumber);
        Account agentAccount = accounts.get(agentAccountNumber);

        //todo do I need synchronized blocks here? remove
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

    /**
     * Gets the accounts hashmap
     */
    public HashMap<Integer,Account> getAccounts(){
        return accounts;
    }


    /**
     * A nested class that acts as a server for a bank proxy client.
     * Handles all of the socket code on the Bank's end.
     */
    private static class ServerThread implements Runnable {

        private Bank bank;
        private Socket client;
        private BufferedReader stdIn;
        private ObjectInputStream inputStream;
        private ObjectOutputStream outputStream;
        private int idNumber;
        private LinkedBlockingQueue<Message> messages = new LinkedBlockingQueue<>();

        /**
         * Constructor for ServerThread
         * @param client
         * @param idNumber unique id indicating when it was created
         * @param bank a reference to the static bank
         */
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

        /**
         * An overridden run method.
         * Receives messages from a client and passes the message to Bank to be processed.
         * After the message is processed, Bank's response is sent back over the socket connection.
         */
        @Override
        public void run() {
            MessageAnalyzer analyzer = new MessageAnalyzer();
            boolean connected = true;

            while(connected) {
                try {
                    Message msg = (Message) inputStream.readObject();
                    messages.add(msg);
                    Message message = messages.take();
                    System.out.println(message);

                    Message response;
                    if(analyzer.analyze(message) == 1){
                        response = bank.responseToAgent(idNumber,message,
                                                       (MessageTypes) message.getMessageList().get(1));
                    }
                    else{
                        response = bank.responseToAuctionHouse(idNumber,message,
                                                              (MessageTypes) message.getMessageList().get(1));
                    }

                    outputStream.writeObject(response);

                }catch(InterruptedException e){
                    e.printStackTrace();
                } catch (EOFException e) {
                    connected = !connected; //todo fix exception when agent closes connection
                } catch(IOException e){
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
