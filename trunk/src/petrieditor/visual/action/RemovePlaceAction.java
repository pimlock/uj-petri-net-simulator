package petrieditor.visual.action;

import petrieditor.model.Place;
import petrieditor.visual.Application;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author wiktor
 */
public class RemovePlaceAction extends AbstractAction {
    private final Place model;

    public RemovePlaceAction(final Place model) {
        super("Remove");
        this.model = model;
    }

    public void actionPerformed(ActionEvent e) {
        Application.getInstance().getCurrentGraphPanel().getModel().removePlace(model);
    }
}
