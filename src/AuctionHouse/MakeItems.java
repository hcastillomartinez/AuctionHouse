package AuctionHouse;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MakeItems {
    private List<Item> items;
    private List<Furniture> furnitureList= Arrays.asList(Furniture.values());
    private String[] colors={"black","red","blue","orange"};

    public MakeItems(){
        items=new ArrayList<>();
    }

    public List<Item> getFurnitureItems(){
        makeFurnitureList();
        return items;
    }

    private void makeFurnitureList(){
        for(Furniture furniture:furnitureList){
            for(String c:colors){
                items.add(new Item(c+" "+furniture.toString(), randPrice()));
            }
        }
    }

    private int randPrice(){
        Random rand =new Random();
        return rand.nextInt((200-50)+1)+50;
    }

}
