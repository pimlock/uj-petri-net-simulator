package petrieditor.visual.view;

import petrieditor.visual.view.GraphPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author wiktor
 */
public class Toolbar extends JToolBar {

    private GraphPanel mainView;

    public Toolbar(final GraphPanel mainView) {
        this.mainView = mainView;

        JButton transitionInsert = new JButton("transition");
        transitionInsert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainView.changeStategy(mainView.transitionInsertStrategy);
            }
        });

        JButton placeInsert = new JButton("place");
        placeInsert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainView.changeStategy(mainView.placeInsertStrategy);
            }
        });

        JButton arcInsert = new JButton("arc");
        arcInsert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("arc");
                mainView.changeStategy(mainView.arcInsertStrategy);
            }
        });


        add(transitionInsert);
        add(placeInsert);
        add(arcInsert);
    }
}
