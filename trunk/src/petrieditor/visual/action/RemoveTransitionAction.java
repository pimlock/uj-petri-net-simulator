package petrieditor.visual.action;

import petrieditor.model.Transition;
import petrieditor.visual.Application;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author wiktor
 */
public class RemoveTransitionAction extends AbstractAction {
    final private Transition model;

    public RemoveTransitionAction(final Transition model) {
        super("Remove");
        this.model = model;
    }

    public void actionPerformed(ActionEvent ae) {
        Application.getInstance().getCurrentGraphPanel().getModel().removeTransition(model);
    }
}
