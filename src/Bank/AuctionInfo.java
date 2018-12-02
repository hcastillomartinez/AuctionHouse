package Bank;

import java.io.Serializable;

/**
 * Used by bank to store Auction House information in a list
 */
public class AuctionInfo implements Serializable{
    
    private static final long serialVersionUID = 3000L;
    String name;
    String IPAddress;
    int auctionID, portNumber;

    public AuctionInfo(String name,
                       String IPAddress,
                       int auctionID,
                       int portNumber) {
        this.name = name;
        this.IPAddress = IPAddress;
        this.auctionID = auctionID;
        this.portNumber = portNumber;
    }

    public String getName() {
        return name;
    }

    public int getAuctionID() {
        return auctionID;
    }

    public String getIPAddress() {
        return IPAddress;
    }

    public int getPortNumber() {
        return portNumber;
    }
}
