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

    public void addNewTransition(Point coords) {
        Transition newTransition = new Transition(coords);
        transitions.add(newTransition);
        setChanged();
        notifyObservers(new NotifyEvent<Transition>(newTransition, EventType.TRANSITION_ADDED));
    }

    public void connectWithArc(Place fromPlace, Transition toTransition) {
        Arc newArc = new Arc(fromPlace, toTransition);
        setChanged();
        notifyObservers(new NotifyEvent<Arc>(newArc, EventType.ARC_ADDED));
    }

    public void connectWithArc(Transition fromTransition, Place toPlace) {
        new Arc(fromTransition, toPlace);
        //TODO: firePropertyChange();
    }

    public void connectWithInhibitorArc(Place fromPlace, Transition toTransition) {
        new InhibitorArc(fromPlace, toTransition);
        //TODO: firePropertyChange();
    }

}
