package petrieditor.visual.view;

import petrieditor.model.PetriNet;
import petrieditor.model.Place;
import petrieditor.model.Transition;
import petrieditor.model.Arc;
import petrieditor.model.event.EventType;
import petrieditor.model.event.NotifyEvent;
import petrieditor.model.viewinterfaces.PetriNetView;
import petrieditor.util.Observable;
import petrieditor.visual.mouselisteners.*;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;

/**
 * @author wiktor
 */
public class GraphPanel extends JLayeredPane implements PetriNetView {

    public static final String MOUSE_LISTENER_CHANGED_PROPERTY = "MouseListenerChanged";

    //TODO: uporzadkowac strategie
    public TransitionInsertMouseStrategy transitionInsertMouseStrategy = new TransitionInsertMouseStrategy(this);
    public PlaceInsertMouseStrategy placeInsertMouseStrategy = new PlaceInsertMouseStrategy(this);
    public ArcInsertMouseStrategy arcInsertMouseStrategy = new ArcInsertMouseStrategy(this);

    public MouseInputAdapter mia = new ComponentMouseListner(this);

    public MouseStrategy currentMouseStrategy;
    private PetriNet model = new PetriNet();

    public GraphPanel() {
        model.addObserver(this);
        currentMouseStrategy = placeInsertMouseStrategy;
        addMouseListener(currentMouseStrategy);
        addPropertyChangeListener(MOUSE_LISTENER_CHANGED_PROPERTY, currentMouseStrategy);
    }

    public void changeStategy(MouseStrategy newMouseStrategy) {
        firePropertyChange(MOUSE_LISTENER_CHANGED_PROPERTY, currentMouseStrategy, newMouseStrategy);
        removeMouseListener(currentMouseStrategy);
        removePropertyChangeListener(MOUSE_LISTENER_CHANGED_PROPERTY, currentMouseStrategy);
        currentMouseStrategy = newMouseStrategy;
        addMouseListener(currentMouseStrategy);
        addPropertyChangeListener(MOUSE_LISTENER_CHANGED_PROPERTY, currentMouseStrategy);
    }

    public PetriNet getModel() {
        return model;
    }

    public void update(Observable<PetriNet, PetriNetView, NotifyEvent> observable, NotifyEvent event) {
        //TODO: dodac warstwy dla poszczegolnych obiektow
        //TODO: zrefaktoryzwoac kod (za duzo if'ow)
        System.out.println(event.getEventType());

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

        if (event.getEventType() == EventType.ARC_ADDED) {
            System.out.println("arc add");
            Arc model = (Arc) event.getObject();
            ArcComponent component = new ArcComponent(model);
            model.addObserver(component);
            component.addMouseListener(mia);
            component.addMouseMotionListener(mia);
            add(component);
        }

        if (event.getEventType() == EventType.PLACE_REMOVED || event.getEventType() == EventType.ARC_REMOVED) {
            remove(findComponentByModel(event.getObject()));
        }

        revalidate();
        repaint();
    }

    private Component findComponentByModel(Object o) {
        for (Component component : getComponents()) 
            if (((PetriNetComponent) component).getModel() == o)
                return component;
        return null;
    }
}
