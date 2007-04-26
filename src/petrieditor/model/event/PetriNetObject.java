package petrieditor.model.event;

import petrieditor.model.Transition;
import petrieditor.model.Place;
import petrieditor.model.Arc;

/**
 * @author wiktor
 */
public class PetriNetObject {

    private Transition transition;
    private Place place;
    private Arc arc;

    public PetriNetObject(Transition transition) {
        this.transition = transition;
    }

    public PetriNetObject(Place place) {
        this.place = place;
    }

    public PetriNetObject(Arc arc) {
        this.arc = arc;
    }

    public Object getPetriNetObject() {
        return arc != null ? arc : transition != null ? transition : place;
    }

    public Transition getTransition() {
        return transition;
    }

    public Place getPlace() {
        return place;
    }

    public Arc getArc() {
        return arc;
    }
}
