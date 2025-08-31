package mvc.model.card;

import mvc.model.exceptions.IllegalMoveException;
import mvc.model.path.Path;
import mvc.model.path.Position;
import mvc.model.pawn.Pawn;

import java.io.Serializable;

/**
 * This class represents a Simple Card
 * @author EliasRishmawi
 */
public class SimpleCard implements Card, Serializable {
    private final int value;
    private int steps;
    private final Path path;
    private final String imagePath;


    /**
     * constructor that creates a simple card that moves the pawn 1 step
     * @param value of the card
     * @param path the path of the card
     * @param imagePath of the card
     */
    public SimpleCard(int value, Path path, String imagePath) {
        this.value = value;
        setSteps(1);
        this.path = path;
        this.imagePath = imagePath;
    }

    /**
     * method getValue : (Accessor) to get the value of the simple card
     * @return the value of the card
     */
    @Override
    public int getValue() {
        return this.value;
    }

    /**
     * method getSteps : (Accessor) to know how many steps the simple card move the pawn
     * @return 1 (1 step forward)
     */
    @Override
    public int getSteps() {
        return this.steps;
    }

    /**
     * method setSteps : (Transformer) to set the number of steps the simple card move the pawn
     * @param steps of the card
     */
    @Override
    public void setSteps(int steps) {
        this.steps = steps;
    }

    /**
     * method getPath : (Accessor) to get the path of the card
     * @return the path of the card
     */
    @Override
    public Path getPath() {
        return this.path;
    }


    /**
     * method getImagePath : (Accessor) to get the path image of the card
     * @return the path image of the card
     */
    @Override
    public String getImagePath() {
        return this.imagePath;
    }

    /**
     * method play : making the pawn move 1 step forward
     * @param pawn that is effected by the simple card that was played
     */
    @Override
    public void play(Pawn pawn) throws IllegalMoveException {
        Path path = pawn.getPath();
        int positionNum = pawn.getPositionNum();
        if(positionNum == 9) throw new IllegalMoveException("The pawn is in the last position, cant move it more");
        positionNum += steps;
        pawn.setPosition(path.getPosition(positionNum));
        pawn.setPositionNum(positionNum);
    }

    /**
     * method matchCard to check if we can play the card after a previous one that was played
     * this.value >= prvcard.value in order to have the right to play the card
     * @param prvCard the previous simple card that was played
     * @return true if we can throw the (this card) after the prv card, false otherwise
     */
    public boolean matchCard(Card prvCard) {
        return this.getValue() >= prvCard.getValue();
    }
}
