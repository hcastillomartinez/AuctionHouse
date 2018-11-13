package Bank;

/**
 * @author Daniel Miller
 * @version 11-13-18
 */
public class Bank implements Runnable{
    //list of agent accounts
    //lists of auction house accounts

    /*
    It is static and at a known address
    It hosts
        a list of agent accounts
        a list of auction house accounts
    It shares the list of auction houses with agents having bank accounts
    It provides agents with secret keys for use in the bidding process
    It transfers funds from agent to auction accounts, under agent control
    It blocks and unblocks funds in agent accounts, at the request of action houses


    Will have a proxy

    Some sort of pending balance:
        every time you make a bid on a new item
            subtract that amount from pending balance
    */

    public static void main(String[] args){
        //todo
    }

    /**
     * Constructor for Bank
     *
     */
    public Bank(){

    }


    @Override
    public void run() {

    }


    /**
     *
     * @return
     */
    private int assignKey(){
        return 0;
    }

    /**
     * Transfers funds from an Agent account to and AuctionHouse account.
     */
//    private void transferFunds(AuctionHouse house, Agent agent, double amount){
//
//    }

}
