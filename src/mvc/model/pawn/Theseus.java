package mvc.model.pawn;

import mvc.model.card.Card;
import mvc.model.exceptions.IllegalMoveException;
import mvc.model.path.Path;
import mvc.model.path.Position;
import mvc.model.player.Player;
import mvc.model.player.PlayerColor;

import java.io.Serializable;

/**
 * this class represents the Theseus which is a Pawn
 */
public class Theseus extends Pawn implements Serializable {
    private final String imagePath;
    private int destroyedBoxes;

    /**
     * constructor that creates a theseus in a path using the superclass constructor
     * @param path of the Theseus
     */
    public Theseus(Path path, Player player) {
        super(path, player);
        this.imagePath = "project_assets/images/pionia/theseus.jpg";
        this.destroyedBoxes = 0;
    }

    /**
     * Accessor of the imagePath
     * @return the imagePath of the Theseus
     */
    @Override
    public String getImagePath(){
        return this.imagePath;
    }

    /**
     * to know how much are the destroyed boxes
     * @return how much does the Theseus destroy
     */
    public int getDestroyedBoxes() {
        return this.destroyedBoxes;
    }

    /**
     * to update the destroyed boxes
     * @param destroyedBoxes of the Theseus
     */
    public void setDestroyedBoxes(int destroyedBoxes) {
        this.destroyedBoxes = destroyedBoxes;
    }

    /**
     * if any pawn interacts with a box it will become visible
     * and it will destroy the finding in the position without collecting it
     * this.destroyedBoxes < 3 is a must
     * @param position that the pawn wants to interact with. (we can access this by this.getPosition()
     *      *   but when an ariadni card is played and the position now is one step behind the pawn should have the
     *      *   ability to interact with it).
     */
    @Override
    public void interactWithBox(Position position) throws IllegalMoveException {
        if(!position.hasFinds()) throw new IllegalMoveException("the position doesnt have a find");
        if(getDestroyedBoxes() >= 3) throw new IllegalMoveException("the Theseus already destroyed 3 boxes, cant destroy more");
        this.setAppear(true);
        position.removeFinds();
        destroyedBoxes++;
    }
}
