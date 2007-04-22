package petrieditor.visual.view;

import petrieditor.model.Arc;
import petrieditor.model.event.NotifyEvent;
import petrieditor.model.viewinterfaces.ArcView;
import petrieditor.util.Observable;
import petrieditor.visual.action.EditWeightAction;
import petrieditor.visual.action.RemoveArcAction;
import petrieditor.visual.util.ArrowHead;
import petrieditor.visual.util.InhibitorArrowHead;
import static petrieditor.visual.view.PlaceComponent.PLACE_RADIUS;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import static java.lang.Math.*;

/**
 * @author wiktor
 */
public class ArcComponent extends PetriNetComponent implements ArcView {

    protected static final Color ARROW_COLOR = new Color(119, 119, 119);

    protected Arc model;
    protected Rectangle bounds;
    protected ArrowHead arrowHead;

    public ArcComponent(Arc model) {
        super(String.valueOf(model.getWeight()));
        this.model = model;
        this.arrowHead = model.getWeight() == 0 ? new InhibitorArrowHead() : new ArrowHead();
        updateBounds();
        setComponentPopupMenu(new ArcComponentPopup());
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(isHover() ? Color.RED : ARROW_COLOR);
        g2d.translate(-getLocation().x, -getLocation().y);
        g2d.draw(arrowHead);
        if (model.getWeight() != 0)
            g2d.fill(arrowHead);
        g2d.translate(getLocation().x, getLocation().y);
    }

    public void update(Observable<Arc, ArcView, NotifyEvent> observable, NotifyEvent event) {
        label.setText(String.valueOf(model.getWeight()));
        label.setLocation(getLabelPosition());
        updateBounds();
        revalidate();
    }

    public boolean contains(int x, int y) {
        return arrowHead.intersects(x + bounds.getX() - 3, y + bounds.getY() - 3, 6, 6);
    }

    public Arc getModel() {
        return model;
    }

    public Point getLabelPosition() {
        return new Point(getLocation().x + bounds.width / 2, getLocation().y + bounds.height / 2);
    }

    private void updateBounds() {
        if (model.getArcDirection() == Arc.ArcDirection.PLACE_TO_TRASNSITION)
            _setPlaceToTransitionArrowLocation();
        else
            _setTransitionToPlaceArrowLocation();

        bounds = arrowHead.getBounds();
        bounds.grow(5, 5);
        setBounds(bounds);
    }


    //TODO: uporzadkowac tworzenie nowych obietkow
    private void _setPlaceToTransitionArrowLocation() {
        Point pTrans = new Point(model.getTransition().getCoords()), pPlace = new Point(model.getPlace().getCoords());
        // centrowanie punktow        
        pTrans.x += TransitionComponent.WIDTH / 2;
        pTrans.y += TransitionComponent.HEIGHT / 2;
        pPlace.x += PLACE_RADIUS;
        pPlace.y += PLACE_RADIUS;

        // wyliczam transY
        double transY = 0;
        if (pPlace.y != pTrans.y)
            transY = (TransitionComponent.HEIGHT / 2) * signum((pPlace.y - pTrans.y) / 2);

        // wyliczam transX
        double transX = 0;
        if (pPlace.x != pTrans.x)
            if (abs(pPlace.x - pPlace.x) <= TransitionComponent.WIDTH / 2) {
                transX = ((pPlace.getX() - pTrans.getX()) / abs(pPlace.getY() - pTrans.getY())) * abs(transY);
            } else {
                transX = (abs(pPlace.getY() - pTrans.getY()) / (pPlace.getX() - pTrans.getX())) * abs(transY);
            }

        if (abs(pPlace.y - pTrans.y) < TransitionComponent.HEIGHT / 2)
            transX = signum(pPlace.x - pTrans.x) * TransitionComponent.WIDTH / 2;
        
        if (abs(transX) > TransitionComponent.WIDTH / 2)
            transX = signum(transX) * TransitionComponent.WIDTH / 2;

        Point2D.Double transLast = new Point2D.Double(pTrans.getX() + transX, pTrans.getY() + transY);
        Point2D.Double placeFirst = new Point2D.Double(pPlace.getX(), pPlace.getY());
        arrowHead.setLocation(placeFirst, transLast);
    }

    private void _setTransitionToPlaceArrowLocation() {
        Point pTrans = new Point(model.getTransition().getCoords()), pPlace = model.getPlace().getCoords();
        pTrans.x += TransitionComponent.WIDTH / 2;
        pTrans.y += TransitionComponent.HEIGHT / 2;

        int dx = abs(pTrans.x - pPlace.x), dy = abs(pTrans.y - pPlace.y);
        double d = PLACE_RADIUS / hypot(dx, dy);

        if (pPlace.x > pTrans.x) dx = -(dx + 1);
        if (pPlace.y > pTrans.y) dy = -(dy + 1);

        Point2D.Double transFirst = new Point2D.Double(pTrans.getX(), pTrans.getY());
        Point2D.Double placeLast = new Point2D.Double(pPlace.getX() + PLACE_RADIUS + d * dx, pPlace.getY() + PLACE_RADIUS + d * dy);

        arrowHead.setLocation(transFirst, placeLast);
    }

    private class ArcComponentPopup extends JPopupMenu {

        public ArcComponentPopup() {
            add(new JMenuItem(new EditWeightAction(model)));
            add(new JMenuItem(new RemoveArcAction(model)));
        }

    }
}