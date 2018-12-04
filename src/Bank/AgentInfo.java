package Bank;

import java.io.Serializable;

/**
 * A dummy class that holds agent information that is pertinent to the bank.
 * @author Daniel Miller
 */
public class AgentInfo implements Serializable {
    
    private static final long serialVersionUID = 250L;
    String name;
    String IPAddress;
    Integer auctionID, portNumber, idNumber, accountNumber;

    /**
     * Constructor for AgentInfo
     */
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

    /**
     * Gets the name of the agent
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the IP Address of the agent
     */
    public String getIPAddress() {
        return IPAddress;
    }

    /**
     * Gets the id of the agent
     */
    public int getAuctionID() {
        return auctionID;
    }

    /**
     * Gets the port number of the agent
     */
    public int getPortNumber() {
        return portNumber;
    }

    /**
     * Gets the ID Number of the agent
     */
    public int getIdNumber() {
        return idNumber;
    }

    /**
     * Gets the account number of the agent
     */
    public int getAccountNumber() {
        return accountNumber;
    }

    /**
     * Sets the account number of the agent
     */
    public synchronized void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }
}
