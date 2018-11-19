package Bank;

import Agent.*;
import AuctionHouse.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
    private String address;
    static private int portNumber;
    // testing code -------------->
    private Socket clientSocket;
    private BufferedReader reader, input;
    private PrintWriter writer;

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
        portNumber = Integer.parseInt(args[0]);

        TestBank bankOne = new TestBank();
        ServerSocket server = new ServerSocket(portNumber);

        while (true) {
            Socket client = server.accept();
            ServerThread bank = new Agent.TestBank.ServerThread(client, bankOne);
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
    public Account openAccount(int balance, Agent agent){
        Account newAccount = new Account(this.assignAccountNumber(), balance, balance, agent);
        this.accounts.add(newAccount);
        this.agents.add(agent);
        return new Account(this.assignAccountNumber(), balance, balance, agent);
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
     * Gets list of agents for a auction house.
     */
    public ArrayList<Agent> getAgents() {
        return agents;
    }

    /**
     * Creates an account for an agent and adds it to the list of agents.
     */

    /**
     * Gets list of auction houses for a agent.
     */
    public ArrayList<AuctionHouse> getAuctionHouses() {
        return auctionHouses;
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
