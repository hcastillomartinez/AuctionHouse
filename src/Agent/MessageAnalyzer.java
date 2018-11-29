package Agent;

import AuctionHouse.*;
import Bank.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to handle the analysis of the messages and providing the correct
 * responses to the messages.
 * @author Danan High, 11/25/2018
 */
public class MessageAnalyzer {

    /**
     * Function to analyze and respond to the messages from the
     * different servers.
     * @param message message from the server or socket
     * @return response based on the sender of the message
     */
    public int analyze(Message message) {
    
         return checkSender((String) message.getMessageList().get(0));
    }

    /**
     * Getting the sender of the message.
     * @param sender from the sender
     * @return number for the specific sender type
     */
    private int checkSender(String sender) {
    
        if (sender.equalsIgnoreCase("agent")) {
            return 1;
        } else if (sender.equalsIgnoreCase("auction house")) {
            return 2;
        } else if (sender.equalsIgnoreCase("bank")) {
            return 3;
        } else if (sender.equalsIgnoreCase("test bank")) {
            return 4;
        } else if (sender.equalsIgnoreCase("test auction house")){
            return 5;
        }
        return 0;
    }
}
