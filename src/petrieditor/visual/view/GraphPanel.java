package petrieditor.visual.view;

import petrieditor.model.Arc;
import petrieditor.model.PetriNet;
import petrieditor.model.Place;
import petrieditor.model.Transition;
import static petrieditor.model.event.EventType.*;
import petrieditor.model.event.NotifyEvent;
import petrieditor.model.viewinterfaces.PetriNetView;
import petrieditor.util.Observable;
import petrieditor.visual.mouselisteners.*;
import petrieditor.visual.renderer.GraphRenderer;
import petrieditor.visual.renderer.SimulationRenderer;
import petrieditor.visual.renderer.DefaultRenderer;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;

/**
 * @author wiktor
 */
public class GraphPanel extends JLayeredPane implements PetriNetView {

    public static final String MOUSE_LISTENER_CHANGED_PROPERTY = "MouseListenerChanged";

    //TODO: renderer
    public GraphRenderer defaultRenderer = new DefaultRenderer();
    public GraphRenderer simulationRenderer = new SimulationRenderer();
    public GraphRenderer currentRenderer = defaultRenderer;

    //TODO: uporzadkowac strategie
    public TransitionInsertMouseStrategy transitionInsertMouseStrategy = new TransitionInsertMouseStrategy(this);
    public PlaceInsertMouseStrategy placeInsertMouseStrategy = new PlaceInsertMouseStrategy(this);
    public NormalArcInsertMouseStrategy normalArcInsertMouseStrategy = new NormalArcInsertMouseStrategy(this);
    public InhibitorArcInsertMouseStrategy inhibitorArcInsertMouseStrategy = new InhibitorArcInsertMouseStrategy(this);

    private MouseInputAdapter mia = new ComponentMouseListner(this);

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

    public void paint(Graphics g) {
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paint(g);
    }


    public void updatePreferredSize() {
        Dimension dim = new Dimension(0, 0);

        Rectangle bounds;
        for (Component component : getComponents()) {
            bounds = component.getBounds();
            if ((bounds.x + bounds.width) > dim.width) dim.width = bounds.x + bounds.width;
            if ((bounds.y + bounds.height) > dim.height) dim.height = bounds.y + bounds.height;
        }

        setPreferredSize(dim);
        revalidate();
    }

    public void update(Observable<PetriNet, PetriNetView, NotifyEvent> observable, NotifyEvent event) {
        //TODO: dodac warstwy dla poszczegolnych obiektow
        //TODO: zrefaktoryzwoac kod (za duzo if'ow)
        System.out.println(event.getEventType());

        if (event.getEventType() == PLACE_ADDED) {
            Place model = (Place) event.getObject();
            PlaceComponent component = new PlaceComponent(model, this);
            model.addObserver(component);
            component.addMouseMotionListener(mia);
            component.addMouseListener(mia);
            add(component);
        }

        if (event.getEventType() == TRANSITION_ADDED) {
            Transition model = (Transition) event.getObject();
            TransitionComponent component = new TransitionComponent(model, this);
            model.addObserver(component);
            component.addMouseMotionListener(mia);
            component.addMouseListener(mia);
            add(component);
        }

        if (event.getEventType() == ARC_ADDED) {
            Arc model = (Arc) event.getObject();
            ArcComponent component = new ArcComponent(model, this);
            model.addObserver(component);
            component.addMouseListener(mia);
            component.addMouseMotionListener(mia);
            add(component);
        }

        if (event.getEventType() == PLACE_REMOVED || event.getEventType() == ARC_REMOVED || event.getEventType() == TRANSITION_REMOVED) {
            remove(findComponentByModel(event.getObject()));
        }

        updatePreferredSize();
    }

    private Component findComponentByModel(Object o) {
        System.out.println("Searching for " + o);
        for (Component component : getComponents())
            if (component instanceof PetriNetComponent)
                if (((PetriNetComponent) component).getModel() == o)
                    return component;
        System.out.println("NOT FOUND!!!!");
        return null;
    }
}
