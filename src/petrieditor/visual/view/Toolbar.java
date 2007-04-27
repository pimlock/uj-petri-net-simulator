package petrieditor.visual.view;

import petrieditor.visual.mouselisteners.SimulationMouseStrategy;
import petrieditor.visual.mouselisteners.MouseStrategy;
import petrieditor.visual.Application;
import petrieditor.model.Transition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author wiktor
 */
public class Toolbar extends JToolBar {

    public Toolbar(final GraphPanel graphPanel, final SimulationHistory simulationHistory, final JScrollPane modulesScroll) {
        final CoolToggleButton modules = new CoolToggleButton(CoolToggleButton.ALONE);
        final CoolToggleButton placeInsert = new CoolToggleButton(CoolToggleButton.LEFT);
        final CoolToggleButton transitionInsert = new CoolToggleButton(CoolToggleButton.MIDDLE);
        final CoolToggleButton arcInsert = new CoolToggleButton(CoolToggleButton.MIDDLE);
        final CoolToggleButton inhibitorArcInsert = new CoolToggleButton(CoolToggleButton.RIGHT);
        final CoolToggleButton simulation = new CoolToggleButton(CoolToggleButton.LEFT);
        final CoolButton random = new CoolButton(CoolToggleButton.RIGHT);

        modules.setFocusable(false);
        placeInsert.setFocusable(false);
        transitionInsert.setFocusable(false);
        arcInsert.setFocusable(false);
        inhibitorArcInsert.setFocusable(false);
        simulation.setFocusable(false);
        random.setFocusable(false);

        modules.setIcon(new ImageIcon(getClass().getResource("../resources/modules.png")));
        placeInsert.setIcon(new ImageIcon(getClass().getResource("../resources/place.png")));
        transitionInsert.setIcon(new ImageIcon(getClass().getResource("../resources/transition.png")));
        arcInsert.setIcon(new ImageIcon(getClass().getResource("../resources/arc.png")));
        inhibitorArcInsert.setIcon(new ImageIcon(getClass().getResource("../resources/inhibitorarc.png")));
        simulation.setIcon(new ImageIcon(getClass().getResource("../resources/simulation.png")));
        random.setIcon(new ImageIcon(getClass().getResource("../resources/random.png")));

        modules.setPreferredSize(new Dimension(45, 22));
        placeInsert.setPreferredSize(new Dimension(60, 22));
        transitionInsert.setPreferredSize(new Dimension(60, 22));
        arcInsert.setPreferredSize(new Dimension(60, 22));
        inhibitorArcInsert.setPreferredSize(new Dimension(60, 22));
        simulation.setPreferredSize(new Dimension(45, 22));
        random.setPreferredSize(new Dimension(45, 22));

        transitionInsert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                graphPanel.changeStategy(graphPanel.transitionInsertMouseStrategy);
                arcInsert.setSelected(false);
                placeInsert.setSelected(false);
                inhibitorArcInsert.setSelected(false);
                _setStatusText("Inserting new transition");
            }
        });

        placeInsert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                graphPanel.changeStategy(graphPanel.placeInsertMouseStrategy);
                transitionInsert.setSelected(false);
                arcInsert.setSelected(false);
                inhibitorArcInsert.setSelected(false);
                _setStatusText("Inserting new place");
            }
        });

        arcInsert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                graphPanel.changeStategy(graphPanel.normalArcInsertMouseStrategy);
                placeInsert.setSelected(false);
                transitionInsert.setSelected(false);
                inhibitorArcInsert.setSelected(false);
                _setStatusText("Connection with arc");
            }
        });

        inhibitorArcInsert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                graphPanel.changeStategy(graphPanel.inhibitorArcInsertMouseStrategy);
                placeInsert.setSelected(false);
                transitionInsert.setSelected(false);
                arcInsert.setSelected(false);
                _setStatusText("Connection with inhibitor arc");
            }
        });

        simulation.addActionListener(new AbstractAction() {
            private MouseStrategy tmpMouseStrategy;

            public void actionPerformed(ActionEvent e) {
                random.setEnabled(!random.isEnabled());
                placeInsert.setEnabled(!placeInsert.isEnabled());
                arcInsert.setEnabled(!arcInsert.isEnabled());
                transitionInsert.setEnabled(!transitionInsert.isEnabled());
                inhibitorArcInsert.setEnabled(!inhibitorArcInsert.isEnabled());
                simulationHistory.setVisible(!simulationHistory.isVisible());

                if (random.isEnabled()) {
                    graphPanel.currentRenderer = graphPanel.simulationRenderer;
                    for (Component component : graphPanel.getComponents()) {
                        if (component instanceof PetriNetComponent) {
                            PetriNetComponent petriNetComponent = (PetriNetComponent) component;
                            petriNetComponent.disablePopup();
                        }
                    }
                    tmpMouseStrategy = graphPanel.currentMouseStrategy;
                    graphPanel.changeStategy(new SimulationMouseStrategy(graphPanel));
                    for (Transition transition : graphPanel.getModel().getTransitions())
                        transition.addObserver(simulationHistory);
                    _setStatusText("Simulation mode: click on transition to fire it or click on random transition fire button");
                } else {
                    graphPanel.currentRenderer = graphPanel.defaultRenderer;
                    for (Component component : graphPanel.getComponents()) {
                        if (component instanceof PetriNetComponent) {
                            PetriNetComponent petriNetComponent = (PetriNetComponent) component;
                            petriNetComponent.enablePopup();
                        }
                    }
                    graphPanel.getModel().reset();
                    graphPanel.changeStategy(tmpMouseStrategy);
                    tmpMouseStrategy = null;
                    for (Transition transition : graphPanel.getModel().getTransitions())
                        transition.deleteObserver(simulationHistory);
                    _setStatusText("Edit mode");
                }

                graphPanel.repaint();
            }
        });

        random.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                try {
                    graphPanel.getModel().fireRandomTransition();
                    _setStatusText("Transition fired at random");
                } catch (Exception exp) {
                    JOptionPane.showMessageDialog(Application.getInstance().getMainFrame(), exp.getMessage());
                }
            }
        });

        modules.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                modulesScroll.setVisible(!modulesScroll.isVisible());
                revalidate();
            }
        });

        JPanel insertPanel = new JPanel();
        insertPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 6, 0));
        insertPanel.setLayout(new FlowLayout(0, 0, 0));

        insertPanel.add(Box.createHorizontalStrut(50));
        insertPanel.add(modules);
        insertPanel.add(Box.createHorizontalStrut(30));
        insertPanel.add(placeInsert);
        insertPanel.add(transitionInsert);
        insertPanel.add(arcInsert);
        insertPanel.add(inhibitorArcInsert);
        insertPanel.add(Box.createHorizontalStrut(30));
        insertPanel.add(simulation);
        insertPanel.add(random);

        add(insertPanel);
        setFloatable(false);
        setBackground(new Color(0xd6d9df));

        placeInsert.setSelected(true);
        random.setEnabled(false);
    }

    private void _setStatusText(String msg) {
        Application.getInstance().getMainFrame().setStatusText(msg);
    }

}
