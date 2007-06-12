package petrieditor.modules.treemodule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import petrieditor.model.PetriNet;
import petrieditor.model.Transition;

/**
 * A structure which represents results of execution tree and graph analysis.
 * 
 * Also used during analysis to store temporary results - that's why direct access is used instead of clean getter/setter solution
 * 
 * This class should never be exposed outside the module (unless some great refactoring is done) because of the above reasons.
 * 
 * @author pawel
 */
class TreeModuleResultSet {
    // the net for which the calculations are/were done
    PetriNet petriNet = null;
    
    // the root vertex.
    GraphVertex rootVertex = null;
    
    // contains only "original vertices". "old vertices" are not included.
    HashMap<ArrayList<Integer>, GraphVertex> vertices = new HashMap<ArrayList<Integer>, GraphVertex>();
    
    // self explainatory
    boolean isBounded = true;    
    
    // self explainatory (this only tells if deadlock state was encountered; if the net is not bounded it's not an IFF)
    boolean deadlockFound = false;
    
    // self explainatory. 0 means SCC analysis was not done.
    int stronglyConnectedComponentsCount = 0;
    
    // Reachable transitions (they are sure to be L1; in bounded nets only those are L1)
    HashSet<Transition> reachableTransitions = new HashSet<Transition>();

    // for bounded - iff for L1, for unbounded - sufficient for L1.
    boolean allTransitionsReachable = false;
    
    //this means L4 for bounded nets. For unbounded it is meaningless
    boolean allTransitionsReachableFromEverySCC = false;
    
    //this means L3 for bounded nets. For unbounded it is meaningless
    boolean everyTransitionsInSomeSCC = false;
    
    TreeModuleResultSet(PetriNet net) {
        this.petriNet = net;
        
        rootVertex = new GraphVertex(petriNet.getNetworkMarking());
        vertices.put(rootVertex.getMarking(), rootVertex);
    }
}
