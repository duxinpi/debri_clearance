package test;

import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;


public class LPExample {


    /**
     * min 12 x + 15y
     * s.t.
     * x +  y >= 5
     * x + 3y  >= 9
     * 2x + y >= 6
     */
    public static void exampleModule() {

        IloCplex cplex = null;

        try {
            cplex = new IloCplex();
            // variable
            IloNumVar x = cplex.numVar(0, Double.MAX_VALUE, "x");
            IloNumVar y = cplex.numVar(0, Double.MAX_VALUE, "y");

            // expression
            IloLinearNumExpr obj = cplex.linearNumExpr();
            obj.addTerm(12, x);
            obj.addTerm(15, y);
            // this above is the expression we want to resolv.e
            // define objective.
            cplex.addMinimize(obj); // min(12x + 15y)
            cplex.addGe(cplex.sum(cplex.prod(1, x), cplex.prod(60, y)), 5);
            cplex.addGe(cplex.sum( x, cplex.prod(3, y)), 9);
            cplex.addGe(cplex.sum(cplex.prod(2, x), y), 6);

            if (cplex.solve()){

                System.out.println("obj = " + cplex.getObjValue());
                System.out.println("x = " + cplex.getValue(x));
                System.out.println("y = " + cplex.getValue(y));

            }






        } catch (IloException e) {
            e.printStackTrace();
        }

    }

    public static void main(String args[]) throws Exception {
        exampleModule();



    }




}
