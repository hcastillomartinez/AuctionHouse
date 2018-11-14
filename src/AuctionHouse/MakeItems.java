package AuctionHouse;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MakeItems {
    private List<Item> items;
    private String[] colors={"black","red","blue","orange"};
    private String[] condition={"used","new","like-new","worn"};

    public MakeItems(){
        items=new ArrayList<>();
    }

    /**
     *Returns list
     * @param typeID
     * @return
     */
    public List<Item> getItems(int typeID){
        if(typeID==1){
            makeFurnitureList();
            return items;
        }
        else if(typeID==2){
            makeTechList();
            return items;
        }
        else{
            makeCarList();
            return items;
        }
    }

    public String getListType(){
        return items.get(0).getType();
    }

    private void makeCarList(){
        List<Car> carList=Arrays.asList(Car.values());
        for(Car car: carList){
            for(String s: condition){
                items.add(new Item(s+" "+car.toString(),randPrice(5000,500),car.getTypeID()));
            }
        }
    }
    private void makeFurnitureList(){
        List<Furniture> furnitureList= Arrays.asList(Furniture.values());
        for(Furniture furniture:furnitureList){
            for(String c:colors){
                items.add(new Item(c+" "+furniture.toString(),randPrice(200,50),furniture.getIDType()));
            }
        }
    }

    private void makeTechList(){
        List<Tech> techList=Arrays.asList(Tech.values());
        for(Tech tech:techList){
            for(String s:condition){
                items.add(new Item(s+" "+tech.toString(),randPrice(600,200),tech.getIDType()));
            }
        }
    }

    private int randPrice(int max,int min){
        Random rand =new Random();
        return rand.nextInt((max-min)+1)+min;
    }


}
