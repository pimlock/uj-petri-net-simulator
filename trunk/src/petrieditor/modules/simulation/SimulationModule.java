package petrieditor.modules.simulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import petrieditor.model.PetriNet;
import petrieditor.model.Place;
import petrieditor.model.Transition;
import petrieditor.modules.HTMLResultPane;
import petrieditor.modules.Module;
import petrieditor.modules.ResultPane;

public class SimulationModule implements Module {
    private static final String MODULE_NAME = "Simulation module";
    private SimulationDialog dialog;
    private int iloscOdpalen;
    private boolean losoweOdpalenia;
    private boolean result;
    
    public String getName() {
        return MODULE_NAME;
    }
    
    public ResultPane run(PetriNet petriNet) {
        createAndShowDialog();
        
        if (result) {
            double all[] = new double[petriNet.getPlaces().size()];
            int j = 0;
            for (int i = 0; i < iloscOdpalen; i++, j++) {
                for (int k = 0; k < petriNet.getPlaces().size(); k++)
                    all[k] += petriNet.getPlaces().get(k).getCurrentMarking();
                List<Transition> enabledTransitions = new ArrayList<Transition>();
                for (Transition transition : petriNet.getTransitions())
                    if (transition.isEnabled())
                        enabledTransitions.add(transition);
                
                if (enabledTransitions.size() == 0)
                    break;
                
                if (losoweOdpalenia)
                    Collections.shuffle(enabledTransitions);
                    
                enabledTransitions.get(0).fire();
            }
            StringBuffer sb = new StringBuffer();
            if (j != iloscOdpalen)
                sb.append("Deadend: Odpalano " + j + " tranzycji i zadna tranzycja nie jest aktywna<br/><br/>");
            sb.append("Stan sieci po symulacji:<br/>");
            sb.append("<table border=\"1\" cellspacing=\"1\">");
            for (int i = 0; i < petriNet.getPlaces().size(); i++) {
                sb.append("<tr><td class=\"colhead\">" + petriNet.getPlaces().get(i).getName() + "</td>");
                sb.append("<td class=\"cell\">" + petriNet.getPlaces().get(i).getCurrentMarking() + "</td></tr>");
            }
            sb.append("</table><br/>Srednia ilosc tokenow w miejscach:<br/>");
            sb.append("<table border=\"1\" cellspacing=\"1\">");
            for (int i = 0; i < petriNet.getPlaces().size(); i++) {
                sb.append("<tr><td class=\"colhead\">" + petriNet.getPlaces().get(i).getName() + "</td>");
                sb.append("<td class=\"cell\">" + all[i] / j + "</td></tr>");
            }
            sb.append("</table><br/>");
            return new HTMLResultPane(sb.toString());
        }
        return null;
    }
    
    private void createAndShowDialog() {
        SimulationDialog simulationDialog = new SimulationDialog(null, true);
        simulationDialog.setVisible(true);
        
        if (simulationDialog.getResult()) {
            iloscOdpalen = simulationDialog.getIloscOdpalen();
            losoweOdpalenia = simulationDialog.getLosoweOdpalenia();
            result = true;
        }
    }
    
}