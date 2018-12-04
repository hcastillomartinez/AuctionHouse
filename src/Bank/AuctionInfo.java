package Bank;

import java.io.Serializable;

/**
 * A dummy class that holds auction house information that is pertinent to the bank.
 * @author Daniel Miller
 */
public class AuctionInfo implements Serializable{
    
    private static final long serialVersionUID = 3000L;
    private String name; //name of the auction house
    private String IPAddress; //the IPAddress of the house
    private int accountNumber, portNumber, auctionID; //it's account number, port number and id

    /**
     * Constructor for AuctionInfo
     */
    public AuctionInfo(String name,
                       String IPAddress,
                       int accountNumber,
                       int portNumber) {
        this.name = name;
        this.IPAddress = IPAddress;
        this.accountNumber = accountNumber;
        this.portNumber = portNumber;
    }

    /**
     * Gets the name of the house
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the account number of the house
     * @return
     */
    public int getAccountNumber() {
        return accountNumber;
    }

    /**
     * Gets the id of the house
     * @return
     */
    public int getAuctionID(){
        return auctionID;
    }

    /**
     * Gets the IP Address of the house
     * @return
     */
    public String getIPAddress() {
        return IPAddress;
    }

    /**
     * Gets the port number of the house
     * @return
     */
    public int getPortNumber() {
        return portNumber;
    }
}
