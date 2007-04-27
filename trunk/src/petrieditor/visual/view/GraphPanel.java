package petrieditor.visual.view;

import petrieditor.model.Arc;
import petrieditor.model.PetriNet;
import petrieditor.model.Place;
import petrieditor.model.Transition;
import petrieditor.model.event.NotifyEvent;
import petrieditor.model.event.PetriNetObject;
import petrieditor.model.viewinterfaces.PetriNetView;
import petrieditor.util.Observable;
import petrieditor.visual.Application;
import petrieditor.visual.mouselisteners.*;
import petrieditor.visual.renderer.DefaultRenderer;
import petrieditor.visual.renderer.GraphRenderer;
import petrieditor.visual.renderer.SimulationRenderer;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.text.MessageFormat;

/**
 * @author wiktor
 */
public class GraphPanel extends JLayeredPane implements PetriNetView {

    public static final String MOUSE_LISTENER_CHANGED_PROPERTY = "MouseListenerChanged";

    // Renderers
    public GraphRenderer defaultRenderer = new DefaultRenderer();
    public GraphRenderer simulationRenderer = new SimulationRenderer();
    public GraphRenderer currentRenderer = defaultRenderer;

    // MouseListners
    public TransitionInsertMouseStrategy transitionInsertMouseStrategy = new TransitionInsertMouseStrategy(this);
    public PlaceInsertMouseStrategy placeInsertMouseStrategy = new PlaceInsertMouseStrategy(this);
    public NormalArcInsertMouseStrategy normalArcInsertMouseStrategy = new NormalArcInsertMouseStrategy(this);
    public InhibitorArcInsertMouseStrategy inhibitorArcInsertMouseStrategy = new InhibitorArcInsertMouseStrategy(this);

    private MouseInputAdapter componentMouseListner = new ComponentMouseListner(this);

    public MouseStrategy currentMouseStrategy;
    private PetriNet model;

    public GraphPanel(PetriNet model) {
        this.model = model;
        this.model.addObserver(this);
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

    public void update(Observable<PetriNet, PetriNetView, NotifyEvent<PetriNetObject>> observable, NotifyEvent<PetriNetObject> event) {
        switch (event.getEventType()) {
            case PLACE_ADDED:
                _addNewPlace(event);
                break;
            case TRANSITION_ADDED:
                _addNewTransition(event);
                break;
            case ARC_ADDED:
                _addNewArc(event);
                break;
            case ARC_REMOVED:
                remove(_findComponentByModel(event.getObject().getPetriNetObject()));
                _setStatusText("Arc removed");
                break;
            case PLACE_REMOVED:
                remove(_findComponentByModel(event.getObject().getPetriNetObject()));
                _setStatusText("Place removed");
                break;
            case TRANSITION_REMOVED:
                remove(_findComponentByModel(event.getObject().getPetriNetObject()));
                _setStatusText("Transition removed");
                break;
        }
        updatePreferredSize();
    }

    private void _addNewArc(NotifyEvent<PetriNetObject> event) {
        Arc model = event.getObject().getArc();
        ArcComponent component = new ArcComponent(model, this);
        model.addObserver(component);
        _addNewComponent(component);
        _setStatusText(MessageFormat.format("Arc added ({0} - {1})", model.getPlace().getName(), model.getTransition().getName()));
    }

    private void _addNewTransition(NotifyEvent<PetriNetObject> event) {
        Transition model = event.getObject().getTransition();
        TransitionComponent component = new TransitionComponent(model, this);
        model.addObserver(component);
        _addNewComponent(component);
        _setStatusText(MessageFormat.format("Transition added ({0})", model.getName()));
    }

    private void _addNewPlace(NotifyEvent<PetriNetObject> event) {
        Place model = event.getObject().getPlace();
        PlaceComponent component = new PlaceComponent(model, this);
        model.addObserver(component);
        _addNewComponent(component);
        _setStatusText(MessageFormat.format("Place added ({0})", model.getName()));
    }

    private void _addNewComponent(PetriNetComponent component) {
        component.addMouseMotionListener(componentMouseListner);
        component.addMouseListener(componentMouseListner);
        add(component);
    }

    private void _setStatusText(String msg) {
        Application.getInstance().getMainFrame().setStatusText(msg);
    }

    private Component _findComponentByModel(Object o) {
        System.out.println("Searching for " + o);
        for (Component component : getComponents())
            if (component instanceof PetriNetComponent)
                if (((PetriNetComponent) component).getModel() == o)
                    return component;
        return null;
    }

    static class Strategy {

    }
}