package Proxies;

import Agent.*;
import AuctionHouse.*;
import MessageHandling.Message;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Proxies.BankProxy.java is the class that is the intermediary between the
 * Bank and the Agent. The class provides higher level functionality to
 * interact with the bank.
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
     * @param port to connect to
     * @param host of the server
     * @param agent to pass the message through
     */
    public BankProxy(String host, int port, Agent agent) {
        this.host = host;
        this.port = port;
        this.agent = agent;
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
    public void closeConnections() {
        connected = !connected;
    }
    
    private void closeSocket() {
        try {
            out.close();
            in.close();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    /**
     * Taking messages and writing them in response.
     */
    private void analyzeMessages() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (connected) {
                    try {
                        out.writeObject((Message) messageQueue.take());
                        out.reset();
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    } catch (IOException io) {
                        io.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    /**
     * Adding a message to the banks input stream.
     * @param inMessage message to place in the queue
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
            Message response = null;
            analyzeMessages();
            while (connected) {
                try {
                    response = (Message) in.readObject();
                    if (agent != null) {
                        if (response != null) {
                            agent.addMessage(response);
                        }
                    }
                } catch (EOFException eof) {
                    closeConnections();
                    break;
                } catch (ClassNotFoundException cnf) {
                    cnf.printStackTrace();
                }
            }
            closeSocket();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
