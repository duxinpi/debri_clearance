package SDCP;

import java.util.List;

public class Reward {
    Observation observation;
    List<Edge> action;
    BState bState;
    public Reward(Observation observation, List<Edge> action, BState bState) {
        this.observation = observation;
        this.bState = bState;
        this.action = action;
    }
}
