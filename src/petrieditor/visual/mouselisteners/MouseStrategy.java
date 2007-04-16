package petrieditor.visual.mouselisteners;

import petrieditor.visual.view.GraphPanel;

import java.awt.event.MouseAdapter;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

/**
 * @author wiktor
 */
public abstract class MouseStrategy extends MouseAdapter implements PropertyChangeListener {

    protected GraphPanel graphPanel;

    public MouseStrategy(GraphPanel graphPanel) {
        this.graphPanel = graphPanel;
    }

    public void propertyChange(PropertyChangeEvent event) {
    }
}
