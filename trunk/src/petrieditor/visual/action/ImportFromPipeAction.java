package petrieditor.visual.action;

import petrieditor.visual.Application;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.io.File;
import petrieditor.io.PIPELoader;
import petrieditor.model.PetriNet;

/**
 * @author psuliga
 */
public class ImportFromPipeAction extends AbstractAction {

    final private JFileChooser fileChooser;

    public ImportFromPipeAction() {
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().endsWith(".xml");
            }

            public String getDescription() {
                return "PIPE files";
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        int returnVal = fileChooser.showOpenDialog(Application.getInstance().getMainFrame());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            PetriNet newPetriNet = PIPELoader.load(fileChooser.getSelectedFile());
            Application.getInstance().getCurrentGraphPanel().getModel().loadPetriNet(newPetriNet);
        }
    }
}
