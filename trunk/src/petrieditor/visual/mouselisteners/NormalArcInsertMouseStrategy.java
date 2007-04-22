package petrieditor.visual.mouselisteners;

import petrieditor.visual.view.GraphPanel;
import petrieditor.model.Place;
import petrieditor.model.Transition;

/**
 * @author wiktor
 */
public class NormalArcInsertMouseStrategy extends ArcInsertMouseStrategy {

    public NormalArcInsertMouseStrategy(GraphPanel graphPanel) {
        super(graphPanel);
    }

    public void connect(Place place, Transition transition) {
        graphPanel.getModel().connectWithArc(place, transition);
    }

    public void connect(Transition transition, Place place) {
        graphPanel.getModel().connectWithArc(transition, place);
    }

    public boolean isTransitionToPlacePossible() {
        return true;
    }

}
