
import SDCP.*;

import java.util.*;


public class Main {


    public static List<List<Integer>> getFactors(int n) {
        List<List<Integer>> res = new ArrayList<>();
        if (n<=1){
            return res;
        }
        List<Integer> cur = new ArrayList<>();
        dfs( n, cur,res, 2);
        return res;

    }


    public static void dfs(int n, List<Integer> cur, List<List<Integer>>res, int k) {

        if (n <=1) {
            System.out.println(cur.size());
            res.add(new ArrayList<Integer>(cur));
            return ;
        }
        for (int i = k; i<n; i++) {
            if (n%i == 0) {
                cur.add(i);
                System.out.println(cur.size());
                dfs( n/i, cur,res, i);
                cur.remove(cur.size() -1);
            }
        }
    }

    public static int minAreaRect(int[][] points) {
        TreeMap<Integer, Set<Integer>> map = new TreeMap<>();
        for (int[] each : points) {
            if (!map.containsKey(each[0])){
                map.put(each[0], new TreeSet<>());
            } map.get(each[0]).add(each[1]);
        }

        int min = Integer.MAX_VALUE;

        for(int i =0; i < points.length; i++) {
            for (int j =0; j< points.length; j++) {
                if (points[i][0] == points[j][0])continue;
                if (points[i][1] == points[j][1]) continue;

                int area = Math.abs(points[i][0] - points[j][0]) *  Math.abs(points[i][1] - points[j][1]);
                min = Math.min(area, min);
            }

        }
        if (min == Integer.MAX_VALUE) return 0;
        return min;
    }

    public static void main(String args[]) {
        int[][] points= {{1,1,},{1,3},{3, 1}, {3, 3}, {2,2}};
        minAreaRect( points);

        String[] source = {"/*Test program */", "int main()", "{ ", "  // variable declaration ", "int a, b, c;", "/* This is a test", "   multiline  ", "   comment for ", "   testing */", "a = b + c;", "}"};



        System.out.print(getFactors( 12));
        Graph graph = new Graph();
        graph.initNodes();
        graph.initEdges();
      //  graph.initUPrime();
        graph.initBlocked();
        graph.initUnblocked();
        graph.initRB();
/*

        try {
         //   getZ( graph, 0, 0);
        } catch (IloException e) {
            e.printStackTrace();
        }
*/
    }


}
