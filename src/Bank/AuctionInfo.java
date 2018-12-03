package Bank;

import java.io.Serializable;

/**
 * Used by bank to store Auction House information in a list
 */
public class AuctionInfo implements Serializable{
    
    private static final long serialVersionUID = 3000L;
    String name;
    String IPAddress;
    int accountNumber, portNumber;

    public AuctionInfo(String name,
                       String IPAddress,
                       int accountNumber,
                       int portNumber) {
        this.name = name;
        this.IPAddress = IPAddress;
        this.accountNumber = accountNumber;
        this.portNumber = portNumber;
    }

    public String getName() {
        return name;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getIPAddress() {
        return IPAddress;
    }

    public int getPortNumber() {
        return portNumber;
    }
}
