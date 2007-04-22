package petrieditor.visual.action;

import petrieditor.visual.Application;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.*;

/**
 * @author wiktor
 */
public class AddTransitionAction extends AbstractAction {

    private final Point coords;

    public AddTransitionAction(Point coords) {
        this.coords = coords;
    }

    public void actionPerformed(ActionEvent e) {
        Application.getInstance().getCurrentGraphPanel().getModel().addNewTransition(coords);
    }
}
