package petrieditor.visual.view;

import petrieditor.model.Place;
import petrieditor.model.event.NotifyEvent;
import petrieditor.model.viewinterfaces.PlaceView;
import petrieditor.util.Observable;
import petrieditor.visual.renderer.DefaultPlaceRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author wiktor
 */
public class PlaceComponent extends PlaceTransitionComponent implements PlaceView {

    private static final int PLACE_COMPONENT_SIZE = 21;
    private static final Rectangle BOUNDS = new Rectangle(0, 0, PLACE_COMPONENT_SIZE, PLACE_COMPONENT_SIZE);

    private final Place model;

    public PlaceComponent(final Place model) {
        this.model = model;
        setBounds(model.getCoords().x, model.getCoords().y, PLACE_COMPONENT_SIZE, PLACE_COMPONENT_SIZE);
        setComponentPopupMenu(new PlaceComponentPopup());
    }

    //TODO:renderer
    private DefaultPlaceRenderer renderer = new DefaultPlaceRenderer();

    public Place getModel() {
        return model;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isSelected())
            g.setColor(Color.BLUE);
        else if (isHover())
            g.setColor(Color.RED);
        else
            g.setColor(Color.BLACK);
        renderer.render(g, this);
    }

    public boolean contains(int x, int y) {
        return BOUNDS.contains(x, y);
    }

    public void translate(int transX, int transY) {
        model.setCoords(new Point(model.getCoords().x - transX, model.getCoords().y - transY));
    }

    public void update(Observable<Place, PlaceView, NotifyEvent> observable, NotifyEvent event) {
        //TODO:
        setBounds(model.getCoords().x, model.getCoords().y, PLACE_COMPONENT_SIZE, PLACE_COMPONENT_SIZE);
    }

    private class PlaceComponentPopup extends JPopupMenu {

        public PlaceComponentPopup() {
            JMenuItem item = new JMenuItem(new AbstractAction() {
                public void actionPerformed(ActionEvent ae) {
                    ((GraphPanel) PlaceComponent.this.getParent()).getModel().removePlace(model);
                }
            });
            item.setText("Remove");
            add(item);
        }
        
    }

}
