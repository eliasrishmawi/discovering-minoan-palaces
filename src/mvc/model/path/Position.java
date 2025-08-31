package mvc.model.path;

import mvc.model.findings.Findings;
import mvc.model.pawn.Pawn;

import java.io.Serializable;

/**
 * this class represents the position. Each path have 9 positions
 */
public class Position implements Serializable {
    private int number;
    private final int points;
    private Findings finds;
    private final String imagePath;
    private final PathColor color;
    private Pawn openedBoxByPawn;  // this is used when a pawn have gone back by a minotavro card and has already opened the box it cant open it again

    /**
     * constructor that creates a Position with specific points and a specific finding
     * @param number of the position (starting from 1 to 9) in each path
     * @param points of the position
     * @param finds in the position
     * @param color of the position
     * @param imagePath of the position
     */
    public Position(int number, int points, Findings finds, PathColor color, String imagePath) {
        setPosition(number);
        this.points = points;
        this.finds = finds;
        this.color = color;
        this.imagePath = imagePath;
        openedBoxByPawn = null;
    }

    /**
     * observer hasFinds to check whether the position have a find or no
     * @return true if the position has finds, false otherwise
     */
    public boolean hasFinds(){
        return finds != null;
    }

    /**
     * method removeFinds to remove a specific finds when the player interacts with the position
     */
    public void removeFinds(){
       finds = null;
    }

    /**
     * Accessor getPoints to access the points of the position
     * @return the points of the position
     */
    public int getPoints() {
        return points;
    }

    /**
     * Accessor getFinds to access the finding in the position
     * @return the finding in the position
     */
    public Findings getFinds() {
        return finds;
    }

    /**
     * Accessor getImagePath to access the image path of the position
     * @return the image path of the position
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Accessor PathColor to access the color of the position that will be the same as the path's color
     * @return the color of the position
     */
    public PathColor getColor() {
        return color;
    }

    /**
     * Accessor of the number of the position
     * @return the number of the position
     */
    public int getNumber() {
        return number;
    }

    /**
     * to set the number of the position
     * @param number the number of the position
     */
    public void setPosition(int number) {
        this.number = number;
    }

    /**
     * method to get the pawn that opened the box.
     * this is used when a pawn have gone back by a minotavro card and has already opened the box it cant open it agan
     * @return the pawn that opened the box
     */
    public Pawn getOpenedBoxByPawn() {
        return openedBoxByPawn;
    }

    /**
     * setter of the pawn that opened the box.
     * this is used when a pawn have gone back by a minotavro card and has already opened the box it cant open it agan
     * @param openedBoxByPawn the pawn that opened the box
     */
    public void setOpenedBoxByPawn(Pawn openedBoxByPawn) {
        this.openedBoxByPawn = openedBoxByPawn;
    }
}
