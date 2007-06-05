package petrieditor.visual.view;

import net.java.swingfx.waitwithstyle.PerformanceInfiniteProgressPanel;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.StackLayout;
import petrieditor.io.IOHelper;
import petrieditor.model.PetriNet;
import petrieditor.modules.Module;
import petrieditor.modules.ResultPane;
import petrieditor.visual.Application;
import petrieditor.visual.util.GradientPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author wiktor
 */
public class ModuleRunnerPanel extends Container {

    final private Module module;

    public ModuleRunnerPanel(Module module) {
        this.module = module;
        setLayout(new StackLayout());
        add(new GradientPanel(), StackLayout.BOTTOM);
        add(new InnerPanel());
    }

    private class InnerPanel extends JPanel {
        public InnerPanel() {
            JButton runButton = new JButton("Run");
            runButton.addActionListener(new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    JRootPane rootPane = getRootPane();
                    Component oldGlassPane = rootPane.getGlassPane();
                    PerformanceInfiniteProgressPanel pane = new PerformanceInfiniteProgressPanel();
                    rootPane.setGlassPane(pane);
                    pane.setVisible(true);
                    PetriNet petriNet = IOHelper.cloneBySerializing(Application.getInstance().getCurrentGraphPanel().getModel());

                    ResultPane resultPane = null;
                    try {                       
                        resultPane = module.run(petriNet);
                    } catch (Exception exc) {                        
                        JXErrorPane.showDialog(exc);
                    } finally {
                        pane.setVisible(false);
                        rootPane.setGlassPane(oldGlassPane);
                    }

                    if (resultPane != null)
                        rootPane.setContentPane(resultPane);
                }
            });
            GroupLayout layout = new javax.swing.GroupLayout(this);
            setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                            .addGap(150, 150, 150)
                            .addComponent(runButton)
                            .addContainerGap(183, Short.MAX_VALUE))
            );
            layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                            .addGap(46, 46, 46)
                            .addComponent(runButton)
                            .addContainerGap(231, Short.MAX_VALUE))
            );
        }
    }
}
