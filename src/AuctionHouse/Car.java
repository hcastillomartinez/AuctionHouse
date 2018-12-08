package AuctionHouse;

/**
 * Contains 6 different auction items, type is car.
 */
public enum Car {
    sedan,SUV,truck,coupe,hatchback,crossover;

    /**
     * Enum type is displayed as "car".
     * @return A String.
     */
    public String getTypeID(){
        return "car";
    }
}
