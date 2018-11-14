package AuctionHouse;

import java.util.List;

public class AuctionHouse {
    private int bidderTally;
    private String type;
    private List<Item> itemList;
    private MakeItems makeItems;

    /**
     * Expects an int that represents what type
     * auction house will be. 1 for furniture, 2 for tech
     * and any other number for car.
     * @param type An int
     */
    public AuctionHouse(int type){
        bidderTally=0;
        makeItems=new MakeItems();
        itemList=makeItems.getItems(type);
        this.type=makeItems.getListType();
    }

    /**
     * Returns the items for sale in auction house.
     * @return a list.
     */
    public List<Item> getItemList(){
        return itemList;
    }

    /**
     * Finds the most expensive item
     * in auction is and gets the price.
     * @return An int that is max price
     */
    public int maxPrice(){
        int max=0;
        for(Item t:itemList){
            if(t.getPrice()>max)max=t.getPrice();
        }
        return max;
    }

    /**
     * Gets the type of items sold at an
     * auction.
     * @return A string that is type.
     */
    public String getType() {
        return type;
    }

    /**
     * Finds the cheapest item in auction
     * and gets its price.
     * @return
     */
    public int lowestPrice(){
        int min=1000;
        for(Item t:itemList){
            if(t.getPrice()<min)min=t.getPrice();
        }
        return min;
    }
}
