package petrieditor.visual;

import petrieditor.visual.view.GraphPanel;
import petrieditor.visual.view.Toolbar;

import javax.swing.*;
import java.awt.*;

/**
 * @author wiktor
 */
public class GUI extends JFrame {

    public GUI() throws HeadlessException {
        buildContentPane();

        setSize(800, 450);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void buildContentPane() {
        setLayout(new BorderLayout());
        GraphPanel graphPanel = new GraphPanel();
        add(graphPanel, BorderLayout.CENTER);
        add(new Toolbar(graphPanel), BorderLayout.NORTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GUI gui = new GUI();
                gui.setVisible(true);
            }
        });
    }
}
