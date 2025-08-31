package mvc.model.player;

import java.awt.*;

/**
 * enum that contains the colors of the players
 */
public enum PlayerColor {
    RED(Color.RED), GREEN(Color.GREEN);

    private final Color awtColor;

    PlayerColor(Color awtColor) {
        this.awtColor = awtColor;
    }
    public Color getAwtColor() {
        return awtColor;
    }
}
