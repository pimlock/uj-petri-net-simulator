package petrieditor.visual.view;

import petrieditor.model.Transition;
import petrieditor.model.event.NotifyEvent;
import petrieditor.model.viewinterfaces.TransitionView;
import petrieditor.util.Observable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author wiktor
 */
public class TransitionComponent extends PlaceTransitionComponent implements TransitionView {

    private static final int WIDTH = 40;
    private static final int HEIGHT = 20;
    private static final Rectangle BOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);

    private final Transition model;

    public TransitionComponent(final Transition model) {
        this.model = model;
        setBounds(model.getCoords().x, model.getCoords().y, WIDTH, HEIGHT);
        setComponentPopupMenu(new TransitionComponentPopup());
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(isSelected() ? Color.BLUE : isHover() ? Color.RED : Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);
    }

    public boolean contains(int x, int y) {
        return BOUNDS.contains(x, y);
    }

    public void translate(int transX, int transY) {
        model.setCoords(new Point(model.getCoords().x - transX, model.getCoords().y - transY));
    }

    public void update(Observable<Transition, TransitionView, NotifyEvent> observable, NotifyEvent event) {
        //TODO:
        setBounds(model.getCoords().x, model.getCoords().y, WIDTH, HEIGHT);
    }

    public Transition getModel() {
        return model;
    }

    private class TransitionComponentPopup extends JPopupMenu {

        public TransitionComponentPopup() {
            JMenuItem item = new JMenuItem(new AbstractAction() {
                public void actionPerformed(ActionEvent ae) {
                    ((GraphPanel) TransitionComponent.this.getParent()).getModel().removeTransition(model);
                }
            });
            item.setText("Remove");
            add(item);
        }

    }
}
