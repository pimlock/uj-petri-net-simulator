package petrieditor.modules.treemodule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import petrieditor.model.Transition;

/**
 * Vertex in the tree/graph
 * 
 * @author pawel
 */
class GraphVertex {
    private ArrayList<Integer> marking;
    private GraphVertex copyOf = null;
    private HashMap<Transition, GraphVertex> exits = new HashMap<Transition, GraphVertex>();
    
    enum Colour {
        WHITE,
        GRAY,
        BLACK
    }
    // DFS search status
    Colour colour = Colour.WHITE;

    GraphVertex(ArrayList<Integer> m) {
        marking = m;
    }
    
    ArrayList<Integer> getMarking() {
        if (copyOf == null) {
            return this.marking;
        } else {
            return copyOf.getMarking();
        }
    }
    
    boolean isCopy () {
        return copyOf != null;
    }
    
    GraphVertex getOriginal() {
        return copyOf;
    }
    
    void markAsCopyOf(GraphVertex c) {
        copyOf = c;
        marking = null;
    }
    
    boolean testAndMarkOmega(ArrayList<Integer> prevMarking) {
        
        LinkedList<Integer> largerFields = new LinkedList<Integer>();

        for (int i = 0; i < prevMarking.size(); i++) {
            int my = getMarking().get(i);
            int previous = prevMarking.get(i);
            if (previous > my || (previous == -1 && my >= 0)) {
                return false;
            } else if ((my > previous || my == -1) && previous >= 0) {
                largerFields.add(i);
            }
        }
        
        // inv: if we are here then this state is equal to or larger than prevMarking
        if (largerFields.isEmpty()) { // but this means we just found a copy
            return false;
        }
        
        // second pass - mark omegas.
        for (Integer i : largerFields) {
            getMarking().set(i, -1);
        }
        return true;
    }
    
    /**
     * Adds or replaces the exit
     * @param t
     * @param v
     */
    void addExit(Transition t, GraphVertex v) {
        exits.put(t, v);
        
    }
    
    Set<Transition> getExitTransitions() {
        return exits.keySet();
    }
    
    GraphVertex getExit(Transition t) {
        return exits.get(t);
    }
    
    @Override
    public int hashCode() {
        return getMarking().hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        
        if (obj instanceof GraphVertex) {
            GraphVertex gobj = (GraphVertex) obj;
            return gobj.getMarking().equals(this.getMarking());
        } else {
            return false;
        }
    }
}
