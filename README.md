# PhaseB_5299

## Overview:

PhaseB_5299 is the second phase of implementing the game "Searching for the Lost Minoan Palaces". It is a Java-based game project designed using the Model-View-Controller (MVC) architecture. 

The game starts by picking a player randomly to have the first turn. Each player have 8 cards. and there is 4 paths in the board of the game. Each player have 4 pawns (3 archaeologists and 1 Theseus) and each one will have its own path.
To start a path you should throw a Simple Card. the simple card moves the pawns 1 step. The Ariadni card 2 moves them 2 steps. and the Minotaur Card move the opponents Archaeologist 2 steps behind and make the Theseus not move for an entire round.
The player have to either play a card or discard a card then he has to draw a card from the card deck. That's how each round will work.
Also, there's positions that have findings, each finding have different points. And also each position that the pawn is on have different points.
if an Archaeologist is on a position it counts the same as its points. but if a Theseus is on a position it counts the double.
The game ends if 4 pawns reach the checkpoint or the card deck has finished. 
The player with the most points wins.

### Some instructions to play the game:

Right-click on a card to discard it, left-click on a card to play it, and click on the label of the last position of any of the four paths (the four castles) to get information about the path.
Also, if a player acquire a finding it will show info about it (only the Archaeologists because Theseus destroys them).
Also, if a player tries to play an illegal move, he won't be able to, and there will be an error message.

### Setup Instructions:
To get started, ensure JDK 8 or higher is installed on your system. Install Apache Ant. Save all source files in UTF-8 encoding to ensure Greek characters display correctly.

We use Apache ant to build and run the project.

#### To clean the project run: "ant clean"

#### To Compile the project run: "ant compile"

#### To run the project without a jar file run: "ant run"

#### To package the project into a jar file run: "ant jar"

#### To run the jar file run: "ant run-jar"


Enjoy playing!

