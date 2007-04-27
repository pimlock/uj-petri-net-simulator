package petrieditor.visual.renderer;

import petrieditor.visual.view.PlaceComponent;
import petrieditor.visual.view.TransitionComponent;
import petrieditor.visual.view.ArcComponent;

import java.awt.*;

/**
 * @author wiktor
 */
public abstract class DefaultRendererBase implements GraphRenderer {

    public Color ARROW_COLOR = new Color(119, 119, 119);
    public Color HOVER_COLOR = new Color(0, 0, 255);
    public Color SELECTED_COLOR = new Color(50, 180, 13);

    protected abstract Color getTransitionColor(TransitionComponent component);

    protected abstract Color getPlaceColor(PlaceComponent component);

    public void render(Graphics2D g2d, PlaceComponent component) {
        g2d.setColor(Color.WHITE);
        g2d.fillOval(0, 0, PlaceComponent.PLACE_DIAMETER, PlaceComponent.PLACE_DIAMETER);
        g2d.setColor(getPlaceColor(component));
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawOval(0, 0, PlaceComponent.PLACE_DIAMETER, PlaceComponent.PLACE_DIAMETER);

        int dx = 9, dy = 17;
        if (component.getModel().getCurrentMarking() > 9) {
            dx = 6;
            dy = 17;
        }

        g2d.drawString(String.valueOf(component.getModel().getCurrentMarking()), dx, dy);
    }

    public void render(Graphics2D g2d, TransitionComponent component) {
        g2d.setColor(getTransitionColor(component));
        g2d.fillRect(0, 0, TransitionComponent.WIDTH, TransitionComponent.HEIGHT);
    }

    public void render(Graphics2D g2d, ArcComponent component) {
        g2d.setColor(component.isHover() ? Color.RED : ARROW_COLOR);
        g2d.translate(-component.getLocation().x, -component.getLocation().y);
        g2d.draw(component.getArrowShape());
        if (component.getModel().getWeight() != 0)
            g2d.fill(component.getArrowShape());
        g2d.translate(component.getLocation().x, component.getLocation().y);
    }
}
