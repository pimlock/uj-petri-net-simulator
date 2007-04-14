package petrieditor.model;

import petrieditor.model.event.NotifyEvent;
import petrieditor.model.viewinterfaces.PlaceView;
import petrieditor.util.Observable;

import java.util.List;
import java.util.ArrayList;
import java.awt.*;

/**
 * @author wiktor
 */
public class Place extends Observable<Place, PlaceView, NotifyEvent> {

    protected List<Arc> inputArcs, outputArcs;
    protected int initialMarking, currentMarking;
    protected Point coords;

    public Place() {
        this.inputArcs = new ArrayList<Arc>();
        this.outputArcs = new ArrayList<Arc>();
        this.initialMarking = 0;
        this.currentMarking = 0;
    }

    public Place(Point coords) {
        this();
        this.coords = coords;
    }

    public void reset() {
        currentMarking = initialMarking;
    }

    public void decreaseMarking(int dec) {
        currentMarking -= dec;
        setChangedAndNotifyObservers();
    }

    public void increaseMarking(int inc) {
        currentMarking += inc;
        setChangedAndNotifyObservers();
    }

    public int getInitialMarking() {
        return initialMarking;
    }

    public void setInitialMarking(int initialMarking) {
        this.initialMarking = initialMarking;
        this.currentMarking = initialMarking;
        setChangedAndNotifyObservers();
    }

    public int getCurrentMarking() {
        return currentMarking;
    }

    public void setCurrentMarking(int currentMarking) {
        this.currentMarking = currentMarking;
        setChangedAndNotifyObservers();
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
}
