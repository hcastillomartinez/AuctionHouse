package Bank;

import Agent.AgentGUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.awt.*;

/**
 * @author Daniel Miller
 */
public class BankGUI extends Application {
    Pane mainPane;

    private final BackgroundFill BLUE
            = new BackgroundFill(Color.BLUE, null, null);//todo remove

    // final variables
    private final double WIDTH
            = Screen.getPrimary().getBounds().getWidth() * 0.75;
    private final double HEIGHT
            = Screen.getPrimary().getBounds().getHeight() * 0.75;

    public BankGUI(){
        mainPane = new Pane();

        //for debugging
        mainPane.setBackground(new Background(new BackgroundFill(Color.BLUE,null,null)));
    }

    /**
     * Launching the application.
     * @param args for the responses input
     */
    public static void launch(String...args) {

    }

    /**
     * Function to start running the application.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        BankGUI gui = new BankGUI();

        Scene scene = new Scene(gui.mainPane);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

}
