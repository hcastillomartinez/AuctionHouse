package Agent;

import Bank.Account;
import Bank.AuctionInfo;
import MessageHandling.Message;
import MessageHandling.MessageTypes;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;

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
    private HashMap<String, Integer> numberList = new HashMap<>();
    private ChoiceBox<AuctionInfo> auctionHouses;

    // filler variables for the boxes
    private Label firName, lastName, acctBalance, pendingBalanceLabel,
        ahLabel, idLabel, itemLabel, bidLabel;
    private TextField firNameField, lastNameField, acctBalanceField,
        pendingField, ahField, idField, itemField, bidField;
    private Button createAccountButton, placeBid, selectItem, updateButton;

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
        AgentGUI.launch(AgentGUI.class);
        agent.closeApplicationConnection();
        System.exit(1);
    }
    
    /**
     * Setting up the map to check against numbers.
     */
    private void setupNumbersAndAgent() {
        for (int i = 0; i < 10; i++) {
            numberList.put("" + i + "", i);
        }

        agent = new Agent(host, port);
        (new Thread(agent)).start();
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
                String name
                        = firNameField.getText() + " " + lastNameField.getText();
                Double balance = Double.parseDouble(acctBalanceField.getText());
                Account account = new Account(name,
                                              agent.getId(),
                                              balance,
                                              balance);
                agent.getBank().sendAgentMessage(new Message(agent.getNAME(),
                                                             MessageTypes.CREATE_ACCOUNT,
                                                             account));
                pendingField.setText(balance.toString());
                agent.setAccount(account);
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
        placeBid = new Button("Place Bid");
        placeBid.setTextFill(Color.WHITE);
        placeBid.setMaxWidth(175);
        placeBid.setMaxHeight(HEIGHT * 0.1 - 5);
        placeBid.setBackground(new Background(GREY));
        placeBid.setOnAction(e -> {
            if (!itemField.getText().isEmpty() &&
                !bidField.getText().isEmpty()) {
                System.out.println("placed bid");
            }
        });
    }
    
    /**
     * Function to make the select item bid.
     */
    private void makeSelectItemButton() {
        selectItem = new Button("Place Bid");
        selectItem.setTextFill(Color.WHITE);
        selectItem.setMaxWidth(175);
        selectItem.setMaxHeight(HEIGHT * 0.1 - 5);
        selectItem.setBackground(new Background(GREY));
        selectItem.setOnAction(e -> {
            System.out.println("select item");
        });
    }
    
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
     * Function to setup auc house horizontal box.
     */
    private void buildAuctionHoueHorizontalBox() {
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
     * Function to set up the boxes.
     */
    private void initializeHBoxes() {
        buildFirstNameBox();
        buildLastNameBox();
        buildAccountBalanceBox();
        buildPendingBalanceBox();
        buildAuctionHoueHorizontalBox();
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
                                              auctionHouses);
        // TODO:
        // Come back here to add the auction update button
        // choice box and list of the items
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
                                          placeBid);
    }

    /**
     * Filling the choice box with the auction house options.
     */
    private void updateAuctionHouseChoices() {
        ArrayList<AuctionInfo> temp = agent.getHouseList();
        for (AuctionInfo s: temp) {
            auctionHouses.getItems().add(s);
        }
    }

    /**
     * Function to build the choice box containing the auction houses.
     */
    private void buildChoiceBox() {
        auctionHouses = new ChoiceBox<>();
        auctionHouses.setMinWidth(175);
        auctionHouses.setMinHeight(HEIGHT * 0.1 - 5);
        auctionHouses.setOnAction(e -> {
            agent.setAuctionHouse(auctionHouses.getValue());
        });
    }

    /**
     * Function to make the update button.
     */
    private void buildUpdateButton() {
        updateButton = new Button("Update");
        updateButton.setTextFill(Color.WHITE);
        updateButton.setMinWidth(175);
        updateButton.setMinHeight(HEIGHT * 0.05);
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
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        setupNumbersAndAgent();
        makePlaceBidButton();
        makeCreateAccountButton();
        makeSelectItemButton();
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

    // TODO:
    /*
        buttons and drop down box
     */
}































