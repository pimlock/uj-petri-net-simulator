package petrieditor.modules.treemodule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import petrieditor.model.PetriNet;
import petrieditor.model.Transition;
import petrieditor.modules.Module;

public class TreeModule implements Module {
    private static final String MODULE_NAME = "Tree and graph module";

    public String getName() {
        return MODULE_NAME;
    }
    
    // permanent - stores results of analysis
    private GraphVertex rootVertex = null;
    private HashMap<ArrayList<Integer>, GraphVertex> vertices = null; // contains only "original vertices". "old vertices" are not included.
    private PetriNet petriNet = null;
    
    //transient:
    LinkedList<ArrayList<Integer>> dfsPathMarkings = null;
    
    synchronized public void run(PetriNet petriNet) {
        System.out.println("Tree module");
        
        this.petriNet = petriNet;
        
        ArrayList<Integer> originalMarking = this.petriNet.getNetworkMarking();
        try {
            this.petriNet.reset();
            
            rootVertex = new GraphVertex(petriNet.getNetworkMarking());
            vertices = new HashMap<ArrayList<Integer>, GraphVertex>();
            vertices.put(rootVertex.getMarking(), rootVertex);
            dfsPathMarkings = new LinkedList<ArrayList<Integer>>();
            
            dfsAnalyze(rootVertex); 
            recursiveOutput(rootVertex);
            
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
}
