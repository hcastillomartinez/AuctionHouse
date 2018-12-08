package AuctionHouse;

import Bank.Account;
import MessageHandling.Message;
import MessageHandling.MessageTypes;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.util.Timer;

/**
 * GUI for the auction house, initial window is for the type of auction
 * house,its port it will exist on, and the banks ip address.
 */
public class AuctionHouseGUI extends Application {

    private AnchorPane root=new AnchorPane();
    private static AuctionHouse auctionHouse;
    private Account auctionAccount;
    private int bankAccount;
    private double balance;
    private ListView<Item> displayItems;
    private ObservableList<Item> itemObservableList;
    private ListView<Auction> auctionListView;
    private ObservableList<Auction> auctionObservableList;
    private Timeline timeline;
    private Timer timer;
    private boolean auctionOpen;
    private Text itemsText,auctionsText;
    private Label accountNumberLabel,balanceLabel,accLabel,balLabel;
    private static String myServername;

    /**
     * Used to launch the GUI from another class.
     */
    public static void launch(String[] args){
        myServername=args[0];
        AuctionHouseGUI.launch(AuctionHouseGUI.class);
    }


    /**
     * Starts a thread that will be updating the item the item list until the
     * GUI is exited via red x.
     */
    private void updateLists(){
        auctionObservableList.setAll(auctionHouse.getAuctions());
        itemObservableList.setAll(auctionHouse.getItemList());
    }

    /**
     * Used to update the bank info on the screen .
     */
   private void updateBankInfo(){
       auctionAccount=auctionHouse.getAccount();
       accLabel.setText(""+auctionAccount.getAccountNumber());
       balance=auctionAccount.getBalance();
       balLabel.setText(""+balance);
    }

    /**
     * Builds the GUI once connection established with bank.
     */
    private void createGUI(){
        root.setStyle("-fx-background-color: lightblue");
        timeline=new Timeline();
        timer=new Timer();

        itemObservableList= FXCollections.observableArrayList();
        auctionObservableList=FXCollections.observableArrayList();
        itemObservableList.setAll(auctionHouse.getItemList());
        auctionObservableList.setAll(auctionHouse.getAuctions());

        displayItems=new ListView<>(itemObservableList);
        auctionListView=new ListView<>(auctionObservableList);

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
    }



    @Override
    public void start(Stage primaryStage) {
        final Stage nestStage=new Stage();
        nestStage.setResizable(false);
        Pane pane=new Pane();
        TextField typeT=new TextField();
        TextField portT=new TextField();
        TextField serverNam=new TextField();
        typeT.setLayoutX(0);
        typeT.setLayoutY(0);
        typeT.setPrefSize(80,20);
        portT.setLayoutX(0);
        portT.setLayoutY(25);
        portT.setPrefSize(80,20);
        serverNam.setLayoutX(0);
        serverNam.setLayoutY(50);
        serverNam.setPrefSize(80,20);

        typeT.setText("furniture");
        portT.setText("5555");
        //64.106.21.151
        serverNam.setText("localhost");

        Button add=new Button("Launch");
        add.setLayoutX(100);
        pane.getChildren().addAll(portT,typeT,serverNam,add);
        add.setOnMousePressed(el->{
            if(!portT.getText().equals("") && !typeT.getText().equals("")
                    && !serverNam.getText().equals("") ) {

                auctionHouse=new AuctionHouse(typeT.getText(),portT.getText() ,
                        serverNam.getText(),myServername);
                Thread t=new Thread(auctionHouse);
                t.start();
                createGUI();

                root.getChildren().addAll(displayItems,auctionListView,balanceLabel,
                        balLabel,itemsText,auctionsText,accLabel,accountNumberLabel);

                timeline=new Timeline(new KeyFrame(Duration.millis(1),
                        event -> {
                    if(auctionHouse.isUpdateGUI()){
                        updateLists();
                        updateBankInfo();
                        auctionHouse.setUpdateGUI(false);
                    }
                }));
                timeline.setCycleCount(Animation.INDEFINITE);
                timeline.play();


                Scene scene1=new Scene(root);
                primaryStage.setTitle("Auction House "+portT.getText());
                primaryStage.setResizable(false);
                primaryStage.setScene(scene1);
                primaryStage.show();
                nestStage.close();
            }
        });

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                if(!auctionHouse.isSafeToClose()){
                    Alert alert=new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Not Safe To Close");
                    alert.setHeaderText("Processes Still Running");
                    alert.show();
                    windowEvent.consume();
                }
            }
        });
        Scene scene = new Scene(pane);
        nestStage.setTitle("Auction Info");
        nestStage.setScene(scene);
        nestStage.show();

    }

    @Override
    public void stop(){
        auctionHouse.sendToAllClient(new Message("auction house",
                MessageTypes.CLOSE,auctionHouse.getPort()));
        System.out.println("GUI closed");
        timeline.stop();
        System.exit(1);
        auctionOpen=false;
    }
}
