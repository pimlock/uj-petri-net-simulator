package petrieditor.visual.action;

import petrieditor.io.IOHelper;
import petrieditor.visual.Application;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * @author wiktor
 */
public class FileSaveAsAction extends AbstractAction {

    final private JFileChooser fileChooser;

    public FileSaveAsAction() {
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
        int returnVal = fileChooser.showSaveDialog(Application.getInstance().getMainFrame());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File saveAsFile = new File(fileChooser.getSelectedFile().getAbsolutePath() + ".petri");
            IOHelper.save(Application.getInstance().getCurrentGraphPanel().getModel(), saveAsFile);
        }
    }
}
