package AuctionHouse;

import MessageHandling.Message;
import MessageHandling.MessageTypes;

/**
 * Used by auction house to decide what action needs to
 * be taken based on the messages it received.
 */
public class HouseMessageAnalyzer {

    /**
     *Breaks down the incoming message, decides what to do based on the first
     * parts of the message and returns and int once it sees what needs to be done.
     * 1 to 2 if response to agent, 3 if response to bank, and 4 to 7 if
     * response to auction.
     * @param message A Message
     * @return An int based on the message passed in.
     */
    public int analyzeMessage(Message message) {
        String sender = (String)message.getMessageList().get(0);
        MessageTypes type = (MessageTypes)message.getMessageList().get(1);
        if(sender.equals("agent")){
            if(type.equals(MessageTypes.GET_ITEMS)){
                return 1;
            }
            else if(type.equals(MessageTypes.BID)){
                return 2;
            }
            else{
                System.out.println("Not a covered message");
            }
        }
        else if( sender.equals("bank")){
            if(type.equals(MessageTypes.ACCOUNT_INFO)){
                return 3;
            }else if(type.equals(MessageTypes.TRANSFER_FUNDS)){
                return 3;
            }

        }
        else if(sender.equals("auction")){
            if(type.equals(MessageTypes.UPDATE)){
                return 4;
            }else if(type.equals(MessageTypes.UPDATE_ITEM)){
                return 5;
            }else if(type.equals(MessageTypes.SAFE_TO_CLOSE)){
                return 6;
            }else if(type.equals(MessageTypes.UNSAFE_TO_CLOSE)){
                return 7;
            }
        }
        return 0;
    }
}
