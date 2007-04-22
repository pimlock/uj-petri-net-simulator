package petrieditor.visual.util;

/**
 * @author wiktor
 */
public class InhibitorArrowHead extends ArrowHead {

    public InhibitorArrowHead() {
        head.reset();
        head.moveTo(0, 0);
        head.curveTo(0f, 0f, 10f, -5f, 0, -10f);
        head.curveTo(0f, -10f, -10f, -5f, 0, 0f);
        head.moveTo(0, -10);
    }
}
