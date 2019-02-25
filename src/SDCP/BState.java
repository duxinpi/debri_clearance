package SDCP;

import java.util.List;

public class BState {

    int t;


    List<Edge> B; // use Edge to track beta i, j.
    double RC_t; // remaining capacity of resource to clear all.
    public double[] RS_t;
    public double[] RD_t;

    public BState(int t, List<Edge> B, double RC, double[] RS, double[] RD) {
        this.t = t;
        this.RC_t = RC;
        this.RS_t = RS;
        this.RD_t = RD;
        this.B = B;

    }


}
