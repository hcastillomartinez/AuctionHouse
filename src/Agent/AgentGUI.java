package Agent;

import AuctionHouse.Item;
import Bank.Account;
import Bank.AgentInfo;
import Bank.AuctionInfo;
import MessageHandling.Message;
import MessageHandling.MessageTypes;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;

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

    // filler variables for the boxes
    private Label firName, lastName, acctBalance, pendingBalanceLabel,
        ahLabel, idLabel, itemLabel, bidLabel;
    private TextField firNameField, lastNameField, acctBalanceField,
        pendingField, ahField, idField, itemField, bidField;
    private Button createAccountButton, placeBidButton, selectItemButton,
            updateButton, chooseAuctionHouseButton;

    // box variables
    private VBox accountBox, bidContainer, auctionContainer, appVertContainer;
    private HBox firstNameBox, lastNameBox, accountBalance, aucHouseHBox,
        idBox, itemBox, bidBox, appFullContainer, pendingBalanceBox;

    // background fillers
    private final BackgroundFill BLUE
            = new BackgroundFill(Color.BLUE, null, null);
    private final BackgroundFill GREY
            = new BackgroundFill(Color.GREY, new CornerRadii(10), null);
    
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
        String aucName = auctionHouses.getValue();
        ArrayList<AuctionInfo> tempList = agent.getHouseList();

        for (AuctionInfo ai: tempList) {
            if (ai.toString().equalsIgnoreCase(aucName)) {
                return agent.setAuctionHouse(ai);
            }
        }
        return false;
    }

    /**
     * Filling the choice box with the auction house options.
     */
    private synchronized void updateAuctionHouseChoices() {
        auctionHouses.getItems().clear();
        agent.getBank().sendAgentMessage(new Message(agent.getNAME(),
                                                     MessageTypes.GET_HOUSES));
        ArrayList<AuctionInfo> temp = agent.getHouseList();
        
        for (AuctionInfo s: temp) {
            auctionHouses.getItems().add(s.toString());
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
    private void addValuesToAucGui() {
        System.out.println(agent.getAuctionInfo() + " AH info in the gui");
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
                itemsFromHouse.getItems().clear();
                ArrayList<Item> list = agent.getItemList();
    
                for (Item i: list) {
                    itemsFromHouse.getItems().add(i.toString());
                }
                addValuesToAucGui();
            }
            
        });
    }

    /*****************************************************************/
    /*                                                               */
    /*       Functions For Making the Bid and Account Buttons        */
    /*                                                               */
    /*****************************************************************/

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
                Bid bid = new Bid(agent.getItem(),
                                  agent.getKey(),
                                  Double.parseDouble(bidField.getText()));
                Message message = new Message(agent.getNAME(),
                                              MessageTypes.BID,
                                              bid);
                agent.getAHProxy(agent.getAuctionInfo()).sendMessage(message);
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
     * Function to add fields to the bidBox container.
     */
    private void buildBidContainer() {
        bidContainer = new VBox();
        bidContainer.setMinWidth(WIDTH * 0.5);
        bidContainer.setMinHeight(HEIGHT * 0.5);
        bidContainer.getChildren().addAll(aucHouseHBox,
                                          idBox,
                                          itemBox,
                                          bidBox,
                                          placeBidButton);
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
     * Function to initialize the vertical boxes
     */
    private void buildAuctionBox() {
        auctionContainer = new VBox();
        auctionContainer.setMinWidth(WIDTH * 0.5);
        auctionContainer.setMinHeight(HEIGHT);
        auctionContainer.setSpacing(5);
        auctionContainer.getChildren().addAll(updateButton,
                                              auctionHouses,
                                              chooseAuctionHouseButton,
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
    
        Scene scene = new Scene(appFullContainer);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}































