package mvc.model.card;

import mvc.model.exceptions.IllegalMoveException;
import mvc.model.path.Path;
import mvc.model.path.Position;
import mvc.model.pawn.Pawn;
import mvc.model.pawn.Theseus;

import java.io.Serializable;

/**
 * This class represents a Minotavro Card
 * @author EliasRishmawi
 */
public class Minotavro implements Card, Serializable {
    private int steps;
    private final Path path;
    private final String imagePath;

    /**
     * constructor that creates a minotavro card that has steps -2
     * @param path of the minotavro card
     * @param imagePath of the card
     */
    public Minotavro(Path path, String imagePath) {
        this.path = path;
        setSteps(-2);
        this.imagePath = imagePath;
    }

    /**
     * method getValue : (Accessor) to get the value of the Minotavro card
     * @return the value of the card
     */
    @Override
    public int getValue() {
        return 0;
    }

    /**
     * method getSteps : (Accessor) to know how many steps does the Minotavro card move the pawn
     * @return the steps of the card
     */
    @Override
    public int getSteps() {
        return this.steps;
    }

    /**
     * method setSteps : (Transformer) to set the number of steps the Minotavro card move the pawn
     * @param steps of the card
     */
    @Override
    public void setSteps(int steps) {
        this.steps = steps;
    }

    /**
     * method getPath : (Accessor) to get the path of the card
     *
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
     * method play : changes the opponents pawn's position to 2 steps backward if the pawn is instance of Arch
     * or stay in the same position but not play the next round if the pawn was instance of Theseus
     * cant be used when the opponents pawn has reached the checkpoint position (position > 7)
     * make the pawn visible if the pawn is instance of Theseus
     * if the pawn is in first position there is no change
     * if the pawn is in second position move it to first
     * @param pawn that is effected by the card that was played
     */
    @Override
    public void play(Pawn pawn) throws IllegalMoveException {
        if(pawn == null) throw new IllegalMoveException("Cant play a Minotavro card. there is no opponent pawn in the path");
        int positionNum = pawn.getPositionNum();
        Path path = pawn.getPath();
        if(positionNum >= 7) throw new IllegalMoveException("The pawn has passed the checkpoint, Cant use the Minotavro card");
        pawn.setAppear(true);
        if(pawn instanceof Theseus) return;
        positionNum += steps;
        if(positionNum < 1) positionNum = 1;
        pawn.setPosition(path.getPosition(positionNum));
        pawn.setPositionNum(positionNum);
    }

}
