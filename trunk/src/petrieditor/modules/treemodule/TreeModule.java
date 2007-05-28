package petrieditor.modules.treemodule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import petrieditor.model.Arc;
import petrieditor.model.InhibitorArc;
import petrieditor.model.PetriNet;
import petrieditor.model.Transition;
import petrieditor.modules.Module;

public class TreeModule implements Module {
    private static final String MODULE_NAME = "Tree and graph module";

    public String getName() {
        return MODULE_NAME;
    }
    
    // permanent - stores results of analysis (from the last run).
    private PetriNet petriNet = null;
    private GraphVertex rootVertex = null;
    private HashMap<ArrayList<Integer>, GraphVertex> vertices = null; // contains only "original vertices". "old vertices" are not included.
    private boolean isBound = false;
    private boolean deadlockFound = false;
    
    
    //transient:
    LinkedList<ArrayList<Integer>> dfsPathMarkings = null;
    
    synchronized public void run(PetriNet petriNet) {
        System.out.println("Tree module");
        
        this.petriNet = petriNet;
        this.rootVertex = null;
        this.vertices = null;
        this.dfsPathMarkings = null;
        this.isBound = true;
        this.deadlockFound = false;
        
        
        for (Transition t : this.petriNet.getTransitions()) {
            for (Arc a : t.getInputArcs()) {
                if (a instanceof InhibitorArc) {
                    System.err.println("Inhibitor arc found, analysis not possible");
                    return;
                }
            }
        }
        
        ArrayList<Integer> originalMarking = this.petriNet.getNetworkMarking();
        try {
            this.petriNet.reset();
            
            rootVertex = new GraphVertex(petriNet.getNetworkMarking());
            vertices = new HashMap<ArrayList<Integer>, GraphVertex>();
            vertices.put(rootVertex.getMarking(), rootVertex);
            dfsPathMarkings = new LinkedList<ArrayList<Integer>>();
            
            dfsAnalyze(rootVertex); 
            recursiveOutput(rootVertex);
            foldGraph();
            
            if (isBound){
                System.out.println("The net is bounded");
            } else {
                System.out.println("The net is NOT bounded");
            }
            
            if (deadlockFound) {
                System.out.println("Deadlock in the network found");
            } else if (isBound){
                System.out.println("No deadlocks found. The net is deadlock-free");
            } else {
                System.out.println("No deadlocks found, but the net is not bounded; The net MIGHT BE deadlock-free");
            }
        } finally {
            dfsPathMarkings = null;
            this.petriNet.setNetworkMarking(originalMarking);
        }
    }
    
    
    /**
     * @warning Changes state of the network and DOES NOT restore it.
     * @param vertex
     */
    private void dfsAnalyze(GraphVertex vertex) {
        if (vertex.colour != GraphVertex.Colour.WHITE) {
            return;
        }
        if (vertex.isCopy()) {
            return;
        }
        
        vertex.colour = GraphVertex.Colour.GRAY;
        dfsPathMarkings.add(vertex.getMarking());
        
        for (Transition transition : petriNet.getTransitions()) {
            petriNet.setNetworkMarking(vertex.getMarking());
            
            if (transition.isEnabled()) {
                transition.fire();
                GraphVertex newVertex = new GraphVertex(petriNet.getNetworkMarking());
                for (ArrayList<Integer> previous: dfsPathMarkings) {
                    if (newVertex.testAndMarkOmega(previous)) {
                        isBound = false;
                        break;
                    }
                }
                
                vertex.addExit(transition, newVertex);
                
                GraphVertex oldVertex = vertices.get(newVertex.getMarking());
                if (oldVertex != null) {
                    newVertex.markAsCopyOf(oldVertex);
                } else {
                    vertices.put(newVertex.getMarking(), newVertex);
                    dfsAnalyze(newVertex);
                }
            }
        }
        
        if (vertex.getExitTransitions().isEmpty()) {
            deadlockFound = true;
        }
        
        dfsPathMarkings.removeLast();
        vertex.colour = GraphVertex.Colour.BLACK;
    }
    
    private void recursiveOutput(GraphVertex vertex) {
        recursiveOutput(vertex, 0);
    }
    
    private void recursiveOutput(GraphVertex vertex, int level) {
        String prefix = "";
        for(int i =0; i< level; i++) {
            prefix = prefix + "    ";
        }
        System.out.print(prefix + "[");
        for (Integer i : vertex.getMarking()) {
            System.out.print(i + ", ");
        }
        if (vertex.isCopy()) {
            System.out.println("] (copy)");
        } else {
            System.out.println("]");
        }
        for (Transition t : vertex.getExitTransitions()) {
            System.out.println(prefix + t.getName() + "->" );
            recursiveOutput(vertex.getExit(t), level+1);
        } 
    }
    
    private void foldGraph() {
        Iterator<ArrayList<Integer>> it = vertices.keySet().iterator();
        while (it.hasNext()) { // for every original vertex
            GraphVertex g = vertices.get(it.next());// fetch the vertex
            
            HashSet<Transition> replaces = new HashSet<Transition>();
            
            for (Transition t : g.getExitTransitions()) { // for every transition - find vertices to be replaced
                if (g.getExit(t).isCopy()) {
                    replaces.add(t);
                }
            }
            
            for (Transition t : replaces) { // replace with the vertice (second phase to avoid ConcurrentModificationException)
                g.addExit(t, g.getExit(t).getOriginal());
            }
        }
    }
}
