package Agent;

import AuctionHouse.*;
import Bank.*;

import java.util.List;

/**
 * Class to handle the analysis of the messages and providing the correct
 * responses to the messages.
 * @author Danan High, 11/25/2018
 */
public class MessageAnalyzer {

    private final int BID_FROM_AGENT = 1;
    private final int CREATE_ACCOUNT = 2;
    private final int BANK_CONFIRMATION = 3;
    private final int GET_HOUSES = 4;
    private final int GET_AUCTIONHOUSE_ID = 5;
    private final int ACCOUNT_INFO = 6;
    private final int ACCOUNT_BALANCE = 7;
    private final int REMOVE_FUNDS = 8;
    private final int BID_ACCEPTANCE = 9;
    private final int BID_DENIAL = 10;
    private final int ITEM_TRANSFER = 11;
    private final int GET_ITEMS = 12;
    private final int CHECK_BID_STATUS = 13;
    private final int ACCOUNT_UPDATE = 14;
    private final int AUCTION_HOUSE_ID = 15;
    private final int OUT_BID = 16;
    private final int BID_STATUS = 17;
    private final int AGENT_BALANCE = 18;

    /**
     * Function to analyze and respond to the messages from the
     * different servers.
     * @param message message from the server or socket
     * @return response based on the sender of the message
     */
    public int analyze(TestMessage<Object, Object> message) {
        int senderID = checkSender(message);
        int messageID = checkDetailedMessage(message);

        if (senderID == 1) { // agent sender
            return agentResponse(messageID,
                                 message.getDetailedMessage());
        } else if (senderID == 2) { // auction house sender
            return auctionHouseResponse(messageID,
                                        message.getDetailedMessage());
        } else if (senderID == 3) { // bank sender
            return bankResponse(messageID,
                                message.getDetailedMessage());
        } else if (senderID == 4){ // test bank
            return testBankResponse(messageID,
                                    message.getDetailedMessage());
        } else if (senderID == 5) { // test auction house
            return testAuctionHouse(messageID,
                                    message.getDetailedMessage());
        }
        return 0;
    }

    /**
     * Handling the output for the bank.
     * @param messageID message type from the bank
     * @return the response to the bank
     */
    private int bankResponse(int messageID,
                             Object message) {
        if (messageID == 2) { // account from bank
            return ACCOUNT_UPDATE;
        } else if (messageID == 3) { // string from bank
            String mail = (String) message;
            if (mail.contains("confirmed")) {
                return BANK_CONFIRMATION;
            }
        } else if (messageID == 4) { // id from the bank
            return AUCTION_HOUSE_ID;
        } else if (messageID == 5) { // list of the auction houses from bank
            return GET_HOUSES;
        } else if (messageID == 6) { // amount
            return ACCOUNT_BALANCE;
        }
        return 0;
    }

    /**
     * Handling the output for the specific message type.
     * @param messageID message type from the auction house
     * @return the response to the auction house
     */
    private int auctionHouseResponse(int messageID,
                                     Object message) {
        if (messageID == 3) { // string from bank
            String mail = (String) message;
            if (mail.contains("bid denied")) {
                return BID_DENIAL;
            } else if (mail.contains("out bid")) {
                return OUT_BID;
            } else if (mail.contains("bid accepted")) {
                return BID_ACCEPTANCE;
            } else if (mail.contains("place")) {
                return BID_STATUS;
            } else if (mail.contains("$")) {
                return REMOVE_FUNDS;
            }
        } else if (messageID == 8) { // transferring item to agent
            return ITEM_TRANSFER;
        } else if (messageID == 9) { // checking agent account balance for bid
            return AGENT_BALANCE;
        }
        return 0;
    }

    /**
     * Handling the output for the agent response.
     * @param messageID message type from the agent
     * @return the response to the agent
     */
    private int agentResponse(int messageID,
                              Object message) {
        if (messageID == 1) { // bid
            return BID_FROM_AGENT;
        } else if (messageID == 2) { // account from agent
            return CREATE_ACCOUNT;
        } else if (messageID == 3) { // string from agent
            String mail = (String) message;
            if (mail.contains("get houses")) {
                return GET_HOUSES;
            } else if (mail.contains("account info")) {
                return ACCOUNT_INFO;
            } else if (mail.contains("get items")) {
                return GET_ITEMS;
            } else if (mail.contains("status")) {
                return CHECK_BID_STATUS;
            }
        } else if (messageID == 7) { // getting the id for the auction house
            return GET_AUCTIONHOUSE_ID;
        }
        return 0;
    }

    /**
     * Handling the output for the agent response.
     * @param messageID message type from the agent
     * @return the response to the agent
     */
    private int testBankResponse(int messageID,
                                 Object message) {
        if (messageID == 2) { // account from bank
            return ACCOUNT_UPDATE;
        } else if (messageID == 3) { // string from bank
            String mail = (String) message;
            if (mail.contains("confirmed")) {
                return BANK_CONFIRMATION;
            }
        } else if (messageID == 4) { // id from the bank
            return AUCTION_HOUSE_ID;
        } else if (messageID == 5) { // list of the auction houses from bank
            return GET_HOUSES;
        } else if (messageID == 6) { // amount
            return ACCOUNT_BALANCE;
        }
        return 0;
    }

    /**
     * Handling the output for the agent response.
     * @param messageID message type from the agent
     * @return the response to the agent
     */
    private int testAuctionHouse(int messageID,
                                 Object message) {
        if (messageID == 3) { // string from bank
            String mail = (String) message;
            if (mail.contains("bid denied")) {
                return BID_DENIAL;
            } else if (mail.contains("out bid")) {
                return OUT_BID;
            } else if (mail.contains("bid accepted")) {
                return BID_ACCEPTANCE;
            } else if (mail.contains("place")) {
                return BID_STATUS;
            }
        } else if (messageID == 6) { // amount to remove from the bank account
            return REMOVE_FUNDS;
        } else if (messageID == 8) { // transferring item to agent
            return ITEM_TRANSFER;
        } else if (messageID == 9) { // checking agent account balance for bid
            return AGENT_BALANCE;
        }
        return 0;
    }

    /**
     * Getting the sender of the message.
     * @param message from the sender
     * @return number for the specific sender type
     */
    private int checkSender(TestMessage<Object, Object> message) {
        if (message.getSender().getClass().equals(Agent.class)) {
            return 1;
        } else if (message.getSender().getClass().equals(AuctionHouse.class)) {
            return 2;
        } else if (message.getSender().getClass().equals(Bank.class)) {
            return 3;
        } else if (message.getSender().getClass().equals(TestBank.class)) {
            return 4;
        } else if (message.getSender()
                          .getClass()
                          .equals(TestAuctionHouse.class)){
            return 5;
        }
        return 0;
    }

    /**
     * Getting the message type.
     * @param message from the sender
     * @return number for the specific message type
     */
    private int checkDetailedMessage(TestMessage<Object, Object> message) {
        if (message.getDetailedMessage()
                   .getClass()
                   .equals(Bid.class)) {
            return 1;
        } else if (message.getDetailedMessage()
                          .getClass()
                          .equals(Account.class)) {
            return 2;
        } else if (message.getDetailedMessage()
                          .getClass()
                          .equals(String.class)) {
            return 3;
        } else if (message.getDetailedMessage()
                          .getClass()
                          .equals(int.class)) {
            return 4;
        } else if (message.getDetailedMessage()
                          .getClass()
                          .equals(List.class)) {
            return 5;
        } else if (message.getDetailedMessage()
                          .getClass()
                          .equals(double.class)) {
            return 6;
        } else if (message.getDetailedMessage()
                          .getClass()
                          .equals(AuctionHouse.class)) {
            return 7;
        } else if (message.getDetailedMessage()
                          .getClass()
                          .equals(Item.class)) {
            return 8;
        } else if (message.getDetailedMessage()
                .getClass()
                .equals(Agent.class)) {
            return 9;
        }
        return 0;
    }
}
