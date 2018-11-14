package AuctionHouse;

/**
 * Furniture possibilities.
 */
public enum Furniture {
    chair,table,nightstand,desk,lamp,stool,trunk,drawer,bedframe;

    /**
     * Enum type is displayed as "furniture".
     * @return A String.
     */
    public String getIDType(){
        return "furniture";
    }
}
