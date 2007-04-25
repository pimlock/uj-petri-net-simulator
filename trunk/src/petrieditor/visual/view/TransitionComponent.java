package petrieditor.visual.view;

import petrieditor.model.Transition;
import petrieditor.model.event.NotifyEvent;
import petrieditor.model.viewinterfaces.TransitionView;
import petrieditor.util.Observable;
import petrieditor.visual.action.EditTransitionNameAction;
import petrieditor.visual.action.RemoveTransitionAction;

import javax.swing.*;
import java.awt.*;

/**
 * @author wiktor
 */
public class TransitionComponent extends PlaceTransitionComponent implements TransitionView {
    public static final int WIDTH = 31;
    public static final int HEIGHT = 11;
    private static final Rectangle BOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);

    private final Transition model;
    private final GraphPanel graphPanel;

    public TransitionComponent(final Transition model, GraphPanel graphPanel) {
        super(model.getName());
        this.model = model;
        this.graphPanel = graphPanel;
        setBounds(model.getCoords().x, model.getCoords().y, WIDTH, HEIGHT);
        setComponentPopupMenu(new TransitionComponentPopup());
    }

    protected void paintComponent(Graphics g) {
        graphPanel.currentRenderer.render((Graphics2D) g, this);
    }

    public boolean contains(int x, int y) {
        return BOUNDS.contains(x, y);
    }

    public void translate(int transX, int transY) {
        model.setCoords(new Point(model.getCoords().x - transX, model.getCoords().y - transY));
    }

    public void update(Observable<Transition, TransitionView, NotifyEvent> observable, NotifyEvent event) {
        setBounds(model.getCoords().x, model.getCoords().y, WIDTH, HEIGHT);
        label.setText(model.getName());
        ((GraphPanel) getParent()).updatePreferredSize();
        repaint();
    }

    public Transition getModel() {
        return model;
    }

    public Point getLabelPosition() {
        return new Point(getLocation().x + WIDTH + 5, getLocation().y + HEIGHT + 5);
    }

    private class TransitionComponentPopup extends JPopupMenu {

        public TransitionComponentPopup() {
            add(new JMenuItem(new EditTransitionNameAction(model)));
            add(new JMenuItem(new RemoveTransitionAction(model)));
        }

    }
}
