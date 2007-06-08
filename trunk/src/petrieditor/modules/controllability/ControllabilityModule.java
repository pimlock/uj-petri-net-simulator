/*
 * ControllabilityModule.java
 *
 * Created on 8 czerwiec 2007, 00:43
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package petrieditor.modules.controllability;

import java.util.Iterator;
import java.util.List;
import petrieditor.model.Arc;
import petrieditor.model.PetriNet;
import petrieditor.model.Place;
import petrieditor.model.Transition;
import petrieditor.modules.HTMLResultPane;
import petrieditor.modules.Module;
import petrieditor.modules.ResultPane;

/**
 *
 * @author rw
 */
public class ControllabilityModule implements Module
{
  
  /** Creates a new instance of ControllabilityModule */
  /*public ControllabilityModule ()
  {
  }*/
  
  public String getName() 
  {
    return "Controllability";
  }

  public ResultPane run (PetriNet pn) 
  {
    StringBuilder sb=new StringBuilder();
    sb.append ("<html><head></head><body>");
    int lt=pn.getTransitions().size ();
    int lm=pn.getPlaces().size ();
    if (lt < lm)
    {
      sb.append ("<p>The number of transitions (");
      sb.append (lt);
      sb.append (") less than the number of places (");
      sb.append (lm);
      sb.append ("), therefore the net is not controllable.</p>");
    }
    else
    {
      Macierz m=zbuduj_macierz (pn);
      Macierz m2=new Macierz(m); 
      int rz=m2.rzad ();
      if (rz < lm)
      {
        sb.append ("<p>The rank of the incidence matrix (");
        sb.append (rz);
        sb.append (") less than the number of places (");
        sb.append (lm);
        sb.append ("), therefore the net is not controllable.</p>");
      }
      else
      {
        sb.append ("<p>The rank of the incidence matrix (");
        sb.append (rz);
        sb.append (") equal to the number of places");
        //sb.append (lm);
        sb.append (", therefore the net might be controllable.</p>");
      }      
      sb.append ("<p>The incidence matrix:</p>");
      pisz_macierz (sb, m);
      //System.out.println ("The incidence matrix after modified Gaussian elimination:");
      //pisz_macierz (m2);
    }
    sb.append ("</body></html>");
    return new HTMLResultPane(sb.toString());
  }
  
  private Macierz zbuduj_macierz (PetriNet pn)
  {
    List<Transition> tt=pn.getTransitions (); //t. tranzycji
    List<Place> tm=pn.getPlaces (); //t. miejsc
    int lt=tt.size (); //l. tranzycji
    int lm=tm.size (); //l. miejsc
    Macierz m=new Macierz(lm,lt);
    int i=0; 
    Iterator<Place> j=tm.iterator (); 
    while (j.hasNext ()) j.next().setInitialMarking (i++);
    Iterator<Transition> k=tt.iterator (); 
    i = 0;
    Transition t;
    List<Arc> tk; //t. krawedzi
    Iterator<Arc> l;
    Arc _k;
    while (k.hasNext ())
    {
      t = k.next ();
      tk = t.getOutputArcs ();
      l = tk.iterator ();
      while (l.hasNext ()) 
      {
        _k = l.next();
        m.add (_k.getPlace().getInitialMarking(), i, _k.getWeight());        
      }
      tk = t.getInputArcs ();
      l = tk.iterator ();
      while (l.hasNext ())
      {
        _k = l.next();
        m.add (_k.getPlace().getInitialMarking(), i, -_k.getWeight());        
      }      
      i++;
    }    
    return m;
  }
  
  /*private void pisz_macierz (Macierz m)
  {
    int i, j;
    for (i=0; i<m.get_ry(); i++)
    {
      for (j=0; j<m.get_rx(); j++) System.out.print (m.get(i,j) + " ");
      System.out.println ();
    }
  }*/

  private void pisz_macierz (StringBuilder sb, Macierz m)
  {
    int i, j;
    sb.append ("<table cellspacing=\"4\">");
    for (i=0; i<m.get_ry(); i++)
    {
      sb.append ("<tr>");
      for (j=0; j<m.get_rx(); j++)
      {
        sb.append ("<td align=\"right\">");
        sb.append (m.get(i,j).toString());
        sb.append ("</td>");
      }
      sb.append ("</tr>");
    }
    sb.append ("</table>");
  }
}
