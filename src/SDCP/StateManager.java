package SDCP;

import java.util.HashMap;

public class StateManager extends HashMap<Integer, BState>{
    private static StateManager stateManager;


    private StateManager() {
    }

    public synchronized static StateManager getInstance() {
        if (stateManager == null) {
            stateManager = new StateManager();
        }
        return stateManager;
    }
}
