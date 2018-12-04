package Bank;

import Agent.Agent;
import Agent.AgentGUI;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
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

    //titled panes for accounts
    private TitledPane agentsPane;
    private TitledPane housesPane;

    private final Background WHITE = new Background(new BackgroundFill(Color.WHITE,
                                                                  null,
                                                                 null));

    public BankGUI(){
        mainPane = new Pane();

        initializeObservableLists();
        initializeListViews();
        initializeTitledPanes();

        mainPane.getChildren().addAll(agentsPane, housesPane);
    }

    void refreshAccountInformation(){
        agentAccountList.clear();
        agentAccountList.addAll(getAgentAccounts());

        auctionHouseAccountList.removeAll();
        auctionHouseAccountList.addAll(getHouseAccounts());
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

    private void initializeObservableLists(){
        agentAccountList = FXCollections.observableArrayList();
        auctionHouseAccountList = FXCollections.observableArrayList();
    }

    /**
     * Launching the application.
     * @param args for the responses input
     */
    public static void launch(String...args) {

        String address;
        int portNumber;
        if(args.length >= 1){
             address = args[0];
             portNumber = Integer.parseInt(args[1]);
        }
        else{
            System.out.println("Error: Invalid program arguments. The first argument must be the bank port number.");
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
        bank.gui = gui;

        Scene scene = new Scene(gui.mainPane);
        primaryStage.setTitle("Bank");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }

}
