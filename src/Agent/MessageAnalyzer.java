package Agent;

public class MessageAnalyzer {
    
    public int checkMessage(Message message) {
        if (message.getDetailedMessage().getClass().equals(String.class)) {
            String sentMessage = (String) message.getDetailedMessage();
            if (sentMessage.contains("open account")) {
                return 1;
            } else if (sentMessage.contains("get houses")) {
                return 2;
            } else if (sentMessage.contains("get items")) {
                return 3;
            } else if (sentMessage.contains("transfer funds")) {
                return 4;
            } else if (sentMessage.contains("get users")) {
                return 5;
            }
        } else if (message.getDetailedMessage().getClass().equals(Bid.class)){
            return 6;
        }
        return -1;
    }
    
}
