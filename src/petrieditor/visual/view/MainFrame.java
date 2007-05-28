package petrieditor.visual.view;

import org.jdesktop.swingx.JXStatusBar;
import petrieditor.model.PetriNet;
import petrieditor.modules.treemodule.TreeModule;
import petrieditor.visual.action.FileExitAction;
import petrieditor.visual.action.FileLoadAction;
import petrieditor.visual.action.FileNewAction;
import petrieditor.visual.action.FileSaveAsAction;
import petrieditor.visual.util.GradientPanel;
import petrieditor.visual.util.StackLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.MessageFormat;

/**
 * @author wiktor
 */
public class MainFrame extends JFrame {

    private GraphPanel graphPanel;
    private JLabel statusText;

    public MainFrame() throws HeadlessException {
        setSize(800, 450);
        setTitle("UJ Petri Net Editor");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        buildModulesList();
        buildContentPane();
        buildMenuBar();
        buildStatusBar();
    }

    private void buildContentPane() {
        graphPanel = new GraphPanel(new PetriNet());
        SimulationHistory simulationHistory = new SimulationHistory();
        simulationHistory.setVisible(false);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new StackLayout());
        panel.add(new GradientPanel(), StackLayout.BOTTOM);
        panel.add(simulationHistory, StackLayout.TOP);
        panel.add(graphPanel, StackLayout.TOP);

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JScrollPane modulesScroll = new JScrollPane(buildModulesList());
        modulesScroll.setVisible(false);

        add(new Toolbar(graphPanel, simulationHistory, modulesScroll), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(modulesScroll, BorderLayout.WEST);
    }

    private void buildStatusBar() {
        JXStatusBar statusBar = new JXStatusBar();
        statusText = new JLabel("Welcome to UJ Petri Net Editor");
        final JLabel mousePosition = new JLabel("Mouse position");

        graphPanel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                mousePosition.setText(MessageFormat.format("x={0}, y={1}", e.getX(), e.getY()));
            }
        });

        JXStatusBar.Constraint c = new JXStatusBar.Constraint(JXStatusBar.Constraint.ResizeBehavior.FILL);

        statusBar.add(statusText, c);
        statusBar.add(mousePosition);

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

        JMenu help = new JMenu("Help");
        help.setMnemonic(KeyEvent.VK_H);
        help.add("About");

        menuBar.add(file);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(help);

        setJMenuBar(menuBar);
    }

    private JList buildModulesList() {
        final JList modulesList = new JList();
        modulesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        modulesList.setOpaque(true);
        modulesList.setFocusable(false);
        modulesList.setBackground(Color.WHITE);
        modulesList.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(0x9297a1)));
        
        /// FIXME: To chyba nie tak ma wyglądać...
        modulesList.setModel(new DefaultListModel() {
            public int getSize() {
                return 10;
            }

            public Object getElementAt(int index) {
                if (index == 0) {
                    return "tree module";
                }
                
                return "element " + index;
            }
        });
        modulesList.setCellRenderer(new MyCellRenderer());
        modulesList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = modulesList.locationToIndex(e.getPoint());
                    
                    /// FIXME: A to na pewno nie ma tak wyglądać.
                    if (index == 0) {
                        new TreeModule().run(graphPanel.getModel());
                    }
                    System.out.println("Double clicked on Item " + index);
                }
            }
        });

        return modulesList;
    }

    public GraphPanel getCurrentGraphPanel() {
        return graphPanel;
    }

    public void setStatusText(String msg) {
        statusText.setText(msg);
    }

    class MyCellRenderer extends JLabel implements ListCellRenderer {
        public MyCellRenderer() {
            setOpaque(true);
        }

        public Component getListCellRendererComponent(JList list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {

            if (isSelected)
                setIcon(new ImageIcon(getClass().getResource("../resources/star_selected.png")));
            else
                setIcon(new ImageIcon(getClass().getResource("../resources/star.png")));
            setText(value.toString());

            setBackground(isSelected ? new Color(0x39698a) : index % 2 == 0 ? new Color(0xf2f2f2) : Color.WHITE);
            setForeground(isSelected ? Color.WHITE : Color.BLACK);

            return this;
        }
    }
}
