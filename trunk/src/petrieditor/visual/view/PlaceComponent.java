package petrieditor.visual.view;

import petrieditor.model.Place;
import petrieditor.model.event.NotifyEvent;
import petrieditor.model.viewinterfaces.PlaceView;
import petrieditor.util.Observable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author wiktor
 */
public class PlaceComponent extends PlaceTransitionComponent implements PlaceView {
    private final static int PLACE_RADIUS = 20;

    private static final int PLACE_COMPONENT_SIZE = 21;
    private static final Rectangle BOUNDS = new Rectangle(0, 0, PLACE_COMPONENT_SIZE, PLACE_COMPONENT_SIZE);

    private final Place model;

    public PlaceComponent(final Place model) {
        this.model = model;
        setBounds(model.getCoords().x, model.getCoords().y, PLACE_COMPONENT_SIZE, PLACE_COMPONENT_SIZE);
        setComponentPopupMenu(new PlaceComponentPopup());
    }

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
        g.drawRect(0, 0, PLACE_RADIUS, PLACE_RADIUS);
        g.drawString(String.valueOf(model.getCurrentMarking()), 10, 10);
    }

    public boolean contains(int x, int y) {
        return BOUNDS.contains(x, y);
    }

    public void translate(int transX, int transY) {
        model.setCoords(new Point(model.getCoords().x - transX, model.getCoords().y - transY));
    }

    public void update(Observable<Place, PlaceView, NotifyEvent> observable, NotifyEvent event) {
        setBounds(model.getCoords().x, model.getCoords().y, PLACE_COMPONENT_SIZE, PLACE_COMPONENT_SIZE);
        repaint();
    }

    private class PlaceComponentPopup extends JPopupMenu {

        public PlaceComponentPopup() {
            JMenuItem removeItem = new JMenuItem(new AbstractAction() {
                public void actionPerformed(ActionEvent ae) {
                    ((GraphPanel) PlaceComponent.this.getParent()).getModel().removePlace(model);
                }
            });
            removeItem.setText("Remove");

            JMenuItem editTokens = new JMenuItem(new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    String input = JOptionPane.showInputDialog("Number of tokens:", String.valueOf(model.getCurrentMarking()));
                    model.setCurrentMarking(Integer.parseInt(input));                       
                }
            });
            editTokens.setText("Edit tokens");


            add(removeItem);
            add(editTokens);
        }

    }

}
