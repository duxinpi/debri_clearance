package SDCP;

public class BState {

    int t;
    public double [][]Wt ;// amount of resource required to clear all.
    public double []RS_t;
    public double[] RD_t;




    double  RC_t; // remaining capacity of resource to clear all.
    public double garmma;




    public BState(int t) {
        this.t = t;
    }

    public BState(int t, double[][]W,  double RC, double[]RS, double []RD ) {
        this.t = t;
        this.Wt = W;
        this.RC_t = RC;
        this.RS_t = RS;
        this.RD_t = RD;
        this.garmma = garmma;

    }













}
