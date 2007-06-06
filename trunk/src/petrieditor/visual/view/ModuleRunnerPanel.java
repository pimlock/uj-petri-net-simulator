package petrieditor.visual.view;

import net.java.swingfx.waitwithstyle.PerformanceInfiniteProgressPanel;
import org.jdesktop.swingx.StackLayout;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
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
            runButton.addActionListener(new RunAction());
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

        private class RunAction extends AbstractAction {
            public void actionPerformed(ActionEvent e) {
                final JRootPane rootPane = getRootPane();
                final Component oldGlassPane = rootPane.getGlassPane();
                final PerformanceInfiniteProgressPanel pane = new PerformanceInfiniteProgressPanel();
                rootPane.setGlassPane(pane);
                pane.setVisible(true);

                new SwingWorker<ResultPane, Object>() {
                    protected ResultPane doInBackground() throws Exception {
                        PetriNet petriNet = IOHelper.cloneBySerializing(Application.getInstance().getCurrentGraphPanel().getModel());

                        ResultPane resultPane = null;
                        try {
                            //Thread.sleep(1000);
                            resultPane = module.run(petriNet);
                        } catch (Exception exc) {
                            JXErrorPane.showDialog(null, new ErrorInfo("Exception happened :(", exc.getClass().getName(), null, null, exc, null, null));
                            exc.printStackTrace();
                        }

                        return resultPane;
                    }

                    protected void done() {
                        pane.setVisible(false);
                        rootPane.setGlassPane(oldGlassPane);
                        try {
                            if (get() != null)
                                rootPane.setContentPane(get());
                        } catch (Exception ignore) {
                        }
                    }
                }.execute();
            }
        }
    }
}
