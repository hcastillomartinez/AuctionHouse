package AuctionHouse;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.util.List;

/**
 * GUI for the auction house, testing currently.
 */
public class AuctionHouseGUI extends Application {

    private AnchorPane root;
    private List<Item> auctionItems;
    private ListView<Item> displayItems;
    private boolean auctionOpen;

    public AuctionHouseGUI(List<Item> items, boolean open){
        root=new AnchorPane();
        displayItems=new ListView<>();
        auctionItems=items;
        auctionOpen=open;
    }

    /**
     * Used to launch the GUI from another class.
     */
    public static void launch(){
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
                    displayItems.getItems().removeAll();
                    displayItems.getItems().addAll(auctionItems);
                }
            }
        });
        t.start();
    }

    @Override
    public void start(Stage primaryStage) {
        root.setLayoutX(Screen.getPrimary().getBounds().getWidth()*.75);
        root.setLayoutY(Screen.getPrimary().getBounds().getHeight()*.75);
        displayItems.setLayoutX(100);
        displayItems.setLayoutY(100);
        displayItems.setPrefSize(200,200);

        root.getChildren().add(displayItems);

        updateItemList();
        Scene scene=new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop(){
        auctionOpen=false;
    }
}
