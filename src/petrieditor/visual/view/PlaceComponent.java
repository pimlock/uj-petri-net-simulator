package petrieditor.visual.view;

import petrieditor.model.Place;
import petrieditor.model.event.NotifyEvent;
import petrieditor.model.viewinterfaces.PlaceView;
import petrieditor.util.Observable;
import petrieditor.visual.action.EditPlaceNameAction;
import petrieditor.visual.action.EditTokensAction;
import petrieditor.visual.action.RemovePlaceAction;

import javax.swing.*;
import java.awt.*;

/**
 * @author wiktor
 */
public class PlaceComponent extends PlaceTransitionComponent implements PlaceView {
    public static final int PLACE_DIAMETER = 25;
    public static final double PLACE_RADIUS = PLACE_DIAMETER / 2.0d;

    private static final Color HOVER_COLOR = new Color(0, 0, 255);
    private static final Color SELECTED_COLOR = new Color(50, 180, 13);
    private static final int PLACE_COMPONENT_SIZE = PLACE_DIAMETER + 2;
    private static final Rectangle BOUNDS = new Rectangle(0, 0, PLACE_COMPONENT_SIZE, PLACE_COMPONENT_SIZE);

    private final Place model;

    public PlaceComponent(final Place model) {
        super(model.getName());
        this.model = model;
        setBounds(model.getCoords().x, model.getCoords().y, PLACE_COMPONENT_SIZE, PLACE_COMPONENT_SIZE);
        setComponentPopupMenu(new PlaceComponentPopup());
    }

    public Place getModel() {
        return model;
    }

    public Point getLabelPosition() {
        return new Point(getLocation().x + PLACE_DIAMETER, getLocation().y + PLACE_DIAMETER);
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.fillOval(0, 0, PLACE_DIAMETER, PLACE_DIAMETER);
        g2d.setColor(isSelected() ? SELECTED_COLOR : isHover() ? HOVER_COLOR : Color.BLACK);
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawOval(0, 0, PLACE_DIAMETER, PLACE_DIAMETER);

        //TODO: mozna tutaj posprzatac
        int dx = 9, dy = 17;
        if (model.getCurrentMarking() > 9) {
            dx = 6;
            dy = 17;
        }

        g2d.drawString(String.valueOf(model.getCurrentMarking()), dx, dy);
    }

    public boolean contains(int x, int y) {
        return BOUNDS.contains(x, y);
    }

    public void translate(int transX, int transY) {
        model.setCoords(new Point(model.getCoords().x - transX, model.getCoords().y - transY));
    }

    public void update(Observable<Place, PlaceView, NotifyEvent> observable, NotifyEvent event) {
        setBounds(model.getCoords().x, model.getCoords().y, PLACE_COMPONENT_SIZE, PLACE_COMPONENT_SIZE);
        label.setText(model.getName());
        ((GraphPanel) getParent()).updatePreferredSize();
    }

    private class PlaceComponentPopup extends JPopupMenu {

        public PlaceComponentPopup() {
            add(new JMenuItem(new EditTokensAction(model)));
            add(new JMenuItem(new EditPlaceNameAction(model)));
            add(new JMenuItem(new RemovePlaceAction(model)));
        }

    }

}
