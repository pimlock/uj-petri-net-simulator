package petrieditor.model;

import petrieditor.util.Observable;
import petrieditor.model.event.NotifyEvent;
import petrieditor.model.viewinterfaces.ArcView;

/**
 * @author wiktor
 */
public class Arc extends Observable<Arc, ArcView, NotifyEvent>  {

    public enum ArcDirection {
        PLACE_TO_TRASNSITION, TRANSITION_TO_PLACE 
    }

    protected Place place;
    protected Transition transition;
    protected int weight;
    protected ArcDirection arcDirection;

    public Arc(Place fromPlace, Transition toTransition) {
        this.place = fromPlace;
        this.transition = toTransition;
        this.weight = 1;
        this.arcDirection = ArcDirection.PLACE_TO_TRASNSITION;
        fromPlace.addOutputArc(this);
        toTransition.addInputArc(this);
    }

    public Arc(Transition fromTransition, Place toPlace) {
        this.place = toPlace;
        this.transition = fromTransition;
        this.weight = 1;
        this.arcDirection = ArcDirection.TRANSITION_TO_PLACE;
        fromTransition.addOutputArc(this);
        toPlace.addInputArc(this);
    }

    public boolean isEnabled() {
        return place.getCurrentMarking() >= weight;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
        setChangedAndNotifyObservers();
    }

    public Place getPlace() {
        return place;
    }

    public Transition getTransition() {
        return transition;
    }

    public ArcDirection getArcDirection() {
        return arcDirection;
    }
}
