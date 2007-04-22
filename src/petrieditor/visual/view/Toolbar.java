package petrieditor.visual.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author wiktor
 */
public class Toolbar extends JToolBar {

    public Toolbar(final GraphPanel mainView) {
        final CoolButton placeInsert = new CoolButton(CoolButton.LEFT);
        final CoolButton transitionInsert = new CoolButton(CoolButton.MIDDLE);
        final CoolButton arcInsert = new CoolButton(CoolButton.MIDDLE);
        final CoolButton inhibitorArcInsert = new CoolButton(CoolButton.RIGHT);

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
                mainView.changeStategy(mainView.transitionInsertMouseStrategy);
                arcInsert.setSelected(false);
                placeInsert.setSelected(false);
                inhibitorArcInsert.setSelected(false);
            }
        });

        placeInsert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainView.changeStategy(mainView.placeInsertMouseStrategy);
                transitionInsert.setSelected(false);
                arcInsert.setSelected(false);
                inhibitorArcInsert.setSelected(false);
            }
        });
        placeInsert.setSelected(true);

        arcInsert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainView.changeStategy(mainView.normalArcInsertMouseStrategy);
                placeInsert.setSelected(false);
                transitionInsert.setSelected(false);
                inhibitorArcInsert.setSelected(false);
            }
        });
        
        inhibitorArcInsert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainView.changeStategy(mainView.inhibitorArcInsertMouseStrategy);
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

        CoolButton modules = new CoolButton(CoolButton.ALONE);
        modules.setIcon(new ImageIcon(getClass().getResource("../resources/modules.png")));
        modules.setFocusable(false);
        modules.setPreferredSize(new Dimension(45, 22));

        CoolButton simulation = new CoolButton(CoolButton.LEFT);
        simulation.setIcon(new ImageIcon(getClass().getResource("../resources/simulation.png")));
        simulation.setFocusable(false);
        simulation.setPreferredSize(new Dimension(45, 22));
        CoolButton random = new CoolButton(CoolButton.RIGHT);
        random.setIcon(new ImageIcon(getClass().getResource("../resources/random.png")));
        random.setFocusable(false);
        random.setPreferredSize(new Dimension(45, 22));
//        random.setEnabled(false);

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
