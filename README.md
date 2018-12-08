# Project: Auction House
## Student(s): Danan High, Hector Castillo-Martinez, Daniel Miller

## Introduction
Distributed auction system with one static Bank, k number of Auction Houses 
that are dynamically created, and i number of Agents that are also 
dynamically created.

### Auction House
AH has a list of items and hosts the status of each auction. It 
should be able to relay at any point the items it has for sale at their most 
current state. AH cannot be closed if there is an active auction if it is 
closed it must be done graciously. When closed it should de-register with the
 bank. 
#### Rules of the auction:
Upon receiving a bid it will acknowledge with a reject or accept message.

When bid is accepted it will notify bank to block funds of the bid amount and
pass notification is sent to agent that is outbid along with an unblock 
funds to bank when it is necessary.
 
Auction runs for 30 seconds and upon finishing will send to winner that it 
has won and it is complete once the funds have been transferred into the AH's
account.  
   

## Contributions
Danan worked on the Agent package and the Proxies package. 
Daniel worked worked on the Bank package.
Hector worked on the AuctionHouse package.
## Usage
Give details about how to use the program. (Imagine making it easy for someone that you don't know to run and use your project.)
### Agent
To begin using Agent.java there need to be command line arguments when running the jar. The first argument is the ip address that the bank server is running on. 
The second argument is the port that the agent connects to. When the application is running the first thing that needs to be performed is the creation of the account.
To create the account the user must enter their first name, last name and their starting balance in their respective fields. Once this has been completed
the user must click on the "New Account" button. This will populate the pending balance to mirror the account balance as no bids have yet been placed.

    java -jar Agent.java IPAdress portNumber

If the user chooses to make a bid on an item they must first choose an Auction House to connect to. This can be achieved by clicking on the "Update" button and then 
viewing all of the options in the "Choose Auction House" drop down. When the user chooses an Auction House from the drop down they are then able to click the 
"Choose House" button. This causes the item list to be populated from which the user can choose an item to bid on. 
Selecting the item happens when the user chooses an item from the list and the clicks the "Select Item" button. 

When an Auction House has been chosen the Auction House field will be populated with they type of Auction House has been chosen, and the ID will be populated
with the id number of the Auction House. Once an item has been selected the Item field will set to the name of the item. It is then up to the user 
to determine if they want to make a bid by inputting an amount into the Bid field. If the user is satisfied with their choice they will click on the 
"Place Bid" button to make the bid. The Current Bids list will populate with the current bids that the user has made. 
When a bid has been placed the Pending Balance field will update to reflect the user's balance - the amount that they have placed on bids.
When a bid has concluded the user will either have funds removed from their account balance, or they will have their 
pending balance updated back to it's status before the bid.

To view if they have won an item the user must click the "Update Items Won" button and then the list will populate with items that they have won.

To close the program the user will click the default red exit button to close the window. If a bid is in process the user will not be able to exit the program
until the conclusion of the bid process.

### Bank
The Bank application is run with the Bank.jar file from the command line with the following:

    java -jar Bank.java IPAddress portNumber
    
IPAddress is the address of the machine bank will run on and portNumber is the port that Bank will run on.

After the jar file is running the GUI will pop up and display empty lists of agent and auction house accounts.
Accounts hold the owner's name, account number, balance, and pending balance. The pending balance represents how much of their money that 
has not already been used to bid. This ensures that an Agent can't win two bids 
simultaneously while only having enough money for one bid.

The Bank thread is constantly searching for and establishing socket connections. Once connected, accounts are created and displayed to the GUI.
After connections are ended, the corresponding account is deleted and removed from the screen.

No user input is required in the Bank GUI.     
To close the program the user will click the default red exit button to close the window. 

### Auction House
The jar expects one command line argument to run and it should be the ip 
address of the computer AH will be run on.

    java -jar AuctionHouse.jar IPAddress
 Once program is launched a small window will pop up which will have three text fields. The first text field 
is for is for the type of AH of which there are 3; car, furniture, and tech. 
The second text field is for the port of which AH will listen for connections
 on. The third is for the ip address of the bank which should be known, it 
 also expects the bank to always run on port 4444 for simplicity.

 Once the launch button is pressed the main GUI of the auction house will be 
 launched once the connection to the bank is established. After this launch 
 no more user input is taken and the GUI updates on its own as things occur 
 internally with the passing of messages from the bank and the agents.
 
 The left window is for all the items that is currently for sale and the 
 price of them. The right window shows the current and previous auctions with 
 the status of an auction, the item for sale, and the current winning bid on 
 it. Once an item is sold it is then removed from the left window (Item list)
 and the funds are then updated to the amount payed for the item. The 
 auction houses own account number is also displayed above the balance of 
 the auction house. 
 
 Window will remain open until the user chooser to close, although it will 
 prevent the user from closing it if there are any active auctions by throwing 
 an alert every time the close button is pressed. Once there are no 
 underlying processes blocking the closing, the GUI will simply close.

## Project Assumptions
In the project we assume that all of the Agents are unique even if they have the same name. 
The bank will only be closed last after all accounts have been closed.
Once an auction house runs out of items it can simply continue
 to run forever or just be closed as their should not be any processes 
 preventing the closing. 
## Versions
Jar files are located in the top level directory.
### V1
explain about how version 1 works
### V2
explain about how version 2 works etc...

## Docs
The folder containing the documentation is in the docs folder. This contains the class diagrams and the 
and the schematic of the design.

## Status
### Implemented Features
State things that work.

The bank blocks and unblocks funds correctly during bidding. After a bid is one, funds are transferred to the correct auction house.
Accounts are closed properly when a connection with an agent/auction house is ended. Unique accounts are created for each connection made with the bank.
The list of auction houses is successfully shared with agents.

Auction House can be dynamically created and keeps track of all active, also 
inactive, auctions and its own items. When asked for items it will send the 
list to the agent. Upon creation it will send message to create the account 
with the bank along with the info it needs to be able to show the auction 
houses info to the agent. When there is no active auctions it can be closed, 
otherwise will not be able to. Can disconnect from the bank and bank handles 
de-registering. If bid is accepted it will send response to agent that it has
 accepted the bid and also when the bid is rejected. On pass of bid, AH will 
 tell bank to block funds of the bid amount and also tells bank to unblock 
 funds from agent that was passed, as well as sending the agent that was out 
 bid a message stating so. Auction runs for 30 seconds then tell the 
 agent it has won and then money is transferred into the auction houses 
 account. When closing it will relay message to agent that it has closed to 
 handle closing of connection on its end and on closing will close all of its
 own active connections to sockets. Updating GUI is in real time, meaning it
 always displays the most current information.

### Known Issues
There are some bugs with gui updating in AgentGUI.java. When a bid is completed the current bid list does not always
update to have removed the finished bid. There is also an issue in the "Choose Auction House" drop down button that does not update 
to show when an Auction House has left. The button still shows the Auction Houses as choices when that should not occur. The item list also 
does not remove items that have been won for an extended period of time. 
Synchronization in auction where not always unblocking the correct amount 
from the agent if bids come in simultaneously as one is passed.

        
    