package Bank;

import Agent.*;
import AuctionHouse.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Move test bank into bank.
 * @author Daniel Miller
 * @version 11-13-18
 */
public class Bank implements Runnable{
    private ArrayList<Agent> agents; //list of agent accounts
    private ArrayList<AuctionHouse> auctionHouses; //list of auction house accounts
    private ArrayList<Account> accounts;
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
     
     
     Will have a proxy
     
     Some sort of pending balance:
     every time you make a bid on a new item
     subtract that amount from pending balance
     
     
     
     we need to create the bank first
     */
    
    public static void main(String[] args) throws Exception {
        address = args[0];
        portNumber = Integer.parseInt(args[1]);
        
        Bank bankOne = new Bank(address,portNumber);
        ServerSocket server = new ServerSocket(portNumber);
        
        while (true) {
            Socket client = server.accept();
            ServerThread bank = new ServerThread(client, bankOne);
            (new Thread(bank)).start();
        }
    }
    
    /**
     * Constructor for Bank
     *
     */
    public Bank(String address, int portNumber){
        agents = new ArrayList<Agent>();
        auctionHouses = new ArrayList<AuctionHouse>();
        accounts = new ArrayList<Account>();
    }
    
    @Override
    public void run() {
        //create server sockets to listen for client connections
        
        //while true to listen for activity
    }
    
    /**
     * Creates and assigns an account to an agent.
     */
    public Account makeAccount(int startingBalance, Agent agent) {
        ArrayList<Account> accounts = this.getAccounts();
        
        Account account = new Account(currentAccountNumber,
                                      startingBalance,
                                      startingBalance,
                                      agent);
        
        if (!accounts.contains(account)) {
            accounts.add(account);
        }
        
        return account;
    }
    
    /**
     * Adds an auction house to the list of auction houses.
     */
    public void addAuctionHouse(AuctionHouse house){
        this.auctionHouses.add(house);
    }
    
    /**
     * Assigns an account number to an agent and increments the current account number
     */
    private int assignAccountNumber() {
        int number = this.currentAccountNumber;
        this.currentAccountNumber++;
        return number;
    }
    
    /**
     * Gets the list of bank accounts
     * @return
     */
    public ArrayList<Account> getAccounts(){
        return accounts;
    }
    
    /**
     * Gets list of agents for a auction house.
     */
    public ArrayList<Agent> getAgents() {
        return agents;
    }
    
    /**
     * Gets list of auction houses for a agent.
     */
    public ArrayList<AuctionHouse> getAuctionHouses() {
        return auctionHouses;
    }
    
    
    
    
    // private sub class
    private static class ServerThread implements Runnable {
        
        private Socket client;
        private BufferedReader in, stdIn;
        private PrintWriter out;
        private Bank bank;
        
        private ObjectInputStream inputStream;
        private ObjectOutputStream outputStream;
        
        // constructor
        public ServerThread(Socket client, Bank bank) {
            this.client = client;
            this.bank = bank;
            
            try {
                stdIn = new BufferedReader(new InputStreamReader(System.in));
                outputStream = new ObjectOutputStream(client.getOutputStream());
                outputStream.flush();
                inputStream = new ObjectInputStream(client.getInputStream());
                outputStream.writeObject("You have been connected!");
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
            String output, input = null;
            
            try {
                do {
                    input = (String) inputStream.readObject();
                    System.out.println(input);
                    
                    output = stdIn.readLine();
                    if (output != "") {
                        outputStream.writeObject("server: " + output);
                    }
                } while (input != null);
                closeClient();
            } catch (IOException io) {
                io.printStackTrace();
            } catch (ClassNotFoundException cnf) {
                cnf.printStackTrace();
            }
        }
    }
    
    
    
    
    
    
    
    //TODO
    
    /**
     * Transfers funds from an Agent account to and AuctionHouse account.
     */
    private void transferFunds(AuctionHouse house, Agent agent, double amount){
    
    }
    
    /**
     * Handles messages received from Houses and Agents.
     */
}
