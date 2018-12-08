package AuctionHouse;

/**
 * possible tech items.
 */
public enum Tech {
    phone,laptop,desktop,keyboard,monitor,processor,GPU;

    /**
     * Enum type is displayed as "tech".
     * @return A String.
     */
    public String getIDType(){
        return "tech";
    }
}
