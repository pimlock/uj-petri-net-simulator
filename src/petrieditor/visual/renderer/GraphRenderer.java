package petrieditor.visual.renderer;

import petrieditor.visual.view.PlaceComponent;
import petrieditor.visual.view.TransitionComponent;
import petrieditor.visual.view.ArcComponent;

import java.awt.*;

/**
 * @author wiktor
 */
public interface GraphRenderer {

    void render(Graphics2D g2d, PlaceComponent component);
    void render(Graphics2D g2d, TransitionComponent component);
    void render(Graphics2D g2d, ArcComponent component);
    
}
