package petrieditor.visual.view;

import javax.swing.*;

/**
 * @author wiktor
 */
public abstract class PetriNetComponent extends JComponent {
    
    private boolean hover;

    public abstract boolean contains(int x, int y);

    public boolean isHover() {
        return hover;
    }

    public void setHover(boolean hover) {
        this.hover = hover;
    }
}
