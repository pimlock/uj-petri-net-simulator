package petrieditor.visual.view;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;

/**
 * @author wiktor
 */
public abstract class PetriNetComponent extends JComponent {

    private boolean hover;
    protected NameLabel label;

    public PetriNetComponent(String name) {
        this.label = new NameLabel(name);
        addAncestorListener(new PetriNetComponentContainterListner());
    }

    public abstract boolean contains(int x, int y);

    public abstract Object getModel();

    public abstract Point getLabelPosition();

    public boolean isHover() {
        return hover;
    }

    public void setHover(boolean hover) {
        this.hover = hover;
    }

    protected class PetriNetComponentContainterListner implements AncestorListener {
        public void ancestorAdded(AncestorEvent e) {
            e.getAncestorParent().add(label);
        }

        public void ancestorRemoved(AncestorEvent e) {
            e.getAncestorParent().remove(label);
        }

        public void ancestorMoved(AncestorEvent e) {
            label.setLocation(getLabelPosition());
            label.updateSize();
            revalidate();
        }
    }
}
