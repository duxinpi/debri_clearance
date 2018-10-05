package SDCP;

import ilog.cplex.IloCplex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class State {

    List<Map<Integer, Integer>> Yt = new ArrayList<>(); // amount of resource required to clear all.
    Map<Integer, Integer> R_C; // remaining capacity of resource to clear all.

    List<Map<Integer, Integer>> R_S_t = new ArrayList<>(); //
    List<Map<Integer, Integer>> R_D_t = new ArrayList<>();





    public State ( List<Resource> v_w_t, Resource r_c, List<Resource> r_s_t, List<Resource> r_d_t ) {
        this.Yt = new ArrayList<>(v_w_t);
        this.R_C = new Resource(r_c);
        this.R_S_t = new ArrayList<>(r_s_t);
        this.R_D_t = new ArrayList<>(r_d_t);
    }



}
