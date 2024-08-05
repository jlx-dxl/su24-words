# Overview

In this project, you'll implement a player in a word game in which you bid on letters in an attempt to form high-scoring words.
The goal is to implement a bidding strategy that will help you form better words and accrue more points without spending too much to do so.

A game consists of _r_ rounds, and a round consists of up to _8p_ bids, in which _p_ is the number of players. 
Although players can only play words of up to 7 letters in a round, there is no limit to the number of letters a player can collect in a round. 
At the end of each round, points are awarded for the word that is created, all letters are reset, and then we move onto the next round. 
At the end of the final round, the player with the most points wins.

There is a common pool of 98 letters, similar to the board game Scrabble. 
At the start of a round, each player starts with _n_ hidden ("secret") letters known only to that player, with _n_ anywhere from 0 to 7.
Players then bid _8p-np_ times for randomly selected letters, with each letter in turn going to the highest bidder. 
In a single round, letters are selected without replacement. For example, since there is only one 'Z' in the pool, once somebody has the single 'Z' nobody else will have a 'Z'. 
Letters are reset at the start of a new round.

Players bid with points. 
Each player starts with 100 points to enable them to bid at the start of the game. 
As the round progresses, the point total will change as the players spend points to get letters, then gain points at the end of the round as they create words. 
A player cannot bid more points than they possess, and a bid of zero points is allowed. 
Past bids are public information: every player knows how much each player has bid on each letter after each bidding cycle.

Players submit a single bid for each letter, and all bids are made simultaneously, without knowledge of other players' bids for that letter. 
The player who obtains the letter is determined according to the rules of a Vickrey Auction: the player who bids the most is the winner, but the amount they pay is that of the second-highest bid. 
If there is a tie for the high bid, the simulator will choose among the highest bids randomly.

After all _8p_ letters have been auctioned off in the round, each player tries to make as high-scoring a word as they can. 
A word will be considered valid if and only if it appears in a standard word list that we will supply. 
The score of the word is based on the points associated with the letters (see table below). 
In addition, a player who uses all 7 letters receives a 50 point bonus. 
There is no bonus for words containing fewer than 7 letters. 
The score is added to the player's total score, and the game continues with the next round. 


# Running the Simulator
Once you have downloaded the code in this repo, use IntelliJ to create a new project with the GitHub repo root as the root of the IntelliJ project.

To start the simulator, simply execute the `words.core.GameEngine.main()` method and you should see the simulator environment appear.

To configure a game:
* Select a player class from the dropdown list (these need to be listed in 'Game.playerlist') and click the "Add Player" button to add it to the game. You may have more than one implementation of a class in the game, and you may have up to 14 players.
* Select the number of hidden/secret letters that each player will have. These are randomly assigned at the start of each round.
* Select the number of rounds for the game.

To play a game:
* Click "Begin New Game" to start a new game.
* Click "Step" to move ahead one letter. You will see info about who won the bid in the lower right corner, and more details in the console output.
* Click "Play" to play continuously (configure the Delay with the slider at the bottom right). This will play to the end of the entire game, not each round.
* Click "Pause" to temporarily pause the game.


# Creating Your Own Player
Create a class called `words.gX.GroupXPlayer` where X is your group number.
This class must extend the `words.core.Player` abstract class, and specifically must implement the `bid` method, which is called each time the player needs to bid on a letter.

The `bid` method must return within two seconds, otherwise the bid will be rejected and the player will be banned from the rest of the game!
However, we can discuss modifying this restriction if it would enhance teams' solutions. 

You may override other methods from the `Player` base class, which are called by the simulator:
* `startNewGame`: called at the start of a new game. The default implementation stores its ID (which it gets from the simulator) and initializes a word list. 
* `startNewRound`: called at the start of each round. The default implementation initializes a List of the player's letters.
* `bidResult`: called after each bidding cycle. The default implementation adds the letter to its List if it is the winner of the bid.
* `returnWord`: called at the end of each round so that the player can return the word it wants to play. The default implementation plays the highest scoring word from the word list.
* `updateScores`: called after each round has finished. The default implementation uses this to keep track of all players' scores.

Note that the `words.core.PlayerBids` class is used to represent all of the bids for a single letter;
your player can use this to see what other players have done for previous bids and to track which player
has which letters.
A List of past bids is provided as an argument to the `bid` method; 
the most recent bids are passed to the `bidResult` method.

For information about individual letters, your class can use the static methods in the `words.core.ScrabbleValues` class,
which has methods for getting the point value of a letter, the number of occurrences of that letter per round (note that there are 98 total letters each round),
and the score of a given word.

The `Player` base class also has instance variables that your implementation can use:
* `wordlist`: an array of all the words in the dictionary; this is read from "files/wordlist.txt" in the IntelliJ project
* `myId`: this player's ID number
* `myLetters`: a List of all letters held by the player during the round, including the hidden ones
* `playerLetters`: a List of all letters held by *all* players during the round, not including the hidden ones (on account of them being... ya know... "hidden")
* `numPlayers`: how many players are in the game
* `scores`: a List indicating the score for each player in the game, updated after the round has finished

See the `words.g0.LetterValuePlayer` and `words.g0.RandomPlayer` classes for example implementations of the `Player` class.

The file "files/wordlist.txt" contains a listing of all the words in the dictionary.
Note that there is a listing of words-letter words in "files/7letterWords.txt" in case you would like to use that.

**As before, please do not change any simulator code**, i.e. code in the `words.core` package. 
Please notify the Instructor if you feel that a change is necessary.

To add your player to the simulation, list it in the Game.playerlist file; each player class should appear on a new line.

Note: To do logging/debugging, you may use System.out.println or System.err.println to write to the console, but please comment these out before submitting players for the tournaments. You may also call the _print_ or _println_ method in the OrganismsGame object to have text appear in the “messages” console in the simulator.

# Frequency and Values of Letters
During each round, the simulator will randomly select one of 98 letters. 
The table below indicates the number of occurrences of each letter and its corresponding point value.

|**Letter** |	**Count** |	**Value** |
|:--------: | :-------: | :-------: |
| A	| 9	| 1 | 
| B	| 2	| 3 |
| C	| 2	| 3 | 
| D	| 4	| 2 |
| E	| 12 | 1 | 
| F	| 2	| 4 |
| G	| 3	| 2 |
| H	| 2	| 4 |
| I	| 9	| 1 |
| J	| 1	| 8 |
| K	| 1	| 5 |
| L	| 4	| 1 |
| M	| 2	| 3 |
| N	| 6	| 1 |
| O	| 8	| 1 |
| P	| 2	| 3 |
| Q	| 1	| 10 |
| R	| 6	| 1 |
| S	| 4	| 1 |
| T	| 6	| 1 |
| U	| 4	| 1 |
| V	| 2	| 4 |
| W	| 2	| 4 |
| X	| 1	| 8 |
| Y	| 2	| 4 |
| Z	| 1	| 10 |

