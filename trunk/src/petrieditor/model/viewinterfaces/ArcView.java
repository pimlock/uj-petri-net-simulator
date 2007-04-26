package petrieditor.model.viewinterfaces;

import petrieditor.model.event.NotifyEvent;
import petrieditor.model.Arc;
import petrieditor.util.Observer;

/**
 * @author wiktor
 */
public interface ArcView extends Observer<Arc, ArcView, NotifyEvent<Arc>> {
}
