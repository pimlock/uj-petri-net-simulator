package petrieditor.modules.treemodule;
import java.awt.BorderLayout;
import java.awt.Insets;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import petrieditor.modules.ResultPane;

public class TreeModuleResultPane extends ResultPane {
    private TreeModuleResultSet resultSet;
    
    public TreeModuleResultPane(TreeModuleResultSet resultSet) {
        super();
        this.resultSet = resultSet;

        setLayout(new BorderLayout());
        add(new JLabel("Execution tree and graph analysis"), BorderLayout.NORTH);
        
        JEditorPane results = new JEditorPane();
        results.setEditable(false);
        results.setMargin(new Insets(5,5,5,5));
        results.setContentType("text/html");
        results.setText(getResultsText());
        add(new JScrollPane(results), BorderLayout.CENTER);
    }
    
    String getResultsText() {
        String text = "<html><head><title>Execution tree and graph analysis</title></head><body>";
        
        text += "<p>The net is " +(resultSet.isBounded ? getGreenText("bounded"): getRedText("NOT bounded"))+ "</p>";
        
        if (resultSet.stronglyConnectedComponentsCount > 0) {
            text += "<p>Number of strongly connected components in execution graph: " +(resultSet.stronglyConnectedComponentsCount)+ "</p>";
            
            if (resultSet.stronglyConnectedComponentsCount > 1) {
                text += "<p>There are more than one strongly connected components in execution graph; this means the net "+ getRedText("is not reversible")+"</p>";
            } else {
                text += "<p>There is exactly one strongly connected component in execution graph; this means the net " + getGreenText("is reversible")+"</p>";
            }    
        }
        
        if (resultSet.deadlockFound) {
            text += "<p>"+getRedText("Deadlock in the network found")+"</p>";
        } else if (resultSet.isBounded){
            text += "<p>"+getGreenText("No deadlocks in the network found. As it is bounded this means it is deadlock-free")+"</p>";
        } else {
            text += "<p>No deadlocks in the network found. As it is not bounded this means it " + getGreenText("MIGHT BE deadlock-free") + "</p>";
        }

        /*
        private void recursiveOutput(GraphVertex vertex) {
            recursiveOutput(vertex, 0);
        }
        
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
        }*/
        
        
        text += "</body></html>";
        
        return text;
    }
    
    String getGreenText(String text) {
        return "<span style='color:green; font-weight:bold'>"+text+"</span>";
    }
    
    String getRedText(String text) {
        return "<span style='color:red; font-weight:bold'>"+text+"</span>";
    }
}
