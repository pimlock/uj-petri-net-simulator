package petrieditor.visual.mouselisteners;

import petrieditor.visual.view.GraphPanel;
import petrieditor.visual.view.PlaceComponent;
import petrieditor.visual.view.PlaceTransitionComponent;
import petrieditor.visual.view.TransitionComponent;
import petrieditor.model.Place;
import petrieditor.model.Transition;

import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;

/**
 * @author wiktor
 */
public abstract class ArcInsertMouseStrategy extends MouseStrategy {

    private PlaceTransitionComponent first;

    public ArcInsertMouseStrategy(GraphPanel graphPanel) {
        super(graphPanel);
    }

    protected abstract void connect(Place place, Transition transition);
    public abstract void connect(Transition transition, Place place);
    public abstract boolean isTransitionToPlacePossible();

    public void mouseClicked(MouseEvent e) {
        if (!(e.getComponent() instanceof PlaceTransitionComponent))
            return;

        PlaceTransitionComponent component = (PlaceTransitionComponent) e.getComponent();

        if (first == component) { // 1. Odznaczamy komponent, ktory wczesniej byl zaznaczony
            component.setSelected(false);
            first = null;
        } else if (first == null) { // 2. Pierwszy komponent, ktory zaznaczamy
            component.setSelected(true);
            first = component;
        } else { // 3. Drugi komponent, ktory zaznaczamy
            if (first instanceof PlaceComponent && component instanceof TransitionComponent) {
                connect(((PlaceComponent) first).getModel(), ((TransitionComponent) component).getModel());                
                first.setSelected(false);
                first.repaint();
                first = null;
            } else if (first instanceof TransitionComponent && component instanceof PlaceComponent && isTransitionToPlacePossible()) {
                connect(((TransitionComponent) first).getModel(), ((PlaceComponent) component).getModel());
                first.setSelected(false);
                first.repaint();
                first = null;
            } else { // 4. Zmieniamy zaznaczenie na akutalnie klikniety komponent tego samego typu
                first.setSelected(false);
                first.repaint();
                first = component;
                first.setSelected(true);
            }
        }

        component.repaint();
    }

    public void propertyChange(PropertyChangeEvent event) {
        if (first != null) {
            first.setSelected(false);
            first.repaint();
            first = null;
        }
    }
}
