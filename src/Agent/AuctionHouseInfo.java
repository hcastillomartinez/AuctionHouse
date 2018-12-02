package Agent;

import java.io.Serializable;

/**
 * AuctionHouseInfo is the class that contains the data from the AuctionHouse to
 * allow for connections.
 * @author Danan High, 12/1/2018
 */
public class AuctionHouseInfo implements Serializable {

    private static final long serialVersionUID = 100L;
    private String host, id;
    private int port;

    /**
     * Constructor for the AuctionHouseInfo.
     * @param id of the AuctionHouse
     * @param host information of the AuctionHouse
     * @param port for the server to connect to
     */
    public AuctionHouseInfo(String id, String host, int port) {
        this.id = id;
        this.host = host;
        this.port = port;
    }

    /**
     * Getting the host name for connection.
     * @return host name
     */
    public String getHost() {
        return host;
    }

    /**
     * Setting the host for the new connection.
     * @param host for the new connection.
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Getting the port for the new connection.
     * @return port for the connection
     */
    public int getPort() {
        return port;
    }

    /**
     * Setting the port for the new connection.
     * @param port to connect to.
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Getting the id for the AuctionHouse.
     * @return id of the specific AuctionHouse
     */
    public String getId() {
        return id;
    }

    /**
     * Setting the id for the AuctionHouseInfo.
     * @param id updated id for the ahi
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Overriding toString() to provide the basic naming of the AuctionHouseInfo
     * @return string representation of the AuctionHouseInfo object
     */
    @Override
    public String toString() {
        return "AuctionHouseInfo{" +
               "host='" + host + '\'' +
               ", id='" + id + '\'' +
               ", port=" + port +
               '}';
    }
}
