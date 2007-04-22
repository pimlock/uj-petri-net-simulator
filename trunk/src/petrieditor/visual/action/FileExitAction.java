package petrieditor.visual.action;

import petrieditor.visual.Application;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author wiktor
 */
public class FileExitAction extends AbstractAction {

    public void actionPerformed(ActionEvent e) {
        int option = JOptionPane.showConfirmDialog(Application.getInstance().getMainFrame(),
                                                   "Really Exit?",
                                                   "Confirm exit",
                                                   JOptionPane.YES_NO_OPTION,
                                                   JOptionPane.QUESTION_MESSAGE);
        if (option == JOptionPane.YES_OPTION)
            System.exit(0);
    }
}
