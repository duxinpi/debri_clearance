package SDCP;

import ilog.concert.IloException;

import java.util.*;

import static SDCP.Utility.getWSum;
import static SDCP.Utility.getZ;


public class Graph {
    private List<Edge> E;
    private List<Node> N;


    List<Resource> Wt = new ArrayList<>(); // amount of resource required to clear all.

    List<Resource> RS_t = new ArrayList<>(); //
    List<Resource> RD_t = new ArrayList<>();

    List<List<Edge>> B = new ArrayList<>();
    List<List<Edge>> UPrime = new ArrayList<>();
    List<List<Edge>> U = new ArrayList<>();
    List<List<Edge>> UB = new ArrayList<>();
    List<List<Edge>> RB = new ArrayList<>();
    double kt = 3; // how do i know this is rational number? TODO: xdu

    List<Map<Integer, Double>> Yt;

    public void initBlocked() {
        double[][] T = getPeriodT();
        List<Edge> blocked = new ArrayList<>();
        for (int i = 0; i < T.length; i++) {
            for (int j = 0; j < i; j++) {
                if (T[i][j] != 0 && i != j) {
                    blocked.add(new Edge(j, i));
                }
            }
        }
        B.add(blocked);
    }

    public void initUnblocked() {
        List<Edge> unbloked = new ArrayList<>();
        U.add(unbloked);
    }

    public void initRB() {
        RB.add(UPrime.get(0));
    }

    public void initUPrime() {


        List<Edge> list1 = new ArrayList<>();
        list1.add(new Edge(4,3));
     /*   list1.add(new Edge(5,6));
        list1.add(new Edge(6,7));*/

        UPrime.add(list1);
        Edge e2 = new Edge(4, 5);
        List<Edge> list2 = new ArrayList<>();
        list2.add(e2);
        UPrime.add(list2);
        List<Edge> list3 = new ArrayList<>();
        list3.add(new Edge(5, 7));
        UPrime.add(list3);
    }

    public List<Edge> getU(int t) {
        if (t == 0) return U.get(0);
        return UPrime.get(t - 1);
    }

    public List<Edge> getB(int t) {
        List<Edge> result = Utility.exclude(E, getU(t));
        return result;
    }

    public List<Edge> getRB(int t) {
        if (t == 0) return UPrime.get(0);
        List<Edge> result = Utility.exclude(getEPrimePrime(t), getEPrime(t));
        return result;
    }

    public double getRCt(int t){
        if (t ==0) {
            return getKt(0);
        }
        return Math.max(getKt(t) - getGarma(t-1), 0);
    }

    public double getGarma(int t){
        if (t==0) return 0;
        double Wsum = getWSum(getEPrime(t));
        double Wsum2 = getWSum(getEPrimePrime(t));
        double RCt = getRCt(t);
        if (RCt > Wsum) return 0;
        if (RCt ==0) return getGarma(t-1) - getKt(t);
        return Wsum2 - RCt;
    }



    public List<Edge> getUB(int t) {
        return Utility.exclude(B.get(t), RB.get(t));
    }

    public List<Edge> getEPrime(int t) {
        return UPrime.get(t);
    }

    public List<Edge> getEPrimePrime(int t) {
        List<Edge> ePrime = getEPrime(t);
        Set<Integer> set = new HashSet<>();
        for (Edge each : ePrime) {
            set.add(each.getI());
            set.add(each.getI());
        }
        List<Edge> result = new ArrayList<>();
        for (Edge each : E) {
            if (set.contains(each.getI()) || set.contains(each.getJ())) {
                result.add(each);
            }
        }
        return result;
    }


    public List<Edge> getUPrime(int t) {
        return UPrime.get(t);
    }

    private Resource k_t = new Resource(); // total of clearance capacity

    public int getNsize() {
        if (N == null) return 0;
        return N.size();
    }

    public int getNsizeSqaure() {
        return N.size() * N.size();
    }

    public double[] getCj() {
        double[] result = new double[getNsize()];
        int k = getR0()[0].length;
        for (int i = 0; i < result.length; i++) {
            result[i] = k;
        }
        return result;
    }


    private Resource RC_t; // total of clearance capacity

    private Resource r_t;  //


    public void initNodes() {
        N = new ArrayList<>();
        RS_t = new ArrayList<>();
        RD_t = new ArrayList<>();
        Resource resource1 = new Resource();
        resource1.put(1, 10);
        resource1.put(2, 100);
        RD_t.add(resource1);

        Node d1 = new Node(1, "d", 0, 200, 0, 0, 0, 56);
        N.add(d1);

        Node t2 = new Node(2, "t", 0, 0, 0, 0, 0, 0);


        Node t3 = new Node(3, "t", 0, 0, 23.99, 4.14, 1, 0);
        String r0N3 = "0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52";
        t3.setR0(converStringToArray(r0N3));
        N.add(t2);
        N.add(t3);


        Resource resource4 = new Resource();
        resource4.put(1, 10);
        resource4.put(2, 100);
        RS_t.add(resource4);
        Node s4 = new Node(4, "s", 300, 0, 20.64, 3.30, 1, 0);
        String r0N4 = "0.64\t3.68\t7.31\t11.19\t15.22\t19.36\t0.64\t3.68\t7.31\t11.19\t15.22\t19.36\t0.64\t3.68\t7.31\t11.19\t15.22\t19.36\t0.64\t3.68\t7.31\t11.19\t15.22\t19.36\t0.64\t3.68\t7.31\t11.19\t15.22\t19.36";
        s4.setR0(converStringToArray(r0N4));
        N.add(s4);


        Resource resource5 = new Resource();
        resource5.put(1, 50);
        resource5.put(2, 100);
        Node s5 = new Node(5, "s", 200, 0, 21.75, 4.78, 1, 0);
        String r0N5 = "1.16\t4.38\t8.07\t11.99\t16.04\t20.19\t1.16\t4.38\t8.07\t11.99\t16.04\t20.19\t1.16\t4.38\t8.07\t11.99\t16.04\t20.19\t1.16\t4.38\t8.07\t11.99\t16.04\t20.19\t1.16\t4.38\t8.07\t11.99\t16.04\t20.19";
        s5.setR0(converStringToArray(r0N5));
        RS_t.add(resource5);
        N.add(s5);


        Node t6 = new Node(6, "t", 0, 0, 23, 6.69, 0, 0);
        String r0N6 = "1.73\t5.09\t8.84\t12.79\t16.87\t21.03\t1.73\t5.09\t8.84\t12.79\t16.87\t21.03\t1.73\t5.09\t8.84\t12.79\t16.87\t21.03\t1.73\t5.09\t8.84\t12.79\t16.87\t21.03\t1.73\t5.09\t8.84\t12.79\t16.87\t21.03";
        t6.setR0(converStringToArray(r0N6));
        N.add(t6);


        Node d7 = new Node(7, "d", 0, 300, 24.71, 3.30, 0, 100);
        String r0N7 = "2.35\t5.82\t9.62\t13.6\t17.69\t21.87\t2.35\t5.82\t9.62\t13.6\t17.69\t21.87\t2.35\t5.82\t9.62\t13.6\t17.69\t21.87\t2.35\t5.82\t9.62\t13.6\t17.69\t21.87\t2.35\t5.82\t9.62\t13.6\t17.69\t21.87";
        d7.setR0(converStringToArray(r0N7));
        N.add(d7);

        System.out.println(N);

    }
/*
0	0	0	0	0	0	0
0	0	0	0	0	0	0
0	0	0	0.94	1.26	0.63	0.94
0	0	0.94	0	0.77	1.47	1.26
0	0	1.26	0.77	0	0.95	1.47
0	0	0.63	1.47	0.95	0	0.63
0	0	0.94	1.26	1.47	0.63	0
Tij

* */


    public void initEdges() {
        E = new ArrayList<>();


        Resource DL34 = new Resource();
        DL34.put(1, 10);
        DL34.put(2, 10);
        Edge edge34 = new Edge(3, 4, DL34, 0, 0.94, 0.04, new double[]{0.3, 0.5},new double[]{0.5, 0.5} );
        E.add(edge34);

        Resource DL35 = new Resource();
        DL35.put(1, 10);
        DL35.put(2, 10);
        Wt.add(DL35);
        Edge edge35 = new Edge(3, 5, DL35, 100, 1.26, 0.03, new double[]{0.5, 0.3}, new double[]{0.5, 0.5});

        E.add(edge35);

        Resource DL36 = new Resource();
        DL36.put(1, 10);
        DL36.put(2, 10);
        Wt.add(DL36);
        Edge edge36 = new Edge(3, 6, DL36, 100, 0.63, 0.04, new double[]{0.2, 0.3}, new double[]{0.5, 0.5});

        E.add(edge36);
        Resource DL37 = new Resource();
        DL37.put(1, 10);
        DL37.put(2, 10);
        Wt.add(DL37);
        Edge edge37 = new Edge(3, 7, DL37, 100, 0.94, 0.04, new double[]{0.2, 0.3} ,new double[]{0.5, 0.5});
        E.add(edge37);


        Resource DL45 = new Resource();
        Wt.add(DL45);
        Edge edge45 = new Edge(4, 5, DL45, 100, 0.77, 0.02, new double[]{0.6, 0.3},new double[]{0.5, 0.5});
        E.add(edge45);

        Resource DL46 = new Resource();
        DL46.put(1, 10);
        DL46.put(2, 10);
        Wt.add(DL46);
        Edge edge46 = new Edge(4, 6, DL46, 100, 1.47, 0.07, new double[]{0.2, 0.3},new double[]{0.5, 0.5});
        E.add(edge46);

        Resource DL47 = new Resource();
        DL47.put(1, 10);
        DL47.put(2, 10);
        Wt.add(DL47);
        Edge edge47 = new Edge(4, 7, DL47, 100, 1.26, 0.04, new double[]{0.2, 0.3},new double[]{0.5, 0.5});
        E.add(edge47);

        Resource DL56 = new Resource();
        DL56.put(1, 10);
        DL56.put(2, 10);
        Wt.add(DL56);
        Edge edge56 = new Edge(5, 6, DL56, 100, 0.95, 0.07, new double[]{1.2, 0.3},new double[]{0.5, 0.5});
        E.add(edge56);

        Resource DL57 = new Resource();
        DL57.put(1, 10);
        DL57.put(2, 10);
        Wt.add(DL57);
        Edge edge57 = new Edge(5, 7, DL57, 100, 1.47, 0.07, new double[]{0.2, 0.9},new double[]{0.5, 0.5});
        E.add(edge57);

        Resource DL67 = new Resource();
        Wt.add(DL67);
        Edge edge67 = new Edge(6, 7, DL67, 100, 0.63, 0.03, new double[]{0.2, 0.8},new double[]{0.5, 0.5});
        E.add(edge67);
    }

    public double[][] getW() {
        double[][] result = new double[E.size()][E.size()];
        for (Edge each : E) {
            result[each.getI()][each.getJ()] = each.W_ij[0]+ each.W_ij[1] ;
        }
        return result;
    }

    public double[][] getbeta(){
        double[][] result = new double[E.size()][E.size()];
        for (Edge each : E) {
            result[each.getI()][each.getJ()] = each.beta[0];
        }
        return result;
    }


    public double[][] getWmin() {
        double[][] result = new double[E.size()][E.size()];
        for (Edge each : E) {
            result[each.getI()][each.getJ()] = Math.min(each.W_ij[0], each.W_ij[1]);
        }
        return result;
    }

    public double getKt(int t) {
        if (t==0){
            return Utility.getSum(getW());
        }
        return getKt(t-1) - getWSum(getUPrime(t));
    }



    public double[][] getFC() {
        double[][] result = new double[N.size()][N.size()];
        result[3][2] = 100;
        result[4][2] = 200;
        result[4][5] = 20;
        result[5][6] = 200;
        return result;
    }

    public double[][] getPeriodT() {
        int n = N.size();
        double[][] result = new double[n][n];
        for (Edge edge : E) {
            result[edge.getI() - 1][edge.getJ() - 1] = edge.getT();
            result[edge.getJ() - 1][edge.getI() - 1] = edge.getT();
        }
        return result;
    }

    public double[][] getR() {
        int n = N.size();
        double[][] result = new double[n][n];
        for (Edge edge : E) {
            result[edge.getI() - 1][edge.getJ() - 1] = edge.getR();
            result[edge.getJ() - 1][edge.getI() - 1] = edge.getR();
            result[edge.getI() - 1][edge.getI() - 1] = 999; // why?
            result[edge.getJ() - 1][edge.getJ() - 1] = 999; // why?
        }
        return result;
    }

    public double[] getRS(int t) {
        double[] result = new double[N.size()];
        for (Node each : N) {
            result[each.getId() - 1] = each.getRs(t);
        }
        return result;
    }

    public void updateRS(int t, double[][] fij) {
        if (t==0) return;

        for (Node each : N) {

            double sum1 =0;
            for (int j =0; j < N.size(); j++) {
                if (Utility.Match(getUPrime(t), each.getId(), j)){
                    sum1+= fij[each.getId()][j];
                }
            }

            double sum2 = 0;
            for (int j =0; j < N.size(); j++) {
                if (Utility.Match(getUPrime(t-1), j, each.getId())){
                    sum2+= fij[j][each.getId()];
                }
            }

            each.setRS(t, each.getRs(t-1) - (sum1 - sum2));

        }
    }

    public void updateRD(int t, double [] xi) {
        if (t==0) return;
        for (Node each : N){
            each.setRD(t, each.getRd(t-1) - xi[each.getId()-1]);
        }
    }

    public double[] getRD(int t) {

        double[] result = new double[N.size()];
        for (Node each : N) {
            result[each.getId() - 1] = each.getRd(t);
        }
        return result;
    }


    public double[][] getR0() {
        int n = N.size();
        Node node = N.get(0);
        double[][] result = new double[n][node.getR0().length];
        for (Node i : N) {
            result[i.getId() - 1] = i.getR0();
        }


        return result;
    }

    public double[] getLamda(int t) {
        double[] result = new double[N.size()];
        for (Node each : N) {
            result[each.getId() - 1] = each.getLamda(t);
        }
        return result;
    }

    public Node[] getNd() {
        return getNodes("d");
    }

    public Node[] getNs() {
        return getNodes("s");
    }

    public Node[] getNT() {
        return getNodes("t");
    }

    public Node[] getNp() {
        List<Node> nd = new ArrayList<>();
        for (Node each : N) {
            if (each.getId() > 2) {
                nd.add(each);
            }
        }
        return nd.toArray(new Node[nd.size()]);
    }

    public Node[] getNodes(String s) {
        List<Node> nd = new ArrayList<>();
        for (Node each : N) {
            if (each.getType().equals(s)) {
                nd.add(each);
            }
        }
        return nd.toArray(new Node[nd.size()]);
    }


    public double[] converStringToArray(String s) {
        double[] result;
        String temp[] = s.split("\\s+");
        result = new double[temp.length];
        for (int i = 0; i < temp.length; i++) {
            result[i] = Double.valueOf(temp[i]);
        }

        return result;

    }

    public double[] getBiArray() {
        double[] result = new double[N.size()];
        for (int i = 0; i < N.size(); i++) {
            result[i] = N.get(i).getB();
        }
        return result;
    }


    public List<Node> getN() {
        return N;
    }


   public List<List<Edge>> getAllSequences(int t) {
       List<Edge> edges = new ArrayList<>(getEPrime(t));
       List<List<Edge>> actions = new ArrayList<>();
       List<Edge> remain = new ArrayList<>(Utility.exclude(E, edges));
       findSequences(edges, remain, actions);
        return actions;
   }

   public List<List<Edge>> getAllActions(int t) {
       List<Edge> eprime = new ArrayList<>(getEPrime(t));
       List<List<Edge>> allSequences= getAllSequences(t);
       for (List<Edge> each : allSequences) {
           for(Edge each2 : eprime){
               each.remove(each2);
           }
       }
       return allSequences;
   }


   //feasible actions (2)
    public List<List<Edge>> getAllFeasibleActions(int t){
        List<List<Edge>> actions = getAllActions( t);
        double RCt = getRCt(t);
        List<List<Edge>> result = new ArrayList<>();
        for (List<Edge> eachActions : actions) {
            if (getWMinSum(eachActions)< RCt){
                result.add(eachActions);
            }
        }

        return result;

    }

    public double getWMinSum(List<Edge> edges){
        double sum =0;
        for (Edge e : edges) {
            sum+= Math.min(e.W_ij[0],e.W_ij[1]);
        }
        return sum;
    }



   public void findSequences(List<Edge> current, List<Edge> remain, List<List<Edge>> result) {
        if (remain.size() ==0){
            return;
        }
        Edge edge = findAdjcentEdge(current,  remain);
        if (edge == null) return ;
        List<Edge> eachResult = new ArrayList<>(current);
        eachResult.add(edge);
        result.add(eachResult);
        current.add(edge);
        remain.remove(edge);
       findSequences(current, remain, result);
   }


   public Edge findAdjcentEdge(List<Edge> current, List<Edge> remaining) {
        for (Edge e: remaining) {
            if (isAdjcent(current, e)) {
                return e;
            }
        }
        return null;
   }



   public boolean isAdjcent(Edge a, Edge b) {
        return a.getI() == b.getI() || a.getJ() == b.getI() || a.getJ() == b.getJ() || a.getI() == b.getJ();
   }

   public boolean isAdjcent(List<Edge> edges, Edge b){
        for (Edge a: edges) {
            if (isAdjcent(a, b))return true;
        }
        return false;
   }

   public Set<Integer> getNodes(List<Edge> edges) {
        Set<Integer> set = new HashSet<>();
        for (Edge e: edges) {
            set.add(e.getI());
            set.add(e.getJ());
        }

        return set;
   }
   //(4)
   public double getPofObservation(List<Edge> observation, int obLevel) {
        double sum =0;
        for (Edge edge : observation) {
            sum+= edge.beta[obLevel];
        }
        return sum;
   }

    public List<Edge> getObservation(List<Edge> action) {

        Set<Integer> set = new HashSet<>();
        for (Edge each : action) {
            set.add(each.getI());
            set.add(each.getI());
        }
        List<Edge> result = new ArrayList<>();
        for (Edge each : E) {
            if (set.contains(each.getI()) || set.contains(each.getJ())) {
                result.add(each);
            }
        }

        //TODO: add logic

        return result;
    }

    public double getReward(  int t, BState bState) throws IloException {

        double bi[] = getBiArray();
        double[] rd = bState.RD_t;
        double z = getZ(this, t);
        double result = 0;
        for(Node d: getNd()){
            int i = d.getId() -1;
            result += bi[i]*(d.getRd(0) - rd[i] );
        }
        result += z;
        return result;
    }

    public double getER(int t, List<Edge> observation, int oblevel) throws IloException {
        getZ(this, t);
        BState bastate = StateManager.getInstance().get(t);
        double result = getPofObservation(observation, oblevel) *getReward(t, bastate);

        return result;
    }



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
            System.out.println(graph.getER( 0, graph.getObservation(graph.getUPrime(0)), 0));
        } catch (IloException e) {
            e.printStackTrace();
        }


    }


}
