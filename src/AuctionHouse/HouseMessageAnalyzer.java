package AuctionHouse;

import Agent.Message;
import Agent.MessageAnalyzer;
import Agent.MessageTypes;

public class HouseMessageAnalyzer {
    private MessageAnalyzer m = new MessageAnalyzer();


    public int analyzeMessage(Message message) {
        int sender = m.analyze(message);

        //agent
        if (sender == 1) {

        } else if (sender == 2) { //bank

        }
        return 0;
    }

    private int breakDown(Message message){
        String sender=(String)message.getMessageList().get(0);
        MessageTypes type=(MessageTypes)message.getMessageList().get(1);
        if(sender.equals("agent")){

        }else if(sender.equals("bank")){

        }
        return 0;
    }
}
