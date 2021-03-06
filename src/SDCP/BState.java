package SDCP;

import java.util.ArrayList;
import java.util.List;

public class BState {

    int t;


    List<Edge> B; // use Edge to track beta i, j.
    double RC_t; // remaining capacity of resource to clear all.
    public double[] RS_t;
    public double kt;
    public double[] RD_t;
    double gamma;
    Observation observation;

    List<Edge> action;
    List<Node> N;

    public BState(int t, List<Edge> B, double RC, double[] RS, double[] RD, double kt, List<Node> N) {
        this.t = t;
        this.RC_t = RC;
        this.RS_t = RS;
        this.RD_t = RD;
        this.B = B;
        this.kt = kt;
        gamma =0;
        this.N = N;

    }

    public BState(List<Edge> action, Observation observation) {
        this.observation = observation;
        this.action = action;
        this.B = new ArrayList<>(observation.bState.B);
        this.RC_t = observation.bState.RC_t;
        this.RS_t = observation.bState.RS_t;
        this.RD_t = observation.bState.RD_t;
        this.kt = observation.bState.kt;
        this.N = observation.N;
        this.t = observation.bState.t; // although here is t, but state is actually t+1;
        updateB();
        updateRC();
       // updateRS();
        updateRD();

    }

    public void updateB() {
       /* for (Edge each : B) {
            if (Utility.contains(observation.UB, each)) {
                for (int i =0 ; i< each.w_ij.length;i++){
                    each.beta[i] = each.beta[i]; //todo: udpate with gamma process.
                }

            } else {

                for (int i =0 ; i< each.w_ij.length;i++){
                    if (i == 1){ // todo: here is supposed to be a real input from system.
                        each.beta[i] = 1;
                    } else {
                        each.beta[i] =0;
                    }

                }

            }
        }*/

    }

    public void updateRC(){

        RC_t = Math.max(getKt()- getGarma(), 0);
    }

    public void updateRS(Observation observation, int t, double[][] fij) {
        if (t == 0) return;

        for (Node each : N) {

            double sum1 = 0;
            for (int j = 1; j <= N.size(); j++) {
                if (Utility.Match(observation.getUprimeNext(), each.getId(), j)) {
                    sum1 += fij[each.getId() - 1][j - 1];
                }
            }

            double sum2 = 0;
            for (int j = 1; j <= N.size(); j++) {
                if (Utility.Match(observation.getUprime(), j, each.getId())) {
                    sum2 += fij[j - 1][each.getId() - 1];
                }
            }

            RS_t[each.getId()-1]=  RS_t[each.getId()-1] - (sum1 - sum2);

        }
    }

    public void updateRD() {
        if (t == 0) return;
        for (Node each : N) {
            RD_t[each.getId()-1] =  RD_t[each.getId()-1] - observation.K[each.getId() - 1];
        }
    }
    public double getGarma( ) {
        return getWsum();
    }

    public double getKt() {

        return kt;
    }

    public double getWsum() {
        double sum = 0;
        for (int i = 0; i < observation.observation.size(); i++) {
            sum += observation.observation.get(i).w_ij[1];
        }
        return sum;
    }

}
