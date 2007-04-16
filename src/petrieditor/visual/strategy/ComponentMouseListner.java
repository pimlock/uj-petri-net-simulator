package petrieditor.visual.strategy;

import petrieditor.visual.view.GraphPanel;
import petrieditor.visual.view.PetriNetComponent;
import petrieditor.visual.view.PlaceTransitionComponent;

import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Domyslna strategia dla obslugi myszki. Obsluguje przeciaganie komponentow oraz podswietlenie komponentu, nad
 * ktorym znajduje sie wzkaznik myszki.
 *
 * @author wiktor
 */
public class ComponentMouseListner extends MouseInputAdapter {

    private Point dragStart;
    private GraphPanel graphPanel;

    public ComponentMouseListner(GraphPanel graphPanel) {
        this.graphPanel = graphPanel;
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON1) {
            e.translatePoint(e.getComponent().getLocation().x, e.getComponent().getLocation().y);
            graphPanel.currentStrategy.mouseClicked(e);
        }
    }

    public void mousePressed(MouseEvent e) {
        dragStart = e.getPoint();
    }

    public void mouseDragged(MouseEvent e) {
        if (!(e.getSource() instanceof PlaceTransitionComponent))
            return;

        ((PlaceTransitionComponent) e.getSource()).translate(dragStart.x - e.getX(), dragStart.y - e.getY());
    }

    public void mouseEntered(MouseEvent e) {
        ((PetriNetComponent) e.getComponent()).setHover(true);
        e.getComponent().repaint();
    }

    public void mouseExited(MouseEvent e) {
        ((PetriNetComponent) e.getComponent()).setHover(false);
        e.getComponent().repaint();
    }
}
