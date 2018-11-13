package AuctionHouse;


public class Item{
    private String itemName;
    private int price;


    public Item(String itemName,int price){
        this.itemName=itemName;
        this.price=price;
    }

    @Override
    public String toString(){
        return "item: "+itemName+" Price: "+price+"\n";
    }

    public String getItemName(){
        return itemName;
    }

    public int getPrice(){
        return price;
    }

}