package AuctionHouse;


import java.io.Serializable;

/**
 * These are created only once and with values determined in MakeItems.
 */
public class Item implements Serializable {
    
    private static final long serialVersionUID = 7L;
    private String itemName;
    private double price;
    private String type;
    private boolean inBid;

    /**
     * Takes in the name of item, price, and type it
     * should be.
     * @param itemName A String
     * @param price An int
     * @param type A String
     */
    public Item(String itemName,int price,String type){
        this.itemName=itemName;
        this.price=price;
        this.type=type;
        inBid=false;
    }

    /**
     * Sets item to be in bid state.
     * @param inBid A boolean
     */
    public void setInBid(boolean inBid) {
        this.inBid = inBid;
    }


    /**
     * Updates the price of item according to the current bid
     * on it.
     * @param price Amount that price now is
     */
    public void updatePrice(double price){
        this.price=price;
    }

    /**
     * Gets the bid state.
     * @return A boolean
     */
    public boolean isInBid() {
        return inBid;
    }


    /**
     * Gets the item name.
     * @return A String
     */
    public String getItemName(){
        return itemName;
    }

    /**
     * Gets the type of the item.
     * @return A String
     */
    public String getType(){
        return type;
    }

    /**
     * Gets the price of the item.
     * @return An int
     */
    public double getPrice(){
        return price;
    }

    /**
     * Way for item to be printed.
     * @return
     */
    @Override
    public String toString(){
        return "item: "+itemName+" Price: "+price+"\n";
    }
}