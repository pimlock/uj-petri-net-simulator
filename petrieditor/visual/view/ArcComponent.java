package petrieditor.visual.view;

import static java.lang.Math.abs;
import static java.lang.Math.min;

import petrieditor.model.viewinterfaces.ArcView;
import petrieditor.model.Arc;
import petrieditor.model.event.NotifyEvent;
import petrieditor.util.Observable;

import javax.swing.*;
import java.awt.*;

/**
 * @author wiktor
 */
public class ArcComponent extends PetriNetComponent implements ArcView {

    private Arc model;
    Rectangle rect, bounds;
    
    public ArcComponent(Arc model) {

        this.model = model;
        Point p1 = model.getTransition().getCoords();
        Point p2 = model.getPlace().getCoords();
        //TODO: do poprawki
        rect = new Rectangle(min(p1.x, p2.x), min(p1.y, p2.y), abs(p1.x - p2.x), abs(p1.y - p2.y));
        bounds = new Rectangle(0, 0, rect.width, rect.height);
        setBounds(rect);
    }

    protected void paintComponent(Graphics g) {
        System.out.println("rysuje");
        g.setColor(isHover() ? Color.RED : Color.BLACK);
        g.translate(-getLocation().x, -getLocation().y);
        g.drawLine(model.getTransition().getCoords().x, model.getTransition().getCoords().y,
                   model.getPlace().getCoords().x, model.getPlace().getCoords().y);
        g.translate(getLocation().x, getLocation().y);
    }

    public void update(Observable<Arc, ArcView, NotifyEvent> observable, NotifyEvent event) {
        Point p1 = model.getTransition().getCoords();
        Point p2 = model.getPlace().getCoords();
        rect = new Rectangle(min(p1.x, p2.x), min(p1.y, p2.y), abs(p1.x - p2.x), abs(p1.y - p2.y));
        bounds = new Rectangle(0, 0, rect.width, rect.height);
        setBounds(rect);
    }

    public boolean contains(int x, int y) {
        return bounds.contains(x, y);
    }
}
