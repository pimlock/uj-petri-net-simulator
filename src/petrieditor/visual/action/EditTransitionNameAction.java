package petrieditor.visual.action;

import petrieditor.model.Transition;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author wiktor
 */
public class EditTransitionNameAction extends AbstractAction {
    private final Transition model;

    public EditTransitionNameAction(final Transition model) {
        super("Edit name");
        this.model = model;
    }

    public void actionPerformed(ActionEvent ae) {
        //TODO:
        String input = JOptionPane.showInputDialog("Current name: " + model.getName());
        model.setName(input);
    }
}
