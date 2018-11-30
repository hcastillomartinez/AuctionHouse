package Agent;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * AgentGUI.java displays the agent gui for user interactions.
 * @author Danan High, 11/29/2018
 */
public class AgentGUI extends Application {
 
    private static String host;
    private static int port;
    
    private Agent agent;
    private HashMap<String, Integer> numberList = new HashMap<>();

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
    private void setup() {
        for (int i = 0; i < 10; i++) {
            numberList.put("" + i + "", i);
        }
        this.agent = new Agent(host, port);
        (new Thread(agent)).start();
    }

    /**
     * Function to start running the application.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        setup();
        
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setMinWidth(Screen.getPrimary().getBounds().getWidth() * 0.75);
        vBox.setMinHeight(Screen.getPrimary().getBounds().getHeight() * 0.75);
        vBox.backgroundProperty().set(new Background(black));
    
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
        
        vBox.getChildren().addAll(field);
    
        Scene scene = new Scene(vBox);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
