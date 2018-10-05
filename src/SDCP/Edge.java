package SDCP;

import java.util.Map;

public class Edge {
    private int i;
    private int j;

    private int edgeType; // B, UB, RB, U




    private Resource W_ij; //required resource


    private Resource DL_ij = new Resource();
    public void init(){
        DL_ij.put(1, 10); // truck.
        DL_ij.put(2, 10); // bricks.
    }

    private double FC_ij;

    private double f_ij;

    private Map<Integer, Double> beta;



    public Edge(int i, int j, Resource DL_ij, double FC_ij) {
        this.i  = i;
        this.j = j;
        this.DL_ij = DL_ij;
        this.FC_ij =FC_ij;
    }


    @Override
    public String toString() {
        return ("[ " + i + " " + j +" ]");
    }

    public Resource getDL() {
        return DL_ij;
    }

    public void setDL(Resource DL) {
        this.DL_ij = DL;
    }
}
