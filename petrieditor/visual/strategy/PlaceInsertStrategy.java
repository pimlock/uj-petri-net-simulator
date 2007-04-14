package petrieditor.visual.strategy;

import petrieditor.visual.view.GraphPanel;

import java.awt.event.MouseEvent;

/**
 * @author wiktor
 */
public class PlaceInsertStrategy extends Strategy {

    public PlaceInsertStrategy(GraphPanel graphPanel) {
        super(graphPanel);
    }

    public void mouseClicked(MouseEvent e) {
        graphPanel.getModel().addNewPlace(e.getPoint());
    }
}
