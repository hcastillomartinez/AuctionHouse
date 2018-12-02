package AuctionHouse;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * GUI for the auction house, testing currently.
 */
public class AuctionHouseGUI extends Application {

    private AnchorPane root=new AnchorPane();
    private static AuctionHouse auctionHouse;
    private List<Item> auctionItems;
    private ListView<Item> displayItems;
    private boolean auctionOpen;


    public void setAuctionItems(List<Item> auctionItems) {
        this.auctionItems = auctionItems;
    }

    public void setAuctionOpen(boolean auctionOpen) {
        this.auctionOpen = auctionOpen;
    }

    /**
     * Used to launch the GUI from another class.
     */
    public static void launch(String[] args){
        auctionHouse=new AuctionHouse("1","4444","localhost");
        Thread t=new Thread(auctionHouse);
        t.start();
        AuctionHouseGUI.launch(AuctionHouseGUI.class);
    }


    /**
     * Starts a thread that will be updating the item the item list until the
     * GUI is exited via red x.
     */
    private void updateItemList(){
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                while(auctionOpen) {
                    System.out.println("caa");
                    displayItems.getItems().removeAll();
                    displayItems.getItems().addAll(auctionItems);
                }
            }
        });
        t.start();
    }

    @Override
    public void start(Stage primaryStage) {
        displayItems=new ListView<>();
        auctionItems=auctionHouse.getItemList();

        root.setPrefSize(500,500);
        displayItems.setLayoutX(0);
        displayItems.setLayoutY(0);
        displayItems.setPrefSize(350,250);

        root.getChildren().add(displayItems);
        displayItems.getItems().addAll(auctionItems);
//        updateItemList();

        Scene scene=new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop(){
        System.out.println("GUI closed");
        auctionOpen=false;
    }
}
