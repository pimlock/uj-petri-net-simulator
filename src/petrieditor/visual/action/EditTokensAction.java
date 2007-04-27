package petrieditor.visual.action;

import petrieditor.model.Place;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author wiktor
 */
public class EditTokensAction extends AbstractAction {

    private final Place model;

    public EditTokensAction(final Place model) {
        super("Edit tokens");
        this.model = model;
    }

    public void actionPerformed(ActionEvent e) {
        String initialVal = String.valueOf(model.getInitialMarking());
        String input = JOptionPane.showInputDialog("Current number of tokens: " + initialVal, initialVal);
        try {
            model.setInitialMarking(Integer.parseInt(input));
        } catch (NumberFormatException nfe) {
        }
    }
}
