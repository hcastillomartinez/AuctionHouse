package Bank;

/**
 * Used by bank to store Auction House information in a list
 */
public class AuctionInfo {
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
}
