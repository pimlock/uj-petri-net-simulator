package petrieditor.visual.mouselisteners;

import petrieditor.visual.view.GraphPanel;

import java.awt.event.MouseEvent;

/**
 * @author wiktor
 */
public class PlaceInsertMouseStrategy extends MouseStrategy {

    public PlaceInsertMouseStrategy(GraphPanel graphPanel) {
        super(graphPanel);
    }

    public void mouseClicked(MouseEvent e) {
        graphPanel.getModel().addNewPlace(e.getPoint());
    }
}
