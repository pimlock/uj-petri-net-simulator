package petrieditor.visual.action;

import petrieditor.visual.Application;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author wiktor
 */
public class FileNewAction extends AbstractAction {

    public void actionPerformed(ActionEvent e) {
        Application.getInstance().getCurrentGraphPanel().getModel().clear();
    }

}
