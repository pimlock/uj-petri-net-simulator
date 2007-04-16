package petrieditor.visual.view;

import petrieditor.model.Arc;
import petrieditor.model.event.NotifyEvent;
import petrieditor.model.viewinterfaces.ArcView;
import petrieditor.util.Observable;
import petrieditor.visual.util.Arrow2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author wiktor
 */
public class ArcComponent extends PetriNetComponent implements ArcView {

    private Arc model;
    private Rectangle bounds;
    private Arrow2D arrow2D = new Arrow2D();

    public ArcComponent(Arc model) {
        this.model = model;
        setComponentPopupMenu(new ArcComponentPopup());
        updateBounds();
    }

    protected void paintComponent(Graphics g) {
        g.setColor(isHover() ? Color.RED : Color.BLACK);
        g.translate(-getLocation().x, -getLocation().y);
        ((Graphics2D) g).draw(arrow2D);
        g.translate(getLocation().x, getLocation().y);
    }

    public void update(Observable<Arc, ArcView, NotifyEvent> observable, NotifyEvent event) {
        updateBounds();
    }

    public boolean contains(int x, int y) {
        return arrow2D.contains(x + bounds.getX(), y + bounds.getY());
    }

    public Arc getModel() {
        return model;
    }

    private void updateBounds() {
        if (model.getArcDirection() == Arc.ArcDirection.PLACE_TO_TRASNSITION)
            arrow2D.setPoints(model.getPlace().getCoords(), model.getTransition().getCoords());
        else
            arrow2D.setPoints(model.getTransition().getCoords(), model.getPlace().getCoords());
        bounds = arrow2D.getBounds();
        bounds.grow(5, 5);
        setBounds(bounds);
    }

    private class ArcComponentPopup extends JPopupMenu {

        public ArcComponentPopup() {
            JMenuItem item = new JMenuItem(new AbstractAction() {
                public void actionPerformed(ActionEvent ae) {
                    ((GraphPanel) ArcComponent.this.getParent()).getModel().removeArc(model);
                }
            });
            item.setText("Remove");
            add(item);
        }

    }
}
