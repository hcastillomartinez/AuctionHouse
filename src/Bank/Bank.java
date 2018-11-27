package Bank;

import Agent.*;
import AuctionHouse.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * The Bank class.
 * @author Daniel Miller
 * @version 11-13-18
 */
public class Bank implements Runnable {
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
        if(args.length >= 1){
            portNumber = Integer.parseInt(args[0]);
        }
        else{
            System.out.println("Error: Invalid program arguments. The first argument must be the bank port number.");
            return;
        }
        
        Bank bank = new Bank(address, portNumber);
        
        (new Thread(bank)).start();
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

    /**
     * Pseudocode
     * while(true)
     *      message = messageQueue.take()
     *      analyze message
     *      carry out action specified by message
     */
    @Override
    public void run(){

        //todo is this correct?
        //Shouldn't this code just be in the main method.
        //Message handling code should be in the run method.
        //Why are we creating multiple bank threads? Do they share the same resources?
        try{
            ServerSocket server = new ServerSocket(portNumber);
            
            while (true) {
                Socket client = server.accept();
                ServerThread bank = new ServerThread(client);
                (new Thread(bank)).start();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * Creates and returns an account.
     */
    public Account makeAccount(String name, int startingBalance) {
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
    
    
    /**
     * Transfers funds from an Agent account to an AuctionHouse account.
     */
    public synchronized void transferFunds(int auctionHouseAccountNumber,
                                           int agentAccountNumber,
                                           double amount) throws Exception{
        
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
                }
                else{
                    throw new Exception(); //"Unable to transfer funds. The agent's pending balance is less than the specified amount."
                }
            }
        }
    }
    
    
    
    
    // private sub class
    private static class ServerThread implements Runnable {
        
        private Socket client;
        private BufferedReader stdIn;
        private ObjectInputStream inputStream;
        private ObjectOutputStream outputStream;
        
        // constructor
        public ServerThread(Socket client) {
            this.client = client;
            
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
                    try {
                        input = (String) inputStream.readObject();
                        System.out.println(input);
                        
                        if (input.equalsIgnoreCase("bye")) {
                            input = null;
                        }
                        
                        output = stdIn.readLine();
                        if (output != "") {
                            outputStream.writeObject("server: " + output);
                        }
                    } catch (ClassNotFoundException cnf) {
                        cnf.printStackTrace();
                    } catch (EOFException eof) {
                        System.out.println("Client has disconnected!");
                        break;
                    }
                } while (input != null);
            } catch (IOException io) {
                io.printStackTrace();
            }
        }
    }
    
    
    
    
    
    //TODO
    
    
    /**
     * Handles messages received from Houses and Agents.
     */
}
