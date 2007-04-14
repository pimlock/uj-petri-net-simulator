package petrieditor.visual.renderer;

import petrieditor.visual.view.PlaceComponent;

import java.awt.*;

/**
 * @author wiktor
 */
public class HoverPlaceRenderer extends DefaultPlaceRenderer {

    public void render(Graphics graphics, PlaceComponent component) {
        graphics.setColor(Color.BLUE);
        super.render(graphics, component);
    }
}
