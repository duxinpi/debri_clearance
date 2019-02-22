package SDCP;


import java.util.ArrayList;
import java.util.List;

public class Edge {
    private int i;
    private int j;

    private int edgeType; // B, UB, RB, U




    public List<double[]> W_ij = new ArrayList<>(); //required resource at each time t.
    public List<double[]> beta = new ArrayList<>();





    private double FC_ij;

    private double f_ij;


    private double R;

    private double T;


    public Edge(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public Edge(int i, int j, double FC_ij, double T, double R, double[] W, double []beta) {
        this.i  = i;
        this.j = j;
        this.FC_ij =FC_ij;
        this.T = T;
        this.R = R;
        this.W_ij.add(W);
        this.W_ij.add(W);
        this.W_ij.add(W);
        this.beta.add(beta);
    }

    public int getI(){
        return i;
    }

    public int getJ() {
        return j;
    }


    @Override
    public String toString() {
        return ("[ " + i + " " + j +" ]");
    }


    public double getT() {
        return T;
    }

    public void setT(double t) {
        T = t;
    }

    public double getR() {
        return R;
    }

    public void setR(double r) {
        R = r;
    }

}
