package Proxies;

import Agent.*;
import AuctionHouse.*;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Proxies.AuctionHouseProxy is the proxy for the auction house class. The proxy
 * provides high level functionality as a mediary between the actual auction
 * house and the agent.
 * Danan High, 11/15/2018
 */
public class AuctionHouseProxy implements Runnable {

    private LinkedList<AuctionHouse> houseList;
    private AuctionHouse auctionHouse;
    private Socket client = null;
    private String host;
    private int port;
    private boolean connected;
    private LinkedBlockingQueue<Message> messages;

    /**
     * Constructor for the Proxies.AuctionHouseProxy, builds a reference to the
     * auction house.
     */
    public AuctionHouseProxy() {
        houseList = new LinkedList<>();
        messages = new LinkedBlockingQueue<Message>();
    }

    /**
     * Returning the port that the auction house sits on.
     * @return number of the port
     */
    public int getPort(){
        return auctionHouse.getPort();
    }

    /**
     * Returning the server name of the auction house.
     * @return name of the server
     */
    public String getServerName() {
        return auctionHouse.getServerName();
    }

    /**
     * Returns the items for sale in auction house.
     * @return a list.
     */
    public List<Item> getItemList(){
        return auctionHouse.getItemList();
    }

    /**
     * Connecting to the server.
     */
    public void connectToAuctionHouse(String host, int port) {
        this.host = host;
        this.port = port;
        connected = true;
        run();
    }

    /**
     * Finds the most expensive item in auction is and gets the price.
     * @return An int that is max price
     */
    public double maxPrice() {
        double max = 0;
        for(Item t: auctionHouse.getItemList()){
            if(t.getPrice() > max) {
                max = t.getPrice();
            }
        }
        return max;
    }

    /**
     * Gets the type of items sold at an auction.
     * @return A string that is type.
     */
    public String getType() {
        return auctionHouse.getType();
    }

    /**
     * Finds the cheapest item in auction and gets its price.
     * @return lowest price in the auction house.
     */
    public double lowestPrice() {
        double min = maxPrice();
        for(Item t: auctionHouse.getItemList()) {
            if (min > t.getPrice()) {
                min = t.getPrice();
            }
        }
        return min;
    }
    
    /**
     * Closing the client port.
     */
    private void closeClient(ObjectInputStream inputStream,
                             ObjectOutputStream outputStream,
                             BufferedReader input) {
        try {
            outputStream.close();
            inputStream.close();
            input.close();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    /**
     * Overriding the run method to perform certain tasks.
     */
    @Override
    public void run() {
        
        try {
            ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(client.getInputStream());
            Message message = null;
            
            do {
                try {
                    message = messages.take();
                    if (message != null) {
                        // this is where the message analysis should take
                        // place when fully implemented.
                        out.writeObject(message);
                    }
                    
                    // testing code to write to the auction house
                    System.out.println(in.readObject());
                } catch (ClassNotFoundException cnf) {
                    cnf.printStackTrace();
                } catch (EOFException eof) {
                    System.out.println("Server has disconnected!");
                    connected = false;
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            } while (connected);
        } catch (IOException io) {
            io.printStackTrace();
        }
        
    }
}





































