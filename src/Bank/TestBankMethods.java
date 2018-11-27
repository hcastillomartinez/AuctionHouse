package Bank;

import AuctionHouse.AuctionHouse;

public class TestBankMethods {

    public static void main(String args[]){
        Bank bank = new Bank(null, 4444);
        Account auctionAccount = bank.makeAccount("phil",500);
        Account agentAccount = bank.makeAccount("phil",1000);
        bank.getAccounts().add(auctionAccount);
        bank.getAccounts().add(agentAccount);

        System.out.println("The accounts are: " + auctionAccount + " " + agentAccount);

        System.out.println("Trying to transfer the first funds.");

        try{
            bank.transferFunds(0,1,500);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        System.out.println("The accounts are: " + auctionAccount + " " + agentAccount);
        System.out.println("Trying to transfer the second funds.");

        try{
            bank.transferFunds(0,1,500);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        System.out.println("The accounts are: " + auctionAccount + " " + agentAccount);
        System.out.println("Trying to transfer the third funds.");

        try{
            bank.transferFunds(0,1,500);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        System.out.println("The accounts are: " + auctionAccount + " " + agentAccount);
    }
}
