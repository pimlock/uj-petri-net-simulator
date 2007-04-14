package petrieditor.visual.view;

import petrieditor.model.PetriNet;
import petrieditor.model.Place;
import petrieditor.model.Transition;
import petrieditor.model.Arc;
import petrieditor.model.event.EventType;
import petrieditor.model.event.NotifyEvent;
import petrieditor.model.viewinterfaces.PetriNetView;
import petrieditor.util.Observable;
import petrieditor.visual.strategy.*;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

/**
 * @author wiktor
 */
public class GraphPanel extends JLayeredPane implements PetriNetView {

    //TODO: uporzadkowac strategie
    public TransitionInsertStrategy transitionInsertStrategy = new TransitionInsertStrategy(this);
    public PlaceInsertStrategy placeInsertStrategy = new PlaceInsertStrategy(this);
    public ArcInsertStrategy arcInsertStrategy = new ArcInsertStrategy(this);
    public MouseInputAdapter mia = new ComponentMouseListner(this);

    public Strategy currentStrategy;

    private PetriNet model = new PetriNet();

    public GraphPanel() {
        model.addObserver(this);
        currentStrategy = placeInsertStrategy;
        addMouseListener(currentStrategy);
        System.out.println(getMouseListeners().length);
    }

    public void changeStategy(Strategy newStrategy) {
        removeMouseListener(currentStrategy);
        currentStrategy = newStrategy;
        addMouseListener(currentStrategy);
    }

    public PetriNet getModel() {
        return model;
    }

    public void update(Observable<PetriNet, PetriNetView, NotifyEvent> observable, NotifyEvent event) {
        //TODO: dodac warstwy dla poszczegolnych obiektow
        if (event.getEventType() == EventType.PLACE_ADDED) {
            Place model = (Place) event.getObject();
            PlaceComponent component = new PlaceComponent(model);
            model.addObserver(component);
            component.addMouseMotionListener(mia);
            component.addMouseListener(mia);
            add(component);
        }

        if (event.getEventType() == EventType.TRANSITION_ADDED) {
            Transition model = (Transition) event.getObject();
            TransitionComponent component = new TransitionComponent(model);
            model.addObserver(component);
            component.addMouseMotionListener(mia);
            component.addMouseListener(mia);
            add(component);
        }

        System.out.println(event.getEventType());

        if (event.getEventType() == EventType.ARC_ADDED) {
            System.out.println("arc add");
            Arc model = (Arc) event.getObject();
            ArcComponent component = new ArcComponent(model);
            model.addObserver(component);
            component.addMouseListener(mia);
            component.addMouseMotionListener(mia);
            add(component);
        }

        revalidate();
        repaint();
    }
}
