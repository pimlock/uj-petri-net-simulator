package petrieditor.visual.action;

import petrieditor.io.IOHelper;
import petrieditor.model.PetriNet;
import petrieditor.visual.Application;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * @author wiktor
 */
public class FileLoadAction extends AbstractAction {

    final private JFileChooser fileChooser;

    public FileLoadAction() {
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().endsWith(".petri");
            }

            public String getDescription() {
                return "Petri editor files";
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        int returnVal = fileChooser.showOpenDialog(Application.getInstance().getMainFrame());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            PetriNet newPetriNet = IOHelper.load(fileChooser.getSelectedFile());
            Application.getInstance().getCurrentGraphPanel().getModel().loadPetriNet(newPetriNet);
        }
    }
}
