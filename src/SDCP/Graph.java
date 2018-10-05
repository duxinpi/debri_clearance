package SDCP;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Graph {
    private List<Edge> E;
    private List<Node> N;

    private List<Node> Ns;
    private List<Node> Nt;
    private List<Node> Nd;

    private List<Edge> Ut;
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

        Node d1 = new Node(1, "d", null, resource1);
        N.add(d1);

        Node t2 = new Node(2, "t", null, null);
        Node t3 = new Node(3, "t", null, null);

        N.add(t2);
        N.add(t3);


        Resource resource4 = new Resource();
        resource4.put(1, 10);
        resource4.put(2, 100);
        RS_t.add(resource4);
        Node s4 = new Node(4, "s", resource4, null);
        N.add(s4);


        Resource resource5 = new Resource();
        resource5.put(1, 50);
        resource5.put(2, 100);
        Node s5 = new Node(5, "s", resource4, null);
        RS_t.add(resource5);
        N.add(s5);


        Node t6 = new Node(6, "t", null, null);

        N.add(t6);


        Node d7 = new Node(7, "d", null, null);
        N.add(d7);

        System.out.println(N);

    }



    public void initEdges() {
        Wt = new ArrayList<>();
        E = new ArrayList<>();
        Resource DL45 = new Resource();
        DL45.put(1, 10);
        DL45.put(2, 10);
        Wt.add(DL45);
        Edge edge45 = new Edge(4, 5, DL45, 100);
        E.add(edge45);

        Resource DL43 = new Resource();
        DL45.put(1, 10);
        DL45.put(2, 10);
        Wt.add(DL43);
        Edge edge43 = new Edge(4, 3, DL45, 100);
        E.add(edge43);

        Resource DL53 = new Resource();
        DL45.put(1, 10);
        DL45.put(2, 10);
        Wt.add(DL53);
        Edge edge53 = new Edge(5, 3, DL45, 100);

        E.add(edge53);

        Resource DL56 = new Resource();
        DL45.put(1, 10);
        DL45.put(2, 10);
        Wt.add(DL56);
        Edge edge56 = new Edge(5, 6, DL45, 100);

        E.add(edge56);

        Resource DL63 = new Resource();
        DL45.put(1, 10);
        DL45.put(2, 10);
        Wt.add(DL63);
        Edge edge63 = new Edge(6, 3, DL45, 100);
        E.add(edge63);

        Resource DL67 = new Resource();
        DL45.put(1, 10);
        DL45.put(2, 10);
        Wt.add(DL67);
        Edge edge67 = new Edge(6, 3, DL45, 100);
        E.add(edge67);

        Resource DL71 = new Resource();
        DL45.put(1, 10);
        DL45.put(2, 10);
        Wt.add(DL71);
        Edge edge71 = new Edge(7, 1, DL45, 100);
        E.add(edge71);

        Resource DL31 = new Resource();
        DL45.put(1, 10);
        DL45.put(2, 10);
        Wt.add(DL31);
        Edge edge31 = new Edge(3, 1, DL45, 100);

        E.add(edge31);

        Resource DL12 = new Resource();
        DL45.put(1, 10);
        DL45.put(2, 10);
        Wt.add(DL12);
        Edge edge12 = new Edge(1, 2, DL45, 100);

        E.add(edge12);

        System.out.println(E);


    }

    public void initState() {
        s0 = new State(Wt, RC_t, RS_t, RD_t);

    }





}
