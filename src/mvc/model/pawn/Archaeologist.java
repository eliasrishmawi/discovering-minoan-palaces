package mvc.model.pawn;

import mvc.model.card.Card;
import mvc.model.exceptions.IllegalMoveException;
import mvc.model.findings.Murals;
import mvc.model.path.Path;
import mvc.model.path.Position;
import mvc.model.player.Player;
import mvc.model.player.PlayerColor;

import java.io.Serializable;

/**
 * this class represents the Archaeologist which is a Pawn
 */
public class Archaeologist extends Pawn implements Serializable {
    private final String imagePath;

    /**
     * constructor that creates an archaeologist using the superclass constructor
     * @param path of the archaeologist
     */
    public Archaeologist(Path path, Player player){
        super(path, player);
        this.imagePath = "project_assets/images/pionia/arch.jpg";
    }

    /**
     * Accessor of the imagePath
     * @return the imagePath of the Arch
     */
    @Override
    public String getImagePath(){
        return this.imagePath;
    }

    /**
     * if any pawn interacts with a box it will become visible
     * also depending on what is the finding in the position it will be added to the player's collection
     * and remove it from the position (using removeFinds() ) if it is not a mural
     * and add the findings to its collection
     * @param position that the pawn wants to interact with. (we can access this by this.getPosition()
     *      *   but when an ariadni card is played and the position now is one step behind the pawn should have the
     *      *   ability to interact with it).
     */
    @Override
    public void interactWithBox(Position position) throws IllegalMoveException {
        if(!position.hasFinds()) throw new IllegalMoveException("The position doesnt have a find");
        this.setAppear(true);
        this.getPlayer().captureFind(position.getFinds());
        if(!(position.getFinds() instanceof Murals)){
            position.removeFinds();
        }
        position.setOpenedBoxByPawn(this);
    }

}


