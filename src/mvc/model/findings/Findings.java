package mvc.model.findings;

import java.io.Serializable;

/**
 * this interface represents the findings of the game
 * it has 3 implementation, RareFinds, SimpleFinds, and Murals
 */
public interface Findings{

    /**
     * Accessor getPoints to get the points of the finding
     * @return the points of the finding
     */
    int getPoints();

    /**
     * method getImagePath : (Accessor) to get the path image of the finding
     * @return the path image of the finding
     */
    String getImagePath();
}
