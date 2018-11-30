package Agent;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
    private Label firName, lastName, acctBalance, ahLabel, idLabel, itemLabel,bidLabel;
    private TextField firNameField, lastNameField, acctBalanceField, ahField;
    private TextField idField, itemField, bidField;
    private Label[] labels = {firName,
                              lastName,
                              acctBalance,
                              ahLabel,
                              idLabel,
                              itemLabel,
                              bidLabel};
    private TextField[] fields = {firNameField,
                                  lastNameField,
                                  acctBalanceField,
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
    private HBox firstNameBox, lastNameBox, accountBalance, aucHouseHBox, idBox;
    private HBox itemBox, bidBox, appFullContainer;
    private HBox[] hBoxes = {firstNameBox,
                             lastNameBox,
                             accountBalance,
                             aucHouseHBox,
                             idBox,
                             itemBox,
                             bidBox};
    private BackgroundFill black = new BackgroundFill(Color.AQUA, null, null);
    
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
                for (int j = 0; j < 3; j++) {
                    setupVBox(vBoxes[i],
                              hBoxes[j],
                              WIDTH * 0.5,
                              HEIGHT * 0.25 - 5);
                }
                vBoxes[i].getChildren().add(createAccountButton);
            } else if (i == 1) {
                for (int j = 3; j < 7; j++) {
                    setupVBox(vBoxes[i],
                              hBoxes[j],
                              WIDTH * 0.5,
                              HEIGHT * 0.25 - 5);
                }
                vBoxes[i].getChildren().add(placeBid);
            } else {
                setupVBox(vBoxes[i], null, WIDTH * 0.5, HEIGHT);
                vBoxes[i].getChildren().add(selectItem);
            }
        }
    }

    /**
     * Function to start running the application.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        setupNumbersAndAgent();

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
        make labels, buttons and drop down box
     */
}































