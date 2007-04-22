package petrieditor.visual.view;

/**
 * @author wiktor
 */
public abstract class PlaceTransitionComponent extends PetriNetComponent {

    private boolean selected;

    public PlaceTransitionComponent(String name) {
        super(name);
    }

    public abstract void translate(int transX, int transY);

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
