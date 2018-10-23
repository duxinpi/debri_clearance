package SDCP;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static SDCP.Utility.printArray;

public class Graph {
    private List<Edge> E;
    private List<Node> N;

    private List<Node> Ns;
    private List<Node> Nt;
    private List<Node> Nd;


    private List<Edge> Bt;
    private List<Edge> RBt;
    private List<Edge> UBt;

    List<Resource> Wt = new ArrayList<>(); // amount of resource required to clear all.

    List<Resource> RS_t = new ArrayList<>(); //
    List<Resource> RD_t = new ArrayList<>();


    List<Map<Integer, Double>> Yt;


    State s0;
    private Resource k_t = new Resource(); // total of clearance capacity

    public void initKt() {
        k_t.put(1, 10000);
        k_t.put(2, 10000);
        RC_t = k_t;
    }

    public int getNsize() {
        if (N ==null) return 0;
        return N.size();
    }
    public int getNsizeSqaure() {
        return N.size()*N.size();
    }

    public double[] getCj() {
        double[] result = new double[getNsize()];
        int k = getR0()[0].length;
        for (int i =0; i < result.length; i ++) {
            result[i] = k ;
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
        Wt = new ArrayList<>();
        E = new ArrayList<>();


        Resource DL34 = new Resource();
        DL34.put(1, 10);
        DL34.put(2, 10);
        Wt.add(DL34);
        Edge edge34 = new Edge(3, 4, DL34, 0, 0.94, 0.04);
        E.add(edge34);

        Resource DL35 = new Resource();
        DL35.put(1, 10);
        DL35.put(2, 10);
        Wt.add(DL35);
        Edge edge35 = new Edge(3, 5, DL35, 100, 1.26, 0.03);

        E.add(edge35);

        Resource DL36 = new Resource();
        DL36.put(1, 10);
        DL36.put(2, 10);
        Wt.add(DL36);
        Edge edge36 = new Edge(3, 6, DL36, 100, 0.63, 0.04);

        E.add(edge36);
        Resource DL37 = new Resource();
        DL37.put(1, 10);
        DL37.put(2, 10);
        Wt.add(DL37);
        Edge edge37 = new Edge(3, 7, DL37, 100, 0.94, 0.04);
        E.add(edge37);




        Resource DL45 = new Resource();
        DL45.put(1, 10);
        DL45.put(2, 10);
        Wt.add(DL45);
        Edge edge45 = new Edge(4, 5, DL45, 100, 0.77, 0.02);
        E.add(edge45);

        Resource DL46 = new Resource();
        DL46.put(1, 10);
        DL46.put(2, 10);
        Wt.add(DL46);
        Edge edge46 = new Edge(4, 6, DL46, 100, 1.47, 0.07);
        E.add(edge46);

        Resource DL47 = new Resource();
        DL47.put(1, 10);
        DL47.put(2, 10);
        Wt.add(DL47);
        Edge edge47 = new Edge(4, 7, DL47, 100, 1.26, 0.04);
        E.add(edge47);

        Resource DL56 = new Resource();
        DL56.put(1, 10);
        DL56.put(2, 10);
        Wt.add(DL56);
        Edge edge56 = new Edge(5, 6, DL56, 100, 0.95, 0.07);
        E.add(edge56);

        Resource DL57 = new Resource();
        DL57.put(1, 10);
        DL57.put(2, 10);
        Wt.add(DL57);
        Edge edge57 = new Edge(5, 7, DL57, 100, 1.47, 0.07);
        E.add(edge57);

        Resource DL67 = new Resource();
        DL67.put(1, 10);
        DL67.put(2, 10);
        Wt.add(DL67);
        Edge edge67 = new Edge(6, 7, DL67, 100, 0.63, 0.03);
        E.add(edge67);
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
        double [][]result = new double[n][n];
        for (Edge edge: E) {
            result[edge.getI()-1][edge.getJ()-1] = edge.getT();
            result[edge.getJ()-1][edge.getI()-1] = edge.getT();
        }
        return result;
    }

    public double[][] getR() {
        int n = N.size();
        double [][]result = new double[n][n];
        for (Edge edge: E) {
            result[edge.getI()-1][edge.getJ()-1] = edge.getR();
            result[edge.getJ()-1][edge.getI()-1] = edge.getR();
            result[edge.getI()-1][edge.getI()-1] = 999; // why?
            result[edge.getJ()-1][edge.getJ()-1] = 999; // why?
        }
        return result;
    }

    public double[] getRS() {

        double []result = new double[N.size()];
        for (Node each : N) {
            result[each.getId()-1] = each.getRs();
        }
        return  result;
    }

    public double[] getRD() {

        double []result = new double[N.size()];
        for (Node each : N) {
            result[each.getId()-1] = each.getRd();
        }
        return  result;
    }



    public double[][] getR0() {
        int n = N.size();
        Node node = N.get(0);
        double [][]result = new double[n][node.getR0().length];
        for (Node i: N) {
            result[i.getId()-1] = i.getR0();
        }


        return result;
    }

    public double[] getLamda() {
        double []result = new double[N.size()];
        for (Node each : N) {
            result[each.getId()-1] = each.getLamda();
        }
        return  result;
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
            if (each.getId() > 2){
                nd.add(each);
            }
        }
        return nd.toArray(new Node[nd.size()]);
    }

    public Node[] getNodes(String s) {
        List<Node> nd = new ArrayList<>();
        for (Node each : N) {
            if (each.getType().equals(s)){
                nd.add(each);
            }
        }
        return nd.toArray(new Node[nd.size()]);
    }










    public double[] converStringToArray(String s) {
        double[] result;
        String temp[] = s.split("\\s+");
        result = new double[temp.length];
        for (int i =0; i < temp.length; i++) {
            result[i] = Double.valueOf(temp[i]);
        }

        return result;

    }

    public double[] getBiArray() {
        double[] result = new double[N.size()];
        for (int i =0; i < N.size(); i++) {
            result[i] = N.get(i).getB();
        }
        return result;
    }
    List<int[][]> Ut = new ArrayList<>();

    public void initUt(){
        int[][] t1 = {{5,3}, {4,3}, {5,6}, {6,7}};
        int[][] t2 = {{7,6}};
        Ut.add(t1);
        Ut.add(t2);
    }
    public List<int[][]> getUt(){
        return Ut;
    }

    public List<Node> getN() {
        return N;
    }


    public static void main(String args[]) {
        System.out.print("Run program ...");
        Graph graph = new Graph();
        graph.initNodes();
        graph.initEdges();
        printArray(graph.getNd());
        printArray(graph.getNp());
        printArray(graph.getNs());
        printArray(graph.getCj());
        printArray(graph.getBiArray());

    }



}
