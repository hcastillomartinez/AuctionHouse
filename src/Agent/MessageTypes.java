package Agent;

/**
 * MessageTypes.java is the Enum class that responds to the different types of
 * messages from the objects.
 * Danan High, 11/20/2018
 */
public enum MessageTypes {

    // will continue adding more as more types are determined for message
// sending between the types
    
    BID("bid"),
    GET_HOUSES("get houses"),
    GET_ITEMS("get items"),
    GET_USERS("get users"),
    CREATE_ACCOUNT("open account"),
    TRANSFER_FUNDS("transfer funds"),
    CONFIRMATION("confirmation"),
    REMOVE_FUNDS("remove funds"),
    BID_ACCEPTED("bid accepted"),
    BID_REJECTED("bid rejected"),
    TRANSFER_ITEM("transfer item"),
    GET_ID_FROM_AGENT("id number"),
    BALANCE("get balance"),
    ACCOUNT_INFO("account information"),
    BID_STATUS("get bid status"),
    GET_AGENT_ID_FOR_HOUSE("get id for house"),
    ACCOUNT_EXISTS("account already exists"),
    THANKS("thanks");

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

    /**
     * Getting the specific message associated with the type.
     * @return message for the type
     */
    public String getMessage() {
        return message;
    }
}















