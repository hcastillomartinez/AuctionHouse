# Project: Auction House
## Student(s): Danan High, Hector Castillo-Martinez, Daniel Miller

## Introduction
Generally describe the project/problem you are solving.

## Contributions
Danan worked on the entire Agent package, the Proxies package and the TestPackage package. 

## Usage
Give details about how to use the program. (Imagine making it easy for someone that you don't know to run and use your project.)
### Agent
To begin using Agent.java there need to be command line arguments when running the jar. The first argument is the ip address that the bank server is running on. 
The second argument is the port that the agent connects to. When the application is running the first thing that needs to be performed is the creation of the account.
To create the account the user must enter their first name, last name and their starting balance in their respective fields. Once this has been completed
the user must click on the "New Account" button. This will populate the pending balance to mirror the account balance as no bids have yet been placed.

If the user chooses to make a bid on an item they must first choose an Auction House to connect to. This can be achieved by clicking on the "Update" button and then 
viewing all of the options in the "Choose Auction House" drop down. When the user chooses an Auction House from the drop down they are then able to click the 
"Choose House" button. This causes the item list to be populated from which the user can choose an item to bid on. 
Selecting the item happens when the user chooses an item from the list and the clicks the "Select Item" button. 

When an Auction House has been choosen the Auction House field will be populated with they type of Auction House has been choosed, and the ID will be populated
with the id number of the Auction House. Once an item has been selected the Item field will set to the name of the item. It is then up to the user 
to determine if they want to make a bid by inputting an amount into the Bid field. If the user is satisfied with their choice they will click on the 
"Place Bid" button to make the bid. The Current Bids list will populate with the current bids that the user has made. 
When a bid has been placed the Pending Balance field will update to reflect the usere's balance - the amount that they have placed on bids.
When a bid has concluded the user will either have funds removed from their account balance, or they will have their 
pending balance updated back to it's status before the bid.

To view if they have won an item the user must click the "Update Items Won" button and then the list will populate with items that they have won.

To close the program the user will click the default red exit button to close the window. If a bid is in process the user will not be able to exit the program
until the conclustion of the bid process.

## Project Assumptions
In the project we assume that all of the Agents are unique even if they have the same name. 

## Versions
Where are the .jar files?
### V1
explain about how version 1 works
### V2
explain about how version 2 works etc...

## Docs
What folder is your documentation (diagram and class diagram) in?
The folder containing the documentation is in the docs folder. This contains the class diagrams and the 
and the schematic of the design.

## Status
### Implemented Features
State things that work.

### Known Issues
There are some bugs with gui updating in AgentGUI.java. When a bid is completed the current bid list does not always
update to have removed the finished bid. There is also an issue in the "Choose Auction House" drop down button that does not update 
to show when an Auction House has left. The button still shows the Auction Houses as choices when that should not occur. The item list also 
does not remove items that have been won for an extended period of time.


## Daniel's Todo's
    
    Comments Completed:
        All
        
    Class Diagrams todo:
        Bank
        BankGUI
        Account
        AgentInfo
        AuctionInfo    
        
    