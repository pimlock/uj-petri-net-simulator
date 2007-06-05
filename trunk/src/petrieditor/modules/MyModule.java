package petrieditor.modules;

import petrieditor.model.PetriNet;

public class MyModule implements Module {
    
    private static final String MODULE_NAME = "My module";

    public String getName() {
        return MODULE_NAME;
    }


    public ResultPane run(PetriNet petriNet) {

        // wykonuje jakies obliczenia na modelu petriNet

        // niech na razie modul wypisuje wyjscie na wyjscie standardowe :)
        // bo pewnie docelowo bedzie tak, ze modul bedzie wyswietlac rezultaty
        // w postaci HTML, ale musze jeszcze pomyslec, jak to graficznie bedzie wygladac

        return null;
    }
}
