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
    private static final Color HOVER_COLOR = new Color(0, 0, 255);
    private static final Color SELECTED_COLOR = new Color(50, 180, 13);
    private static final Rectangle BOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);

    private final Transition model;

    public TransitionComponent(final Transition model) {
        super(model.getName());
        this.model = model;
        setBounds(model.getCoords().x, model.getCoords().y, WIDTH, HEIGHT);
        setComponentPopupMenu(new TransitionComponentPopup());
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawString(model.getName(), -10, (int) (1.5 * HEIGHT));
        g.setColor(isSelected() ? SELECTED_COLOR : isHover() ? HOVER_COLOR : Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);
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
