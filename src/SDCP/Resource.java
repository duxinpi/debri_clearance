package SDCP;

import java.util.HashMap;
import java.util.Map;

public class Resource extends HashMap<Integer, Integer> {



    public Resource() {
       super();
       //this.put(1, 10); // truck.

       // this.put(2, 10); // bricks.



    }

    public Resource(Map<Integer, Integer> resouce) {
        super(resouce);
    }


    public int getTrucks(){
        return this.get(2);
    }


    public int norm() {
        return this.get(1) + this.get(2);
    }

}
