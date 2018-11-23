package Proxies;

import Agent.*;
import AuctionHouse.*;
import Bank.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
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
    private BufferedReader stdIn;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket client = null;
    
    private LinkedBlockingQueue<TestMessage<Agent, String>> messageList =
        new LinkedBlockingQueue<>();
    
    /**
     * Constructor for the bank proxy.
     * Builds a reference to the bank for bank functionality
     */
    public BankProxy(String host, int port, Agent agent) {
        this.host = host;
        this.port = port;
        this.agent = agent;
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
     * Adding a message to the banks input stream.
     */
    public void sendMessage(String inMessage, Agent agent) {
        try {
            messageList.put(new TestMessage<>(agent, inMessage));
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    /**
     * Overriding the run method to perform specialized tasks.
     */
    @Override
    public void run() {
        try {
            TestMessage<Agent, String> response;

            do {
                try {
                    response = messageList.take();
                    if (response != null) {
                        // analyze the message will happen here
                        // and then proper passing of the object will occur
                        out.writeObject(response);
                    }
                    
                    // testing code to read from the server
                    System.out.println(in.readObject());
                } catch (EOFException eof) {
                    System.out.println("Server has been closed");
                    break;
                } catch (ClassNotFoundException cnf) {
                    cnf.printStackTrace();
                }
            } while (connected);
            agent.setConnected();
            out.close();
            in.close();
            stdIn.close();
        } catch (IOException io) {
            io.printStackTrace();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    /**
     * Creates and assigns an account to an agent.
     * @return the newly opened account
     */
    public Account openAccount(int balance){
        
        //// -------------------------------------------------------------->
        return new Account(8, 90, 90);
        
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
