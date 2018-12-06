package Agent;

import AuctionHouse.Item;
import Bank.Account;
import Bank.AgentInfo;
import Bank.AuctionInfo;
import MessageHandling.Message;
import MessageHandling.MessageTypes;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * AgentGUI.java displays the agent gui for user interactions.
 * @author Danan High, 11/29/2018
 */
public class AgentGUI extends Application {

    // final variables
    private final double WIDTH
        = Screen.getPrimary().getBounds().getWidth() * 0.75;
    private final double HEIGHT
        = Screen.getPrimary().getBounds().getHeight() * 0.75;
    
    // static variables
    private static String host;
    private static int port;
    
    // worker fields
    private static Agent agent;
    private ChoiceBox<String> auctionHouses;
    private ListView<String> itemsFromHouse = new ListView<>();
    private ListView<String> bids = new ListView<>();
    private ListView<String> wonItems = new ListView<>();
    
    // filler variables for the boxes
    private Label firName, lastName, acctBalance, pendingBalanceLabel,
        ahLabel, idLabel, itemLabel, bidLabel, bidListLabel, itemListLabel;
    private static TextField firNameField, lastNameField, acctBalanceField,
        pendingField, ahField, idField, itemField, bidField;
    private Button createAccountButton, placeBidButton, selectItemButton,
        updateButton, chooseAuctionHouseButton, updateWonItemsButton;
    
    // box variables
    private VBox accountBox, bidContainer, auctionContainer, appVertContainer;
    private HBox firstNameBox, lastNameBox, accountBalance, aucHouseHBox,
        idBox, itemBox, bidBox, appFullContainer, pendingBalanceBox,
        itemAndBidBox, labelItemAndBidBox, aucHBox, bidAndUpdateBox;
    
    // background fillers
    private final BackgroundFill BLUE
        = new BackgroundFill(Color.BLUE, null, null);
    private final BackgroundFill GREY
        = new BackgroundFill(Color.GREY, new CornerRadii(10), null);

    // timer for updating the lists
    static TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            long past = System.currentTimeMillis();
            while (System.currentTimeMillis() - past < 300) {}
        }
    };
    
    /**
     * Launching the application.
     * @param args for the responses input
     */
    public static void launch(String...args) {
        host = args[0];
        port = Integer.parseInt(args[1]);
        agent = new Agent(host, port);
        AgentGUI.launch(AgentGUI.class);
        agent.closeApplicationConnection();
    }
    
    /**
     * Starting the agent thread.
     */
    private void setupNumbersAndAgent() {
        (new Thread(agent)).start();
    }
    
    /*****************************************************************/
    /*                                                               */
    /*               Functions For Making the AH choice box          */
    /*                                                               */
    /*****************************************************************/
    
    /**
     * Function to choose the auction house.
     */
    private boolean setAuctionHouseOnChoice() {
        String aucName = auctionHouses.getValue(), check;
        ArrayList<AuctionInfo> tempList = agent.getHouseList();
        
        for (AuctionInfo ai: tempList) {
            check = ai.getName() + " " + ai.getPortNumber();
            if (check.equalsIgnoreCase(aucName)) {
                return agent.setAuctionHouse(ai);
            }
        }
        return false;
    }
    
    /**
     * Filling the choice box with the auction house options.
     */
    private void updateAuctionHouseChoices() {
        auctionHouses.getItems().clear();
        agent.getBank().sendAgentMessage(new Message(agent.getNAME(),
                                                     MessageTypes.GET_HOUSES));
        timerTask.run();
        ArrayList<AuctionInfo> temp = agent.getHouseList();
        auctionHouses.getItems().add("Choose Auction House");
        auctionHouses.setValue("Choose Auction House");
        for (AuctionInfo s: temp) {
            auctionHouses.getItems().add(s.getName() + " " + s.getPortNumber());
        }
    }
    
    /**
     * Function to build the choice box containing the auction houses.
     */
    private void buildChoiceBox() {
        auctionHouses = new ChoiceBox<>();
        auctionHouses.setMinWidth(175);
        auctionHouses.setMinHeight(25);
        auctionHouses.getItems().add("Choose Auction House");
        auctionHouses.setValue("Choose Auction House");
    }
    
    /**
     * Function to make the the values in the auction house match the selection.
     */
    private static void addValuesToAucGui() {
        if (agent.getAuctionInfo() != null) {
            ahField.setText(agent.getAuctionInfo().getName());
            idField.setText("" + agent.getAuctionInfo().getAuctionID() + "");
            if (agent.getItem() != null) {
                itemField.setText(agent.getItem().getItemName());
            }
            ahField.setEditable(false);
            idField.setEditable(false);
            itemField.setEditable(false);
        }
    }
    
    /**
     * Function to make the auction house selection button
     */
    private void makeChooseAuctionHouseButton() {
        chooseAuctionHouseButton = new Button("Choose House");
        chooseAuctionHouseButton.setTextFill(Color.WHITE);
        chooseAuctionHouseButton.setBackground(new Background(GREY));
        chooseAuctionHouseButton.setMinWidth(175);
        chooseAuctionHouseButton.setMinHeight(25);
        chooseAuctionHouseButton.setOnAction(e -> {
            if (setAuctionHouseOnChoice()) {
                AuctionInfo ai = agent.getAuctionInfo();
                agent.getAHProxy(ai).sendMessage(new Message(agent.getNAME(),
                                                             MessageTypes.GET_ITEMS));
                if (!agent.getAuctionHouseKeys()
                          .containsKey(ai.getPortNumber())) {
                    agent.getBank().sendAgentMessage(new Message(agent.getNAME(),
                                                                 MessageTypes.GET_AGENT_ID_FOR_HOUSE,
                                                                 ai));
                    updateGUI();
                }
            }
        });
    }
    
    /*****************************************************************/
    /*                                                               */
    /*       Functions For Making the Bid and Account Buttons        */
    /*                                                               */
    /*****************************************************************/
    
    /**
     * Function to update the fields for the user account.
     */
    private static void updateAccountFields() {
        Account account = agent.getAccount();
        acctBalanceField.setText("" + account.getBalance() + "");
        pendingField.setText("" + account.getPendingBalance() + "");
    }
    
    /**
     * Function to make the createAccountButton.
     */
    private void makeCreateAccountButton() {
        createAccountButton = new Button("New Account");
        createAccountButton.setTextFill(Color.WHITE);
        createAccountButton.setMaxWidth(175);
        createAccountButton.setMaxHeight(HEIGHT * 0.1 - 5);
        createAccountButton.setBackground(new Background(GREY));
        createAccountButton.setOnAction(e -> {
            if (!firNameField.getText().isEmpty() &&
                !lastNameField.getText().isEmpty() &&
                !acctBalanceField.getText().isEmpty()) {
                
                String nam = firNameField.getText()+" "+lastNameField.getText();
                Double balance = Double.parseDouble(acctBalanceField.getText());
                Account account = new Account(nam,
                                              agent.getId(),
                                              balance,
                                              balance);
                
                if (agent.setAccount(account)) {
                    AgentInfo agInfo = new AgentInfo(agent.getNAME(),
                                                     agent.getHostName(),
                                                     agent.getCurrentAuctionID(),
                                                     agent.getPortNumber(),
                                                     agent.getId());
                    agent.getBank().sendAgentMessage(new Message(agent.getNAME(),
                                                                 MessageTypes.CREATE_ACCOUNT,
                                                                 account,
                                                                 agInfo));
                }
                
                pendingField.setText(balance.toString());
                firNameField.setEditable(false);
                lastNameField.setEditable(false);
                acctBalanceField.setEditable(false);
                pendingField.setEditable(false);
            }
        });
    }
    
    /**
     * Function for updating the list view to contain the current bids.
     */
    private void updateBidList() {
        ArrayList<Bid> bidList = agent.getBids();
        bids.getItems().clear();
        
        for (Bid b: bidList) {
            bids.getItems().add(b.toString());
        }
    }
    
    /**
     * Function to place a bid.
     */
    private void placeBid() {
        Bid bid = new Bid(agent.getItem(),
                          agent.getId(),// look here
                          Double.parseDouble(bidField.getText()));
        Message message = new Message(agent.getNAME(),
                                      MessageTypes.BID,
                                      bid);
        agent.getAHProxy(agent.getAuctionInfo()).sendMessage(message);
    }
    
    /**
     * Function to make the place bid button.
     */
    private void makePlaceBidButton() {
        placeBidButton = new Button("Place Bid");
        placeBidButton.setTextFill(Color.WHITE);
        placeBidButton.setMaxWidth(175);
        placeBidButton.setMaxHeight(HEIGHT * 0.1 - 5);
        placeBidButton.setBackground(new Background(GREY));
        placeBidButton.setOnAction(e -> {
            if (!itemField.getText().isEmpty() &&
                !bidField.getText().isEmpty()) {
                if (Integer.parseInt(bidField.getText()) <
                    agent.getAccount().getPendingBalance()) {
                    placeBid();
                    bidField.setText("");
                    itemField.setText("");
                    timerTask.run();
                    updateBidList();
                    updateAccountFields();
                }
            }
        });
    }
    
    /**
     * Function to make the select item bid.
     */
    private void makeSelectItemButton() {
        selectItemButton = new Button("Select Item");
        selectItemButton.setTextFill(Color.WHITE);
        selectItemButton.setMaxWidth(175);
        selectItemButton.setMaxHeight(25);
        selectItemButton.setBackground(new Background(GREY));
        selectItemButton.setOnAction(e -> {
            boolean set = false;
            String temp = itemsFromHouse.getSelectionModel().getSelectedItem();
            ArrayList<Item> items = agent.getItemList();
            for (Item i: items) {
                if (i.toString().equalsIgnoreCase(temp) && !set) {
                    agent.setItem(i);
                    set = true;
                }
            }
            addValuesToAucGui();
        });
    }
    
    /*****************************************************************/
    /*                                                               */
    /*              Functions For Making the Fields and Labels       */
    /*                                                               */
    /*****************************************************************/
    
    /**
     * Function to make the labels for testing.
     */
    private void makeLabels() {
        firName = new Label("First Name: ");
        lastName = new Label("LastName: ");
        acctBalance = new Label("Account Balance: ");
        pendingBalanceLabel = new Label("Pending Balance: ");
        ahLabel = new Label("Auction House: ");
        idLabel = new Label("ID: ");
        itemLabel = new Label("Item: ");
        bidLabel = new Label("Bid: ");
        bidListLabel = new Label("Current Bids: ");
        itemListLabel = new Label("Won Items: ");
    }
    
    /**
     * Function to make the fields for testing.
     */
    private void makeTextFields() {
        firNameField = new TextField();
        lastNameField = new TextField();
        acctBalanceField = new TextField();
        pendingField = new TextField();
        ahField = new TextField();
        idField = new TextField();
        itemField = new TextField();
        bidField = new TextField();
    }
    
    /*****************************************************************/
    /*                                                               */
    /*               Functions For Making New Account Box            */
    /*                                                               */
    /*****************************************************************/
    
    /**
     * Function to setup last name box.
     */
    private void buildLastNameBox() {
        lastNameBox = new HBox();
        lastNameBox.setMinWidth(WIDTH * 0.25);
        lastNameBox.setMinHeight(HEIGHT * 0.1);
        lastNameBox.setSpacing(5);
        lastNameBox.getChildren().addAll(lastName, lastNameField);
    }
    
    /**
     * Function to setup first name box.
     */
    private void buildFirstNameBox() {
        firstNameBox = new HBox();
        firstNameBox.setMinWidth(WIDTH * 0.25);
        firstNameBox.setMinHeight(HEIGHT * 0.1);
        firstNameBox.setSpacing(5);
        firstNameBox.getChildren().addAll(firName, firNameField);
    }
    
    /**
     * Function to setup account balance box.
     */
    private void buildAccountBalanceBox() {
        accountBalance = new HBox();
        accountBalance.setMinWidth(WIDTH * 0.25);
        accountBalance.setMinHeight(HEIGHT * 0.1);
        accountBalance.setSpacing(5);
        accountBalance.getChildren().addAll(acctBalance, acctBalanceField);
    }
    
    /**
     * Function to setup pending balance box.
     */
    private void buildPendingBalanceBox() {
        pendingBalanceBox = new HBox();
        pendingBalanceBox.setMinWidth(WIDTH * 0.25);
        pendingBalanceBox.setMinHeight(HEIGHT * 0.1);
        pendingBalanceBox.setSpacing(5);
        pendingBalanceBox.getChildren().addAll(pendingBalanceLabel,
                                               pendingField);
    }
    
    /**
     * Function to add fields to the accountBox container.
     */
    private void buildAccountContainer() {
        accountBox = new VBox();
        accountBox.setMinWidth(WIDTH * 0.5);
        accountBox.setMinHeight(HEIGHT * 0.5);
        accountBox.getChildren().addAll(firstNameBox,
                                        lastNameBox,
                                        accountBalance,
                                        pendingBalanceBox,
                                        createAccountButton);
    }
    
    /*****************************************************************/
    /*                                                               */
    /*          Functions For Making the Auction House VBox          */
    /*                                                               */
    /*****************************************************************/
    
    /**
     * Function to setup auc house horizontal box.
     */
    private void buildAuctionHouseHBox() {
        aucHouseHBox = new HBox();
        aucHouseHBox.setMinWidth(WIDTH * 0.25);
        aucHouseHBox.setMinHeight(HEIGHT * 0.1);
        aucHouseHBox.setSpacing(5);
        aucHouseHBox.getChildren().addAll(ahLabel, ahField);
    }
    
    /**
     * Function to setup the idBox.
     */
    private void buildIDBox() {
        idBox = new HBox();
        idBox.setMinWidth(WIDTH * 0.25);
        idBox.setMinHeight(HEIGHT * 0.1);
        idBox.setSpacing(5);
        idBox.getChildren().addAll(idLabel, idField);
    }
    
    /**
     * Function to setup the item box.
     */
    private void buildItemBox() {
        itemBox = new HBox();
        itemBox.setMinWidth(WIDTH * 0.25);
        itemBox.setMinHeight(HEIGHT * 0.1);
        itemBox.setSpacing(5);
        itemBox.getChildren().addAll(itemLabel, itemField);
    }
    
    /**
     * Function to setup the bid box.
     */
    private void buildBidBox() {
        bidBox = new HBox();
        bidBox.setMinWidth(WIDTH * 0.25);
        bidBox.setMinHeight(HEIGHT * 0.1);
        bidBox.setSpacing(5);
        bidBox.getChildren().addAll(bidLabel, bidField);
    }
    
    /**
     * Function to build the label box for the lists
     */
    private void buildLabelItemAndBidBox() {
        labelItemAndBidBox = new HBox();
        labelItemAndBidBox.setSpacing(WIDTH * 0.135);
        labelItemAndBidBox.setMaxWidth(WIDTH * 0.5);
        labelItemAndBidBox.setMaxHeight(HEIGHT * 0.5 * 0.05);
        labelItemAndBidBox.getChildren().addAll(bidListLabel, itemListLabel);
    }
    
    /**
     * Function to build the HBox containing the bid and item lists
     */
    private void buildItemAndBidBox() {
        itemAndBidBox = new HBox();
        itemAndBidBox.setMaxHeight(HEIGHT * 0.5 * 0.6);
        itemAndBidBox.setMaxWidth(WIDTH * 0.5);
        itemAndBidBox.setSpacing(5);
        buildLabelItemAndBidBox();
        itemAndBidBox.getChildren().addAll(bids, wonItems);
    }
    
    /**
     * Function to make the bid and update box
     */
    private void buildBidAndUpdateBox() {
        bidAndUpdateBox = new HBox();
        bidAndUpdateBox.setMaxHeight(28);
        bidAndUpdateBox.setSpacing(10);
        bidAndUpdateBox.setMaxWidth(WIDTH * 0.5);
        bidAndUpdateBox.getChildren().addAll(placeBidButton,
                                             updateWonItemsButton);
    }
    
    /**
     * Function to build the won items button update.
     */
    private void buildItemUpdateButton() {
        updateWonItemsButton = new Button("Update Items Won");
        updateWonItemsButton.setTextFill(Color.WHITE);
        updateWonItemsButton.setMaxWidth(175);
        updateWonItemsButton.setMaxHeight(25);
        updateWonItemsButton.setBackground(new Background(GREY));
        updateWonItemsButton.setOnAction(e -> {
            System.out.println("here in the update items");
            ArrayList<Item> tempWin = agent.getWonItems();
            wonItems.getItems().clear();
            
            for (Item item: tempWin) {
                wonItems.getItems()
                        .add(item.getItemName() + " " + item.getPrice());
            }
        });
    }
    
    /**
     * Function to add fields to the bidBox container.
     */
    private void buildBidContainer() {
        bidContainer = new VBox();
        bidContainer.setMinWidth(WIDTH * 0.5);
        bidContainer.setMinHeight(HEIGHT * 0.5);
        buildItemUpdateButton();
        buildLabelItemAndBidBox();
        buildItemAndBidBox();
        buildBidAndUpdateBox();
        bidContainer.getChildren().addAll(aucHouseHBox,
                                          idBox,
                                          itemBox,
                                          bidBox,
                                          bidAndUpdateBox,
                                          labelItemAndBidBox,
                                          itemAndBidBox);
    }
    
    /*****************************************************************/
    /*                                                               */
    /*          Functions For Making the Full GUI Containers         */
    /*                                                               */
    /*****************************************************************/
    
    /**
     * Function to set up the boxes.
     */
    private void initializeHBoxes() {
        buildFirstNameBox();
        buildLastNameBox();
        buildAccountBalanceBox();
        buildPendingBalanceBox();
        buildAuctionHouseHBox();
        buildIDBox();
        buildItemBox();
        buildBidBox();
    }
    
    /**
     * Function to build the horizontal box for the update and select
     * buttons and the drop down box.
     */
    private void buildAucHBox() {
        aucHBox = new HBox();
        aucHBox.setSpacing(10);
        aucHBox.setMaxWidth(WIDTH * 0.5);
        aucHBox.setMaxHeight(HEIGHT * 0.85);
        aucHBox.getChildren().addAll(auctionHouses,
                                     chooseAuctionHouseButton);
    }
    
    /**
     * Function to initialize the vertical boxes
     */
    private void buildAuctionBox() {
        auctionContainer = new VBox();
        auctionContainer.setMinWidth(WIDTH * 0.5);
        auctionContainer.setMinHeight(HEIGHT);
        auctionContainer.setSpacing(5);
        buildAucHBox();
        Label chooseAuctionHouseBox = new Label("Choose House from Dropdown " +
                                                    "Box Below: ");
        auctionContainer.getChildren().addAll(updateButton,
                                              chooseAuctionHouseBox,
                                              aucHBox,
                                              itemsFromHouse,
                                              selectItemButton);
        // TODO:
        // Come back here to add the auction update button
        // choice box and list of the items
    }
    
    /**
     * Function to make the update button.
     */
    private void buildUpdateButton() {
        updateButton = new Button("Update");
        updateButton.setTextFill(Color.WHITE);
        updateButton.setMinWidth(175);
        updateButton.setMinHeight(25);
        updateButton.setBackground(new Background(GREY));
        updateButton.setOnAction(e -> {
            updateAuctionHouseChoices();
        });
    }
    
    /**
     * Function to combine the containers into the main view.
     */
    private void fillContainers() {
        appVertContainer = new VBox();
        appVertContainer.setMinWidth(WIDTH * 0.5);
        appVertContainer.setMinHeight(HEIGHT);
        appVertContainer.setSpacing(5);
        appVertContainer.getChildren().addAll(accountBox, bidContainer);
        
        appFullContainer = new HBox();
        appFullContainer.setMinWidth(WIDTH);
        appFullContainer.setMinHeight(HEIGHT);
        appFullContainer.setSpacing(5);
        appFullContainer.getChildren().addAll(appVertContainer, auctionContainer);
    }

    /*
     *
     */
    public void updateGUI() {
        if (agent.getAccountChange() && agent.getAccount() != null) {
            updateAccountFields();
            agent.setAccountChange(false);
        }

        if (agent.getAuctionInfo() != null) {
            Message message  = new Message(agent.getNAME(),
                                           MessageTypes.GET_ITEMS);
            agent.getAHProxy(agent.getAuctionInfo()).sendMessage(message);
            timerTask.run();
            itemsFromHouse.getItems().clear();
            ArrayList<Item> list = agent.getItemList();

            for (Item i: list) {
                itemsFromHouse.getItems().add(i.toString());
            }
            addValuesToAucGui();
        }
    }
    
    /**
     * Function to start running the application.
     * @param primaryStage stage for the application
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setOnCloseRequest(e -> {
            agent.setConnected();
            primaryStage.close();
            System.exit(3);
        });
        AgentGUI agui = new AgentGUI();
        setupNumbersAndAgent();
        makePlaceBidButton();
        makeCreateAccountButton();
        makeSelectItemButton();
        makeChooseAuctionHouseButton();
        makeLabels();
        makeTextFields();
        initializeHBoxes();
        buildChoiceBox();
        buildUpdateButton();
        buildAuctionBox();
        buildBidContainer();
        buildAccountContainer();
        fillContainers();

        primaryStage.setTitle("Agent " + agent.getAccountNumber());
        Scene scene = new Scene(appFullContainer);
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
                        agui.updateGUI();
                    }
                });
            }
        }, 0, 1000);
    }
}

