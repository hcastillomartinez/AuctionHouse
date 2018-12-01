package Agent;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

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
    private Agent agent;
    private HashMap<String, Integer> numberList = new HashMap<>();

    // filler variables for the boxes
    private Label firName, lastName, acctBalance, pendingBalanceLabel,
        ahLabel, idLabel, itemLabel, bidLabel;
    private TextField firNameField, lastNameField, acctBalanceField,
        pendingField, ahField, idField, itemField, bidField;
    private Label[] labels = {firName,
                              lastName,
                              acctBalance,
                              pendingBalanceLabel,
                              ahLabel,
                              idLabel,
                              itemLabel,
                              bidLabel};
    private TextField[] fields = {firNameField,
                                  lastNameField,
                                  acctBalanceField,
                                  pendingField,
                                  ahField,
                                  idField,
                                  itemField,
                                  bidField};
    private Button createAccountButton, placeBid, selectItem;

    // box variables
    private VBox accountBox, bidContainer, auctionContainer, appVertContainer;
    private VBox[] vBoxes = {accountBox,
                           bidContainer,
                           auctionContainer};
    private HBox firstNameBox, lastNameBox, accountBalance, aucHouseHBox,
        idBox, itemBox, bidBox, appFullContainer;
    private HBox[] hBoxes = {firstNameBox,
                             lastNameBox,
                             accountBalance,
                             aucHouseHBox,
                             idBox,
                             itemBox,
                             bidBox};
    private BackgroundFill black = new BackgroundFill(Color.BLUE,
                                                      null,
                                                      null);
    private BackgroundFill grey = new BackgroundFill(Color.GREY, new
                                                     CornerRadii(10),
                                                     null);
    
    /**
     * Launching the application.
     * @param args for the responses input
     */
    public static void launch(String...args) {
        host = args[0];
        port = Integer.parseInt(args[1]);
        AgentGUI.launch(AgentGUI.class);
    }
    
    /**
     * Setting up the map to check against numbers.
     */
    private void setupNumbersAndAgent() {
        for (int i = 0; i < 10; i++) {
            numberList.put("" + i + "", i);
        }

        this.agent = new Agent(host, port);
        (new Thread(agent)).start();
    }

    /**
     * Setting up the horizontal boxes that contain the input fields
     * @param box vertical box.
     * @param label label for the box
     * @param field textField for the box
     * @param width width of the box
     * @param height height of the box
     */
    private void setupHBox(HBox box,
                           Label label,
                           TextField field,
                           double width,
                           double height) {
        box = new HBox();
        box.setMinWidth(width);
        box.setMinHeight(height);
        box.getChildren().addAll(label, field);
    }

    /**
     * Setting up the vertical boxes that contain the fields.
     * @param box vertical box
     * @param hBox horizontal box
     * @param width width of the box
     * @param height height of the box
     */
    private void setupVBox(VBox box,
                           HBox hBox,
                           double width,
                           double height) {
        box = new VBox();
        box.setMinWidth(width);
        box.setMinHeight(height);
        box.getChildren().add(hBox);
    }

    /**
     * Function for distributing making of the vBoxes.
     */
    private void makeVBoxes() {
        for (int i = 0; i < vBoxes.length; i++) {
            if (i == 0) {
                for (int j = 0; j < 4; j++) {
                    setupVBox(vBoxes[i],
                              hBoxes[j],
                              WIDTH * 0.5,
                              HEIGHT * 0.1 - 5);
                }
                vBoxes[i].getChildren().add(createAccountButton);
            } else if (i == 1) {
                for (int j = 4; j < 8; j++) {
                    setupVBox(vBoxes[i],
                              hBoxes[j],
                              WIDTH * 0.5,
                              HEIGHT * 0.10 - 5);
                }
                vBoxes[i].getChildren().add(placeBid);
            } else {
                setupVBox(vBoxes[i], null, WIDTH * 0.5, HEIGHT);
                vBoxes[i].getChildren().add(selectItem);
            }
        }
    }
    
    /**
     * Function to make the createAccountButton.
     */
    private void makeCreateAccountButton() {
        createAccountButton = new Button("New Account");
        createAccountButton.setTextFill(Color.WHITE);
        createAccountButton.setMinWidth(WIDTH * 0.75);
        createAccountButton.setMinHeight(HEIGHT * 0.1 - 5);
        createAccountButton.setBackground(new Background(grey));
        createAccountButton.setOnAction(e -> {
            if (!firName.getText().isEmpty() &&
                !lastName.getText().isEmpty() &&
                !acctBalance.getText().isEmpty()) {
                System.out.println("new account");
                firName.setDisable(true);
                lastName.setDisable(true);
                acctBalance.setDisable(true);
            }
        });
    }
    
    /**
     * Function to make the place bid button.
     */
    private void makePlaceBidButton() {
        placeBid = new Button("Place Bid");
        placeBid.setTextFill(Color.WHITE);
        placeBid.setMinWidth(WIDTH * 0.75);
        placeBid.setMinHeight(HEIGHT * 0.1 - 5);
        placeBid.setBackground(new Background(grey));
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
        selectItem.setMinWidth(WIDTH * 0.75);
        selectItem.setMinHeight(HEIGHT * 0.1 - 5);
        selectItem.setBackground(new Background(grey));
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

        for (int i = 0; i < hBoxes.length; i++) {
            setupHBox(hBoxes[i],
                      labels[i],
                      fields[i],
                      WIDTH * 0.5,
                      HEIGHT * 0.25 - 5);
        }
        makeVBoxes();
    
        TextField field = new TextField();
        field.setOnAction(e -> {
            boolean goodInput = true;
            if (!field.getText().isEmpty()) {
                String text = field.getText();
                for (String s: numberList.keySet()) {
                    if (s.equalsIgnoreCase(text)) {
                        agent.handleChoice(numberList.get(s));
                    }
                }
            }
        });

        appVertContainer = new VBox();
        appVertContainer.setMinWidth(WIDTH * 0.5);
        appVertContainer.setMinHeight(HEIGHT);
        appVertContainer.getChildren().addAll(vBoxes[0], vBoxes[1]);

        appFullContainer = new HBox();
        appFullContainer.setMinWidth(WIDTH * 0.5);
        appFullContainer.setMinHeight(HEIGHT);
        appFullContainer.getChildren().addAll(vBoxes[2]);
    
        Scene scene = new Scene(appFullContainer);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // TODO:
    /*
        finish debugging issue with null child, buttons and drop down box
     */
}































