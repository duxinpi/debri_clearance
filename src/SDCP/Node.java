package SDCP;

public class Node {


    private int id;

    private String type; //1 s, , 2 t, 3 d

    private Resource rs; // supply capacity;
    private Resource rd; // demand resource

    private Resource b;  // benefit accrued

    public Node(int id, String type, Resource rs, Resource rd) {
        this.id = id;
        this.type = type;
        this.rs = rs;
        this.rd = rd;
    }

    public int getName() {
        return id;
    }

    public void setName(int name) {
        this.id = name;
    }


    public Resource getRs() {
        return rs;
    }

    public void setRs(Resource rs) {
        this.rs = rs;
    }

    public Resource getRd() {
        return rd;
    }

    public void setRd(Resource rd) {
        this.rd = rd;
    }

    public Resource getB() {
        return b;
    }

    public void setB(Resource b) {
        this.b = b;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    @Override
    public String toString() {
        return (" " + id + type + " ->");
    }

}
