package AuctionHouse;

import java.util.List;

public class AuctionHouse {
    private int bidderTally;
    private String type;
    private List<Item> itemList;
    private MakeItems makeItems;
    public AuctionHouse(int type){
        bidderTally=0;
        makeItems=new MakeItems();
        itemList=makeItems.getItems(type);
        this.type=makeItems.getListType();
    }

    public List<Item> getItemList(){
        return itemList;
    }

    public int maxPrice(){
        int max=0;
        for(Item t:itemList){
            if(t.getPrice()>max)max=t.getPrice();
        }
        return max;
    }

    public String getType() {
        return type;
    }

    public int lowestPrice(){
        int min=1000;
        for(Item t:itemList){
            if(t.getPrice()<min)min=t.getPrice();
        }
        return min;
    }
}
