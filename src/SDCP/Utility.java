package SDCP;

import CPLEXUtil.cplexutils.CplexParameterSetter;
import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloNumVarType;
import ilog.cplex.IloCplex;

import java.io.*;
import java.util.*;
import java.util.function.DoubleBinaryOperator;

import static org.junit.Assert.fail;

public class Utility {

    public static double getSum(double[] array) {
        double sum = 0;
        for (int i = 0; i < array.length; i++) {
            sum += array[i];
        }
        return sum;
    }

    public static double getSum(int[] array) {
        double sum = 0;
        for (int i = 0; i < array.length; i++) {
            sum += array[i];
        }
        return sum;
    }

    public static double getSum(double[] array, int s, int e) {
        double sum = 0;
        for (int i = s; i < e; i++) {
            sum += array[i];
        }
        return sum;
    }

    public static void fill(double[][] array, double v) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                array[i][j] = v;
            }
        }
    }


    public static void printArray(Object[] array) {
        for (Object each : array) {
            System.out.print(each + " ");
        }
        System.out.println();
    }

    public static void printArray(double[] array) {
        for (double each : array) {
            System.out.print(each + " ");
        }
        System.out.println();
    }

    public static void printArray(int[] array) {
        for (int each : array) {
            System.out.print(each + " ");
        }
        System.out.println();
    }

    public static void printDoubleArray(double[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                System.out.print(array[i][j] + "\t");
            }
            System.out.println();
        }
    }

    public static boolean Match(List<Edge> array, int m, int n) {
        for (Edge each : array) {
            if (each.getI() == m && each.getJ() == n) {
                return true;
            }
        }
        return false;
    }

    public static double getSum(double[][] array) {
        double sum =0;
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                sum+= array[i][j];
            }

        }
        return sum;
    }

    /*public static double getWSum(List<Edge> edges, int t, int level) {
        double sum =0;
        for (Edge e : edges) {
            sum+= e.W_ij.get(t)[level];
        }
        return sum;
    }
*/

    public static void main(String ars[]) {
        // generateParenthesis(3);


     //   compare("javaA.txt","matlabA.txt", 1512, 378);
     //   compare("javaAeq.txt","matlab_Aeq.txt", 36, 378);

    //    compare("javabeq.txt","matlab_beq.txt", 1512 );
    //    compare("javab.txt","matlab_b.txt", 1512 );
     //   compare("javaf.txt","matlab_f.txt", 1512 );
    }

    public static void compare(String file1, String file2, int r, int c) {
        double matlabA[][] = null;
        double javaA[][] = null;
        try {
            javaA = read(file1, r, c);
            matlabA = read(file2, r, c);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i =0; i < matlabA.length; i++) {
            for (int j =0; j < matlabA[0].length; j++) {
                if (Math.abs(matlabA[i][j]- javaA[i][j]) >0.01){

                    System.out.println("file: " + file1 + "diff: " + i + "-----" + j + " : " + matlabA[i][j] +"----java---" + javaA[i][j]);
                }
            }
        }
    }

    public static void compare(String file1, String file2, int r) {
        double matlabA[] = null;
        double javaA[] = null;
        try {
            javaA = read(file1, r);
            matlabA = read(file2, r);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i =0; i < matlabA.length; i++) {

                if (Math.abs(matlabA[i]- javaA[i]) >0.000000000001){
                    System.out.println("file: " + file1 + "=====diff: " + i + "----- : " + matlabA[i] +"----java---" + javaA[i]);
                }

        }
    }


    public static void writeFile(String filePath, double[][] array) throws IOException {
        PrintWriter writer = new PrintWriter(filePath, "UTF-8");
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                writer.print(array[i][j]);
                writer.print("\t");
                if (array[i][j] != 0) {
             //       System.out.println(i + "-----" + j + " : " + array[i][j]);
                }
            }
            writer.println();

        }
        writer.close();
    }

    public static void writeFile(String filePath, double[] array) throws IOException {
        PrintWriter writer = new PrintWriter(filePath, "UTF-8");
        for (int i = 0; i < array.length; i++) {
                writer.print(array[i]);
                writer.print("\t");
                if (array[i] != 0) {
                 //   System.out.println(i + "-----" + array[i]);
                }
            writer.println();

        }
        writer.close();
    }


    public static double[][] read(String path, int r, int c) throws Exception {
        // We need to provide file path as the parameter:
        // double backquote is to avoid compiler interpret words
        // like \test as \t (ie. as a escape sequence)
        File file = new File(path);

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        double[][] result = new double[r][c];
        int row = 0;
        while ((st = br.readLine()) != null) {
            String[] line = st.split("\t");
            for (int i = 0; i < line.length; i++) {
                result[row][i] = Double.valueOf(line[i]);
                if (result[row][i] != 0) {
          //          System.out.println(row + "-----" + i + " : " + result[row][i]);

                }
            }
            row++;
        }
        return result;
    }

    public static double[] read(String path, int r) throws Exception {
        // We need to provide file path as the parameter:
        // double backquote is to avoid compiler interpret words
        // like \test as \t (ie. as a escape sequence)
        File file = new File(path);

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        double[] result = new double[r];
        int i =0;

        while ((st = br.readLine()) != null) {


                result[i] = Double.valueOf(st);
                if (result[i] != 0) {
                    //          System.out.println(row + "-----" + i + " : " + result[row][i]);

                }
                i++;
            }
        return result;
    }

    public static double[] unique(double[] c) {
        Set<Double> set = new HashSet<>();
        for (Double each : c) {
            set.add(each);
        }
        double r[] = new double[set.size()];
        int i =0;
        for (Double each : set) {
            r[i++] = each;
        }
        return r;
    }

    public static double max(double c[]) {
        double max=0;
        for (Double each : c){
            if (each > max) {
                max = each;
            }
        }

        return max;
    }

    public static double[] subarray(double x[], int i , int j) {
        double[] r = new double[j-i];
        for (int k =0; k < r.length; k++) {
            r[k] = x[i++];
        }
        return r;
    }

    public static List<Edge> findAllReachableBlock(List<Edge> unblockedEdges, List<Edge> edgesSet, List<Node> nodes) {
        Set<Edge> result = new HashSet<>();
        for (Edge edge : edgesSet) {
            if (isAdjcent(unblockedEdges, edge) || Utility.isSupply(edge.getJ(), nodes) || Utility.isSupply(edge.getI(), nodes)) {
                result.add(edge);
            }
        }
        result.addAll(unblockedEdges);

        return new ArrayList<>(result);
    }

    public static boolean isSupply(int i, List<Node> nodes) {
        for(Node each : nodes) {
            if (each.getId() == i && each.getType().equals("s")){
                return true;
            }
        }
        return false;
    }


    public static boolean isAdjcent(List<Edge> edges, Edge b) {

        for (Edge a : edges) {
            if (isAdjcent(a, b)) return true;
        }
        return false;
    }

    public static List<Edge> exclude(List<Edge> edgeSet, List<Edge> subset) {
        List<Edge> result = new ArrayList<>();
        for(Edge each: edgeSet) {
            if (!contains(subset, each)) {
                result.add(each);
            }
        }
        return result;
    }

    public static boolean contains(List<Edge> edgeSet, Edge edge) {
        for (Edge each: edgeSet) {
            if (each.getI() == edge.getI() && each.getJ() == edge.getJ()){
                return true;
            }
            if (each.getJ() == edge.getI() && each.getI() == edge.getJ()){
                return true;
            }
        }
        return false;
    }

    public static Edge getEdge(List<Edge> edges, int i, int j){
        for (Edge each : edges){
            if (each.getJ() == j && each.getI() == i){
                return  each;
            }
            if (each.getI() == j && each.getJ() == i){
                return  each;
            }
        }
        return null;
    }

    public static boolean isAdjcent(Edge a, Edge b){
        if (a.getJ() == b.getJ() && a.getI() == b.getI()){
            return false;
        }
        if (a.getJ() == b.getI() && a.getI() == b.getJ()){
            return false;
        }

        return a.getI() == b.getI() || a.getJ() == b.getI() || a.getJ() == b.getJ() || a.getI() == b.getJ();
    }


    public static double getZ(Graph graph, int t, Observation observation) throws IloException {

        int N = graph.getNsize();
        int nX = 0;
        int nY = N * N;
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

        double[] Cj = graph.getCj();
        int rowsNumAb = N + N + N + N * ((int) Utility.getSum(Cj) - Cj.length) + N * N + N + N + N;

        double[][] A = new double[rowsNumAb][nKn];
        double[] b = new double[rowsNumAb];
        int rowsNumAeq = N + N + 1 + N + N + N;
        double[][] Aeq = new double[rowsNumAeq][nKn];
        double[] beq = new double[rowsNumAeq];
        double[] f = new double[nKn];

        // objective
        for (Node each : graph.getNd()) {// what is this?
            f[each.getId() + nK - 1] = -graph.getBiArray()[each.getId() - 1];
        }
        List<Double> tempForLog1 = new ArrayList<>();
        List<Double> tempForLog2 = new ArrayList<>();
        for (Node i : observation.getNp()) {
            for (Node j : observation.getNp()) {
                int m = nX + (i.getId() - 1) * N + j.getId() - 1;
                int n = nW + (i.getId() - 1) * N + j.getId() - 1;

                f[nX + (i.getId() - 1) * N + j.getId() - 1] = i.getLamda(t) * graph.getPeriodT()[i.getId() - 1][j.getId() - 1];
                f[nW + (i.getId() - 1) * N + j.getId() - 1] = GlobalData.SIGMA * graph.getR()[i.getId() - 1][j.getId() - 1];
                tempForLog1.add(   f[nX + (i.getId() - 1) * N + j.getId() - 1]);
                tempForLog2.add(   f[nW + (i.getId() - 1) * N + j.getId() - 1]);
            }

        }

       // printArray(f);


        // 1.2
        int row = 0;
      //  System.out.println("1.2-----------------");
        // row 1, 7
        for (Node i : graph.getNd()) {

            for (Node j : graph.getN()) {
                int tempColumn = nF + (j.getId() - 1) * N + i.getId();
                int tempColumn2 = nF + (j.getId() - 1) * N + j.getId();
                if (Match(observation.getUprime(), j.getId(), i.getId())) {
                    //     System.out.println("line number: row +i.getId() - 1 positive: " + (i.getId() - 1));
                    Aeq[i.getId() - 1][tempColumn - 1] = 1;
                }
                if (Match(observation.getUprime(), i.getId(), j.getId())) {
                    //             System.out.println("line number: row +i.getId() - 1 positive1: " + (i.getId() - 1));
                    Aeq[i.getId() - 1][tempColumn2 - 1] = -1;
                }
            }
            //  System.out.println("line number: row +i.getId() - 1 positive2: " + (i.getId() - 1));
            Aeq[i.getId() - 1][nK + i.getId() - 1] = -1;
            beq[i.getId() - 1] = 0;
        }

        // todo: unitest

        //1.4


        row += graph.getNsize();

        for (Node i : graph.getNT()) {

            for (Node j : graph.getN()) {
                int tempColumn = nF + (j.getId() - 1) * N + i.getId();
                int tempColumn2 = nF + (i.getId() - 1) * N + j.getId();
                if (Match(observation.getUprimeNext(), j.getId(), i.getId())) {
                    //     System.out.println("line number: row + i.getId() - 1 ~~~~~1: " + (row + i.getId() - 1));
                    Aeq[row + i.getId() - 1][tempColumn - 1] = 1;
                }
                if (Match(observation.getUprime(), i.getId(), j.getId())) {
                    //    System.out.println("line number: row + i.getId() - 1 ~~~~~2: " + (row + i.getId() - 1));
                    Aeq[row + i.getId() - 1][tempColumn2 - 1] = -1;
                }
            }

            beq[i.getId() - 1] = 0;
        }


        // 1.10
        row += graph.getNsize();
        for (Node each : observation.getNp()) {
            for (int m = 1; m <= Cj[each.getId() - 1]; m++) {
                if (each.getId() > 1) {
                    //             System.out.println("line number: row + i.getId() - 1 ~~~~~2: " + (row + 1 - 1));
                    Aeq[row + 1 - 1][nY + (int) getSum(Cj, 0, each.getId() - 1) + m - 1] = 1;
                } else {
                    //           System.out.println("line number: row + i.getId() - 1 ~~~~~2: " + (row + 1 - 1));
                    Aeq[row + 1 - 1][nY + m - 1] = 1;
                }
            }
        }

        beq[row + 1 - 1] = GlobalData.B;


        //1.11
        row++;

        for (Node i : observation.getNp()) {
            for (Node j : observation.getNp()) {
                //     System.out.println("line number: row + i.getId() - 1 ~~~~~1: " + (row + i.getId() - 1));
                Aeq[row + i.getId() - 1][nW + (i.getId() - 1) * N + j.getId() - 1] = 1;
            }
            //  System.out.println("line number: row + i.getId() - 1 ~~~~~2: " + (row + i.getId() - 1));
            Aeq[row + i.getId() - 1][nS + i.getId() - 1] = -1;
            beq[row + i.getId() - 1] = 0;
        }


        //1.12
        row = row + N;

        for (Node j : observation.getNp()) {
            for (Node i : observation.getNp()) {
                Aeq[row + j.getId() - 1][nW + (i.getId() - 1) * N + j.getId() - 1] = 1;
            }
            Aeq[row + j.getId() - 1][nD + j.getId() - 1] = -1;
            beq[row + j.getId() - 1] = 0;
        }


        // 19 New Cons
        row = row + N;

        for (Node i : observation.getNp()) {
            for (Node j : observation.getNp()) {

                Aeq[row + i.getId() - 1][nX + (i.getId() - 1) * N + j.getId() - 1] = 1;
            }
            beq[row + i.getId() - 1] = 1;
        }


        //1.3
        row = 0;
        // line 4 and 5
        for (Node i : graph.getNs()) {
            for (Node j : graph.getN()) {
                if (Match(observation.getUprime(), i.getId(), j.getId())) {
                    //     System.out.println("line number: row + i.getId() - 1 positive: " + (row + i.getId() - 1));
                    A[row + i.getId() - 1][nF + (i.getId() - 1) * N + j.getId() - 1] = 1;
                }
                if (Match(observation.getUprime(), j.getId(), i.getId())) {
                    //   System.out.println("line number: row + i.getId() - 1: " + (row + i.getId() - 1));
                    A[row + i.getId() - 1][nF + (j.getId() - 1) * N + i.getId() - 1] = -1;
                }
            }
            b[row + i.getId() - 1] = observation.bState.RS_t[i.getId()-1];
            System.out.println("---rs------" + observation.bState.RS_t[i.getId()-1]);
        }

      //  System.out.println("1.5-----------");
        //1.5
        row = row + N;
        for (Node i : graph.getNd()) {
            A[row + i.getId() - 1][nK + i.getId() - 1] = 1;
            //     System.out.println("line number: row + i.getId() - 1 positive: " + (row + i.getId() - 1));
            b[row + i.getId() - 1] = i.getRd(t);
            System.out.println("---- RD: " + i.getRd(t));
        }


        //1.6

        row += N;
    //    System.out.println("1.6--------------");
        for (Node j : observation.getNp()) {
            double temp = 0;
            for (Node i : graph.getN()) {
                if (Match(observation.getUprime(), i.getId(), j.getId())) {
                    //            System.out.println("line number: row + i.getId() - 1 positive: " + (row + i.getId() - 1));
                    A[row + j.getId() - 1][nF + (i.getId() - 1) * N + j.getId() - 1] = 1;
                    temp = temp - graph.getFC()[i.getId() - 1][j.getId() - 1];
                }
            }
            if (j.getId() > 1) {
                //          System.out.println("line number: row + i.getId() - 1 temp: " + (row + j.getId() - 1));
                A[row + j.getId() - 1][nY + (int) getSum(Cj, 0, j.getId() - 1) + 1 - 1] = temp;
            } else {
                //    System.out.println("line number: row + i.getId() - 1 temp: " + (row + j.getId() - 1));
                A[row + j.getId() - 1][nY + 1 - 1] = temp;
            }

        }

        // 1.7
        row += N * N;
        //row 1.7
      //  System.out.println("1.7---------------");
        for (Node j : observation.getNp()) {
            for (int m = 2; m <= Cj[j.getId() - 1]; m++) {
                if (j.getId() > 1) {
                    //        System.out.println("line number: row + (int) getSum(Cj, 0, j.getId() - 1) + m - 1: " + (row + (int) getSum(Cj, 0, j.getId() - 1) + m - 1));
                    A[row + (int) getSum(Cj, 0, j.getId() - 1) + m - 1][nY + (int) getSum(Cj, 0, j.getId() - 1) + m - 1] = 1;
                    A[row + (int) getSum(Cj, 0, j.getId() - 1) + m - 1][nY + (int) getSum(Cj, 0, j.getId() - 1) + m - 1 - 1] = -1;
                } else {
                    //         System.out.println("line number: row + (row + m - 1: " + (row+m -1));
                    A[row + m - 1][nY + m - 1] = 1;
                    A[row + m - 1][nY + m - 1 - 1] = -1;
                }
            }

        }


        // 1.8
     //   System.out.println("1.8---------------");
        row += getSum(Cj);
        for (Node i : observation.getNp()) {
            for (Node j : observation.getNp()) {
                //      System.out.println("line number: row + (row + (i.getId() - 1) * N + j.getId() - 1: " + (row + (i.getId() - 1) * N + j.getId() - 1));
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
        for (Node j : observation.getNp()) {
            for (Node i : observation.getNp()) {
                A[row + j.getId() - 1][nX + (i.getId() - 1) * N + j.getId() - 1] = i.getLamda(t);
            }

            if (j.getId() > 1) {
                A[row + j.getId() - 1][nY + (int) getSum(Cj, 0, j.getId() - 1) + 1 - 1] = -j.getR0()[0] * j.getMu();
            } else {
                A[row + j.getId() - 1][nY + 1 - 1] = -j.getR0()[0] * j.getMu();
            }
            for (int m = 2; m <= Cj[j.getId() - 1]; m++) {
                if (j.getId() > 1) {
                    A[row + j.getId() - 1][nY + (int) getSum(Cj, 0, j.getId() - 1) + m - 1] = j.getMu() * (-j.getR0()[m - 1] + j.getR0()[m - 1 - 1]);

                } else {
                    A[row + j.getId() - 1][nY + m - 1] = j.getMu() * (-j.getR0()[m - 1] + j.getR0()[m - 1 - 1]);
                }

            }
        }

        //1.13
        row += N;
        for (Node j : observation.getNp()) {
            A[row + j.getId() - 1][nD + j.getId() - 1] = -1;
            for (int m = 1; m <= Cj[j.getId() - 1]; m++) {
                if (j.getId() > 1) {
                    A[row + j.getId() - 1][nY + (int) getSum(Cj, 0, j.getId() - 1) + m - 1] = 1;
                } else {
                    A[row + j.getId() - 1][nY + m - 1] = 1;
                }
            }
            b[row + j.getId() - 1] = j.getYt(t);
        }
        // 1.14

        row += N;
        for (Node j : observation.getNp()) {
            A[row + j.getId() - 1][nS + j.getId() - 1] = -1;
            for (int m = 1; m <= Cj[j.getId() - 1]; m++) {
                if (j.getId() > 1) {
                    A[row + j.getId() - 1][nY + (int) getSum(Cj, 0, j.getId() - 1) + m - 1] = -1;
                } else {
                    A[row + j.getId() - 1][nY + m - 1] = -1;
                }
            }
            b[row + j.getId() - 1] = -j.getYt(t);
        }

        IloCplex model = null;
        CplexParameterSetter setter;
        try {
            model = new IloCplex();
        } catch (IloException ex) {
            fail("Unable to initialize the CPLEX object!");
        }


        IloNumVar[] x = new IloNumVar[f.length];

      /*  try {
         *//*   writeFile("javaA.txt", A);
            writeFile("javaAeq.txt", Aeq);
            writeFile("javab.txt", b);
            writeFile("javabeq.txt", beq);
            writeFile("javaf.txt", f);*//*
            // writeFile("matlab_f.txt", f);


        } catch (IOException e) {
            e.printStackTrace();
        }*/

        try {
            // init our min expression
            for (int i = 0; i < f.length; i++) {
                if (Ctype[i] == 'C'){
                    x[i] = model.semiContVar(0, Double.MAX_VALUE, IloNumVarType.Float);
                }
                else if (Ctype[i] == 'B'){
                    x[i] = model.boolVar();
                } else {

                    x[i] = model.numVar(0, Double.MAX_VALUE, IloNumVarType.Int);
                }

                //   x[i] = model.numVar(0, Double.MAX_VALUE);
                //   IloNumVarType
                x[i].setLB(0);
                x[i].setUB(10000);
            }

            IloLinearNumExpr obj = model.linearNumExpr();
            for (int j = 0; j < f.length; j++) {
                obj.addTerm(f[j], x[j]);
            }

            model.addMinimize(obj);
            for (int i = 0; i < A.length; i++) {
                IloLinearNumExpr constraint1 = model.linearNumExpr();
                for (int j = 0; j < A[0].length; j++) {

                    constraint1.addTerm(A[i][j], x[j]);

                }

                model.addLe(constraint1, b[i]);

            }

            //    model.addRange(0, 10000);


            for (int i = 0; i < Aeq.length; i++) {
                IloLinearNumExpr constraint1 = model.linearNumExpr();
                for (int j = 0; j < Aeq[0].length; j++) {

                    constraint1.addTerm(Aeq[i][j], x[j]);
                }

                model.addEq(constraint1, beq[i]);

            }


            IloLinearNumExpr constraint1 = model.linearNumExpr();
            for (int j = 0; j < LB[0].length; j++) {

                constraint1.addTerm(1, x[j]);
            }


        } catch (IloException e) {
            e.printStackTrace();
        }

        try {
            model.exportModel("Module.lp");
        } catch (IloException e) {
            e.printStackTrace();
        }


        try {

            if (model.solve()) {

           //     System.out.println("obj = " + model.getObjValue());

            }

       //     System.out.println("Complete");
            double []xDouble = new double[x.length];
            for (int i =0; i < xDouble.length; i++) {
                xDouble[i] = model.getValue(x[i]);

            }
            double X[][] = Matrix.transpose(Matrix.reshape(xDouble, 0, nY, N));
      //      printDoubleArray(X);
            double [][]Y = null;

            if (unique(Cj).length ==1) {
          //      System.out.println(nW);
        //        System.out.println(nY +1 -1);

                Y = Matrix.transpose(Matrix.reshape(xDouble, nY +1 -1, nW+1,(int)Utility.max(Cj)));
            } else {
                //Y = subarray(xDouble, nY +1 -1, nW+1);
            }

            double W[] = subarray(xDouble, nW+1 -1, nS);
            double S[] = subarray(xDouble, nS+1 -1, nD );
            double D[] = subarray(xDouble, nD +1 -1, nF );
            double F[][] =  Matrix.transpose(Matrix.reshape(xDouble, nF +1 -1, nK+1,N));
            printDoubleArray(F);
            double K[] = subarray(xDouble, nK +1-1, xDouble.length);  // ki.
            System.out.println("----k 0-" + K[0]);
            System.out.println("----k 7-" + K[6]);
            System.out.println("----bi-" + graph.getBiArray()[0]);
            System.out.println("----bi-" + graph.getBiArray()[7-1]);

            observation.K = K;


         //   graph.updateRD( t+1,  K);
         //   graph.updateRS(observation, t+1, F);

           // System.out.println("time t : " +t);

         //   BState s0 = new BState(t,graph.getbeta(t, level) , 0, graph.getRS(t), graph.getRD(t));
         //   StateManager.getInstance().put(t, s0 );
          /*  try {
                writeFile("javaW.txt", W);
                writeFile("javaS.txt", S);
                writeFile("javaD.txt", D);
                writeFile("javaFF.txt", F);
                writeFile("javaK.txt", K);
                writeFile("javaY.txt", Y);
                writeFile("javaXi.txt", xDouble);
            } catch (IOException e) {
                e.printStackTrace();
            }
            */
        } catch (IloException e) {
            e.printStackTrace();
        }


        return model.getObjValue();
    }

    public static void printArray(List<Edge> edges) {
        for (Edge edge : edges) {

            System.out.print("--" + edge.toString()+" --");

        }
        System.out.println();
    }

    public static double getWSum(List<Edge> edges) {
        double res = 0;
        for (Edge each : edges) {
            double min = Double.MAX_VALUE;
            for (Double eachW : each.w_ij){
                min = Math.min(eachW, min);
            }
            res += min;
        }
        return res;
    }

    public static void getAllSequences(List<Edge> edges, List<Edge> current, List<List<Edge>> result, int k) {
        if (k == current.size()) {
            return ;
        }
        for (int i =0; i < current.size();i++) {
            Edge e = current.get(i);
            edges.add(e);
            result.add(new ArrayList<>(edges));
            getAllSequences(edges, current, result,  k+1);
            edges.remove(e);
        }
        return ;
    }



}
