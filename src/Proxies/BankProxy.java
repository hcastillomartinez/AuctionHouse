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
    private BufferedReader stdIn;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket client = null;
    
    private LinkedBlockingQueue<TestMessage<Object, Object>> messageList =
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
    public void sendMessage(Object inMessage, Object agent) {
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
    @SuppressWarnings("unchecked")
    public void run() {
        try {
            TestMessage<Object, Object> response;

            do {
                try {
                    response = messageList.take();
                    if (response != null) {
                        agent.addMessage(analyzeMessages(response));
                    }
                    
                    // testing code to read from the server
                    @SuppressWarnings("unchecked") TestMessage<Object, Object> m
                        = (TestMessage<Object, Object>) in.readObject();
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
     * Function to handle the analyzing of the messages.
     */
    @SuppressWarnings("unchecked")
    private TestMessage<Object, Object> analyzeMessages(TestMessage<Object, Object> message) {
        if (message.getSender()
                   .getClass()
                   .equals(AuctionHouseProxy.class)) {
            if (message.getDetailedMessage()
                       .getClass()
                       .equals(String.class)) {
                String response = (String) message.getDetailedMessage();
                // look back here for setting up the auction house proxy
                // response.
                
            }
        } else if (message.getSender()
                          .getClass()
                          .equals(Agent.class)) {
            if (message.getDetailedMessage()
                       .getClass()
                       .equals(String.class)) {
                String mail = (String) message.getDetailedMessage();
                
                if (mail.contains("new account")) {
                    String sendBack = "name and amount";
                    return new TestMessage<Object, Object>(this, sendBack);
                }
            } else if (message.getDetailedMessage()
                              .getClass()
                              .equals(Account.class)) {
                return new TestMessage<Object, Object>(this,
                                                       message.getDetailedMessage());
            }
        }
        return null;
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
