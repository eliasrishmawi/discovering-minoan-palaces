package mvc.model.pawn;

import mvc.model.card.Card;
import mvc.model.exceptions.IllegalMoveException;
import mvc.model.path.Path;
import mvc.model.path.Position;
import mvc.model.player.Player;
import mvc.model.player.PlayerColor;

import java.io.Serializable;

/**
 * This class represents the pawn which will have 2 subclasses (Archaeologist,Theseus)
 */

public abstract class Pawn implements Serializable {
    private int positionNum;
    private Position position;
    private final Path path;
    private final String imagePath;
    private final Player player;
    private boolean visible;

    /**
     * constructor that creates a pawn in a path in the first position
     * @param path of the pawn
     * @param player that has this pawn
     */
    public Pawn(Path path, Player player) {
        this.path = path;
        setPositionNum(1);
        setPosition(path.getPosition(1));
        assert this.positionNum == 1 : "Pawn position must start at 1";
        this.imagePath = "project_assets/images/pionia/question.jpg";
        this.player = player;
        this.visible = false;
    }

    /**
     * method getPosition : is an Accessor to know the position of the pawn
     * @return the position of the pawn in the path
     */
    public int getPositionNum() {
        return this.positionNum;
    }

    /**
     * method to access the position of the pawn. by using the position number and the path of the pawn
     * @return the position of the pawn
     */
    public Position getPosition() {
        return this.position;
    }
    /**
     * method setPositionNum : is a Transformer to set the position of the pawn in the path
     * @param position of the pawn
     */
    public void setPositionNum(int position) {
        if(position < 1) position = 1;
        else if(position > 9) position = 9;
        this.positionNum = position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * method getPath : is an Accessor to know the path of the pawn
     * @return the path of the pawn
     */
    public Path getPath() {
        return path;
    }

    /**
     * method playedCard : to change the position of the pawn depending on which card that was played
     * it will be in sync with the method play() in class Card
     * @param card that was played during the game
     * @post the pawn moved forward or backward in the path depending on the card that was thrown
     */
    public void playedCard(Card card) throws IllegalMoveException {
        card.play(this);
    }


    /**
     * Accessor of the imagePath
     * @return the imagePath of the pawn
     */
    public String getImagePath(){
        return this.imagePath;
    }

    /**
     * Accessor of the pawn's player's
     * @return the pawn's player
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * to know if the pawn is visible or no
     * @return true if the pawn is visible, false otherwise
     */
    public boolean isVisible() {
        return this.visible;
    }

    /**
     * to update the pawn's visibility
     * @param visible the visibility of the pawn
     */
    public void setAppear(boolean visible) {
        this.visible = visible;
    }

    /**
     * if any pawn interacts with a box it will become visible,
     * and add the findings to its collection
     * @param position that the pawn wants to interact with. (we can access this by this.getPosition()
     *   but when an ariadni card is played and the position now is one step behind the pawn should have the
     *   ability to interact with it).
     */
    public abstract void interactWithBox(Position position);

}
