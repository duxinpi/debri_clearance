package SDCP;



public class Edge {
    private int i;
    private int j;

    private int edgeType; // B, UB, RB, U



    public double[] beta = new double[2];

    public double w_ij[];





    private double FC_ij;

    private double f_ij;


    private double R;

    private double T;


    public Edge(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public Edge(int i, int j, double FC_ij, double T, double R, double []W, double []beta) {
        this.i  = i;
        this.j = j;
        this.FC_ij =FC_ij;
        this.T = T;
        this.R = R;
        this.w_ij = W;
        this.beta=beta;
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
