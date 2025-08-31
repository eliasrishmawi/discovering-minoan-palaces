package mvc.model.findings;

import java.io.Serializable;

/**
 * this class represents the Simple finds that is implementing the Findings
 */
public class SimpleFinds implements Findings, Serializable {
    private final String imagePath;

    /**
     * constructor that creates a simple find
     * @param imagePath of the simple find
     */
    public SimpleFinds(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * Accessor getPoints to get the points of the finding
     * @return the points of the finding
     */
    @Override
    public int getPoints() {
        return 0;
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
