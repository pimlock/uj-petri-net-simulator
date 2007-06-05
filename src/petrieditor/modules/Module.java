package petrieditor.modules;

import petrieditor.model.PetriNet;

/**
 * Interfejs dla modulow.
 *
 * @author wiktor
 */

public interface Module {

    /** 
     * @return Zwraca nazwe modulu.
     */
    public String getName();

    /**
     * Odpala modul, ktory wykonuje obliczenia na podstawie modelu sieci Petriego.
     *
     * @param petriNet Model sieci Petriego, na ktorym modul bedzie operowac.
     * @return Zwraca rezultat.
     */
    public ResultPane run(PetriNet petriNet);
}
