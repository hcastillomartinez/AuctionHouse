package Bank;

import Agent.Agent;
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
    static Bank bank = new Bank(null,0);

    private final BackgroundFill BLUE
            = new BackgroundFill(Color.BLUE, null, null);//todo remove

    // final variables
    private final double WIDTH
            = Screen.getPrimary().getBounds().getWidth() * 0.75;
    private final double HEIGHT
            = Screen.getPrimary().getBounds().getHeight() * 0.75;

    private final Background WHITE = new Background(new BackgroundFill(Color.WHITE,
                                                                  null,
                                                                 null));

    public BankGUI(){
        mainPane = new Pane();
        //for debugging
        mainPane.setBackground(WHITE);
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

        Thread bankThread = new Thread(bank);
    }

}
