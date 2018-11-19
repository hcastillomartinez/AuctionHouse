package Proxies;

import Agent.Agent;
import AuctionHouse.AuctionHouse;
import Bank.Bank;
import Bank.Account;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
    private BufferedReader reader, input;
    private PrintWriter writer;
    private Socket client;
    
    /**
     * Constructor for the bank proxy.
     * Builds a reference to the bank for bank functionality
     */
    public BankProxy(Bank bank) {
        this.bank = bank;
    }

    public BankProxy(Socket client) throws IOException {
        this.client = client;
        reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        input = new BufferedReader(new InputStreamReader(System.in));
        writer = new PrintWriter(client.getOutputStream(), true);
    }

    /**
     * Agent connecting to the bank.
     * @param host name of the host server.
     * @param port number for the port.
     */
    public void connectToServer(String host, int port) {
        try {
            new Socket(host, port);
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
            String response = reader.readLine();

            while (response != null) {
                writer.println(input.readLine());
                response = reader.readLine();
            }
            writer.close();
            reader.close();
            input.close();
        } catch (IOException io) {
            io.printStackTrace();
        }

        if (client != null) {
            try {
                client.close();
            } catch (IOException io) {
                io.printStackTrace();
            }
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
