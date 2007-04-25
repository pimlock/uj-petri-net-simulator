package petrieditor.visual.renderer;

import petrieditor.visual.view.PlaceComponent;
import petrieditor.visual.view.TransitionComponent;
import petrieditor.visual.view.ArcComponent;

import java.awt.*;

/**
 * @author wiktor
 */
public interface GraphRenderer {
    public Color ARROW_COLOR = new Color(119, 119, 119);
    public Color HOVER_COLOR = new Color(0, 0, 255);
    public Color SELECTED_COLOR = new Color(50, 180, 13);

    void render(Graphics2D g2d, PlaceComponent component);

    void render(Graphics2D g2d, TransitionComponent component);

    void render(Graphics2D g2d, ArcComponent component);
}
