package petrieditor.visual.view;

import javax.swing.*;
import java.awt.*;

/**
 * @author wiktor
 */
public class CoolButton extends JButton {

    private CoolToggleButton button;

    public CoolButton(int style) {
        button = new CoolToggleButton(style);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setForeground(getForeground());
        button.setModel(getModel());
    }

    public void paintComponent(Graphics g) {
        button.setSize(getSize());
        button.paintComponent(g);
        super.paintComponent(g);
    }

}
