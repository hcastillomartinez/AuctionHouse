package Bank;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * The GUI for Bank.
 * @author Daniel Miller
 */
public class BankGUI extends Application {
    Pane mainPane;
    static Bank bank; //reference to the static bank

    // final variables
    private final double WIDTH
            = Screen.getPrimary().getBounds().getWidth() * 0.75;
    private final double HEIGHT
            = Screen.getPrimary().getBounds().getHeight() * 0.75;

    //observable lists for agent and auction house bank accounts
    private ObservableList<Account> agentAccountList = FXCollections.observableArrayList();
    private ObservableList<Account> auctionHouseAccountList = FXCollections.observableArrayList();

    //list views for displaying agent and auction house bank accounts
    private ListView<Account> agentAccountsListView;
    private ListView<Account> auctionHouseAccountsListView;

    //titled panes to display accounts
    private TitledPane agentsPane;
    private TitledPane housesPane;

    /**
     * Constructor for BankGUI
     */
    public BankGUI() {
        mainPane = new Pane();

        initializeObservableLists();
        initializeListViews();
        initializeTitledPanes();

        mainPane.getChildren().addAll(agentsPane, housesPane);
    }

    /**
     * Updates the observable lists to contain the most recent account information
     */
    void refreshAccountInformation(){
        agentAccountList.setAll(getAgentAccounts());
        auctionHouseAccountList.setAll(getHouseAccounts());
    }

    /**
     * @return a list of all auction house bank accounts
     */
    private ArrayList<Account> getHouseAccounts(){
        ArrayList<Account> accounts = new ArrayList<>();
        for(Account account : bank.getAccountsAsList()){
            if(!account.isAgent()){
                accounts.add(account);
            }
        }
        return accounts;
    }

    /**
     * @return a list of all agent bank accounts
     */
    private ArrayList<Account> getAgentAccounts(){
        ArrayList<Account> accounts = new ArrayList<>();
        for(Account account : bank.getAccountsAsList()){
            if(account.isAgent()){
                accounts.add(account);
            }
        }
        return accounts;
    }

    /**
     * Initializes the two titled panes used in the GUI.
     */
    private void initializeTitledPanes(){
        agentsPane = new TitledPane("Agent Accounts",agentAccountsListView);
        agentsPane.setPrefSize(WIDTH / 2,HEIGHT);
        agentsPane.setLayoutX(0);
        agentsPane.setLayoutY(0);

        housesPane = new TitledPane("Auction House Accounts",auctionHouseAccountsListView);
        housesPane.setPrefSize(WIDTH / 2,HEIGHT);
        housesPane.setLayoutX(WIDTH / 2);
        housesPane.setLayoutY(0);
    }

    /**
     * Initializes the two list views used by the GUI.
     */
    private  void initializeListViews(){
        agentAccountsListView = new ListView<>(agentAccountList);
        agentAccountsListView.setEditable(true);
        agentAccountsListView.setLayoutX(0);
        agentAccountsListView.setLayoutY(0);

        auctionHouseAccountsListView = new ListView<>(auctionHouseAccountList);
        auctionHouseAccountsListView.setEditable(true);
        agentAccountsListView.setLayoutX(0);
        agentAccountsListView.setLayoutY(0);
    }

    /**
     * Initializes the two observable lists used by the GUI
     */
    private void initializeObservableLists(){
        agentAccountList = FXCollections.observableArrayList();
        auctionHouseAccountList = FXCollections.observableArrayList();
    }

    /**
     * Launches the application.
     * @param args IP Address and Port Number
     */
    public static void launch(String...args) {

        String address;
        int portNumber;
        if(args.length >= 1){
             address = args[0];
             portNumber = Integer.parseInt(args[1]);
        }
        else{
            System.out.println("Error: Invalid program arguments.");
            return;
        }

        bank = new Bank(address, portNumber);
        Thread bankThread = new Thread(bank);
        bankThread.start();

        BankGUI.launch(BankGUI.class);
    }

    /**
     * Function to start running the application.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        BankGUI gui = new BankGUI();
        //bank.gui = gui;

        Scene scene = new Scene(gui.mainPane);
        primaryStage.setTitle("Bank");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();


        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        gui.refreshAccountInformation();
                    }
                });
            }
        }, 0, 1000);

    }

    /**
     * Stops the program.
     */
    @Override
    public void stop(){
        System.out.println("GUI closed");
        System.exit(1);
    }
}
