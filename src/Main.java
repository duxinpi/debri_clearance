
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


    public static String minWindow(String s, String t) {
        Map<Character, Integer> map = new HashMap<>();

        int lo =0;
        int minlo =0;
        int minLen = Integer.MAX_VALUE;
        String res = "";
        for (int i =0; i < t.length(); i++) {
            map.put(t.charAt(i), map.getOrDefault(t.charAt(i), 0) +1);
        }

        int counter =0;
        for(int hi =0; hi < s.length(); hi++) {
            char c = s.charAt(hi);
            if ( !map.containsKey(c)) continue;
            else {
                if (map.get(c) >0) counter++;
                map.put(c, map.get(c) -1);
            }


            while(counter == t.length()){
                if (minLen > hi -lo+1) {
                    minLen = hi -lo+1;
                    res = s.substring(lo, hi+1);
                }

                if ( !map.containsKey(s.charAt(lo))) {
                    lo++;
                    continue;
                }
                map.put(s.charAt(lo), map.get(s.charAt(lo)) +1);
                if (map.get(c) >0) counter--;
                lo++;

            }
        }

        return res;
    }


    // Recursive function to return gcd of a and b
    static int __gcd(int a, int b)
    {
        // Everything divides 0
        if (a == 0)
            return b;
        if (b == 0)
            return a;

        // base case
        if (a == b)
            return a;

        // a is greater
        if (a > b)
            return __gcd(a-b, b);
        return __gcd(a, b-a);
    }

    // Function to return the count of
// primitive roots modulo p
    static int countPrimitiveRoots(int p)
    {
        int result = 1;
        for (int i = 2; i < p; i++)
            if (__gcd(i, p) == 1){
                System.out.println("found i: " + i);
                return i;
            }


        return result;
    }




    public static void run() {


            //prompt the user to enter a and b
            int a = 480;
            int b = 7;

            //initialize variables in accordance with the algorithm
            int u1 = 1;
            int u2 = 0;
            int u3 = a;
            int v1 = 0;
            int v2 = 1;
            int v3 = b;
            int q = 0;


            System.out.println("u1\tv1\tu2\tv2\tu3\tv3\tq");

            //while v3 is not zero, calculate and display each row...

            //print the first intermediate steps in the first row

            System.out.println(u1 + "\t" + v1 + "\t" + u2 + "\t" + v2 + "\t" + u3 +
                    "\t" + v3 + "\t" + q);

            //keep printing the intermediate steps in subsequent rows until v3 is zero
            while (v3 != 0) {

                //compute the greatest integer less than or equal to u3/v3
                //(note this is "int division")
                q = u3 / v3;

                int temp;

                //remember the old value of vi, and compute vi = ui - q * vi
                //for i = 1 to 3 also make new ui equal to the old vi
                //(note the need for a temp variable here)
                temp = v1;
                v1 = u1 - q * v1;
                u1 = temp;

                temp = v2;
                v2 = u2 - q * v2;
                u2 = temp;

                temp = v3;
                v3 = u3 - q * v3;
                u3 = temp;

                //print the intermediate steps

                System.out.println(u1 + "\t" + u2   +
                        "\t" + u3 + "\t" + q);
            }

            //the gcd ends up being u3, and m is u1
            //n can be computed from gcd = m*a + n*b
            int gcd = u3;
            int m = u1;
            int n = (gcd - m*a) / b;


            System.out.println("One linear combination of " + a +
                    " and " + b + " that equals the gcd is given by:");

            System.out.println(gcd + " = " + m + "*" + a + " + " + n + "*" + b);
        }

    public static int subarraysDivByK(int[] A, int K) {
        // very simple
        if (A.length ==0) return 0;
        int p[] = new int[A.length+1];



        for (int i=0; i <A.length; i++) {
            p[i+1] = A[i]+ p[i];
        }
        Map<Integer, Integer> map = new HashMap<>();
        for (int i =0; i < p.length; i++) {
            int remainder = (p[i]%K+K)%K;
            map.put(remainder, map.getOrDefault(remainder, 0) +1);
        }
        int res =0;
        for (Integer each : map.keySet()) {
            res+= map.get(each)*(map.get(each) -1)/2;
        }
        return res;
    }

    public static void main(String args[]) {
        int[][] points= {{1,1,},{1,3},{3, 1}, {3, 3}, {2,2}};
     //   minAreaRect( points);

        String[] source = {"/*Test program */", "int main()", "{ ", "  // variable declaration ", "int a, b, c;", "/* This is a test", "   multiline  ", "   comment for ", "   testing */", "a = b + c;", "}"};
        run();
    //    minWindow("ADOBECODEBANC", "BANC");
        int[] A = {-1, 2, 9};

        subarraysDivByK(A,  2);

        System.out.println( "!!!!!" + countPrimitiveRoots(480));


        System.out.print(getFactors( 12));
        Graph graph = new Graph();
        graph.initGraph();
     //   graph.initEdges();
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
