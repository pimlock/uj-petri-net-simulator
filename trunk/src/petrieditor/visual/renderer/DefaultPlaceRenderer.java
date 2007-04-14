package petrieditor.visual.renderer;

import petrieditor.visual.view.PlaceComponent;

import static java.lang.String.valueOf;

import java.awt.*;

/**
 * @author wiktor
 */
public class DefaultPlaceRenderer implements PlaceRenderer {

    private final static int PLACE_RADIUS = 20;

    public void render(Graphics graphics, PlaceComponent component) {
        graphics.drawOval(0, 0, PLACE_RADIUS, PLACE_RADIUS);
        graphics.drawRect(0, 0, PLACE_RADIUS, PLACE_RADIUS);
        //graphics.drawString(valueOf(place.getInitialMarking()), place.getCoords().x + 7, place.getCoords().y + 12);
    }
}
