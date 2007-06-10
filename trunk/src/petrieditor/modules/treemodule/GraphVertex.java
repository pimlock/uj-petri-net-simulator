package petrieditor.modules.treemodule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import petrieditor.model.Transition;

/**
 * Vertex in the execution tree/graph
 * 
 * @author pawel
 */
class GraphVertex {
    /**
     * Marking represented by this vertex
     */
    private ArrayList<Integer> marking;
    
    /**
     * If this vertex is a copy of another vertex (same marking reached by another path)
     * then this is the "original" vertex.
     */
    private GraphVertex copyOf = null;
    
    /**
     * Exits from this vertex. 
     */
    private HashMap<Transition, GraphVertex> exits = new HashMap<Transition, GraphVertex>();
    
    /**
     * Used in DFS search
     * @author pawel
     */
    enum Colour {
        WHITE,
        GRAY,
        BLACK
    }
    /**
     * Used in DFS search.
     */
    Colour colour = Colour.WHITE;

    /**
     * Constructs new {@link GraphVertex} using representing given marking.
     * @param m Marking represented by newly constructed vertex.
     */
    GraphVertex(ArrayList<Integer> m) {
        marking = m;
    }
    
    /**
     * Returns the marking represented this vertex.
     * @return {@link ArrayList} of {@link Integer}s
     */
    ArrayList<Integer> getMarking() {
        if (copyOf == null) {
            return this.marking;
        } else {
            return copyOf.getMarking();
        }
    }
    
    /**
     * Tells whether this vertex is a copy (meaning that another vertex with same marking was already
     * found).
     * 
     * @return
     */
    boolean isCopy () {
        return copyOf != null;
    }
    
    /**
     * Returns the vertex of which this vertex is a copy.
     * @return
     */
    GraphVertex getOriginal() {
        return copyOf;
    }
    
    /**
     * Returns original vertex if this is a copy, returns this vertex otherwise
     * @return 
     */
    GraphVertex getPrimaryVertex() {
        if (getOriginal() == null) {
            return this;
        } else {
            return getOriginal();
        }
    }
    
    /**
     * Marks this vertex as a copy of another vertex (meaning this vertex represents a marking that was
     * encounterered earlier in execution tree construction)
     * 
     * @param c
     */
    void markAsCopyOf(GraphVertex c) {
        copyOf = c;
        marking = null;
    }
    
    // FIXME: this does not belong here, move it to TreeModule.
    /**
     * Tests for inequality in markings and marks proper places with omega
     * if neccessary
     * 
     * @return True iff some new omegas were found.
     */
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
     * Adds or replaces an exit from this vertex (an edge in execution tree/graph)
     * @param t
     * @param v
     */
    void addExit(Transition t, GraphVertex v) {
        exits.put(t, v);
        
    }
    
    /**
     * Returns a set of transitions that form edges beginning in this vertex.
     * @return
     */
    Set<Transition> getExitTransitions() {
        return exits.keySet();
    }
    
    /**
     * Returns a vertex that is directly reachable after following transition t from this vertex.
     * @param t
     * @return
     */
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
    
    @Override
    public String toString() {
        String repr = "[";
        
        for (Integer i : getMarking()) {
            repr += (i + ", ");
        }
        if (isCopy()) {
            repr += ("] (copy)");
        } else {
            repr += ("]");
        }
        
        return repr;
    }
}
