package Agent;

import AuctionHouse.*;
import Bank.*;

public class MessageAnalyzer {
    
    private TestMessage<Object, Object> message;
    
    public MessageAnalyzer(TestMessage<Object, Object> message) {
        this.message = message;
    }
    
    public int checkSender() {
        if (message.getSender().getClass().equals(Agent.class)) {
            return 1;
        } else if (message.getSender().getClass().equals(AuctionHouse.class)) {
            return 2;
        } else if (message.getSender().getClass().equals(Bank.class)) {
            return 3;
        } else if (message.getSender().getClass().equals(TestBank.class)) {
            return 4;
        }
        return 0;
    }
    
    public int checkDetailedMessage() {
        if (message.getDetailedMessage().getClass().equals(Bid.class)) {
            return 1;
        } else if (message.getDetailedMessage()
                          .getClass()
                          .equals(Account.class)) {
            return 2;
        } else if (message.getDetailedMessage()
                          .getClass()
                          .equals(String.class)) {
            return 3;
        }
        return 0;
    }
    
    public  void setMessage(TestMessage<Object, Object> messUpdate) {
        message = messUpdate;
    }
}
