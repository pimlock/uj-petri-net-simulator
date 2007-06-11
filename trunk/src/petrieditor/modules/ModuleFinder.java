package petrieditor.modules;

import petrieditor.modules.controllability.ControllabilityModule;
import petrieditor.modules.invariants.InvariantModule;
import petrieditor.modules.simulation.SimulationModule;
import petrieditor.modules.treemodule.TreeModule;

import java.util.List;
import java.util.ArrayList;

/**
 * Zawiera liste zarejestrowanych modulow.
 *
 * @author wiktor
 */
public class ModuleFinder {

    public static List<Class> registredModules;
    public static List<String> registredModulesName;

    static {
        registredModules = new ArrayList<Class>();
        registredModulesName = new ArrayList<String>();

        registredModules.add(InvariantModule.class);
        registredModulesName.add(new InvariantModule().getName());

        registredModules.add(TreeModule.class);
        registredModulesName.add(new TreeModule().getName());

        registredModules.add(ControllabilityModule.class);
        registredModulesName.add(new ControllabilityModule().getName());
        
        registredModules.add(SimulationModule.class);
        registredModulesName.add(new SimulationModule().getName());        
    }

    public static List<Class> getModules() {
        return registredModules;
    }

    public static  List<String> getModulesName() {
        return registredModulesName;
    }

}
