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
     *Returns a list based on the parameter. One
     * of three type of item list are created with
     * each call. 1 for furniture,2 for tech, and anything
     * else for cars.
     * @param typeID An int
     * @return A List
     */
    public List<Item> getItems(String typeID){
        if(typeID.equals("furniture")){
            makeFurnitureList();
            return items;
        }
        else if(typeID.equals("tech")){
            makeTechList();
            return items;
        }
        else{
            makeCarList();
            return items;
        }
    }

    /**
     * Gets the type of item in the List.
     * @return A String
     */
    public String getListType(){
        return items.get(0).getType();
    }

    /**
     * Makes a List with car items.
     */
    private void makeCarList(){
        List<Car> carList=Arrays.asList(Car.values());
        for(Car car: carList){
            for(String s: condition){
                items.add(new Item(s+" "+car.toString()
                        ,randPrice(5000,500),car.getTypeID()));
            }
        }
    }

    /**
     * Makes a List with furniture items.
     */
    private void makeFurnitureList(){
        List<Furniture> furnitureList= Arrays.asList(Furniture.values());
        for(Furniture furniture:furnitureList){
            for(String c:colors){
                items.add(new Item(c+" "+furniture.toString()
                        ,randPrice(200,50),furniture.getIDType()));
            }
        }
    }

    /**
     * Makes a List with tech items.
     */
    private void makeTechList(){
        List<Tech> techList=Arrays.asList(Tech.values());
        for(Tech tech:techList){
            for(String s:condition){
                items.add(new Item(s+" "+tech.toString(),
                        randPrice(600,200),tech.getIDType()));
            }
        }
    }

    /**
     * Makes a random price based on the parameters
     * then returns it.
     * @param max An int
     * @param min An int
     * @return An int
     */
    private int randPrice(int max,int min){
        Random rand =new Random();
        return rand.nextInt((max-min)+1)+min;
    }


}
