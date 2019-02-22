package SDCP;

import java.util.*;
import java.lang.*;
import java.io.*;

public class Dijsktra {
    // A utility function to find the vertex with minimum distance value,
    // from the set of vertices not yet included in shortest path tree
   int V ;
   double totalCpacity;
    public Dijsktra(int v, double totalCpacity){
        this.V = v;
        this.totalCpacity = totalCpacity;
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
        System.out.println("Vertex   Distance from Source " + source);
        for (int i = 0; i < V; i++)
            System.out.println("to " + i + ", distance: " + dist[i]);
    }

    public List<List<Integer>> dijkstra(double graph[][], int src,int des[]) {
        double dist[] = new double[V]; // The output array. dist[i] will hold
        List<List<Integer>> paths = new ArrayList<>();
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
        printSolution(dist, src);
        printPath( paths);
        List<List<Integer>> res= new ArrayList<>();
        for (int i =0; i < des.length;i++ ) {
            List<Integer> each = paths.get(des[i]);
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
                System.out.print(each  + " :  ");
            }
            System.out.println();
        }
    }

    // Driver method
    public static void main(String[] args) {
        /* Let us create the example graph discussed above */
        double graph[][] = new double[][]{{0, 4, 0, 0, 0, 0, 0, 8, 0},
                {4, 0, 8, 0, 0, 0, 0, 11, 0},
                {0, 8, 0, 7, 0, 4, 0, 0, 2},
                {0, 0, 7, 0, 9, 14, 0, 0, 0},
                {0, 0, 0, 9, 0, 10, 0, 0, 0},
                {0, 0, 4, 14, 10, 0, 2, 0, 0},
                {0, 0, 0, 0, 0, 2, 0, 1, 6},
                {8, 11, 0, 0, 0, 0, 1, 0, 7},
                {0, 0, 2, 0, 0, 0, 6, 7, 0}
        };
    //    Dijsktra t = new Dijsktra();
       // t.dijkstra(graph, 0);
    }
}