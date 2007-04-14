package petrieditor.model;

/**
 * @author wiktor
 */
public class InhibitorArc extends Arc {
                                  
    public InhibitorArc(Place fromPlace, Transition toTransition) {
        super(fromPlace, toTransition);
        this.weight = 0;
    }


    public boolean isEnabled() {
        return place.getCurrentMarking() == 0;
    }

    public void setWeight(int weight) {
        throw new IllegalArgumentException("Inhibitor arc's weight is always 0!");
    }
}
