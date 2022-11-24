

<h3 > OOP Homework: </h3>   
<h1 align="center"> GwentStone </h1>

## Skel Structure

* src/
  * checker/ - checker files
  * fileio/ - contains classes used to read data from the json files
  * game/  - contains the classes used for implementing the game
  * main/
      * Main - the Main class runs the checker on the current implementation of the game. Run Main to test your implementation from the IDE or from command line.
      * Test - run the main method from Test class with the name of the input file from the command line and the result will be written
        to the out.txt file. 
* input/ - contains the tests in JSON format
* ref/ - contains all reference output for the tests in JSON format

### Package game   
  The game package contains the following classes: <br>
* Game - the class where the logic and rules for the game are implemented 
* Player - stores all the information necessary for a player 
* Table - helps cards interact between them  
* Card - stores all the information necessary for a card
  * Environment
  * Minion
  * Hero 
* Deck - a collection of Card type elements 

The Card class is a superclass for the Environment, Minion, and Hero classes.
This is done because all the inherited classes have a lot of common attributes, such as mana, description, name, and colors. 
The Environment and Minion classes represent the two different type of cards that exists. 
The Hero can be considered a special card that is unique for each player.

### Functionality
At the beginning of the implementation, all the data necessary to start the game is put in the corresponding fields. 
For each game, we listen for a predefined number of actions, actions that produce an output that is stored in the output
variable and later exported in JSON format. A game ends when a player's hero is killed( the health variable is equal to
or less than 0).

