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
//        TODO:
        String input = JOptionPane.showInputDialog("Current number of tokens: " + String.valueOf(model.getCurrentMarking()));
        model.setCurrentMarking(Integer.parseInt(input));
    }
}
