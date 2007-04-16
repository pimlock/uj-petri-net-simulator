package petrieditor.visual.strategy;

import petrieditor.visual.view.GraphPanel;

import java.awt.event.MouseAdapter;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

/**
 * @author wiktor
 */
public abstract class Strategy extends MouseAdapter implements PropertyChangeListener {

    protected GraphPanel graphPanel;

    public Strategy(GraphPanel graphPanel) {
        this.graphPanel = graphPanel;
    }

    public void propertyChange(PropertyChangeEvent event) {
    }
}
