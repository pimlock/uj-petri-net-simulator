package petrieditor.visual.action;

import petrieditor.model.Arc;
import petrieditor.visual.Application;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author wiktor
 */
public class RemoveArcAction extends AbstractAction {

    final private Arc model;

    public RemoveArcAction(Arc model) {
        super("Remove");
        this.model = model;
    }

    public void actionPerformed(ActionEvent ae) {
        Application.getInstance().getCurrentGraphPanel().getModel().removeArc(model);
    }
}
