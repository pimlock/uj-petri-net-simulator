package petrieditor.visual;

import org.jdesktop.swingx.plaf.nimbus.NimbusLookAndFeel;
import petrieditor.visual.view.MainFrame;
import petrieditor.visual.view.GraphPanel;

import javax.swing.*;

/**
 * @author wiktor
 */
public class Application {

    private static Application INSTANCE = null;

    private MainFrame mainFrame;

    private Application() {
        mainFrame = new MainFrame();
    }

    public static synchronized Application getInstance() {
        if (INSTANCE == null)
            INSTANCE = new Application();
        return INSTANCE;
    }

    public JFrame getMainFrame() {
        return mainFrame;
    }

    public GraphPanel getCurrentGraphPanel() {
        return mainFrame.getCurrentGraphPanel();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(new NimbusLookAndFeel());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Application.getInstance().getMainFrame().setVisible(true);
            }
        });
    }
}
