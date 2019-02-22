package SDCP;

import java.util.HashMap;
import java.util.Map;

public class Node {


    private int id;
    private  double B ;

    private String type; //1 s, , 2 t, 3 d

    public Map<Integer, Double> Yt = new HashMap<>();
    public Map<Integer, Double> RS = new HashMap<>();
    public Map<Integer, Double> RD = new HashMap<>();
    public Map<Integer, Double> lamda = new HashMap<>(); // arrival rate at node.


//    private Resource b;  // benefit accrued

    private double mu = 0; // average rate that service the node.



    private double[] R0 = new double[30];  //todo: remove constant
    //todo: Yjt matlab update.





    public Node(int id, String type, double RS, double RD, double mu, double lamda, double Yt, double B) {
        this.id = id;
        this.type = type;
        this.RS.put(0, RS);
        this.RD.put(0,RD);
        this.mu = mu;
        this.lamda.put(0, lamda);
        this.Yt.put(0, Yt);
        this.B = B;
    }



    public int getId() {
        return id;
    }

    public double getRs(int t) {
        return RS.get(t);
    }

    public double getRd(int t) {
        return RD.get(t);
    }

    public void setRS(int t, double value) {
        this.RS.put(t, value);
    }

    public void setRD(int t, double value) {
        this.RD.put(t, value);
    }


    public double getB() {
        return B;
    }

    public void setB(double b) {
        this.B = b;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    @Override
    public String toString() {
        return (" " + id + type + " ");
    }

    public double getMu() {
        return mu;
    }

    public void setMu(double mu) {
        this.mu = mu;
    }

    public double getLamda(int t) {
        return lamda.get(t);
    }


    public void setR0( double []R0) {
        this.R0 = R0;
    }

    public double[] getR0() {
        return R0;
    }

    public double getYt(int t) {
        return Yt.get(t);
    }


}
