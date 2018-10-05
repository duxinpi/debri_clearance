package SDCP;

import java.util.List;

public class StateManager {
    private static StateManager stateManager;
    List<Node> N;
    List<Edge> E;


    List<Edge> edges;


    private StateManager() {

    }

    public synchronized static StateManager getInstance() {
        if (stateManager == null) {
            stateManager = new StateManager();
        }
        return stateManager;
    }


}
