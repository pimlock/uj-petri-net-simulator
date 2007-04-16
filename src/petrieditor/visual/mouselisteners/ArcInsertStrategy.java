package petrieditor.visual.strategy;

import petrieditor.visual.view.GraphPanel;
import petrieditor.visual.view.PlaceComponent;
import petrieditor.visual.view.TransitionComponent;
import petrieditor.model.Place;
import petrieditor.model.Transition;

import java.awt.event.MouseEvent;

/**
 * @author wiktor
 */
public class ArcInsertStrategy extends Strategy {

    private Object first, second;
    
    public ArcInsertStrategy(GraphPanel graphPanel) {
        super(graphPanel);
    }

    public void mouseClicked(MouseEvent e) {
        System.out.println("o ja nie moge");
        //TODO: czy na pewno jest to Place lub Transition
        if (first == null) {
            //TODO: zaznaczenie
            first = e.getComponent();
        } else {
            second = e.getComponent();
            System.out.println("i mamy pare :)");
            System.out.println(first + " + " + second);
            if (first instanceof PlaceComponent)
                graphPanel.getModel().connectWithArc(((PlaceComponent) first).getModel(), ((TransitionComponent) second).getModel());
            first = null;
        }

    }
}
