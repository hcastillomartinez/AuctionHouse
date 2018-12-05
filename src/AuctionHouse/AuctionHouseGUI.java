package AuctionHouse;

import Bank.Account;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
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

    private AnchorPane root;
    private static AuctionHouse auctionHouse;
    private Account auctionAccount;
    private List<Item> auctionItems;
    private int bankAccount;
    private double balance;
    private ListView<Item> displayItems;
    private List<Auction> auctions;
    private ListView<Auction> auctionListView;
    private Timeline timeline;
    private Timer timer;
    private boolean auctionOpen;
    private Text itemsText,auctionsText;
    private Label accountNumberLabel,balanceLabel,accLabel,balLabel;

    public AuctionHouseGUI(){
        root=new AnchorPane();
        createGUI();
        root.getChildren().addAll(displayItems,auctionListView,balanceLabel,
                balLabel,itemsText,auctionsText,accLabel,accountNumberLabel);
    }

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
    public void updateItemList(){
        System.out.println("item list");
        displayItems.getItems().clear();
        displayItems.getItems().addAll(auctionItems);
    }

    public void updateAuctionList(){
        System.out.println("auction list");
        auctionListView.getItems().clear();
        auctionListView.getItems().addAll(auctions);
    }

    void updateBalance(){
        balance=auctionAccount.getBalance();
        balLabel.setText(""+balance);
    }

    private void createGUI(){
        root.setStyle("-fx-background-color: lightblue");
        displayItems=new ListView<>();
        auctionItems=auctionHouse.getItemList();
        auctions=auctionHouse.getAuctions();
        auctionListView=new ListView<>();

        accountNumberLabel=new Label("ACCOUNT NUMBER: ");
        accountNumberLabel.setStyle("-fx-font-size: 18");
        balanceLabel=new Label("BALANCE: ");
        balanceLabel.setStyle("-fx-font-size: 18");
        accountNumberLabel.setLayoutX(14);
        accountNumberLabel.setLayoutY(322);
        balanceLabel.setLayoutX(14);
        balanceLabel.setLayoutY(389);

        itemsText=new Text("ITEMS");
        itemsText.setStyle("-fx-font-size: 20");
        auctionsText=new Text("AUCTIONS");
        auctionsText.setStyle("-fx-font-size: 20");
        itemsText.setLayoutX(114);
        itemsText.setLayoutY(22);
        auctionsText.setLayoutX(386);
        auctionsText.setLayoutY(22);

        accLabel=new Label(""+auctionHouse.getHouseID());
        accLabel.setStyle("-fx-background-color: white;"+"-fx-font-size: 18");
        accLabel.setLayoutX(192);
        accLabel.setLayoutY(322);
        balLabel=new Label("0");
        balLabel.setStyle("-fx-background-color: white;"+"-fx-font-size: 18");
        balLabel.setLayoutX(109);
        balLabel.setLayoutY(389);

        auctionAccount=auctionHouse.getAccount();
        balance=auctionAccount.getBalance();
        bankAccount=auctionAccount.getAccountNumber();

        root.setPrefSize(600,500);
        displayItems.setLayoutX(0);
        displayItems.setLayoutY(30);
        displayItems.setPrefSize(300,250);
        auctionListView.setLayoutY(30);
        auctionListView.setLayoutX(300);
        auctionListView.setPrefSize(300,250);
        displayItems.getItems().addAll(auctionItems);
    }



    @Override
    public void start(Stage primaryStage) {
        AuctionHouseGUI ah=new AuctionHouseGUI();
        auctionHouse.auctionHouseGUI=ah;



        Scene scene=new Scene(ah.root);
        primaryStage.setTitle("Auction House");
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
