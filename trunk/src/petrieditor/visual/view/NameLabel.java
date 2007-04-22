package petrieditor.visual.view;

import javax.swing.*;

/**
 * @author wiktor
 */
public class NameLabel extends JLabel {

    public NameLabel(String text) {
        super(text);
    }

    public void updateSize() {
        setSize(getPreferredSize());
    }

    public void setText(String text) {
        super.setText(text);
        updateSize();
    }
}
