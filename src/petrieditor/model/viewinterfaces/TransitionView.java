package petrieditor.model.viewinterfaces;

import petrieditor.model.event.NotifyEvent;
import petrieditor.model.Transition;
import petrieditor.util.Observer;

/**
 * @author wiktor
 */
public interface TransitionView extends Observer<Transition, TransitionView, NotifyEvent<Transition>> {
}
