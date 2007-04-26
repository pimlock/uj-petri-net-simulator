package petrieditor.model.viewinterfaces;

import petrieditor.model.event.NotifyEvent;
import petrieditor.model.Place;
import petrieditor.util.Observer;

/**
 * @author wiktor
 */
public interface PlaceView extends Observer<Place, PlaceView, NotifyEvent<Place>> {
}
