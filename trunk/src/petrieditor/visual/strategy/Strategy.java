package petrieditor.visual.strategy;

import petrieditor.visual.view.GraphPanel;

import java.awt.event.MouseAdapter;

/**
 * @author wiktor
 */
public abstract class Strategy extends MouseAdapter {

    protected GraphPanel graphPanel;

    public Strategy(GraphPanel graphPanel) {
        this.graphPanel = graphPanel;
    }
}
