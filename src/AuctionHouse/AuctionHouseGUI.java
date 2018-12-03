package AuctionHouse;

import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * GUI for the auction house, testing currently.
 */
public class AuctionHouseGUI extends Application {

    private AnchorPane root=new AnchorPane();
    private static AuctionHouse auctionHouse;
    private List<Item> auctionItems;
    private ListView<Item> displayItems;
    private List<Auction> auctions;
    private ListView<Auction> auctionListView;
    private Timeline timeline;
    private Timer timer;
    private boolean auctionOpen;


//    public void setAuctionItems(List<Item> auctionItems) {
//        this.auctionItems = auctionItems;
//    }
//
//    public void setAuctionOpen(boolean auctionOpen) {
//        this.auctionOpen = auctionOpen;
//    }

    /**
     * Used to launch the GUI from another class.
     */
    public static void launch(String[] args){
        auctionHouse=new AuctionHouse(args[0],args[1],args[2]);
        Thread t=new Thread(auctionHouse);
        t.start();
        AuctionHouseGUI.launch(AuctionHouseGUI.class);
    }


    /**
     * Starts a thread that will be updating the item the item list until the
     * GUI is exited via red x.
     */
    private void updateItemList(){
        System.out.println("item list");
        displayItems.getItems().clear();
        displayItems.getItems().addAll(auctionItems);
    }

    private void updateAuctionList(){
        System.out.println("auction list");
        auctionListView.getItems().clear();
        auctionListView.getItems().addAll(auctions);
    }

    @Override
    public void start(Stage primaryStage) {
        timeline=new Timeline();
        timer=new Timer();
        displayItems=new ListView<>();
        auctionItems=auctionHouse.getItemList();
        auctions=auctionHouse.getAuctions();
        auctionListView=new ListView<>();


        root.setPrefSize(600,500);
        displayItems.setLayoutX(0);
        displayItems.setLayoutY(20);
        displayItems.setPrefSize(300,250);
        auctionListView.setLayoutY(20);
        auctionListView.setLayoutX(300);
        auctionListView.setPrefSize(300,250);

        root.getChildren().addAll(displayItems,auctionListView);
        displayItems.getItems().addAll(auctionItems);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
//                updateItemList();
//                updateAuctionList();
            }
        },1,1);


        Scene scene=new Scene(root);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop(){
//        auctionHouse.sendToBank("close");
        System.out.println("GUI closed");
        System.exit(1);
        timer.cancel();
        auctionOpen=false;
    }
}
