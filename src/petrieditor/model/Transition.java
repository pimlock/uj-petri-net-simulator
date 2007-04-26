package petrieditor.model;

import petrieditor.model.event.NotifyEvent;
import petrieditor.model.event.EventType;
import petrieditor.model.viewinterfaces.TransitionView;
import petrieditor.util.Observable;

import java.util.List;
import java.util.ArrayList;
import java.awt.*;

/**
 * @author wiktor
 */
public class Transition extends Observable<Transition, TransitionView, NotifyEvent<Transition>> {

    protected List<Arc> inputArcs, outputArcs;
    protected Point coords;
    protected String name;

    public Transition(String name) {
        this.inputArcs = new ArrayList<Arc>();
        this.outputArcs = new ArrayList<Arc>();
        this.name = name;
    }

    public Transition(Point coords, String name) {
        this(name);
        this.coords = coords;
    }

    public void fire() {
        if (!isEnabled())
            throw new IllegalStateException("Cannot fire transition. Transition is not enabled!");

        for (Arc inputArc : inputArcs)
            inputArc.getPlace().decreaseMarking(inputArc.getWeight());

        for (Arc outputArc : outputArcs)
            outputArc.getPlace().increaseMarking(outputArc.getWeight());

        setChangedAndNotifyObservers(new NotifyEvent<Transition>(this, EventType.TRANSITION_FIRED));
    }

    public boolean isEnabled() {
        if (inputArcs.size() == 0 && outputArcs.size() == 0)
            return false;
        for (Arc inputArc : inputArcs)
            if (!inputArc.isEnabled())
                return false;        
        return true;
    }

    public void addInputArc(Arc newInputArc) {
        inputArcs.add(newInputArc);
        setChangedAndNotifyObservers();
    }

    public void addOutputArc(Arc newOutputArc) {
        outputArcs.add(newOutputArc);
        setChangedAndNotifyObservers();
    }

    public Point getCoords() {
        return coords;
    }

    public void setCoords(Point coords) {
        this.coords = coords;
        for (Arc inputArc : inputArcs)
            inputArc.setChangedAndNotifyObservers();
        for (Arc outputArc : outputArcs)
            outputArc.setChangedAndNotifyObservers();        
        setChangedAndNotifyObservers();
    }

    public List<Arc> getInputArcs() {
        return inputArcs;
    }

    public List<Arc> getOutputArcs() {
        return outputArcs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setChangedAndNotifyObservers();
    }
}