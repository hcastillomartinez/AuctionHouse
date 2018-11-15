package Agent;

import AuctionHouse.AuctionHouse;
import Bank.Bank;
import Proxies.AuctionHouseProxy;
import Proxies.BankProxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Agent.java is the class that bids on objects inside the auction house.
 * Danan High, 11/13/2018
 */
public class Agent implements Runnable {
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

    private int id, key;
//    private Account account;
    private AuctionHouseProxy auctionHouseProxy;
    private BankProxy bankProxy;
    private Bank bank;
    private AuctionHouse auctionHouse;


    /**
     * Constructor for the Agent.
     */
    public Agent() {
        auctionHouseProxy = new AuctionHouseProxy();
        bankProxy = new BankProxy();
        bank = new Bank();
        auctionHouse = new AuctionHouse();
    }

    /**
     * Overrides run to perform certain tasks.
     */
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        String answer = "y";

        while (answer.equalsIgnoreCase("y")) {
            System.out.println("continue?");
            answer = scanner.next();
        }
    }

    //TODO

    /**
     * Making a bid to an auction house.
     */
    private void makeBid(int bidAmount) {

    }

    /**
     * Set chosen auction house from the bank.
     */
    private void setAuctionHouse() {

    }
    
    /**
     * Get Auction houses from the bank list
     */
    private void getAuctionHouses() {

    }

    /**
     * Get items from the auction house that can be bid on.
     */
    private void getItems() {

    }

    /**
     * Gets a key from the bank on a per auction house basis.
     */
    private void getKey() {

    }
    
    /**
     * Getting and setting the new account information from the bank.
     */
    private void openNewBankAccount() {
        // open account and set account to self
    }

    /**
     * Analyzing the replies from the auction houses, based on the bids the
     * agent has made.
     */
    
    /**
     * Overriding toString to print out the class.
     */
    @Override
    public String toString() { return id + " has balance: "; }

    /**
     * Main method to start the program for the user/agent.
     */
    public static void main(String[] args) throws IOException {
        Agent agent = new Agent();
        String hostName = args[0];
//        int portNumber = Integer.parseInt(args[1]);
//
//        try (Socket socket = new Socket(hostName, portNumber);
//             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
//             BufferedReader in =
//                     new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
//
//        }

        (new Thread(agent)).start();
    }

}
