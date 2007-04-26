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
 * TODO: musze uporzadkowac ten kod
 *
 * @author wiktor
 */
public class Toolbar extends JToolBar {

    public Toolbar(final GraphPanel graphPanel, final SimulationHistory simulationHistory) {
        final CoolToggleButton placeInsert = new CoolToggleButton(CoolToggleButton.LEFT);
        final CoolToggleButton transitionInsert = new CoolToggleButton(CoolToggleButton.MIDDLE);
        final CoolToggleButton arcInsert = new CoolToggleButton(CoolToggleButton.MIDDLE);
        final CoolToggleButton inhibitorArcInsert = new CoolToggleButton(CoolToggleButton.RIGHT);

        final CoolToggleButton simulation = new CoolToggleButton(CoolToggleButton.LEFT);
        final CoolButton random = new CoolButton(CoolToggleButton.RIGHT);

        placeInsert.setFocusable(false);
        transitionInsert.setFocusable(false);
        arcInsert.setFocusable(false);
        inhibitorArcInsert.setFocusable(false);

        placeInsert.setIcon(new ImageIcon(getClass().getResource("../resources/place.png")));
        transitionInsert.setIcon(new ImageIcon(getClass().getResource("../resources/transition.png")));
        arcInsert.setIcon(new ImageIcon(getClass().getResource("../resources/arc.png")));
        inhibitorArcInsert.setIcon(new ImageIcon(getClass().getResource("../resources/inhibitorarc.png")));

        transitionInsert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                graphPanel.changeStategy(graphPanel.transitionInsertMouseStrategy);
                arcInsert.setSelected(false);
                placeInsert.setSelected(false);
                inhibitorArcInsert.setSelected(false);
            }
        });

        placeInsert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                graphPanel.changeStategy(graphPanel.placeInsertMouseStrategy);
                transitionInsert.setSelected(false);
                arcInsert.setSelected(false);
                inhibitorArcInsert.setSelected(false);
            }
        });
        placeInsert.setSelected(true);

        arcInsert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                graphPanel.changeStategy(graphPanel.normalArcInsertMouseStrategy);
                placeInsert.setSelected(false);
                transitionInsert.setSelected(false);
                inhibitorArcInsert.setSelected(false);
            }
        });

        inhibitorArcInsert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                graphPanel.changeStategy(graphPanel.inhibitorArcInsertMouseStrategy);
                placeInsert.setSelected(false);
                transitionInsert.setSelected(false);
                arcInsert.setSelected(false);
            }
        });

        JPanel insertPanel = new JPanel();
        insertPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 6, 0));
        insertPanel.setLayout(new FlowLayout(0, 0, 0));

        placeInsert.setPreferredSize(new Dimension(60, 22));
        transitionInsert.setPreferredSize(new Dimension(60, 22));
        arcInsert.setPreferredSize(new Dimension(60, 22));
        inhibitorArcInsert.setPreferredSize(new Dimension(60, 22));

        CoolToggleButton modules = new CoolToggleButton(CoolToggleButton.ALONE);
        modules.setIcon(new ImageIcon(getClass().getResource("../resources/modules.png")));
        modules.setFocusable(false);
        modules.setPreferredSize(new Dimension(45, 22));

        // Przycisk symulacji
        simulation.setIcon(new ImageIcon(getClass().getResource("../resources/simulation.png")));
        simulation.setFocusable(false);
        simulation.setPreferredSize(new Dimension(45, 22));
        simulation.addActionListener(new AbstractAction() {
            private MouseStrategy tmpMouseStrategy;

            public void actionPerformed(ActionEvent e) {
                random.setEnabled(!random.isEnabled());
                placeInsert.setEnabled(!placeInsert.isEnabled());
                arcInsert.setEnabled(!arcInsert.isEnabled());
                transitionInsert.setEnabled(!transitionInsert.isEnabled());
                inhibitorArcInsert.setEnabled(!inhibitorArcInsert.isEnabled());

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
                    simulationHistory.setVisible(true);
                    for (Transition transition : graphPanel.getModel().getTransitions())
                        transition.addObserver(simulationHistory);
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
                    simulationHistory.setVisible(false);
                    for (Transition transition : graphPanel.getModel().getTransitions())
                        transition.deleteObserver(simulationHistory);                    
                }

                graphPanel.repaint();
            }
        });

        // Przycisk odpalenia losowej tranzycji
        random.setIcon(new ImageIcon(getClass().getResource("../resources/random.png")));
        random.setFocusable(false);
        random.setPreferredSize(new Dimension(45, 22));
        random.setEnabled(false);
        random.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                try {
                    graphPanel.getModel().fireRandomTransition();
                } catch (Exception exp) {
                    JOptionPane.showMessageDialog(Application.getInstance().getMainFrame(), exp.getMessage());
                }
            }
        });

        insertPanel.add(Box.createHorizontalStrut(30));
        insertPanel.add(modules);
        insertPanel.add(Box.createHorizontalStrut(30));
        insertPanel.add(placeInsert);
        insertPanel.add(transitionInsert);
        insertPanel.add(arcInsert);
        insertPanel.add(inhibitorArcInsert);
        insertPanel.add(Box.createHorizontalStrut(30));
        insertPanel.add(simulation);
        insertPanel.add(random);

        add(Box.createGlue());
        add(insertPanel);
        add(Box.createGlue());

        setFloatable(false);
        setBackground(new Color(0xd6d9df));
    }

}
