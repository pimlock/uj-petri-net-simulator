package petrieditor.visual.view;

import javax.swing.*;
import java.awt.*;

/**
 * @author wiktor
 */
public abstract class PlaceTransitionComponent extends PetriNetComponent {

    public abstract void translate(int transX, int transY);

}
