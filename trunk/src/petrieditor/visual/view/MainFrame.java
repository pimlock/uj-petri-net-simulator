package petrieditor.visual.view;

import org.jdesktop.swingx.JXStatusBar;
import petrieditor.visual.action.FileExitAction;
import petrieditor.visual.action.FileLoadAction;
import petrieditor.visual.action.FileNewAction;
import petrieditor.visual.action.FileSaveAsAction;
import petrieditor.visual.util.GradientPanel;
import petrieditor.visual.util.StackLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author wiktor
 */
public class MainFrame extends JFrame {

    private GraphPanel graphPanel;

    public MainFrame() throws HeadlessException {
        setSize(800, 450);
        setTitle("UJ Petri Net Editor");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        buildMenuBar();
        buildContentPane();
        buildStatusBar();
    }

    private void buildContentPane() {
        graphPanel = new GraphPanel();
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new StackLayout());
        panel.add(new GradientPanel(), StackLayout.BOTTOM);
        panel.add(graphPanel, StackLayout.TOP);
        
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        add(new Toolbar(graphPanel), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void buildStatusBar() {
        JXStatusBar statusBar = new JXStatusBar();
        final JLabel label = new JLabel("Mouse status");
        statusBar.add(label);
        graphPanel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                label.setText(e.getPoint().toString());
            }
        });
        statusBar.addSeparator();        
        add(statusBar, BorderLayout.SOUTH);
    }

    private void buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);

        JMenuItem newFile = new JMenuItem("New");
        newFile.setMnemonic(KeyEvent.VK_N);
        newFile.addActionListener(new FileNewAction());

        JMenuItem load = new JMenuItem("Load");
        load.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        load.setMnemonic(KeyEvent.VK_O);
        load.addActionListener(new FileLoadAction());

        JMenuItem save = new JMenuItem("Save", new ImageIcon(getClass().getResource("../resources/save.png")));
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        save.setMnemonic(KeyEvent.VK_S);

        JMenuItem saveAs = new JMenuItem("Save as...", new ImageIcon(getClass().getResource("../resources/save_as.png")));
        saveAs.addActionListener(new FileSaveAsAction());

        JMenuItem exit = new JMenuItem("Exit");
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
        exit.setMnemonic(KeyEvent.VK_E);
        exit.addActionListener(new FileExitAction());

        file.add(newFile);
        file.add(load);
        file.add(save);
        file.add(saveAs);
        file.addSeparator();
        file.add(exit);

        menuBar.add(file);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(new JMenu("About"));

        setJMenuBar(menuBar);
    }

    public GraphPanel getCurrentGraphPanel() {
        return graphPanel;
    }
}
