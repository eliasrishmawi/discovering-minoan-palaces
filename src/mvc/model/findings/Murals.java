package mvc.model.findings;

import java.io.Serializable;

/**
 * this class represents the Murals which are an implementation of the Findings
 */
public class Murals implements Findings, Serializable {
    private final int points;
    private final String imagePath;

    /**
     * constructor that creates a mural with a specific points
     * @param points of the mural
     * @param imagePath of the mural
     */
    public Murals(int points, String imagePath) {
        this.points = points;
        this.imagePath = imagePath;
    }

    /**
     * Accessor getPoints to get the points of the Mural
     * @return the points of the mural
     */
    @Override
    public int getPoints() {
        return this.points;
    }

    /**
     * method getImagePath : (Accessor) to get the path image of the finding
     * @return the path image of the finding
     */
    @Override
    public String getImagePath() {
        return this.imagePath;
    }
}
