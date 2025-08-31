package mvc.model.path;

import java.awt.*;

/**
 * this enum contains the colors of the path
 * @author Elias Rishmawi
 */
public enum PathColor {
    RED(Color.RED), YELLOW(Color.YELLOW), GRAY(Color.GRAY), BLUE(Color.BLUE);

    private final Color awtColor;

    PathColor(Color awtColor) {
        this.awtColor = awtColor;
    }
    public Color getAwtColor() {
        return awtColor;
    }
}
