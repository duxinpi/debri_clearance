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
      //  graph.initUPrime();
        graph.initBlocked();
        graph.initUnblocked();
        graph.initRB();
/*

        try {
         //   getZ( graph, 0, 0);
        } catch (IloException e) {
            e.printStackTrace();
        }
*/

    }






}
