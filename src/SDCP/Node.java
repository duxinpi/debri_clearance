package SDCP;

public class Node {


    private int id;

    private String type; //1 s, , 2 t, 3 d

    private Resource rs; // supply capacity;
    private Resource rd; // demand resource

//    private Resource b;  // benefit accrued

    private double mu =0; // average rate that service the node.

    private double lamda =0; // arrival rate at node.

    private double []R0 = new double[30];  //todo: remove constant






    private double Yt;
    private double RS;
    private double RD;
    private double B;


    public Node(int id, String type, double RS, double RD, double mu, double lamda, double Yt, double B) {
        this.id = id;
        this.type = type;
        this.RS = RS;
        this.RD = RD;
        this.mu = mu;
        this.lamda = lamda;
        this.Yt = Yt;
        this.B = B;
    }



    public int getId() {
        return id;
    }

    public void setId(int name) {
        this.id = name;
    }


    public double getRs() {
        return RS;
    }

   public double getRd() {
        return RD;
    }

    public void setRd(Resource rd) {
        this.rd = rd;
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

    public double getLamda() {
        return lamda;
    }

    public void setLamda(double lamda) {
        this.lamda = lamda;
    }

    public void setR0(double []R0) {
        this.R0 = R0;
    }

    public double[] getR0() {
        return R0;
    }

    public double getYt() {
        return Yt;
    }

    public void setYt(double yt) {
        Yt = yt;
    }
}
