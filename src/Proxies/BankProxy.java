package Proxies;

import Agent.Agent;
import AuctionHouse.AuctionHouse;
import Bank.Bank;
import Bank.Account;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Proxies.BankProxy.java is the class that is the mediary between the Bank and the
 * Agent. The class provides higher level functionality to interact with the
 * bank.
 */
public class BankProxy implements Runnable {

    private Bank bank;
    private Account accout;
    private String host;
    private int port;
    private BufferedReader stdIn;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket client = null;
    
    /**
     * Constructor for the bank proxy.
     * Builds a reference to the bank for bank functionality
     */
    public BankProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }
    
    /**
     * Setting up the input and output streams for the client connection.
     */
    private void setupInputAndOutputStreams() {
        try {
            if (client != null) {
                out = new ObjectOutputStream(client.getOutputStream());
                out.flush();
                in = new ObjectInputStream(client.getInputStream());
                stdIn = new BufferedReader(new InputStreamReader(System.in));
            } else {
                System.out.println("Client has not been connected");
            }
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    /**
     * Agent connecting to the bank through the proxy.
     */
    public void connectToServer() {
        try {
            client = new Socket(host, port);
            setupInputAndOutputStreams();
            (new Thread(this)).start();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    /**
     * Overriding the run method to perform specialized tasks.
     */
    @Override
    public void run() {
        try {
            out.writeObject("New client" + client.getClass());
            String response = null, user = null;

            do {
                try {
                    response = (String) in.readObject();
                    System.out.println(response);
    
                    user = stdIn.readLine();
                    if (user != "") {
                        out.writeObject("client: " + user);
                    }
                } catch (EOFException eof) {
                    System.out.println("Server has been closed");
                }
            } while (response != null);
            out.close();
            in.close();
            stdIn.close();
        } catch (IOException io) {
            io.printStackTrace();
        } catch (ClassNotFoundException cnf) {
            cnf.printStackTrace();
        }
    }

    /**
     * Creates and assigns an account to an agent.
     * @return the newly opened account
     */
    public Account openAccount(int balance, Agent agent){
        return bank.makeAccount(balance, agent);
    }

    /**
     * Adds an auction house to the list of auction houses.
     * @param house the house to add to the bank
     */
    public void addAuctionHouse(AuctionHouse house){
        bank.getAuctionHouses().add(house);
    }

    /**
     * Gets list of agents for a auction house.
     * @return the list of the agents from the bank
     */
    public ArrayList<Agent> getAgents() {
        return bank.getAgents();
    }

    /**
     * Gets list of auction houses for a agent.
     * @return the list of auction houses from the bank
     */
    public ArrayList<AuctionHouse> getAuctionHouses() {
        return bank.getAuctionHouses();
    }
}
