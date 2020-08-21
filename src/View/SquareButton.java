package View;

import javax.swing.*;
import java.awt.*;

/**
 * Used to keep track of the buttons coordinate.
 */
public class SquareButton extends JButton {
    public Integer x;
    public Integer y;

    public SquareButton(Integer x, Integer y) {
        if (x != null && y != null) {//To not allow for the one being set to a Integer without the other.
            this.x = x;
            this.y = y;
        }
        this.setPreferredSize(new Dimension(50, 50));
    }
}
