package SDCP;


import java.util.ArrayList;
import java.util.List;

public class Edge {
    public int i;
    public int j;

    public Edge(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public static void dfs(List<Edge> temp, List<Edge> edgeSet, List<List<Edge>> res, int k, int start) {

        System.out.println("wo budong ");
            res.add(new ArrayList<>(temp));
        for (int i = start; i <edgeSet.size(); i++) {
            temp.add(edgeSet.get(i));
            dfs(temp, edgeSet,res, k-1,   i+1);
            temp.remove(temp.size() -1);
        }
    }
     public static void main(String [] args) {
        List<Edge> current = new ArrayList<>();
        current.add(new Edge(1,2));
        current.add(new Edge(3,4));

        List<Edge> tempEdges = new ArrayList<>();
        List<List<Edge>> res = new ArrayList<>();
        dfs(tempEdges,current,res, current.size(), 0);
        System.out.println("size" + res.size());
    }
    public String edgeType; // B, UB, RB, U



    public double[] beta = new double[2];

    public double w_ij[];





    private double FC_ij;

    private double f_ij;


    private double R;

    private double T;




    public Edge(int i, int j, double FC_ij, double T, double R, double []W, double []beta, String edgeType) {
        this.i  = i;
        this.j = j;
        this.FC_ij =FC_ij;
        this.T = T;
        this.R = R;
        this.w_ij = W;
        this.beta=beta;
        this.edgeType = edgeType;
    }

    public int getI(){
        return i;
    }

    public int getJ() {
        return j;
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

    public double getWmin(){
        double min = Double.MAX_VALUE;
        for (double each : w_ij) {
            min = Math.min(each, min);
        }
        return min;
    }

    public double getFC_ij() {
        return FC_ij;
    }

    public void setFC_ij(double FC_ij) {
        this.FC_ij = FC_ij;
    }
    @Override
    public String toString(){
        return (i <j)? i + "-"+j : j + "-" +i;

    }

}
