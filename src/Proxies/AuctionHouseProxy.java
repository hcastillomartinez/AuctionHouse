package Proxies;

import AuctionHouse.AuctionHouse;
import AuctionHouse.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

/**
 * Proxies.AuctionHouseProxy is the proxy for the auction house class. The proxy
 * provides high level functionality as a mediary between the actual auction
 * house and the agent.
 * Danan High, 11/15/2018
 */
public class AuctionHouseProxy implements Runnable {

    private LinkedList<AuctionHouse> houseList;
    private AuctionHouse auctionHouse;
    private PrintWriter writer;
    private BufferedReader reader, input;
    private Socket client = null;
    private String host;
    private int port;

    /**
     * Constructor for the Proxies.AuctionHouseProxy, builds a reference to the
     * auction house.
     */
    public AuctionHouseProxy() {
        houseList = new LinkedList<>();
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
        run();
    }

    /**
     * Finds the most expensive item in auction is and gets the price.
     * @return An int that is max price
     */
    public int maxPrice() {
        int max = 0;
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
    public int lowestPrice() {
        int min = maxPrice();
        for(Item t: auctionHouse.getItemList()) {
            if (min > t.getPrice()) {
                min = t.getPrice();
            }
        }
        return min;
    }

    @Override
    public void run() {
        try {
            String response = reader.readLine();
            client = new Socket(host, port);
            reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            input = new BufferedReader(new InputStreamReader(System.in));
            writer = new PrintWriter(client.getOutputStream(), true);

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
}





































