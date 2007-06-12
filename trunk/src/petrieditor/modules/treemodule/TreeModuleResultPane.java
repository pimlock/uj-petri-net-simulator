package petrieditor.modules.treemodule;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import petrieditor.model.Transition;
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
        if (resultSet.isBounded && !resultSet.rootVertex.getMarking().isEmpty()) {
            JButton testReachability = new JButton("Test reachability");
            
            testReachability.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String testedMarkingString = JOptionPane.showInputDialog(TreeModuleResultPane.this, "Enter " + TreeModuleResultPane.this.resultSet.rootVertex.getMarking().size() + " comma delimited integers", "Reachability test", JOptionPane.QUESTION_MESSAGE);
                    if (testedMarkingString == null) {
                        return;
                    }
                    try {
                        String[] parts = testedMarkingString.split("\\s*,\\s*");
                        ArrayList<Integer> testedMarking = new ArrayList<Integer>();
                        for (int i = 0; i < parts.length; i++) {
                            testedMarking.add(Integer.parseInt(parts[i]));
                        }
                        if (testedMarking.size() != TreeModuleResultPane.this.resultSet.rootVertex.getMarking().size()) {
                            JOptionPane.showMessageDialog(TreeModuleResultPane.this, "Invalid number of items entered", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if (TreeModuleResultPane.this.resultSet.vertices.keySet().contains(testedMarking)) {
                            JOptionPane.showMessageDialog(TreeModuleResultPane.this, "This marking is reachable", "Reachability test", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(TreeModuleResultPane.this, "This marking is NOT reachable", "Reachability test", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(TreeModuleResultPane.this, "Invalid numeric format", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } 
            });

            add(testReachability, BorderLayout.SOUTH);
            
        }
    }

    String getResultsText() {
        String text = "<html><head><title>Execution tree and graph analysis</title></head><body>";

        text += "<p>The net is "
                + (resultSet.isBounded ? getGreenText("bounded")
                        : getRedText("NOT bounded")) + "</p>";

        if (resultSet.stronglyConnectedComponentsCount > 0) { // działa tylko jeżeli sieć jest ograniczona. WPP tu jest wpisane 0.
            text += "<p>Number of strongly connected components in execution graph: "
                    + (resultSet.stronglyConnectedComponentsCount) + "</p>";

            if (resultSet.stronglyConnectedComponentsCount > 1) {
                text += "<p>There are more than one strongly connected components in execution graph; this means the net "
                        + getRedText("is not reversible") + "</p>";
            } else {
                text += "<p>There is exactly one strongly connected component in execution graph; this means the net "
                        + getGreenText("is reversible") + "</p>";
            }
        }

        if (resultSet.deadlockFound) {
            text += "<p>" + getRedText("Deadlock in the network found")
                    + "</p>";
        } else if (resultSet.isBounded) {
            text += "<p>"
                    + getGreenText("No deadlocks in the network found. As it is bounded this means it is deadlock-free")
                    + "</p>";
        } else {
            text += "<p>No deadlocks in the network found. As it is not bounded this means it "
                    + getGreenText("MIGHT BE deadlock-free") + "</p>";
        }
        
        if (resultSet.isBounded) {
            if (resultSet.allTransitionsReachableFromEverySCC) {
                text += "<p>The network is "+getGreenText("L4 live")+"</p>"; 
            } else if (resultSet.everyTransitionsInSomeSCC) {
                text += "<p>The network is "+getGreenText("L3 live")+"</p>";
            } else if (resultSet.allTransitionsReachable) {
                text += "<p>The network is "+getGreenText("L1 live")+"</p>";
            } else {
                text += "<p>The network is "+getRedText("L0 live")+"</p>";
            }
        } else {
            if (resultSet.allTransitionsReachable) {
                text += "<p>Every transition can be fired - so the network is at least " + getGreenText("L1-live") +"</p>";
            } else {
                text += "<p>The network is not boundned and sufficient condition for L1 liveness is not met. The net might be of any liveness class</p>";
            }
        }

        text += recursiveOutput(resultSet.rootVertex);
        System.out.println(recursiveOutput(resultSet.rootVertex));
        text += "</body></html>";

        return text;
    }

    String getGreenText(String text) {
        return "<span style='color:green; font-weight:bold'>" + text
                + "</span>";
    }

    String getRedText(String text) {
        return "<span style='color:red; font-weight:bold'>" + text + "</span>";
    }

    String recursiveOutput(GraphVertex vertex) {
        return recursiveOutput(vertex, null);
    }

    String recursiveOutput(GraphVertex vertex, String reachedBy) {
        String text = "<li>" + vertex;
        if (reachedBy != null) {
            text += "(by " + reachedBy + ")";
        }
        if (!vertex.getExitTransitions().isEmpty()) {
            text += "<ul>";
            for (Transition t : vertex.getExitTransitions()) {
                text += recursiveOutput(vertex.getExit(t), t.getName());
            }
            text += "</ul>";
        }

        text += "</li>";

        return text;
    }
}
