package MessageHandling;

/**
 * MessageTypes.java is the Enum class that responds to the different types of
 * messages from the objects.
 * Danan High, 11/20/2018
 */
public enum MessageTypes {
    
    BID("bid"),
    CLOSE("close"),
    GET_HOUSES("get houses"),
    GET_ITEMS("get items"),
    GET_USERS("get users"),
    CREATE_ACCOUNT("open account"),
    TRANSFER_FUNDS("transfer funds"),
    CONFIRMATION("confirmation"),
    REMOVE_FUNDS("remove funds"),
    BLOCK_REJECTED("block rejected"),
    TRANSFER_REJECTED("transfer rejected"),
    BID_ACCEPTED("bid accepted"),
    UNBLOCK_FUNDS("unblock funds"),
    BLOCK_FUNDS("block funds"),
    BID_REJECTED("bid rejected"),
    TRANSFER_ITEM("transfer item"),
    UPDATE_ITEM("update item"),
    GET_ID_FROM_AGENT("id number"),
    BALANCE("get balance"),
    ACCOUNT_INFO("account information"),
    BID_STATUS("get bid status"),
    GET_AGENT_ID_FOR_HOUSE("get id for house"),
    ACCOUNT_EXISTS("account already exists"),
    THANKS("thanks"),
    BANK_ACCOUNT("account number"),
    OUT_BID("out bid"),
    ITEMS("items"),
    UPDATE("update"),
    HOUSES("houses"),
    SAFE_TO_CLOSE("safe to close"),
    UNSAFE_TO_CLOSE("unsafe to close"),
    ID_FOR_HOUSE("id for house");

    private final String message;

    /**
     * Constructor for the MessageType.
     * @param message message for the constructor.
     */
    MessageTypes(String message) {
        this.message = message;
    }

    /**
     * Getting the specific message associated with the type.
     * @return message for the type
     */
    public String getMessage() {
        return message;
    }
}















