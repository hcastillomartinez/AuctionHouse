package Proxies;

import Agent.*;
import AuctionHouse.*;
import MessageHandling.Message;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Proxies.BankProxy.java is the class that is the mediary between the Bank and the
 * Agent. The class provides higher level functionality to interact with the
 * bank.
 * @author Danan High, 11/21/2018
 */
public class BankProxy implements Runnable {

    private Agent agent;
    private String host;
    private int port;
    private boolean connected = true;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket client = null;
    private AuctionHouse house;
    private BlockingQueue<Message> messageQueue = new LinkedBlockingQueue<>();
    
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
     * Function to close all of the open connections.
     */
    private void closeConnections() {
        try {
            out.close();
            in.close();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    /**
     * Adding a message to the banks input stream.
     */
    public void sendAgentMessage(Message inMessage) {
        try {
            messageQueue.put(inMessage);
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
            Message response = null, messageInput = null;

            while (connected) {
                try {
                    messageInput = messageQueue.take();
                    if (messageInput != null) {
                        out.writeObject(messageInput);
                    }
        
                    response = (Message) in.readObject();
                    if (agent != null) {
                        if (response != null) {
                            agent.addMessage(response);
                        }
                    } else if (house != null) {
                        if (response != null) {
                            house.placeMessageForAnalyzing(response);
                        }
                    }
                } catch (EOFException eof) {
                    closeConnections();
                    break;
                } catch (ClassNotFoundException cnf) {
                    cnf.printStackTrace();
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
            closeConnections();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
