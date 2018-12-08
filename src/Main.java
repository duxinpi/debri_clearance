import CPLEXUtil.cplexutils.CplexParameterSetter;
import SDCP.*;
import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloNumVarType;
import ilog.cplex.IloCplex;

import java.io.IOException;
import java.util.Arrays;

import static SDCP.Utility.*;
import static org.junit.Assert.fail;

public class Main {


    public static void main(String args[]) {

        System.out.print("Run program ...");
        Graph graph = new Graph();
        graph.initNodes();
        graph.initEdges();
        graph.initUPrime();
        graph.initBlocked();
        graph.initUnblocked();
        graph.initRB();

        try {
            getZ( graph, 0);
        } catch (IloException e) {
            e.printStackTrace();
        }

    }







    public static double getRD(double prevRd, double x) {
        return prevRd - x;
    }





    public static void updateTplusOne(Graph graph, int t,  double W[], double S[], double[]D, double [][]F, double K[], int kt1, BState previousBState) {
        BState s1 = new BState(t+1);
        for (Node node : graph.getN()) {
           // node.setRS(t+1, node.getRs(t) -  );
            node.setRD(t+1, node.getRd(t) -K[node.getId() -1]);
        }
        StateManager.getInstance().put(t, s1 );
    }




}
