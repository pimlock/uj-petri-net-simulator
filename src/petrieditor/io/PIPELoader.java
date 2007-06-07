package petrieditor.io;

import java.awt.Point;
import java.io.File;
import java.util.Map;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import petrieditor.model.*;

/**
 *
 * @author psuliga
 */
public class PIPELoader {

    public PIPELoader() {
        
    }

    public static PetriNet load(File file) {        
        Map<String, Place> placesMap = new HashMap<String, Place>();
        Map<String, Transition> transitionsMap = new HashMap<String, Transition>();
        
        PetriNet petriNet = new PetriNet();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();
            
            NodeList nodeList = (NodeList)xpath.evaluate("/pnml/net/place", document, XPathConstants.NODESET);
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                String id = node.getAttributes().getNamedItem("id").getNodeValue();
                String name = (String)xpath.evaluate("name/value", node, XPathConstants.STRING);
                int x = Double.valueOf((String)xpath.evaluate("graphics/position/@x", node, XPathConstants.STRING)).intValue();
                int y = Double.valueOf((String)xpath.evaluate("graphics/position/@y", node, XPathConstants.STRING)).intValue();                
                int initialMarking = Integer.valueOf((String)xpath.evaluate("initialMarking/value", node, XPathConstants.STRING));
                
                Place place = petriNet.addNewPlace(new Point(x, y), name);
                place.setInitialMarking(initialMarking);
                placesMap.put(id, place);
            }
            
            nodeList = (NodeList)xpath.evaluate("/pnml/net/transition", document, XPathConstants.NODESET);
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                
                String id = getAttribute(node, "id");
                String name = (String)xpath.evaluate("name/value", node, XPathConstants.STRING);
                int x = Double.valueOf((String)xpath.evaluate("graphics/position/@x", node, XPathConstants.STRING)).intValue();
                int y = Double.valueOf((String)xpath.evaluate("graphics/position/@y", node, XPathConstants.STRING)).intValue();                                
                
                Transition transition = petriNet.addNewTransition(new Point(x, y), name);                               
                transitionsMap.put(id, transition);
            }
            
            nodeList = (NodeList)xpath.evaluate("/pnml/net/arc", document, XPathConstants.NODESET);
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                
                String id = getAttribute(node, "id");
                String source = getAttribute(node, "source");
                String target = getAttribute(node, "target");
                int weight = Integer.valueOf((String)xpath.evaluate("inscription/value", node, XPathConstants.STRING));
                                
                if (placesMap.containsKey(source)) {
                    petriNet.connectWithArc(placesMap.get(source), transitionsMap.get(target), weight);
                } else {
                    petriNet.connectWithArc(transitionsMap.get(source), placesMap.get(target), weight);
                }                                
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return petriNet;
    }
    
    private static String getAttribute(Node node, String attributeName) {
        return node.getAttributes().getNamedItem(attributeName).getNodeValue();
    }
}
