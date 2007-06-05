package petrieditor.modules;

import javax.swing.*;
import java.awt.*;

/**
 * @author wiktor
 */
public class HTMLResultPane extends ResultPane {

    public HTMLResultPane(String htmlDocument) {
        setLayout(new BorderLayout());
        JEditorPane results = new JEditorPane();
        results.setEditable(false);
        results.setMargin(new Insets(5,5,5,5));
        results.setContentType("text/html");
        results.setText(htmlDocument);
        add(new JScrollPane(results), BorderLayout.CENTER);
    }
}
