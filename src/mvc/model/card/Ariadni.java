package mvc.model.card;

import mvc.model.exceptions.IllegalMoveException;
import mvc.model.path.Path;
import mvc.model.path.Position;
import mvc.model.pawn.Pawn;

import java.io.Serializable;

/**
 * This class represents an Ariadni Card
 * @author EliasRishmawi
 */
public class Ariadni implements Card, Serializable {
    private int steps;
    private final Path path;
    private final String imagePath;

    /**
     * constructor that creates an ariadni card that moves the pawn 2 steps
     * @param path the path of the ariadni card
     * @param imagePath of the card
     */
    public Ariadni(Path path, String imagePath) {
        this.path = path;
        setSteps(2);
        this.imagePath = imagePath;
    }
    /**
     * method getValue : (Accessor) to get the value of the card
     *
     * @return the value of the card
     */
    @Override
    public int getValue() {
        return 0;
    }

    /**
     * method getSteps : (Accessor) to know how many steps does this card move the pawn
     *
     * @return the steps of the card
     */
    @Override
    public int getSteps() {
        return this.steps;
    }

    /**
     * method setSteps : (Transformer) to set the number of steps this card move the pawn
     *
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
     * method play : making the pawn move 2 steps forward
     * @param pawn that is effected by the ariadni card that was played
     * @throws IllegalMoveException when we try to throw an ariadni card in the start of a path
     */
    @Override
    public void play(Pawn pawn) throws IllegalMoveException {
        if(pawn == null) throw new IllegalMoveException("Cant throw an ariadni card at the start");
        int positionNum = pawn.getPositionNum();
        Path path = pawn.getPath();
        if(positionNum == 9) throw new IllegalMoveException("The pawn is in the last position, cant move it more");
        positionNum += steps;
        if(positionNum > 9) positionNum = 9;
        pawn.setPosition(path.getPosition(positionNum));
        pawn.setPositionNum(positionNum);
    }

}
