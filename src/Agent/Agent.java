package Agent;

/**
 * Agent.java is the class that bids on objects inside the auction house.
 * Danan High, 11/13/2018
 */
public class Agent implements Runnable {
/*
Agent is dynamically created
opens a bank account by providing a name and an initial balance
receives a unique account number
gets from the bank a list of active auctions
asks an auction house for a list of items being auctioned
gets from the bank a secret key to be used when interacting with a specific auction house
Gets replies from the auction house:
    acceptance
    rejection
    pass (higher bid in place)
    winner
notifies the bank to transfer the blocked funds to the auction house when winning a bid
terminates and closes the account when no bidding action is in progress

    Will have proxies to bank and auction house
        proxy for house will communicate with specific auction houses
        proxy for the bank will communicate with the bank
            bank will contain list of all of the auction houses

    will have to
 */

    private int id, accountBalance, pendingBalance;
    private long key, accountNumber;

    /**
     * Constructor for the Agent.
     * @param id Unique id for the agent.
     * @param key Unique key from the bank used for making bids.
     */
    public Agent(int id, int accountBalance, long key, long accountNumber) {
        this.id = id;
        this.accountBalance = accountBalance;
        this.key = key;
        this.accountNumber = accountNumber;
    }

    /**
     * Overrides run to perform certain tasks.
     */
    @Override
    public void run() {

    }

    /**
     * Making a bid to an auction house.
     */

    /**
     * Get auction houses from the bank.
     */

    /**
     * Get items from the auction house that can be bid on.
     */

    /**
     * Gets a key from the bank on a per auction house basis.
     */

    /**
     * Analyzing the replies from the auction houses, based on the bids the
     * agent has made.
     */

}
