package petrieditor.modules.treemodule;

import petrieditor.model.Arc;
import petrieditor.model.InhibitorArc;
import petrieditor.model.PetriNet;
import petrieditor.model.Transition;
import petrieditor.modules.Module;
import petrieditor.modules.ResultPane;

import java.util.*;


/**
 * Module for constructing execution tree and graph and doing related analyses.
 * 
 * @author pawel
 */
public class TreeModule implements Module {
    private static final String MODULE_NAME = "Tree and graph";

    /**
     * Returns the module name.
     */
    public String getName() {
        return MODULE_NAME;
    }
    
    // permanent - stores results of analysis (from the last run).
    private PetriNet petriNet = null; // the net for which the calculations are/were done
    private GraphVertex rootVertex = null; // the root vertex.
    private HashMap<ArrayList<Integer>, GraphVertex> vertices = null; // contains only "original vertices". "old vertices" are not included.
    private boolean isBounded = false;    //self explainatory
    private boolean deadlockFound = false;//self explainatory (this only tells if deadlock state was encountered; if the net is not bounded it's not an IFF)
    private int stronglyConnectedComponentsCount = 0;
    
    /**
     * Builds the tree and graph and does related analyses./
     * 
     * @warning Temporarily tampers with the net, but restores its state afterwards.
     */
    synchronized public ResultPane run(PetriNet petriNet) {
        System.out.println("Tree module");
        
        this.petriNet = petriNet;
        this.rootVertex = null;
        this.vertices = null;
        this.isBounded = true;
        this.deadlockFound = false;
        this.stronglyConnectedComponentsCount = 0;
        
        for (Transition t : this.petriNet.getTransitions()) {
            for (Arc a : t.getInputArcs()) {
                if (a instanceof InhibitorArc) {
                    System.err.println("Inhibitor arc found, analysis not possible");
                    return null;
                }
            }
        }
        
        ArrayList<Integer> originalMarking = this.petriNet.getNetworkMarking();
        try {
            this.petriNet.reset();
            
            rootVertex = new GraphVertex(petriNet.getNetworkMarking());
            vertices = new HashMap<ArrayList<Integer>, GraphVertex>();
            vertices.put(rootVertex.getMarking(), rootVertex);
            
            dfsTreeConstruction(rootVertex); 
            recursiveOutput(rootVertex);
            foldGraph();
            
            if (isBounded){
                System.out.println("The net is bounded");
                stronglyConnectedComponents();
                System.out.println("Number of strongly connected components in execution graph: " + stronglyConnectedComponentsCount);
                if (stronglyConnectedComponentsCount > 1) {
                    System.out.println("There are more than one strongly connected components in execution graph; this means the net is not reversible");
                } else {
                    System.out.println("There is exactly one strongly connected component in execution graph; this means the net is reversible");
                }
            } else {
                System.out.println("The net is NOT bounded");
            }
            
            if (deadlockFound) {
                System.out.println("Deadlock in the network found");
            } else if (isBounded){
                System.out.println("No deadlocks found. The net is deadlock-free");
            } else {
                System.out.println("No deadlocks found, but the net is not bounded; The net MIGHT BE deadlock-free");
            }
        } finally {
            this.petriNet.setNetworkMarking(originalMarking);
        }
        return null;
    }
    
    /**
     * Internal use only.
     * 
     * DFS method for constructing the tree.
     * 
     * @warning Changes state of the network and DOES NOT restore it.
     * 
     * @param vertex Vertex to be analysed.
     * @param dfsPathMarkings List of markings encountered during the search - from the root (inclusive) to supplied vertex (not inclusive)
     */
    private void dfsTreeConstruction(GraphVertex vertex, LinkedList<ArrayList<Integer>> dfsPathMarkings) {
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
                        isBounded = false;
                        break;
                    }
                }
                
                vertex.addExit(transition, newVertex);
                
                GraphVertex oldVertex = vertices.get(newVertex.getMarking());
                if (oldVertex != null) {
                    newVertex.markAsCopyOf(oldVertex);
                } else {
                    vertices.put(newVertex.getMarking(), newVertex);
                    dfsTreeConstruction(newVertex, dfsPathMarkings);
                }
            }
        }
        
        if (vertex.getExitTransitions().isEmpty()) {
            deadlockFound = true;
        }
        
        dfsPathMarkings.removeLast();
        vertex.colour = GraphVertex.Colour.BLACK;
    }
    
    /**
     * Convenience method for the above one.
     */
    private void dfsTreeConstruction(GraphVertex vertex) {
        dfsTreeConstruction(vertex, new LinkedList<ArrayList<Integer>>());
    }
    
    /**
     * Recurisvely outputs the tree.
     * 
     * @param vertex
     */
    // FIXME: to be rewritten
    private void recursiveOutput(GraphVertex vertex) {
        recursiveOutput(vertex, 0);
    }
    
    /**
     * Recurisvely outputs the tree.
     * 
     * @param vertex
     */
    // FIXME: to be rewritten
    private void recursiveOutput(GraphVertex vertex, int level) {
        String prefix = "";
        for(int i =0; i< level; i++) {
            prefix = prefix + "    ";
        }
        System.out.println(prefix + vertex);
        
        for (Transition t : vertex.getExitTransitions()) {
            System.out.println(prefix + t.getName() + "->" );
            recursiveOutput(vertex.getExit(t), level+1);
        } 
    }
    
    /**
     * Folds the tree into a graph (by removing old/copy vertices).
     */
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
 
    /**
     * Finds the strongly connected components of the execution graph.
     */
    private void stronglyConnectedComponents() {
        HashMap<ArrayList<Integer>, GraphVertex> transposed = new HashMap<ArrayList<Integer>, GraphVertex>();
        for (ArrayList<Integer> arrlist : vertices.keySet()) {
            transposed.put(arrlist, new GraphVertex(arrlist));
            vertices.get(arrlist).colour = GraphVertex.Colour.WHITE;
        }
        
        for (ArrayList<Integer> arrlist : vertices.keySet()) {
            GraphVertex from = transposed.get(arrlist);
            GraphVertex originalFrom = vertices.get(arrlist);
            for (Transition t : originalFrom.getExitTransitions()) {
                GraphVertex to = transposed.get(originalFrom.getExit(t).getMarking());
                to.addExit(t, from);
            }
        }
        // got transposed graph here and cleared the colours.
        
        
        LinkedList<ArrayList<Integer>> visitTimes = new LinkedList<ArrayList<Integer>>();
        
        for (ArrayList<Integer> arrlist : vertices.keySet()) {
            dfsTimestamping(vertices.get(arrlist), visitTimes);
        }
        
        System.out.println("Order:");
        for (ArrayList<Integer> list : visitTimes) {
            System.out.print(vertices.get(list));
        }
        System.out.println("--");
        
        
        HashMap<ArrayList<Integer>, Integer> sssnumbers = new HashMap<ArrayList<Integer>, Integer>();
        int sssId = 0;
        
        
        for (ArrayList<Integer> list : visitTimes) {
            if (dfsDoSSC(transposed.get(list), sssnumbers, sssId)) {
                sssId++;
            }
        }
        
        for (ArrayList<Integer> arrlist: sssnumbers.keySet()) {
            System.out.println(transposed.get(arrlist) + " " + sssnumbers.get(arrlist));
            
        }
        transposed = null;
        visitTimes = null;
        stronglyConnectedComponentsCount = sssId;
    }

    /**
     * DFS used by ssc algorithm in timestamping phase
     * @param gv
     * @param visitTimes
     */
    private void dfsTimestamping(GraphVertex gv, LinkedList<ArrayList<Integer>> visitTimes) {
        if (gv.colour != GraphVertex.Colour.WHITE) {
            return;
        }
        
        gv.colour = GraphVertex.Colour.GRAY;
        
        for (Transition t : gv.getExitTransitions()) {
            GraphVertex newVertex = gv.getExit(t);
            dfsTimestamping(newVertex, visitTimes);
        }
        visitTimes.addFirst(gv.getMarking());
        gv.colour = GraphVertex.Colour.BLACK;        
    }

    /**
     * DFS used by ssc algorithm in second (enumeration) phase
     * 
     * @param vertex
     * @param sssnumbers
     * @param sssid
     * @return
     */
    private boolean dfsDoSSC(GraphVertex vertex, HashMap<ArrayList<Integer>, Integer> sssnumbers, int sssid) {
        if (vertex.colour != GraphVertex.Colour.WHITE) {
            return false;
        }
        
        vertex.colour = GraphVertex.Colour.GRAY;
        sssnumbers.put(vertex.getMarking(), sssid);
        
        for (Transition t : vertex.getExitTransitions()) {
            GraphVertex newVertex = vertex.getExit(t);
            dfsDoSSC(newVertex, sssnumbers, sssid);
        }
        
        vertex.colour = GraphVertex.Colour.BLACK;
        return true;
    }
}
