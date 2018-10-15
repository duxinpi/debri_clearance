import CPLEXUtil.cplexutils.CplexParameterSetter;
import SDCP.*;
import ilog.concert.IloException;
import ilog.cplex.IloCplex;

import java.util.Arrays;

import static SDCP.Utility.Match;
import static SDCP.Utility.getSum;
import static SDCP.Utility.printArray;
import static org.junit.Assert.fail;

public class Main {


    public static void main(String args[]) {

        System.out.print("Run program ...");
        Graph graph = new Graph();
        graph.initNodes();
        graph.initEdges();
        graph.initUt();
        int N = graph.getNsize();
        int nX = 0;
        int nY = N*N;
        int nW = nY + (int) Utility.getSum(graph.getCj()); // what it is.
        int nS = nW + nY;
        int nD = nS + N;
        int nF = nD + N;
        int nK = nF + nY;
        int nKn = nK + N;
        char[] Ctype = new char[nKn];
        Arrays.fill(Ctype, 'C');


        double[][] LB = new double[nKn][1];

        double[][] UB = new double[nKn][1];
        for (int i = 0; i < UB.length; i++) {
            UB[i][0] = 10000;
        }
        Arrays.fill(Ctype, 0, nW, 'B');
        Arrays.fill(Ctype, nK, Ctype.length, 'I');
        //todo: need to verify.
        int t = 0;
        double[] Cj = graph.getCj();
        int rowsNumAb = N + N + N + N * ((int) Utility.getSum(Cj) - Cj.length) + N *N+ N + N + N;

        double[][] A = new double[rowsNumAb][nKn];
        double[] b = new double[rowsNumAb];
        int rowsNumAeq = N + N + 1 + N + N + N;
        double[][] Aeq = new double[rowsNumAeq][nKn];
        double[] beq = new double[rowsNumAeq];
        double[] f = new double[nKn];

        // objective
        for (Node each : graph.getNd()) {// what is this?
            f[each.getId() + nK -1] = -graph.getBiArray()[each.getId()-1];
        }
        for (Node i : graph.getNp()) {
            for(Node j: graph.getNp()) {
                int m = nX + (i.getId() -1)*N + j.getId() -1;
                int n = nW + (i.getId() -1)*N + j.getId() -1;
                System.out.println(m +"---" + n);

                f[nX + (i.getId() -1)*N + j.getId() -1] = i.getLamda() * graph.getPeriodT()[i.getId() -1][j.getId()-1];
                f[nW + (i.getId() -1)*N + j.getId() -1] = GlobalData.SIGMA * graph.getR()[i.getId()-1][j.getId()-1];
            }

        }
        printArray(f);


        // 1.2
        int row = 0;

        for (Node i : graph.getNd()) {

            for (Node j : graph.getN()) {
                int tempColumn = nF + (j.getId() - 1) * N + i.getId();
                int tempColumn2 = nF + (j.getId() - 1) * N + j.getId();
                if (Match(graph.getUt().get(t), j.getId(), i.getId())) {
                    Aeq[i.getId() -1][tempColumn -1] = 1;
                }
                if (Match(graph.getUt().get(0), i.getId(), j.getId())) {
                    Aeq[i.getId()-1][tempColumn2 -1] = -1;
                }
            }
            Aeq[i.getId()-1][nK + i.getId()-1] = -1;
            beq[i.getId()-1] = 0;

        }

        // todo: unitest


        //1.4


        row += graph.getNsize();

        for (Node i : graph.getNT()) {

            for (Node j : graph.getN()) {
                int tempColumn = nF + (j.getId() - 1) * N + i.getId();
                int tempColumn2 = nF + (j.getId() - 1) * N + j.getId();
                if (Match(graph.getUt().get(t + 1), j.getId(), i.getId())) {
                    Aeq[row + i.getId()-1][tempColumn-1] = 1;
                }
                if (Match(graph.getUt().get(0), i.getId(), j.getId())) {
                    Aeq[row + i.getId()-1][tempColumn2-1] = -1;
                }
            }

            beq[i.getId()-1] = 0;
        }

        // 1.10
        row += graph.getNsize();
        for (Node each : graph.getNp()) {
            for (int m = 1; m < Cj[each.getId() - 1]; m++) {
                if (each.getId() > 1) {
                    Aeq[row + 1 -1][nY + (int) getSum(Cj, 0, each.getId() - 1) + m -1] = 1;
                } else {
                    Aeq[row + 1 -1][nY + m -1] = 1;
                }
            }
        }

        beq[row + 1-1] = GlobalData.B;


        //1.11
        row++;

        for (Node i : graph.getNp()) {
            for (Node j : graph.getNp()) {
                Aeq[row + i.getId() - 1][nW + (i.getId()) * N + j.getId() - 1] = 1;
            }
            Aeq[row + i.getId() - 1][nS + i.getId() - 1] = -1;
            beq[row + i.getId() - 1] = 0;
        }


        //1.12
        row = row + N;

        for (Node j : graph.getNp()) {
            for (Node i : graph.getNp()) {
                Aeq[row + j.getId() - 1][nW + (i.getId() - 1) * N + j.getId() - 1] = 1;
            }
            Aeq[row + j.getId() - 1][nD + j.getId() - 1] = -1;
            beq[row + j.getId() - 1] = 0;
        }


        // 19 New Cons
        row = row + N;

        for (Node i : graph.getNp()) {
            for (Node j : graph.getNp()) {

                Aeq[row + i.getId() - 1][nX + (i.getId() - 1) * N + j.getId()-1] = 1;
            }
            beq[row + i.getId() - 1] = 1;
        }


        //1.3
        row = 0;
        for (Node i : graph.getNs()) {
            for (Node j : graph.getN()) {
                if (Match(graph.getUt().get(t), i.getId(), j.getId())) {
                    A[row + i.getId() - 1][nF + (i.getId() - 1) * N + j.getId() - 1] = 1;
                }
                if (Match(graph.getUt().get(t), j.getId(), i.getId())) {
                    A[row + i.getId() - 1][nF + (j.getId() - 1) * N + i.getId() - 1] = -1;
                }
            }
            b[row + i.getId() - 1] = i.getRs();

        }

        //1.5
        row = row + N;
        for (Node i : graph.getNd()) {
            A[row + i.getId() - 1][nK + i.getId() - 1] = 1;
            b[row + i.getId() - 1] = i.getRd();
        }


        //1.6

        row += N;

        for (Node j : graph.getNp()) {
            double temp = 0;
            for (Node i : graph.getN()) {
                if (Match(graph.getUt().get(t), i.getId(), j.getId())) {
                    A[row + j.getId() - 1][nF + (i.getId() - 1) * N + j.getId() - 1] = 1;
                    temp = temp - graph.getFC()[i.getId() - 1][j.getId() - 1];
                }
            }
            if (j.getId() > 1) {
                A[row + j.getId() - 1][nY + (int) getSum(Cj, 0, j.getId() - 1) + 1 - 1] = temp;
            } else {
                A[row + j.getId() - 1][nY + 1 - 1] = temp;
            }

        }

        // 1.7
        row += N * N;

        for (Node j : graph.getNp()) {
            for (int m = 2; m < Cj[j.getId() - 1]; m++) {
                if (j.getId() > 1) {
                    A[row + (int) getSum(Cj, 0, j.getId() - 1) + m - 1][nY + (int) getSum(Cj, 0, j.getId() - 1) + m - 1] = 1;
                    A[row + (int) getSum(Cj, 0, j.getId() - 1) + m - 1][nY + (int) getSum(Cj, 0, j.getId() - 1) + m - 1 - 1] = -1;
                } else {
                    A[row + m - 1][nY + m - 1] = 1;
                    A[row + m - 1][nY + m - 1 - 1] = -1;
                }
            }

        }


        // 1.8

        row += getSum(Cj);
        for (Node i : graph.getNp()) {
            for (Node j : graph.getNp()) {
                A[row + (i.getId() - 1) * N + j.getId() - 1][nX + (i.getId() - 1) * N + j.getId() - 1] = 1;
                if (j.getId() > 1) {
                    A[row + (i.getId() - 1) * N + j.getId() - 1][nY + (int) getSum(Cj, 0, j.getId() - 1) + 1 - 1] = -1;
                } else {
                    A[row + (i.getId() - 1) * N + j.getId() - 1][nY + 1 - 1] = -1;
                }

            }
        }

        //1.9
        row = row + N * N;
        for (Node j : graph.getNp()) {
            for (Node i : graph.getNp()) {
                A[row + j.getId() - 1][nX + (i.getId() - 1) * N + j.getId() -1] = i.getLamda();
            }

            if (j.getId() > 1) {
                A[row + j.getId() - 1][nY + (int) getSum(Cj, 0, j.getId() - 1) + 1 - 1] = -j.getR0()[0] * j.getMu();
            } else {
                A[row + j.getId() - 1][nY + 1 - 1] = -j.getR0()[0] * j.getMu();
            }
            for (int m = 2; m < Cj[j.getId() - 1]; m++) {
                if (j.getId() > 1) {
                    A[row + j.getId() - 1][nY + (int) getSum(Cj, 0, j.getId() - 1) + m - 1] = j.getMu() * (-j.getR0()[m - 1] + j.getR0()[m - 1 - 1]);

                } else {
                    A[row + j.getId() - 1][nY + m - 1] = j.getMu() * (-j.getR0()[m - 1] + j.getR0()[m - 1 - 1]);
                }

            }
        }

        //1.13
        row += N;
        for (Node j : graph.getNp()) {
            A[row + j.getId() - 1][nD + j.getId() - 1] = -1;
            for (int m = 1; m < Cj[j.getId() - 1]; m++) {
                if (j.getId() > 1) {
                    A[row + j.getId() - 1][nY + (int) getSum(Cj, 0, j.getId() - 1) + m - 1] = 1;
                } else {
                    A[row + j.getId() - 1][nY + m - 1] = 1;
                }
            }
            b[row + j.getId() - 1] = j.getYt();
        }
        // 1.14

        row += N;
        for (Node j : graph.getNp()) {
            A[row + j.getId() - 1][nS + j.getId() - 1] = -1;
            for (int m = 1; m < Cj[j.getId() - 1]; m++) {
                if (j.getId() > 1) {
                    A[row + j.getId() - 1][nY + (int) getSum(Cj, 0, j.getId() - 1) + m - 1] = -1;
                } else {
                    A[row + j.getId() - 1][nY + m - 1] = -1;
                }
            }
            b[row + j.getId() - 1] = -j.getYt();
        }

        IloCplex cpx = null;
        CplexParameterSetter setter;
        try {
            cpx = new IloCplex();
        } catch (IloException ex) {
            fail("Unable to initialize the CPLEX object!");
        }

        try {
            cpx.exportModel("Module.lp");
        } catch (IloException e) {
            e.printStackTrace();
        }

    //    IloColumn
     //   IloNumVar x = cpx .numVar()


/*
        setter = new CplexParameterSetter();
        try {
            setter.set(cpx, "exportmodel", "Model.lp");


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


        } catch (UnknownParameterException e) {
            e.printStackTrace();
        } catch (NullArgumentException e) {
            e.printStackTrace();
        } catch (IloException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (AmbiguousParameterException e) {
            e.printStackTrace();
        }*/
        // int nY = graph.get


    }
}
