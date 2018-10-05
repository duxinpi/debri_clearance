package test;

import ilog.concert.*;
import ilog.cplex.*;
public class Parking {
    public static void main(String[] args) {
        int n = 2; // number of parking
        int m = 2; // number of vehicle
        int k = 2; // number of intervals
        //dij
        double[][] distance = {{1, 1}, {1, 1}};
        //cik
        double[][] capacity = {{3, 3}, {3, 3}};
        //rjk
        double[][] interval= {{1, 1}, {1, 1}};
        //sik
        double[][] available = {{1, 1}, {1, 1}};

        double penalty = 0.5;
        double request = 10;
        solveModel(n,m,k, distance, capacity, interval,available,penalty,request);
    }




    public static void solveModel(int n, int m, int k, double[][] distance,double[][] capacity,double[][] interval,double[][] available,
                                  double penalty, double request) {
        try {
            // Define an empty model
            IloCplex model = new IloCplex();
            IloNumVar[][] x = new IloNumVar[n][m];
            for(int i = 0; i < n; i++) {
                for(int j = 0; j < m; j++) {
                    x[i][j] = model.boolVar();

                }
            }
            // Define the objective function

            IloLinearNumExpr obj = model.linearNumExpr();

            for(int i = 0; i < n; i++) {
                for(int s = 0; s < k; s++) {
                    for(int j = 0; j < m; j++) {


                        obj.addTerm(distance[i][j]*interval[j][s], x[i][j]);
                    }
                }
            }
            for(int i = 0; i < n; i++) {
                for(int j = 0; j < m; j++) {

                    obj.addTerm(-penalty, x[i][j]);

                }
            }


            model.addMinimize(obj);
            // Define the constraints
            for(int s = 0; s < k; s++) {
                for(int i = 0; i < n; i++) {
                    IloLinearNumExpr constraint1 = model.linearNumExpr();
                    for(int j = 0; j < m; j++) {

                        constraint1.addTerm(interval[j][s], x[i][j]);
                    }
                    model.addLe(constraint1, capacity[i][s]*available[i][s]);
                }
            }

            for(int j = 0; j < m; j++) {
                IloLinearNumExpr constraint2 = model.linearNumExpr();
                for(int i = 0; i < n; i++) {


                    constraint2.addTerm(1, x[i][j]);
                    model.addEq(constraint2, 1);
                }
            }

            boolean isSolved = model.solve();
            if(isSolved) {
                double objValue = model.getObjValue();
                System.out.println("obj_val = " + objValue);
                for(int i = 0; i < n; i++) {
                    for(int j = 0; j < m; j++) {
                        System.out.println("x[" + i + "," + j + "] = " + model.getValue(x[i][j]));

                    }
                }
            } else {
                System.out.println("Model not solved :(");
            }

            // Free the memory
            model.end();
        } catch(IloException ex) {
            ex.printStackTrace();
        }

    }
}