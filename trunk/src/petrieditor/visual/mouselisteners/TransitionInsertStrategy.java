package petrieditor.visual.strategy;

import petrieditor.visual.view.GraphPanel;

import java.awt.event.MouseEvent;

/**
 * @author wiktor
 */
public class TransitionInsertStrategy extends Strategy {

    public TransitionInsertStrategy(GraphPanel graphPanel) {
        super(graphPanel);
    }

    public void mouseClicked(MouseEvent e) {
        graphPanel.getModel().addNewTransition(e.getPoint());
    }
}
