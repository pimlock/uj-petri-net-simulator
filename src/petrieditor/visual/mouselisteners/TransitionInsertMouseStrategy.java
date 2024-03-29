package petrieditor.visual.mouselisteners;

import petrieditor.visual.view.GraphPanel;

import java.awt.event.MouseEvent;

/**
 * @author wiktor
 */
public class TransitionInsertMouseStrategy extends MouseStrategy {

    public TransitionInsertMouseStrategy(GraphPanel graphPanel) {
        super(graphPanel);
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1)
            graphPanel.getModel().addNewTransition(e.getPoint());
    }
}
