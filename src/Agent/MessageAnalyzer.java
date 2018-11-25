package Agent;

import AuctionHouse.*;
import Bank.*;

public class MessageAnalyzer {
    
    private TestMessage<Object, Object> message;
    
    public MessageAnalyzer(TestMessage<Object, Object> message) {
        this.message = message;
    }
    
    public int checkSender() {
        Class sender = this.message.getSender().getClass();
        
        if (sender.equals(Agent.class)) {
            return 1;
        } else if (sender.equals(AuctionHouse.class)) {
            return 2;
        } else if (sender.equals(Bank.class)) {
            return 3;
        } else if (sender.equals(TestBank.class)) {
            return 4;
        }
        return 0;
    }
    
    public int checkDetailedMessage() {
        Class detailedMessage = message.getDetailedMessage().getClass();
        
        if (detailedMessage.equals(Bid.class)) {
            return 1;
        } else if (detailedMessage.equals(Account.class)) {
            return 2;
        } else if (detailedMessage.equals(String.class)) {
            return 3;
        }
        return 0;
    }
    
    public  void setMessage(TestMessage<Object, Object> messUpdate) {
        message = messUpdate;
    }
}
