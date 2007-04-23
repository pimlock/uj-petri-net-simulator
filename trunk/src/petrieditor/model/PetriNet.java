package petrieditor.model;

import petrieditor.model.event.EventType;
import petrieditor.model.event.NotifyEvent;
import petrieditor.model.viewinterfaces.PetriNetView;
import petrieditor.util.Observable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Glowna klasa modelu. Tutaj znajduja sie wszystkie informacje o sieci.
 *
 * @author wiktor
 */

public class PetriNet extends Observable<PetriNet, PetriNetView, NotifyEvent> {

    private List<Place> places = new ArrayList<Place>();
    private List<Transition> transitions = new ArrayList<Transition>();
    private List<Arc> arcs = new ArrayList<Arc>();
    private int nextPlaceNumber = 0, nextTransitionNumber = 0;

    public void addNewPlace(Point coords) {
        Place newPlace = new Place(coords, "P" + nextPlaceNumber++);
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
        for (Arc removedArc : removedArcs)
            setChangedAndNotifyObservers(new NotifyEvent<Arc>(removedArc, EventType.ARC_REMOVED));

        arcs.removeAll(removedArcs);

        places.remove(place);
        setChangedAndNotifyObservers(new NotifyEvent<Place>(place, EventType.PLACE_REMOVED));
    }

    public void addNewTransition(Point coords) {
        Transition newTransition = new Transition(coords, "T" + nextTransitionNumber++);
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
        for (Arc removedArc : removedArcs)
            setChangedAndNotifyObservers(new NotifyEvent<Arc>(removedArc, EventType.ARC_REMOVED));

        arcs.removeAll(removedArcs);

        transitions.remove(transition);
        setChangedAndNotifyObservers(new NotifyEvent<Transition>(transition, EventType.TRANSITION_REMOVED));
    }

    public void connectWithArc(Place fromPlace, Transition toTransition) {
        if (fromPlace.hasOutputArcToTransition(toTransition))
            return;
        Arc newArc = new Arc(fromPlace, toTransition);
        arcs.add(newArc);
        setChangedAndNotifyObservers(new NotifyEvent<Arc>(newArc, EventType.ARC_ADDED));
    }

    public void connectWithArc(Transition fromTransition, Place toPlace) {
        if (toPlace.hasInputArcFromTransition(fromTransition))
            return;
        Arc newArc = new Arc(fromTransition, toPlace);
        arcs.add(newArc);
        setChangedAndNotifyObservers(new NotifyEvent<Arc>(newArc, EventType.ARC_ADDED));
    }

    public void connectWithInhibitorArc(Place fromPlace, Transition toTransition) {
        if (fromPlace.hasOutputArcToTransition(toTransition))
            return;
        Arc newArc = new InhibitorArc(fromPlace, toTransition);
        arcs.add(newArc);
        setChangedAndNotifyObservers(new NotifyEvent<Arc>(newArc, EventType.ARC_ADDED));
    }

    public void removeArc(Arc arc) {
        arcs.remove(arc);
        if (arc.getArcDirection() == Arc.ArcDirection.PLACE_TO_TRASNSITION) {
            arc.getPlace().getOutputArcs().remove(arc);
            arc.getTransition().getInputArcs().remove(arc);
        } else {
            arc.getPlace().getInputArcs().remove(arc);
            arc.getTransition().getOutputArcs().remove(arc);
        }
        setChangedAndNotifyObservers(new NotifyEvent<Arc>(arc, EventType.ARC_REMOVED));
    }

    public void loadPetriNet(PetriNet newModel) {
        clear();

        this.places = newModel.places;
        this.transitions = newModel.transitions;
        this.arcs = newModel.arcs;

        for (Place place : places)
            setChangedAndNotifyObservers(new NotifyEvent<Place>(place, EventType.PLACE_ADDED));
        for (Transition transition : transitions)
            setChangedAndNotifyObservers(new NotifyEvent<Transition>(transition, EventType.TRANSITION_ADDED));
        for (Arc arc : arcs)
            setChangedAndNotifyObservers(new NotifyEvent<Arc>(arc, EventType.ARC_ADDED));
    }

    public void clear() {
        for (Place place : places)
            setChangedAndNotifyObservers(new NotifyEvent<Place>(place, EventType.PLACE_REMOVED));
        for (Transition transition : transitions)
            setChangedAndNotifyObservers(new NotifyEvent<Transition>(transition, EventType.TRANSITION_REMOVED));
        for (Arc arc : arcs)
            setChangedAndNotifyObservers(new NotifyEvent<Arc>(arc, EventType.ARC_REMOVED));

        places = new ArrayList<Place>();
        transitions = new ArrayList<Transition>();
        arcs = new ArrayList<Arc>();
    }
}