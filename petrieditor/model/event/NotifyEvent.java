package petrieditor.model.event;

/**
 * @author wiktor
 */
public class NotifyEvent<T> {

    private final T object;
    private final EventType eventType;

    public NotifyEvent(final T object, final EventType eventType) {
        this.object = object;
        this.eventType = eventType;
    }

    public EventType getEventType() {
        return eventType;
    }

    public T getObject() {
        return object;
    }
}
