package Agent;

/**
 * MessageTypes.java is the Enum class that responds to the different types of
 * messages from the objects.
 * Danan High, 11/20/2018
 */
public enum MessageTypes {

    BID("bid"),
    GET_HOUSES("get houses"),
    GET_ITEMS("get items"),
    GET_USERS("get users"),
    OPEN_ACCOUNT("open account"),
    TRANSFER_FUNDS("transfer funds");

    private final String message;

    /**
     * Constructor for the MessageType.
     * @param message message for the constructor.
     */
    MessageTypes(String message) {
        this.message = message;
    }

    /**
     * Analyzing the message and returning the appropriate response to the
     * message.
     * @return response to the message.
     */
    public int analyze(String inMessage) {
        int response = 0;

        for (int i = 1; i < values().length; i++) {
            if (values()[i].message.equalsIgnoreCase(inMessage)) {
                return i;
            }
        }

        return response;
    }
}