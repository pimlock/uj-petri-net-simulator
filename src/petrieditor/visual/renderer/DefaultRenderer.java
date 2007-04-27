package petrieditor.visual.renderer;

import petrieditor.visual.view.TransitionComponent;
import petrieditor.visual.view.PlaceComponent;

import java.awt.*;

/**
 * @author wiktor
 */
public class DefaultRenderer extends DefaultRendererBase {

    protected Color getTransitionColor(TransitionComponent component) {
        return component.isSelected() ? SELECTED_COLOR : component.isHover() ? HOVER_COLOR : Color.BLACK;
    }

    protected Color getPlaceColor(PlaceComponent component) {
        return component.isSelected() ? SELECTED_COLOR : component.isHover() ? HOVER_COLOR : Color.BLACK;
    }
}
