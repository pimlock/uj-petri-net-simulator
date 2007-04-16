package petrieditor.model;

import petrieditor.util.Observable;
import petrieditor.model.event.NotifyEvent;
import petrieditor.model.event.EventType;
import petrieditor.model.viewinterfaces.PetriNetView;

import java.util.List;
import java.util.ArrayList;
import java.awt.*;

/**
 * Glowna klasa modelu. Tutaj znajduja sie wszystkie informacje o sieci.
 *
 * @author wiktor
 */

public class PetriNet extends Observable<PetriNet, PetriNetView, NotifyEvent> {

    private List<Place> places = new ArrayList<Place>();
    private List<Transition> transitions = new ArrayList<Transition>();


    public void addNewPlace(Point coords) {
        Place newPlace = new Place(coords);
        places.add(newPlace);
        setChanged();
        notifyObservers(new NotifyEvent<Place>(newPlace, EventType.PLACE_ADDED));
    }

    public void removePlace(Place place) {
        List<Arc> removedArcs = new ArrayList<Arc>();

        for (Arc arc : place.getInputArcs()) {
            arc.getTransition().getOutputArcs().remove(arc);
            removedArcs.add(arc);
        }
        for (Arc arc : place.getOutputArcs()) {
            arc.getTransition().getInputArcs().remove(arc);
            removedArcs.add(arc);
        }

        places.remove(place);

        setChangedAndNotifyObservers(new NotifyEvent<Place>(place, EventType.PLACE_REMOVED));
        for (Arc removedArc : removedArcs)
            setChangedAndNotifyObservers(new NotifyEvent<Arc>(removedArc, EventType.ARC_REMOVED));
    }

    public void addNewTransition(Point coords) {
        Transition newTransition = new Transition(coords);
        transitions.add(newTransition);
        setChangedAndNotifyObservers(new NotifyEvent<Transition>(newTransition, EventType.TRANSITION_ADDED));
    }

    public void removeTransition(Transition transition) {
        List<Arc> removedArcs = new ArrayList<Arc>();

        for (Arc arc : transition.getInputArcs()) {
            arc.getPlace().getOutputArcs().remove(arc);
            removedArcs.add(arc);
        }
        for (Arc arc : transition.getOutputArcs()) {
            arc.getPlace().getInputArcs().remove(arc);
            removedArcs.add(arc);
        }

        transitions.remove(transition);

        setChangedAndNotifyObservers(new NotifyEvent<Transition>(transition, EventType.TRANSITION_REMOVED));
        for (Arc removedArc : removedArcs)
            setChangedAndNotifyObservers(new NotifyEvent<Arc>(removedArc, EventType.ARC_REMOVED));
    }

    public void connectWithArc(Place fromPlace, Transition toTransition) {
        if (fromPlace.hasOutputArcToTransition(toTransition))
            return;
        Arc newArc = new Arc(fromPlace, toTransition);
        setChanged();
        notifyObservers(new NotifyEvent<Arc>(newArc, EventType.ARC_ADDED));
    }

    public void connectWithArc(Transition fromTransition, Place toPlace) {
        if (toPlace.hasInputArcFromTransition(fromTransition))
            return;
        Arc newArc = new Arc(fromTransition, toPlace);
        setChanged();
        notifyObservers(new NotifyEvent<Arc>(newArc, EventType.ARC_ADDED));
    }

    public void connectWithInhibitorArc(Place fromPlace, Transition toTransition) {
        new InhibitorArc(fromPlace, toTransition);
        //TODO: firePropertyChange();
    }

    public void removeArc(Arc arc) {
        if (arc.getArcDirection() == Arc.ArcDirection.PLACE_TO_TRASNSITION) {
            arc.getPlace().getOutputArcs().remove(arc);
            arc.getTransition().getInputArcs().remove(arc);
        } else {
            arc.getPlace().getInputArcs().remove(arc);
            arc.getTransition().getOutputArcs().remove(arc);
        }
        setChangedAndNotifyObservers(new NotifyEvent<Arc>(arc, EventType.ARC_REMOVED));
    }
}
