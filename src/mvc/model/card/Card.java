package mvc.model.card;

import mvc.model.exceptions.IllegalMoveException;
import mvc.model.path.Path;
import mvc.model.pawn.Pawn;

import java.io.Serializable;

/**
 * Card interface
 * Contains the methods signatures needed for
 * creating a simple or Ariadni or Minotavro card
 * @author EliasRishmawi
 */

public interface Card {
    /**
     * method getValue : (Accessor) to get the value of the card
     * @return the value of the card
     */
    int getValue();

    /**
     * method getSteps : (Accessor) to know how many steps does this card move the pawn
     * @return the steps of the card
     */
    int getSteps();

    /**
     * method setSteps : (Transformer) to set the number of steps this card move the pawn
     * @param steps of the card
     */
    void setSteps(int steps);

    /**
     * method getPath : (Accessor) to get the path of the card
     * @return the path of the card
     */
    Path getPath();


    /**
     * method getImagePath : (Accessor) to get the path image of the card
     * @return the path image of the card
     */
    String getImagePath();

    /**
     * method play : changes the pawn's position depending on which card that was played
     * @param pawn that is effected by the card that was played
     */
    void play(Pawn pawn) throws IllegalMoveException;


}
