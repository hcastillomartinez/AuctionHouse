package Bank;

import Agent.Agent;
import Agent.AgentGUI;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * @author Daniel Miller
 */
public class BankGUI extends Application {
    Pane mainPane;
    static Bank bank;

    private final BackgroundFill BLUE
            = new BackgroundFill(Color.BLUE, null, null);//todo remove

    // final variables
    private final double WIDTH
            = Screen.getPrimary().getBounds().getWidth() * 0.75;
    private final double HEIGHT
            = Screen.getPrimary().getBounds().getHeight() * 0.75;

    private ObservableList<Account> agentAccountList = FXCollections.observableArrayList();
    private ObservableList<Account> auctionHouseAccountList = FXCollections.observableArrayList();


    //list views for displaying agent and auction house bank accounts
    private ListView<Account> agentAccountsListView;
    private ListView<Account> auctionHouseAccountsListView;

    private final Background WHITE = new Background(new BackgroundFill(Color.WHITE,
                                                                  null,
                                                                 null));

    public BankGUI(){
        mainPane = new Pane();
        //for debugging
        mainPane.setBackground(WHITE);

        //initialize ObservableLists
        agentAccountList = FXCollections.observableArrayList(new ArrayList<Account>());
        auctionHouseAccountList = FXCollections.observableArrayList(new ArrayList<Account>());

        //initialize listviews todo move to methods
        agentAccountsListView = new ListView<>(agentAccountList);
        agentAccountsListView.setEditable(true);
        agentAccountsListView.setLayoutX(0);
        agentAccountsListView.setLayoutY(0);

        auctionHouseAccountsListView = new ListView<>(auctionHouseAccountList);
        auctionHouseAccountsListView.setEditable(true);
        auctionHouseAccountsListView.setLayoutX(WIDTH / 2);
        auctionHouseAccountsListView.setLayoutY(0);

        mainPane.getChildren().addAll(agentAccountsListView,auctionHouseAccountsListView);






    }

    ArrayList<Account> getHouseAccounts(){
        ArrayList<Account> accounts = new ArrayList<>();
        for(AuctionInfo house : bank.getAuctionHouses()){
            Account account = bank.getAccounts().get(house.getAccountNumber());
            accounts.add(account);
        }
        return accounts;
    }

    ArrayList<Account> getAgentAccounts(){
        ArrayList<Account> accounts = new ArrayList<>();
        for(AgentInfo agent : bank.getAgents()){
            Account account = bank.getAccounts().get(agent.getAccountNumber());
            accounts.add(account);
        }
        return accounts;
    }

    /**
     * Launching the application.
     * @param args for the responses input
     */
    public static void launch(String...args) {

        String address;
        int portNumber;
        if(args.length >= 1){
             portNumber = Integer.parseInt(args[1]);
             address = args[0];
        }
        else{
            System.out.println("Error: Invalid program arguments. The first argument must be the bank port number.");
            return;
        }

        bank = new Bank(address, portNumber);
        Thread bankThread = new Thread(bank);
        bankThread.start();

        BankGUI.launch(BankGUI.class);
        //bank.closeApplicationConnection();
    }

    /**
     * Function to start running the application.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        BankGUI gui = new BankGUI();


        Scene scene = new Scene(gui.mainPane);
        primaryStage.setTitle("Bank");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        System.out.println("here");
    }

}
