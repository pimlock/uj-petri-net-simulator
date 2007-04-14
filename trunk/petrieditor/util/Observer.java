package petrieditor.util;

/**
 * @author wiktor
 */
public interface Observer<S extends Observable<S, O, A>, O extends Observer<S, O, A>, A> {

    void update(Observable<S,O,A> observable, A event);

}
