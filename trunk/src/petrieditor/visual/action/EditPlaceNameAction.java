package petrieditor.visual.action;

import petrieditor.model.Place;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author wiktor
 */
public class EditPlaceNameAction extends AbstractAction {
    
    private final Place model;

    public EditPlaceNameAction(final Place model) {
        super("Edit name");
        this.model = model;
    }

    public void actionPerformed(ActionEvent e) {
        //TODO:
        String input = JOptionPane.showInputDialog("Current name: " + model.getName());
        model.setName(input);
    }

}
