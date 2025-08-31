package mvc.model.findings;

import mvc.model.path.Path;

import java.io.Serializable;

/**
 * this class represents the rare finds that is implementing the Findings
 */
public class RareFinds implements Findings, Serializable {
    private Path path;
    private int points;
    private String imagePath;
    private String name;
    /**
     * constructor that creates a rare find with points and a path that will be in
     * @param path that the rare find will be in
     * @param points of the rare find
     * @paramm imagePath of the rare find
     */
    public RareFinds(Path path, int points, String name, String imagePath) {
        this.path = path;
        this.points = points;
        this.imagePath = imagePath;
        this.name = name;
    }

    /**
     * Accessor getPath to access the path of the rare find
     * @return the path that the rare find will be in
     */
    public Path getPath() {
        return this.path;
    }

    /**
     * Accessor getPoints to get the points of the rare find
     * @return the points of the rare find
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

    public String getName() {
        return this.name;
    }

}
