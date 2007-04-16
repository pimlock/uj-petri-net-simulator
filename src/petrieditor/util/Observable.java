package petrieditor.util;

import java.util.Vector;

/**
 * @author wiktor
 */
public class Observable<S extends Observable<S, O, A>, O extends Observer<S, O, A>, A> {
    private boolean changed = false;
    private Vector<O> obs = new Vector<O>();

    public synchronized void addObserver(O o) {
        if (o == null)
            throw new NullPointerException();
        if (!obs.contains(o))
            obs.addElement(o);
    }

    public synchronized void deleteObserver(O o) {
        obs.removeElement(o);
    }

    public void setChangedAndNotifyObservers() {
        setChanged();
        notifyObservers();
    }

    public void setChangedAndNotifyObservers(A arg) {
        setChanged();
        notifyObservers(arg);
    }

    public void notifyObservers() {
        notifyObservers(null);
    }

    public void notifyObservers(A arg) {
        Vector<O> obsLocal = new Vector<O>();

        synchronized (this) {
            if (!changed)
                return;
            copy(obsLocal, obs);
            obs.toArray();
            clearChanged();
        }

        for (O o : obsLocal)
            o.update(this, arg);
    }

    private static <O> void copy(Vector<O> dest, Vector<O> src) {
        for (O o : src)
            dest.add(o);
    }

    public synchronized void deleteObservers() {
        obs.removeAllElements();
    }

    protected synchronized void setChanged() {
        changed = true;
    }

    protected synchronized void clearChanged() {
        changed = false;
    }

    public synchronized boolean hasChanged() {
        return changed;
    }

    public synchronized int countObservers() {
        return obs.size();
    }

}
