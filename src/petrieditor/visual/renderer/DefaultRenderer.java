package petrieditor.visual.renderer;

import petrieditor.visual.view.ArcComponent;
import petrieditor.visual.view.PlaceComponent;
import petrieditor.visual.view.TransitionComponent;

import java.awt.*;

/**
 * @author wiktor
 */
public class DefaultRenderer implements GraphRenderer {

    public void render(Graphics2D g2d, PlaceComponent component) {
        g2d.setColor(Color.WHITE);
        g2d.fillOval(0, 0, PlaceComponent.PLACE_DIAMETER, PlaceComponent.PLACE_DIAMETER);
        g2d.setColor(component.isSelected() ? SELECTED_COLOR : component.isHover() ? HOVER_COLOR : Color.BLACK);
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
        g2d.setColor(component.isSelected() ? SELECTED_COLOR : component.isHover() ? HOVER_COLOR : Color.BLACK);
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
