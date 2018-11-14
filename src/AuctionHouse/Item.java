package AuctionHouse;


public class Item{
    private String itemName;
    private int price;
    private String type;

    public Item(String itemName,int price,String type){
        this.itemName=itemName;
        this.price=price;
        this.type=type;
    }

    @Override
    public String toString(){
        return "item: "+itemName+" Price: "+price+"\n";
    }

    public String getItemName(){
        return itemName;
    }

    public String getType(){
        return type;
    }
    public int getPrice(){
        return price;
    }

}