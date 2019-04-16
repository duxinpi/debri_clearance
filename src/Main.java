import CPLEXUtil.cplexutils.CplexParameterSetter;
import SDCP.*;

import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloNumVarType;
import ilog.cplex.IloCplex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static SDCP.Utility.*;
import static org.junit.Assert.fail;

public class Main {


    public static List<List<Integer>> getFactors(int n) {
        List<List<Integer>> res = new ArrayList<>();
        if (n<=1){
            return res;
        }
        List<Integer> cur = new ArrayList<>();
        dfs( n, cur,res, 2);
        return res;

    }


    public static void dfs(int n, List<Integer> cur, List<List<Integer>>res, int k) {

        if (n <=1) {
            System.out.println(cur.size());
            res.add(new ArrayList<Integer>(cur));
            return ;
        }
        for (int i = k; i<n; i++) {
            if (n%i == 0) {
                cur.add(i);
                System.out.println(cur.size());
                dfs( n/i, cur,res, i);
                cur.remove(cur.size() -1);
            }
        }
    }

    public static void main(String args[]) {



        String[] source = {"/*Test program */", "int main()", "{ ", "  // variable declaration ", "int a, b, c;", "/* This is a test", "   multiline  ", "   comment for ", "   testing */", "a = b + c;", "}"};



        System.out.print(getFactors( 12));
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
