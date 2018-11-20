# Project: NAME OF PROJECT
## Student(s):  Name(s)

## Introduction
Generally describe the project/problem you are solving.

## Contributions
If this is a group project then detail which group member worked on what aspect(s) of the project.

## Usage
Give details about how to use the program. (Imagine making it easy for someone that you don't know to run and use your project.)

## Project Assumptions
This section is where you put any clarifications about your project & how it works concerning any vagueness in the specification documents.

## Versions
Where are the .jar files?
### V1
explain about how version 1 works
### V2
explain about how version 2 works etc...

## Docs
What folder is your documentation (diagram and class diagram) in?

## Status
### Implemented Features
State things that work.

### Known Issues
If there are things that don't work put them here. It will save the graders time and keep them in a good mood.

## Testing and Debugging
If you have tests, then explain how they work and how to use them.


## Daniel's Todo's
Ask Danan why address and port number are static. Does every instance of Bank need to share the port and address?
Should we have references to the agent in the bank account? Shouln't it be a reference to the local agent proxy the bank is going through?
Why does the bank proxy have a run method? It isn't a thread in roman's design.