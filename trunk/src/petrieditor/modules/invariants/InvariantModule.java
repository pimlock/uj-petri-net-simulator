package petrieditor.modules.invariants;

import petrieditor.model.Arc;
import petrieditor.model.PetriNet;
import petrieditor.model.Place;
import petrieditor.model.Transition;
import petrieditor.modules.HTMLResultPane;
import petrieditor.modules.Module;
import petrieditor.modules.ResultPane;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Piotr M�ocek
 */
public class InvariantModule implements Module {
    private static final String MODULE_NAME = "Invariant module";
    private String[] placesNames;
    private Place[] places;
    private String[] transitionsNames;
    private PetriNet petriNet;
    
    public String getName() {
        return MODULE_NAME;
    }

    public ResultPane run(PetriNet petriNet) {  
        this.petriNet = petriNet;
        
        int[][] incidence = getIncidenceMatrix();
        
        new Matrix(incidence).print(1, 0);
        Matrix p = Invariants.computePInvariants(incidence);
        System.out.println(p.getRowDimension() + " x " + p.getColumnDimension());
        if(p.getRowDimension() == 0 || p.getColumnDimension() == 0) {
            System.out.println("Brak p-niezmiennikow");
        } else p.print(1, 0);
        
        Matrix t = Invariants.computeTInvariants(incidence);
        System.out.println(t.getRowDimension() + " x " + t.getColumnDimension());
        if(t.getRowDimension() == 0 || t.getColumnDimension() == 0) {
            System.out.println("Brak t-niezmiennikow");
        } else t.print(1, 0);
        
        JFrame frame = new JFrame("Invariants");
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.setLayout(new GridLayout(1, 1));
        
        JEditorPane results = new JEditorPane();
        results.setEditable(false);
        results.setMargin(new Insets(5,5,5,5));
        results.setContentType("text/html");
        
        JScrollPane scroller=new JScrollPane(results);
        scroller.setPreferredSize(new Dimension(400,300));

        frame.add(scroller);
        
        results.setText(printResults(incidence, t, p));
        
        frame.pack();
        //frame.setVisible(true);

        return new HTMLResultPane(printResults(incidence, t, p)); 
    }
    
    private int[][] getIncidenceMatrix() {
        List<Transition> transitions = petriNet.getTransitions();
        List<Place> places = petriNet.getPlaces();
        
        int placesCount = places.size();
        int transitionsCount = transitions.size();
        
        int[][] incidence = new int[placesCount][transitionsCount];
        placesNames = new String[placesCount];
        this.places = new Place[placesCount];
        transitionsNames = new String[transitionsCount];
        
        Map<Place, Integer> placeToNumber = new HashMap<Place, Integer>();
        
        int number = 0;
        for(Place place : places) {
            placesNames[number] = place.getName();
            this.places[number] = place;
            
            placeToNumber.put(place, number);
            ++number;
        }
        
        number = 0;
        for(Transition transition : transitions) {
            System.out.println(transition.getName());
            transitionsNames[number] = transition.getName();
            
            List<Arc> input = transition.getInputArcs();
            List<Arc> output = transition.getOutputArcs();
            
            for(Arc arc : input) {
                incidence[placeToNumber.get(arc.getPlace())][number] -= arc.getWeight();
            }
            
            for(Arc arc : output) {
                incidence[placeToNumber.get(arc.getPlace())][number] += arc.getWeight();
            }
            ++number;
        }
        
        return incidence;
    }
    
    private String printMatrix(Matrix matrix, String[] captions) {
        StringBuffer html = new StringBuffer();
        
        if(matrix.getRowDimension() == 0 || matrix.getColumnDimension() == 0) {
            html.append("<div>None<br /></div>");
        }
        else {
            html.append("<table border=\"1\" cellspacing=\"1\">");

            for(int i = 0; i < matrix.getRowDimension(); ++i) {
                html.append("<tr><td class=\"colhead\">" + captions[i] + "</td>");

                for(int j = 0; j < matrix.getColumnDimension(); ++j) {
                    html.append("<td class=\"cell\">" + matrix.get(i, j) + "</td>");
                }

                html.append("</tr>");
            }
            html.append("</table>");
        }
        
        return html.toString();
    }
    
    private boolean isCovered(Matrix matrix) {
        if(matrix.getColumnDimension() == 0 || matrix.getRowDimension() == 0) 
            return false;
        
        for(int i = 0; i < matrix.getRowDimension(); ++i) {
            boolean cover = false;
            for(int j = 0; j < matrix.getColumnDimension(); ++j) {
                cover |= (matrix.get(i, j) != 0);
            }
            if(!cover) return false;
        }
        return true;
    }
    
    private String printResults(int[][] incidence, Matrix t, Matrix p) {
        StringBuffer html = new StringBuffer();
        html.append(
            "<html><head><style type=\"text/css\">" +
                "body {font-family:Verdana,sans-serif;text-align:center;background:#ffffff}" +
                "td.colhead {font-weight:bold;text-align:center;background:#ffffff}" +
                "td.rowhead {font-weight:bold;background:#ffffff}"+
                "td.cell {text-align:center;padding:10px,5px}" +
                "tr.even {background:#b0b0b0}" +
                "tr.odd {background:#d0d0d0}" +
                "td.empty {background:#ffffff}" +
            "</style><body>"
        );
        html.append("<h3>Incidence matrix:</h3>");
        
        html.append("<table border=\"1\" cellspacing=\"1\"><tr><td>&nbsp;</td>");
        
        for(int i = 0; i < transitionsNames.length; ++i) {
            html.append("<td class=\"rowhead\">"+transitionsNames[i]+"</td>");
        }
        
        html.append("</tr>");
        for(int i = 0; i < incidence.length; ++i) {
            html.append("<tr class=\"" + (((i%2)==1)?"odd":"even") + "\"><td class=\"colhead\">" + placesNames[i] + "</td>");
            
            for(int j = 0; j < incidence[i].length; ++j) {
                html.append("<td class=\"cell\">"+incidence[i][j]+"</td>");
            }
            
            html.append("</tr>");
        }
        html.append("</table>");
        
        // t - niezmienniki
        html.append("<h3> T - Invariants: </h3>");
        
        html.append(printMatrix(t, transitionsNames));
        if(isCovered(t)) {
            html.append("<br />The net is covered by positive T-Invariants, therefore it might be bounded and live.");
        } else {
            html.append("<br />The net is <strong>not</strong> covered by positive T-Invariants, therefore we do not know if it is bounded and live.");
        }
        
        html.append("<h3> P - Invariants: </h3>");
        
        html.append(printMatrix(p, placesNames));
        if(isCovered(p)) {
            html.append("<br />The net is covered by positive P-Invariants, therefore it is bounded.");
        } else {
            html.append("<br />The net is <strong>not</strong> covered by positive P-Invariants, therefore we do not know if it is bounded.");
        }
        
        html.append(printEquations(p, placesNames));
        
        html.append("</body></html>");
        return html.toString();
    }

    private String printEquations(Matrix p, String[] placesNames) {
        StringBuffer html = new StringBuffer();
        html.append("<br /><h4>Net equations:</h4>");
        
        if(p.getColumnDimension() != 0) {
            List<Integer> marking = petriNet.getNetworkMarking();
            Map<Place, Integer> placeToTokenCount = new HashMap<Place, Integer>();
            
            int placeNumber = 0;
            for(Integer tokenCount : marking) {
                placeToTokenCount.put(places[placeNumber], tokenCount);
                ++placeNumber;
            }
            
            boolean first = true;
            for(int i = 0; i < p.getColumnDimension(); ++i) {
                
                int tokens = 0;
                int number = 0;
                for(int j = 0; j < p.getRowDimension(); ++j) {
                    if((number = p.get(j, i)) != 0) {
                        html.append((first?"":" + ") + number + " * M(" + placesNames[j] + ")");
                        tokens += number * placeToTokenCount.get(places[j]);
                    }
                }
                html.append(" = "+tokens+"</br>");
                
            }
        }
        return html.toString();
    }
}
