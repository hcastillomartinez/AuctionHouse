package Bank;

/**
 * Used by bank to store Agent information in a list.
 */
public class AgentInfo {
    String name;
    String IPAddress;
    int auctionID, portNumber, idNumber, accountNumber;

    public AgentInfo(String name,
                       String IPAddress,
                       int auctionID,
                       int portNumber,
                       int idNumber) {
        this.name = name;
        this.IPAddress = IPAddress;
        this.auctionID = auctionID;
        this.portNumber = portNumber;
        this.idNumber = idNumber;
    }

}
