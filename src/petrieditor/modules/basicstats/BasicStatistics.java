package petrieditor.modules.basicstats;

import petrieditor.model.PetriNet;
import petrieditor.modules.HTMLResultPane;
import petrieditor.modules.Module;
import petrieditor.modules.ResultPane;

public class BasicStatistics implements Module {
    
    private static final String MODULE_NAME = "Basic statistics";

    public String getName() {
        return MODULE_NAME;
    }


    public ResultPane run(PetriNet petriNet) {
        int places = petriNet.getPlaces().size();
        int transitions = petriNet.getTransitions().size();
        
        StringBuilder sb = new StringBuilder();
        
        sb.append("<h3> Basic statistics </h3>");
        sb.append("Places count: " + places + "<br>");
        sb.append("Transitions count: " + transitions + "<br>");
        
        return new HTMLResultPane(sb.toString()); 
    }
}
