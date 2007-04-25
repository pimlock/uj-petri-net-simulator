package petrieditor.visual.mouselisteners;

import petrieditor.visual.view.GraphPanel;
import petrieditor.visual.view.TransitionComponent;

import java.awt.event.MouseEvent;

/**
 * @author wiktor
 */
public class SimulationMouseStrategy extends MouseStrategy {

    public SimulationMouseStrategy(GraphPanel graphPanel) {
        super(graphPanel);
    }

    public void mouseClicked(MouseEvent e) {
        if (!(e.getComponent() instanceof TransitionComponent))
            return;

        TransitionComponent component = (TransitionComponent) e.getComponent();

        if (!component.getModel().isEnabled())
            return;

        component.getModel().fire();
    }
}
