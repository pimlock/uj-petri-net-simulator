package petrieditor.io;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import petrieditor.model.PetriNet;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author wiktor
 */
public class IOHelper {

    public static void save(PetriNet petriNet, File outputFile) {
        ObjectOutputStream out;
        try {
            out = new XStream().createObjectOutputStream(
                    new OutputStreamWriter(
                            new GZIPOutputStream(
                                    new FileOutputStream(outputFile))));
            out.writeObject(petriNet);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PetriNet load(File inputFile) {
        ObjectInputStream in;
        PetriNet ret = null;
        try {
            in = new XStream(new DomDriver()).createObjectInputStream(
                    new InputStreamReader(
                            new GZIPInputStream(
                                    new FileInputStream(inputFile))));
            ret = (PetriNet) in.readObject();
            in.close();
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static PetriNet cloneBySerializing(PetriNet petriNet) {
        PetriNet ret = null;
        
        StringWriter stringWriter = new StringWriter();

        try {
            ObjectOutputStream out = new XStream(new DomDriver()).createObjectOutputStream(stringWriter);
            out.writeObject(petriNet);
            out.flush();
            out.close();

            ObjectInputStream in = new XStream(new DomDriver()).createObjectInputStream(new StringReader(stringWriter.toString()));
            ret = (PetriNet) in.readObject();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();  
        }

        return ret;
    }

}
