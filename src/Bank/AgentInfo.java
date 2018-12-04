package Bank;

import java.io.Serializable;

/**
 * Used by bank to store Agent information in a list.
 */
public class AgentInfo implements Serializable {
    
    private static final long serialVersionUID = 250L;
    String name;
    String IPAddress;
    Integer auctionID, portNumber, idNumber, accountNumber;

    public AgentInfo(String name,
                       String IPAddress,
                       Integer auctionID,
                       int portNumber,
                       int idNumber) {
        this.name = name;
        this.IPAddress = IPAddress;
        this.auctionID = auctionID;
        this.portNumber = portNumber;
        this.idNumber = idNumber;
    }

    public String getName() {
        return name;
    }

    public String getIPAddress() {
        return IPAddress;
    }

    public int getAuctionID() {
        return auctionID;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public int getIdNumber() {
        return idNumber;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public synchronized void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }
}
