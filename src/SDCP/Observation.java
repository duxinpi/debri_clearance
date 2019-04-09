package SDCP;


import java.util.ArrayList;
import java.util.List;
import static SDCP.GlobalData.*;

public class Observation extends ArrayList<Edge> {
    List<Edge> observation;
    List<Edge> B = new ArrayList<>();

    List<Edge> U = new ArrayList<>();
    List<Edge> UB = new ArrayList<>();
    List<Edge> RB = new ArrayList<>();
    List<Edge> E;
    List<Node> N;
    int t;
    BState bState;
    List<Edge> action;
    List<List<Edge>> Uprime = new ArrayList<>();
    double K[];

    public Observation(List<Edge> observation, List<Edge> action, BState bState, List<Edge> E, List<Node> N, int t) {

        this.E = E;
        this.N = N;
        this.observation = observation;
        this.t = t;
        this.bState = bState;
        K = new double[N.size()];
        initBlocked();
        initUnblocked();
        initRB();
        initUB();
        this.action = action;
    }

    public void initBlocked() {
        for (Edge e : E) {
            for(int i =0; i <levels;i++ ) {
                boolean isBlocked = false;
                if (e.W_ij.get(t)[i] > 0) {
                    isBlocked = true;
                }
                if(isBlocked) {
                    B.add(e);
                }
            }
        }
    }

    public void initUnblocked() {
        U = Utility.exclude(E, B);
    }

    public void initRB() {
         RB = Utility.findAllReachableBlock(U, E, N);
    }

    public void initUB() {
        UB = Utility.exclude(B, RB);
    }

    public void updateEdges() {
        t = t + 1;
        //update U
        for (int i = 0; i < action.size(); i++) {
            if (!U.contains(action.get(i))) {
                U.add(action.get(i));
            }
        }
        //update B
        for (int i = 0; i < E.size(); i++) {
            boolean contains = false;
            for (int j = 0; j < U.size(); j++) {
                if (U.get(j).equals(E.get(i))) {
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                B.remove(E.get(i));
            }
        }
        //update RB
        for (int i = 0; i < observation.size(); i++) {
            RB.add(observation.get(i));
        }//
        RB = Utility.exclude(RB, action);
        //update UB
        UB = Utility.exclude(B, RB);
    }
    public void update(){
        updateEdges();
        updateBstate();
    }

    public void updateBstate() {
        for (Edge e: UB){
            e.beta[0] = e.beta[0];
            e.beta[1] = e.beta[1];
        }

        // todo: update beta for RB.
    }
/*
    public List<List<Edge>> getUprimeList(){
        List<List<Edge>> allCombinations = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();
        Utility.getAllSequences(edges, action, allCombinations, 0);
        return allCombinations;
    }

    public List<Edge> getUprimeNext(List<Edge> uprime){
        List<Edge> resultSet = Utility.exclude(observation, uprime);
        return resultSet;
    }*/

    public List<Edge> getUprime(){
        return action;
    }
    public List<Edge> getUprimeNext(){
        return Utility.exclude(observation, action);
    }


}
