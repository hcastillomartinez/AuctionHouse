package AuctionHouse;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MakeItems {
    private List<Item> items;
    private List<Furniture> furnitureList= Arrays.asList(Furniture.values());
    private String[] colors={"black","red","blue","orange"};

    private MakeItems(){
        items=new ArrayList<>();
    }

    public List<Item> getItems(){
        makeList();
        return items;
    }
    private void makeList(){
        for(Furniture furniture:furnitureList){
            for(String c:colors){
                items.add(new Item(furniture.toString()+" "+c, randPrice()));
            }
        }
    }
    private int randPrice(){
        Random rand =new Random();
        return rand.nextInt((200-50)+1)+50;
    }

}
