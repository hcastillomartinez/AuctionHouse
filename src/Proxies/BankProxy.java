package Proxies;

import Agent.*;
import AuctionHouse.*;
import Bank.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Proxies.BankProxy.java is the class that is the mediary between the Bank and the
 * Agent. The class provides higher level functionality to interact with the
 * bank.
 * @author Danan High, 11/21/2018
 */
public class BankProxy implements Runnable {

    private Bank bank;
    private Account accout;
    private Agent agent;
    private String host;
    private int port;
    private boolean connected = true;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket client = null;
    private AuctionHouse house;
    
    private LinkedBlockingQueue<TestMessage> messageList =
        new LinkedBlockingQueue<>();
    
    /**
     * Constructor for the bank proxy.
     * Builds a reference to the bank for bank functionality
     */
    public BankProxy(String host, int port, Agent agent, AuctionHouse house) {
        this.host = host;
        this.port = port;
        this.agent = agent;
        this.house = house;
        connectToServer();
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
    private void connectToServer() {
        try {
            client = new Socket(host, port);
            setupInputAndOutputStreams();
            (new Thread(this)).start();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    /**
     * Adding a message to the banks input stream.
     */
    @SuppressWarnings("unchecked")
    public void sendAgentMessage(Object client, Object inMessage) {
        try {
            messageList.put(new TestMessage(inMessage));
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    /**
     * Overriding the run method to perform specialized tasks.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void run() {
        try {
            TestMessage response = null, messageInput = null;

            do {
                try {
                    messageInput = messageList.take();
                    if (messageInput != null) {
                        out.writeObject(messageInput);
                        messageInput = null;
                    }
    
                    // testing code to read from the server
                    response = (TestMessage) in.readObject();
                    System.out.println("Response = " + response);
                    if (agent != null) {
                        if (response != null) {
                            agent.addMessage(response);
                            response = null;
                        }
                    } else if (house != null) {
//                        house.addMessage(response);
                    }
                } catch (EOFException eof) {
                    agent.setConnected();
                    out.close();
                    in.close();
                    System.out.println("Server has been closed");
                    break;
                } catch (ClassNotFoundException cnf) {
                    cnf.printStackTrace();
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            } while (connected);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    /**
     * Creates and assigns an account to an agent.
     * @return the newly opened account
     */
    public Account openAccount(String name, double balance){
        return new Account(name,
                           agent.getId(),
                           balance,
                           balance);
    }

    /**
     * Adds an auction house to the list of auction houses.
     * @param house the house to add to the bank
     */
    public void addAuctionHouse(AuctionHouse house) {
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
