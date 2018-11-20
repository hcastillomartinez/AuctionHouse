package Agent;

/**
 * MessageTypes.java is the Enum class that responds to the different types of
 * messages from the objects.
 * Danan High, 11/20/2018
 */
public enum MessageTypes {

    OPEN_ACCOUNT("open account"),
    GET_HOUSES("get houses"),
    GET_ITEMS("get items"),
    TRANSFER_FUNDS("transfer funds"),
    GET_USERS("get users"),
    BID(Bid.class);

    private final Object message;

    /**
     * Constructor for the MessageType.
     * @param message message for the constructor.
     */
    MessageTypes(Object message) {
        this.message = message;
    }

    /**
     * Analyzing the message and returning the appropriate response to the
     * message.
     * @return response to the message.
     */
    public int analyze(MessageTypes type) {
        int response = 0;

        switch (type) {
            case OPEN_ACCOUNT:
                response = 1;
                break;
            case GET_ITEMS:
                response = 2;
                break;
            case GET_USERS:
                response = 3;
                break;
            case GET_HOUSES:
                response = 4;
                break;
            case TRANSFER_FUNDS:
                response = 5;
                break;
            case BID:
                response = 6;
                break;
        }
        return response;
    }
}