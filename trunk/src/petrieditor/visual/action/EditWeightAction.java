package petrieditor.visual.action;

import petrieditor.model.Arc;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author wiktor
 */
public class EditWeightAction extends AbstractAction {
    final private Arc model;

    public EditWeightAction(final Arc model) {
        super("Edit weight");
        this.model = model;
    }

    public void actionPerformed(ActionEvent ae) {
        //TODO:
        String input = JOptionPane.showInputDialog("Current weight: " + String.valueOf(model.getWeight()));
        model.setWeight(Integer.parseInt(input));
    }
}
