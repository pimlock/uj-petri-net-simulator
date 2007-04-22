package petrieditor.visual.mouselisteners;

import petrieditor.visual.view.GraphPanel;
import petrieditor.model.Place;
import petrieditor.model.Transition;

/**
 * @author wiktor
 */
public class InhibitorArcInsertMouseStrategy extends ArcInsertMouseStrategy {

    public InhibitorArcInsertMouseStrategy(GraphPanel graphPanel) {
        super(graphPanel);
    }

    public void connect(Place place, Transition transition) {
        graphPanel.getModel().connectWithInhibitorArc(place, transition);
    }

    public void connect(Transition transition, Place place) {
        throw new IllegalArgumentException();
    }

    public boolean isTransitionToPlacePossible() {
        return false;  
    }


}
