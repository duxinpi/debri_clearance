package SDCP;

import java.util.*;
import java.lang.*;
import java.io.*;

public class Dijsktra {
    // A utility function to find the vertex with minimum distance value,
    // from the set of vertices not yet included in shortest path tree
   int V ;

    public Dijsktra(int v){
        this.V = v;
    }

    public int minDistance(double dist[], Boolean sptSet[]) {
        // Initialize min value
        double min = Integer.MAX_VALUE;
        int min_index = -1;

        for (int v = 0; v < V; v++)
            if (sptSet[v] == false && dist[v] <= min) {
                min = dist[v];
                min_index = v;
            }

        return min_index;
    }

    public void printSolution(double dist[], int source) {
        System.out.println("Vertex   Distance from Source " + (source+1));
        for (int i = 0; i < V; i++)
            System.out.println("to " + (i+1) + ", distance: " + dist[i]);
    }

    public void fillShortestPath(double graph[][], double[][] result,   List<List<Integer>> paths){
      //  double[][] result = new double[graph.length][graph[0].length];
        int des[] = new int[graph.length];
        for (int i =0 ; i< des.length; i++){
            des[i] = i;
        }

        for (int  i =0; i < graph.length; i++ ){
            List<List<Integer>> each = new ArrayList<>();
            dijkstra(graph,  i, des, result[i], each);
            paths.addAll(each);
        }

    }

   public List<List<int[]>> getFeasiblePaths(double graph[][]){

       Map<String, int[]> edges = new HashMap<>();
       for (int i =0; i < graph.length; i++){
           for (int j =0; j< graph[0].length;j++){
               List<int[]> one = getExtendedPath( graph,  i,   j, 6);
               for (int k =0; k < one.size(); k++) {
                   edges.put(one.get(k)[0]+ "-" + one.get(k)[1], one.get(k));
               }
           }
       }
return null;

   }


    public List<List<Integer>> dijkstra(double graph[][], int src,int des[]) {
        double [] dist = new double[graph.length];
        List<List<Integer>> paths = new ArrayList<>();
        return dijkstra(graph, src, des, dist, paths);


    }

   public List<int[]>[][] getPath(List<List<Integer>> paths, int n ) {
        List<int[]>[][] result = new List[n][n];
        for (List<Integer> eachPath : paths){
            if (eachPath.size() ==1){
                continue;
            }
            int i = eachPath.get(0);
            int j = eachPath.get(eachPath.size()-1);
            result[i][j] = getEdges(eachPath);
        }
        return  result;
    }

    public List<int[]> getExtendedPath(double graph[][], int src, int des, int R){

        double[][]shortestPathCostMatrix = new double[graph.length][graph[0].length];
        List<List<Integer>> paths = new ArrayList<>();
        fillShortestPath(graph, shortestPathCostMatrix, paths);
        List<int[]>[][] pathsMatrix = getPath(paths, graph.length);
        List<int[]> edges = new ArrayList<>();
        Set<String> set = new HashSet<>();
        List<int[]> shortPath = pathsMatrix[src][des];




        double sum =0;
        for (int i =0; shortPath !=null && i <   shortPath.size() ; i++){
            sum += shortestPathCostMatrix[shortPath.get(i)[0]][shortPath.get(i)[1]];
            if (sum<= R){
                if (!set.contains( shortPath.get(i)[0] +"-"+ shortPath.get(i)[1])) {
                    edges.add(shortPath.get(i));
                    set.add( shortPath.get(i)[0] +"-"+ shortPath.get(i)[1]);
                }
            }
        }
      //  edges.addAll(pathsMatrix[src][des]);
       // double Wmin = shortestPathCostMatrix[src][des];
        for (int i =0; i < graph.length; i++){
            if (i== src)continue;
            if (shortestPathCostMatrix[src][i]<R/2){
                for (int[] each : pathsMatrix[src][i]){
                    if (!set.contains( each[0] +"-"+ each[1])){
                        edges.add(each);
                        set.add( each[0] +"-"+ each[1]);
                    }
                }
            }
        }



        return edges;
    }


    public boolean contains(List<int[]>arrays, int[] b){
        for (int[] a: arrays) {
          if (a[0] == b[0]&& a[1] == b[1])return true;
            if (a[1] == b[0]&& a[0] == b[1])return true;

        }
        return false;
    }


    public List<int[]> getEdges(List<Integer> eachPath){
        List<int[]> result = new ArrayList<>();
        for (int i =1 ; i< eachPath.size(); i++){
            result.add(new int[]{eachPath.get(i-1), eachPath.get(i)});
        }
        return result;
    }

    public List<List<Integer>> dijkstra(double graph[][], int src,int des[], double dist[], List<List<Integer>> paths) {
      //  double dist[] = new double[V]; // The output array. dist[i] will hold
       // List<List<Integer>> paths = new ArrayList<>();
        Boolean sptSet[] = new Boolean[V];

        // Initialize all distances as INFINITE and stpSet[] as false
        for (int i = 0; i < V; i++) {
            dist[i] = Integer.MAX_VALUE;
            sptSet[i] = false;
            List<Integer> path = new ArrayList<>();
            paths.add(path);
            path.add(src);

        }

        // Distance of source vertex from itself is always 0
        dist[src] = 0;

        // Find shortest path for all vertices
        for (int count = 0; count < V - 1; count++) {

            // Pick the minimum distance vertex from the set of vertices
            // not yet processed. u is always equal to src in first
            // iteration.
            int u = minDistance(dist, sptSet);

            // Mark the picked vertex as processed
            sptSet[u] = true;

            // Update dist value of the adjacent vertices of the
            // picked vertex.
            for (int v = 0; v < V; v++) {
                // Update dist[v] only if is not in sptSet, there is an
                // edge from u to v, and total weight of path from src to
                // v through u is smaller than current value of dist[v]
                if (!sptSet[v] && graph[u][v] != 0 &&
                        dist[u] != Integer.MAX_VALUE &&
                        dist[u] + graph[u][v] < dist[v]) {

                    dist[v] = dist[u] + graph[u][v];
                    paths.get(v).clear();
                    paths.get(v).addAll(paths.get(u));
                    paths.get(v).add(v);
                }
            }
        }

        // print the constructed distance array
      //  printSolution(dist, src);
       // printPath( paths);
        List<List<Integer>> res= new ArrayList<>();
        for (int i =0; i < des.length;i++ ) {
            List<Integer> each = new ArrayList<>(paths.get(des[i]));
            for(int j =0; j < each.size(); j++) {
                each.set(j, each.get(j)+1);
            }
            res.add(each);
        }
        return res;
    }

    public void printPath(List<List<Integer>> result) {
        for(List<Integer> eachList: result) {
            for(Integer each : eachList) {
                System.out.print((each+1)  + " :  ");
            }
            System.out.println();
        }
    }



    // Driver method
    public static void main(String[] args) {
        /* Let us create the example graph discussed above */
        /*double graph[][] = new double[][]
                       {{0, 4, 0, 0, 0, 0, 0, 8, 0},
                        {4, 0, 8, 0, 0, 0, 0, 11, 0},
                        {0, 8, 0, 7, 0, 4, 0, 0, 2},
                        {0, 0, 7, 0, 9, 14, 0, 0, 0},
                        {0, 0, 0, 9, 0, 10, 0, 0, 0},
                        {0, 0, 4, 14, 10, 0, 2, 0, 0},
                        {0, 0, 0, 0, 0, 2, 0, 1, 6},
                        {8, 11, 0, 0, 0, 0, 1, 0, 7},
                        {0, 0, 2, 0, 0, 0, 6, 7, 0}
                };*/


        double graph[][] = new double[][]
                        {{0, 4, 2, 0,0, 0, 6},
                        {4, 0, 3,  0,0, 0, 0},
                        {2, 3, 0,  4,5, 4, 0},
                        {0, 0, 4,  0,3, 0, 0},//[1,2, 5, 6]
                        {0, 0, 5,  3,0, 3, 0},
                        {0, 0, 4,  0,3, 0, 1},
                        {6, 0, 0,  0,0, 1, 0},
                };

      /*  double graph[][] = new double[][]
                {{0, 4, 2, 6,7, 6, 6},
                        {4, 0, 3, 7,8, 7, 8},
                        {2, 3, 0, 4,5, 4, 5},
                        {6, 7, 4, 0,3, 6, 7},//[1,2, 5, 6]
                        {7, 8, 5, 3,0, 3, 4},
                        {6, 7, 4, 6,3, 0, 1},
                        {6, 8, 5, 7,4, 1, 0},
                };*/
        Dijsktra t = new Dijsktra(graph.length);
        double[][]result = new double[graph.length][graph[0].length];
        List<List<Integer>> paths = new ArrayList<>();


        t.fillShortestPath( graph, result, paths);
        // print shortest path matrix.
        Utility.printDoubleArray(result);

        for (int i =0; i < paths.size(); i++){
            for (int j =0; j < paths.get(i).size(); j++){
                System.out.print(paths.get(i).get(j) +"~~~");
            }
            System.out.println();
        }
   /*     List<int[]> edges = t.getExtendedPath( graph,  2,  0);
        for (int[] each : edges){
            System.out.println(each[0] +"-"+each[1]);
        }*/

        Map<String, int[]> edges = new HashMap<>();
        for (int i =0; i < graph.length; i++){
            for (int j =0; j< graph[0].length;j++){
                List<int[]> one = t.getExtendedPath( graph,  i,   j, 6);
                for (int k =0; k < one.size(); k++) {
                    if(one.get(k)[1] > one.get(k)[0] ) {
                        edges.put(one.get(k)[0] + "-" + one.get(k)[1], one.get(k));
                    }
                }
            }
        }


        for (int[] each : edges.values()){
            System.out.println((each[0]+1) +"-"+(1+each[1]) + ", cost: " + result[each[0]][each[1]]);
        }
        System.out.println("size of edges: " + edges.size());

        // t.dijkstra(graph, 0, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8});
        // test case 1: set 1 supply node, 1 demand node.


        // test case 2: set 2 supply nodes{0,1} and 2 demand nodes. {7,4}


    }


}