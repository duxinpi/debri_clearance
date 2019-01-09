package SDCP;

import com.sun.media.jfxmedia.logging.Logger;
import ilog.concert.IloException;

import java.util.*;

import static SDCP.Utility.*;


public class Graph {
    private List<Edge> E;
    private List<Node> N;



    List<List<Edge>> B = new ArrayList<>();
    List<List<Edge>> UPrime = new ArrayList<>();
    List<List<Edge>> U = new ArrayList<>();
    List<List<Edge>> UB = new ArrayList<>();
    List<List<Edge>> RB = new ArrayList<>();
    double kt = 3; // how do i know this is rational number? TODO: xdu


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
       return;
    }

    public void initUB() {
        UB.add(E);
    }
    public void initUPrime(List<Edge> edges, int t) {
         UPrime.add(t, edges);
        List<Edge> neibours = findAllNeigbours(edges, getUB(t));
         UPrime.add(t+1,Utility.exclude(neibours, edges));

    }
    public void setUPrime(List<Edge> edges, int t) {
        UPrime.set(t, edges);
        List<Edge> neibours = findAllNeigbours(edges, getUB(t));
        UPrime.set(t+1,Utility.exclude(neibours, edges));

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
        if (t == 0) return getUPrime(null,0);
        List<Edge> result = Utility.exclude(getEPrimePrime(t), getEPrime(t));
        return result;
    }

    public double getRCt(int t, int level){
        if (t ==0) {
            return getKt(0, level);
        }
        return Math.max(getKt(t, level) - getGarma(t-1, level), 0);
    }

    public double getGarma(int t, int level){
        if (t==0) return 0;
        double Wsum = getWSum(getEPrime(t), t, level);
        double Wsum2 = getWSum(getEPrimePrime(t), t, level);
        double RCt = getRCt(t, level);
        if (RCt > Wsum) return 0;
        if (RCt ==0) return getGarma(t-1, level) - getKt(t,level);
        return Wsum2 - RCt;
    }



    public List<Edge> getUB(int t) {
        return Utility.exclude(getB(t), getRB(t));
    }

    public List<Edge> getEPrime(int t) {
        return getU(t);
    }

    public List<Edge> getEprimeOnA(List<Edge> actions) {
        return actions;
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


    public List<Edge> getUPrime(List<Edge>observation, int t) {
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




    public void initNodes() {
        N = new ArrayList<>();
        Resource resource1 = new Resource();
        resource1.put(1, 10);
        resource1.put(2, 100);

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
        N.add(s5);


        Node t6 = new Node(6, "t", 0, 0, 23, 6.69, 0, 0);
        String r0N6 = "1.73\t5.09\t8.84\t12.79\t16.87\t21.03\t1.73\t5.09\t8.84\t12.79\t16.87\t21.03\t1.73\t5.09\t8.84\t12.79\t16.87\t21.03\t1.73\t5.09\t8.84\t12.79\t16.87\t21.03\t1.73\t5.09\t8.84\t12.79\t16.87\t21.03";
        t6.setR0(converStringToArray(r0N6));
        N.add(t6);


        Node d7 = new Node(7, "d", 0, 300, 24.71, 3.30, 0, 100);
        String r0N7 = "2.35\t5.82\t9.62\t13.6\t17.69\t21.87\t2.35\t5.82\t9.62\t13.6\t17.69\t21.87\t2.35\t5.82\t9.62\t13.6\t17.69\t21.87\t2.35\t5.82\t9.62\t13.6\t17.69\t21.87\t2.35\t5.82\t9.62\t13.6\t17.69\t21.87";
        d7.setR0(converStringToArray(r0N7));
        N.add(d7);

      //
        //  System.out.println(N);

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
        Edge edge35 = new Edge(3, 5, DL35, 100, 1.26, 0.03, new double[]{0.5, 0.3}, new double[]{0.5, 0.5});

        E.add(edge35);

        Resource DL36 = new Resource();
        DL36.put(1, 10);
        DL36.put(2, 10);
        Edge edge36 = new Edge(3, 6, DL36, 100, 0.63, 0.04, new double[]{0.2, 0.3}, new double[]{0.5, 0.5});

        E.add(edge36);
        Resource DL37 = new Resource();
        DL37.put(1, 10);
        DL37.put(2, 10);
        Edge edge37 = new Edge(3, 7, DL37, 100, 0.94, 0.04, new double[]{0.2, 0.3} ,new double[]{0.5, 0.5});
        E.add(edge37);


        Resource DL45 = new Resource();
        Edge edge45 = new Edge(4, 5, DL45, 100, 0.77, 0.02, new double[]{0.6, 0.3},new double[]{0.5, 0.5});
        E.add(edge45);

        Resource DL46 = new Resource();
        DL46.put(1, 10);
        DL46.put(2, 10);
        Edge edge46 = new Edge(4, 6, DL46, 100, 1.47, 0.07, new double[]{0.2, 0.3},new double[]{0.5, 0.5});
        E.add(edge46);

        Resource DL47 = new Resource();
        DL47.put(1, 10);
        DL47.put(2, 10);
        Edge edge47 = new Edge(4, 7, DL47, 100, 1.26, 0.04, new double[]{0.2, 0.3},new double[]{0.5, 0.5});
        E.add(edge47);

        Resource DL56 = new Resource();
        DL56.put(1, 10);
        DL56.put(2, 10);
        Edge edge56 = new Edge(5, 6, DL56, 100, 0.95, 0.07, new double[]{1.2, 0.3},new double[]{0.5, 0.5});
        E.add(edge56);

        Resource DL57 = new Resource();
        DL57.put(1, 10);
        DL57.put(2, 10);
        Edge edge57 = new Edge(5, 7, DL57, 100, 1.47, 0.07, new double[]{0.2, 0.9},new double[]{0.5, 0.5});
        E.add(edge57);

        Resource DL67 = new Resource();
        Edge edge67 = new Edge(6, 7, DL67, 100, 0.63, 0.03, new double[]{0.2, 0.8},new double[]{0.5, 0.5});
        E.add(edge67);
    }

    public double[][] getW(int t) {
        double[][] result = new double[E.size()][E.size()];
        for (Edge each : E) {
            result[each.getI()][each.getJ()] = each.W_ij.get(t)[0]+ each.W_ij.get(t)[1];
        }
        return result;
    }

    public double[][] getbeta(int t, int level){
        double[][] result = new double[E.size()][E.size()];
        for (Edge each : E) {
            result[each.getI()][each.getJ()] = each.beta.get(t)[level];
        }
        return result;
    }

    public void updateBeta(int t){
        if (t==0) {
            return ;
        }
        for (Edge edge : E) {
            double[] previousBeta = edge.beta.get(t-1);
            double[] newBeta = new double[2];
            if (Utility.contains(getUB(t), edge)){
                    newBeta = previousBeta;
            } else if (Utility.contains(getRB(t), edge)){
                newBeta[0] =1;
                newBeta[1] =1;
            }
            edge.beta.add(newBeta);
        }

    }


    public double getKt(int t, int level) {
        if (t==0){
            return 5;// TODO: xdu. kt should have multiple.
        }
        return getKt(t-1, level) - getWSum(getEPrime(t), t, level);
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

    public void updateRS(List<Edge> observation, int t, double[][] fij) {
        if (t==0) return;

        for (Node each : N) {

            double sum1 =0;
            for (int j =1; j <= N.size(); j++) {
                if (Utility.Match(getUPrime(observation, t), each.getId(), j)){
                    sum1+= fij[each.getId()-1][j-1];
                }
            }

            double sum2 = 0;
            for (int j =1; j <= N.size(); j++) {
                if (Utility.Match(getUPrime(observation,t-1), j, each.getId())){
                    sum2+= fij[j-1][each.getId()-1];
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
        List<List<Edge>> actions = getAllActions(t);
        List<List<Edge>> result = new ArrayList<>();
        for (List<Edge> eachActions : actions) {
            List<Edge> e = new ArrayList<>(eachActions);
            List<Edge> temp = exclude(e, getEPrime(t));
            if (isWminSumFeasible(temp, t)){
                result.add(eachActions);
            }
        }
        return result;
    }

    public boolean isWminSumFeasible(List<Edge> edges, int t) {
        double rct0 = getRCt(t, 0);
        double rct1 = getRCt(t, 1);
        for (Edge e : edges) {
           if (e.W_ij.get(t)[0] > e.W_ij.get(t)[1]) {
               rct1-= e.W_ij.get(t)[1];
           } else {
               rct0 -=e.W_ij.get(t)[0];
           }
            if (rct0 <=0 || rct1<=0) return false;
        }
        return true;

    }

    public double getWMinSum(List<Edge> edges, int t){
        double sum =0;
        for (Edge e : edges) {
            sum+= Math.min(e.W_ij.get(t)[0],e.W_ij.get(t)[1]);
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
            if (isAdjcent(current, e) ||  Utility.isSupply( e.getJ(), N) || Utility.isSupply( e.getI(), N)) {
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

   public List<Edge> getSupplies() {
        Set<Edge> set = new HashSet<>();
        for (Edge each: E) {
            if( Utility.isSupply( each.getJ(), N) || Utility.isSupply( each.getI(), N)){
                set.add(each);
            }
        }
        return new ArrayList<>(set);
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
   public double getPofObservation(List<Edge> observation, int t, int level) {
        double sum =1;
        for (Edge edge : observation) {
            sum*= edge.beta.get(t)[level];
        }
        return sum;
   }

    public List<List<Edge>> getObservation(List<Edge> action, int t) {
        List<List<Edge>> result = new ArrayList<>();

        List<Edge> current = new ArrayList<>();
        findAllCombination(action, result,   0, current);

        for (List<Edge> eachSet: new ArrayList<>(result)) {
            if (!eachSet.isEmpty()) {
                result.add(findAllNeigbours(eachSet, getUB(t)));
            }

        }
        return result;
    }

    public List<Edge> findAllNeigbours(List<Edge> edges, List<Edge> edgesSet){
            Set<Edge> result = new HashSet<>();
            for (Edge edge: edgesSet){
                if (isAdjcent(edges, edge)){
                    result.add(edge);
                }
            }
            result.addAll(edges);

            return new ArrayList<>(result);
    }



    public void findAllCombination(List<Edge> action, List<List<Edge>>result,  int n, List<Edge> current) {
        if (n == action.size()) {
            List<Edge> newEdgesSet = new ArrayList<>(current);
            result.add(newEdgesSet);
            return ;
        }
        current.add(action.get(n));
        findAllCombination(action, result,n+1, current);
        current.remove(action.get(n));
        findAllCombination(action, result,n+1, current);
    }



    public double getReward( List<Edge> observation,  int t, int level) throws IloException {
        double bi[] = getBiArray();

        double z = getZ(this, t, observation,level);
        double[] rd = StateManager.getInstance().get(t).RD_t;
        double result = 0;
        for(Node d: getNd()){
            int i = d.getId() -1;
            result += bi[i]*(d.getRd(0) - rd[i] );
        }
        result += z;
        return result;
    }

    public double getER(int t, List<Edge> action, int level) throws IloException {

        double result =0;
        for (List<Edge> eachObservation : getObservation(action, t)){
            result += getPofObservation(eachObservation, t, level) *getReward(eachObservation, t, level);

        }

        return result;
    }


    public double getBenefit(int t) throws IloException {
        List<List<Edge>> allActions = getAllFeasibleActions(t);

        double max =-1;
        if (t==2) {
            return 0;
        }
        for (List<Edge> action: allActions) {
            initUPrime(action, t);
            double er= getER(t, action,0 );
            updateBeta(t+1);
            updateLamda(t+1);
            updateYt(t+1);
            max = Math.max(max, er) + getBenefit(t+1);
          //  System.out.println(action);
           // System.out.println(max);
        }
        return max;
    }

    public void updateLamda(int t) {
        for(Node each : N) {
            each.lamda.put(t, each.getLamda(0));
        }
    }

    public void updateYt(int t) {
        for(Node each : N) {
            each.Yt.put(t, each.getYt(0));
        }
    }




    public static void main(String args[]) {
        System.out.print("Run program ...");
        Graph graph = new Graph();
        graph.initNodes();
        graph.initEdges();
        graph.initBlocked();
        graph.initUnblocked();
        graph.initRB();
        graph.initUB();
        long t_start =  System.currentTimeMillis();
        System.out.println(System.currentTimeMillis());
        System.out.print("test .........................................................");
        System.out.println(graph.getAllFeasibleActions(0).size());
        try {
          //  System.out.print("test ...:"+ graph.getBenefit(0));
            graph.getBenefit(0);
        } catch (IloException e) {
            e.printStackTrace();
        }
        long t_end =  System.currentTimeMillis();
        System.out.println("xdu: cost: " + (t_end-t_start));

    }


}
