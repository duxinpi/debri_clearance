package SDCP;


import ilog.concert.IloException;

import java.util.*;

import static SDCP.Utility.*;
import static SDCP.GlobalData.*;

public class Graph {
    private List<Edge> E;
    private List<Node> N;

    Dijsktra dijsktra;
    public int t = 0;

    double max = -1;
    List<Edge> bestAction = null;
    List<double[]> bestXY = new ArrayList<>();
    double[] bestX = null;
    Set<String> visitedAction = new HashSet<>();
    Set<String> visitedObservation = new HashSet<>();

    List<List<Edge>> B = new ArrayList<>();
    List<List<Edge>> U = new ArrayList<>();
    List<List<Edge>> UB = new ArrayList<>();
    List<List<Edge>> RB = new ArrayList<>();
    double kt;
    BState bState;


    public Graph() {
        kt = RC;

    }


    public void initBlocked() {
        B.clear();
        B.add(E);
        B.add(new ArrayList<>());
        B.add(new ArrayList<>());
        B.add(new ArrayList<>());
        B.add(new ArrayList<>());

    }

    public void initUnblocked() {
        U.clear();
        U.add(new ArrayList<>());
        U.add(new ArrayList<>());
        U.add(new ArrayList<>());
        U.add(new ArrayList<>());
    }

    public void initRB() {
        RB.clear();
        RB.add(new ArrayList<>());
        RB.add(new ArrayList<>());
        RB.add(new ArrayList<>());
        RB.add(new ArrayList<>());


    }

    public void initUB() {
        UB.clear();
        List<Edge> ub = Utility.exclude(B.get(0), RB.get(0));
        UB.add(ub);
        UB.add(new ArrayList<>());
        UB.add(new ArrayList<>());
        UB.add(new ArrayList<>());

    }


    public double[][] getW() {
        double[][] result = new double[N.size()][N.size()];
        for (Edge each : E) {
            double min = each.w_ij[0];
            for (int i = 1; i < each.w_ij.length; i++) {
                min = Math.min(min, each.w_ij[i]);
            }
            result[each.getI() - 1][each.getJ() - 1] = min;
            result[each.getJ() - 1][each.getI() - 1] = min;
        }
        return result;
    }

    public List<List<Edge>> getAllSequencesShortestPath(int t) {
        List<Edge> remain = B.get(t);
        List<List<Edge>> actions = new ArrayList<>();
        dijsktra = new Dijsktra(N.size());
        Set<Integer> nodeSet = new HashSet<>();
        for (int i = 0; i < U.get(t).size(); i++) {
            nodeSet.add(U.get(t).get(i).getI());
            nodeSet.add(U.get(t).get(i).getJ());
        }
        for (int i = 0; i < getNs().length; i++) {
            nodeSet.add(getNs()[i].getId());
        }

        Node[] demandNodes = getNd();

        List<List<Integer>> paths = new ArrayList<>();
        List<int[]> extendedPaths = new ArrayList<>();
        Set<String> set = new HashSet<>();
        for (Integer eachNode : nodeSet) {
            //  paths = dijsktra.dijkstra(getW(), eachNode - 1, demandNodesId);
            for (int i = 0; i < demandNodes.length; i++) {
                List<int[]> extend = dijsktra.getExtendedPath(getW(), eachNode - 1, demandNodes[i].getId() - 1, RC);
                List<Edge> action = new ArrayList<>();
                for (int[] each : extend) {
                    Edge edge = Utility.getEdge(remain, each[0] + 1, each[1] + 1);
                    if (edge != null)
                        action.add(edge);
                }
                if (action.size() > 0) {
                    actions.add(action);
                }
            }
        }

        //   findSequences(edges, remain, actions);
        //    actions = convertToActions(extendedPaths, remain,nodeSet);
        //todo: xdu, complete. test:  System.out.println("number of actions: " + actions.size());
        return actions;
    }


    public List<List<Edge>> convertToActions(List<int[]> paths, List<Edge> remaining, Set<Integer> nodeSet) {
        List<List<Edge>> result = new ArrayList<>();

        for (int[] each : paths) {
            Edge edge = Utility.getEdge(remaining, each[0] + 1, each[1] + 1);
            if (edge != null && (nodeSet.contains(edge.getI()) || nodeSet.contains(edge.getJ()))) {
                List<Edge> eachAction = new ArrayList<>();
                eachAction.add(edge);
                result.add(eachAction);
            }
        }
        List<List<Edge>> twoStepAction = new ArrayList<>();
        for (int[] each : paths) {
            Edge edge = Utility.getEdge(remaining, each[0] + 1, each[1] + 1);
            if (edge == null) continue;
            for (List<Edge> eachAction : result) {
                if (Utility.isAdjcent(eachAction.get(0), edge) && !(nodeSet.contains(edge.getI()) || nodeSet.contains(edge.getJ()))) {
                    List<Edge> newAction = new ArrayList<>();
                    newAction.add(eachAction.get(0));
                    newAction.add(edge);
                    twoStepAction.add(newAction);
                }
            }

        }
        result.addAll(twoStepAction);

        return result;
    }

    public List<List<Edge>> convertToActions(List<List<Integer>> paths, List<Edge> remaining) {
        List<List<Edge>> actions = new ArrayList<>();
        List<Edge> action = new ArrayList<>();
        for (List<Integer> path : paths) {
            converToActions(path, actions, action, remaining, 0);
        }
        return actions;
    }


    public List<List<Edge>> getAllFeasibleActions(int t) {
        List<List<Edge>> allActions = getAllSequencesShortestPath(t);
        //todo: question, what is our goal? what if the shortest path is cleared completely.
        /*for (int i = allActions.size() - 1; i >= 0; i--) {
            List<Edge> eachAction = allActions.get(i);
            if (getWMinSum(eachAction, t) > RC) {
                allActions.remove(i);
            }
        }*/
        getAllFeasibleActions2(t);
        return allActions;
    }

    public List<List<Edge>> getAllFeasibleActions2(int t) {
        List<List<Edge>> allActions = getAllSequencesShortestPath(t);
        List<List<Edge>> newAllActions = new ArrayList<>();
        //todo: question, what is our goal? what if the shortest path is cleared completely.
        for (int i = allActions.size() - 1; i >= 0; i--) {
            List<Edge> eachAction = allActions.get(i);
            List<Edge> newAction = new ArrayList<>();
            double B = GlobalData.B;
            for (Edge each : eachAction) {
                int id = each.j;
                Node node = Utility.getNode(id, N);
                double lamda1 = node.getLamda(t);

                double rightSide = node.getMu() * (node.getR0()[0] + Utility.getSumCj(node, B));
                if (lamda1 > rightSide) {
                    break;
                }
                newAction.add(each);
                double cost = B;
                while (lamda1 <= rightSide && cost > 0) {
                    rightSide = node.getMu() * (node.getR0()[0] + Utility.getSumCj(node, cost));
                    cost--;
                }
                cost++;
                B = B - cost;
            }
            newAllActions.add(newAction);

        }

        return newAllActions;
    }

    public List<Edge> getObservation(List<Edge> action, int t) {

        List<Edge> UBt = UB.get(t);
        Set<Edge> result = new HashSet<>();
        Set<Integer> nodeSet = new HashSet<>();
        for (Edge each : action) {
            nodeSet.add(each.getI());
            nodeSet.add(each.getJ());
        }
        for (Edge each : UBt) {
            if (nodeSet.contains(each.getI()) || nodeSet.contains(each.getJ())) {
                result.add(each);
            }
        }
        return new ArrayList<>(result);
    }

 /*   public double getW(List<Edge> edges int t){
        double tempsum = 0;
        for (Edge each : edges) {
            tempsum += getWsum(each, t);
        }
        return  tempsum;

    }
*/

    public List<Observation> getAllObservations(List<Edge> action, List<Edge> UB, List<Edge> U, int t) {
        List<Observation> result = new ArrayList<>();
        List<Integer> nodes = Utility.getNodeIndex(action);

        for (Integer each : nodes) {
            List<Edge> edges = Utility.getNeighbours(each, UB);
            double min = Integer.MAX_VALUE;
            double max = Integer.MIN_VALUE;
            Edge minEdge = null;
            Edge maxEdge = null;
            for (Edge eachEdge : edges) {
                if (eachEdge.getWmin() < min) {
                    minEdge = eachEdge;
                    min = eachEdge.getWmin();
                }
                if (eachEdge.getWmin() > max) {
                    maxEdge = eachEdge;
                    max = eachEdge.getWmin();
                }
            }
            if (min == Integer.MAX_VALUE) {
                break;
            }

            bState = new BState(t, B.get(t), RC, getRS(t), getRD(t), kt, N);
            List<Edge> obEdges = new ArrayList<>();
            obEdges.add(minEdge);
            Observation observationObj = new Observation(obEdges, action, bState, E, N, t);
            result.add(observationObj);
            if (minEdge != maxEdge) {
                bState = new BState(t, B.get(t), RC, getRS(t), getRD(t), kt, N);
                List<Edge> obEdges2 = new ArrayList<>();
                obEdges2.add(maxEdge);
                Observation observationObj2 = new Observation(obEdges2, action, bState, E, N, t);
                result.add(observationObj2);
            }
        }


        return result;
    }




/*



    public List<Observation> getAllObservations(List<Edge> action, int t) {
        // observation is supposed to contain only w_i_j, but here I use edges, it's easier to find w_ij and beta of the corresponding w_ij.
        List<Observation> result = new ArrayList<>();

        if (action.size() == 0) return result;
        List<List<Edge>> allCombinations = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();

        getAllSequences(edges, action, allCombinations, 0);

        double min = Double.MAX_VALUE;
        //   double min = Double.MAX_VALUE;
        List<Edge> eachObs = new ArrayList<>();
        for (int i = 0; i < allCombinations.size(); i++) {
            List<Edge> each = allCombinations.get(i);
            if (each.size() == 0) continue;
            double eachSum = Utility.getWSum( each);
            if (min < eachSum) {
                eachObs = each;
                min = eachSum;
            }
        }

        List<Edge> observation = getObservation(eachObs, t);

        bState = new BState(t, B.get(t), RC, getRS(t), getRD(t), kt, N);

        Observation observationObj = new Observation(observation, action, bState, E, N, t);
        result.add(observationObj);

        double max = Double.MIN_VALUE;
        //   double min = Double.MAX_VALUE;
      //  List<Edge> eachObs = new ArrayList<>();
        for (int i = 0; i < allCombinations.size(); i++) {
            List<Edge> each = allCombinations.get(i);
            if (each.size() == 0) continue;
            double eachSum = Utility.getWSum( each);
            if (max > eachSum) {
                eachObs = each;
                max = eachSum;
            }
        }

        if (eachObs.size() ==0) {
            System.out.println("observation is empty for action:  " + action );
        }
       observation = getObservation(eachObs, t);

        bState = new BState(t, B.get(t), RC, getRS(t), getRD(t), kt, N);

        observationObj = new Observation(observation, action, bState, E, N, t);

        result.add(observationObj);
        System.out.println("observation size: " + result.size());
        return result;
    }
*/


    /**
     * Equation#4, compute probability of an observation.
     *
     * @param observation
     * @return
     */
    public double getPofObservation(Observation observation) {
        double sum = 1;
        for (Edge edge : observation.observation) {
            for (int i = 0; i < levels; i++) {
                if (edge.beta[i] > 0) {
                    sum *= edge.beta[i];
                }
            }
        }
        return sum;
    }

    public double getReward(Observation observation, int t, List<double[]> bestXY) throws IloException {
        double bi[] = getBiArray();
        System.out.println("compute Z---action is: ");
        Utility.printArray(observation.action);
        System.out.println("---observation is: ");
        Utility.printArray(observation.observation);
        double z = 0;
        z = getZ(this, t, observation, bestXY);
        System.out.println("-------get Z ------:  " + z);

        double result = 0;
        for (Node d : getNd()) {
            int i = d.getId() - 1;
            result += bi[i] * (d.getRd(0) - d.getRd(t));
        }
        result += z;
        System.out.println("-------get v------:  " + Math.abs(result));


        return result;
    }



/*
    public List<Edge> getUprime(List<Edge> observation, int t) {
        return observation;
    }*/

    // above date : 2/19/2019




/*

    public void setUPrime(List<Edge> edges, int t) {
        UPrime.set(t, edges);
        List<Edge> neibours = findAllNeigbours(edges, getUB(t));
        UPrime.set(t + 1, Utility.exclude(neibours, edges));

    }

*/


   /* public List<Edge> getB(int t) {
        List<Edge> result = Utility.exclude(E, getU(t));
        return result;
    }
*/
   /* public List<Edge> getRB(int t) {
        if (t == 0) return getUPrime(null,0);
        List<Edge> result = Utility.exclude(getEPrimePrime(t), getEPrime(t));
        return result;
    }*/

/*    public double[] getRCt(int t, int level) {
        if (t == 0) {
            return getKt(0);
        }
        return Math.max(getKt(t) - getGarma(t - 1), 0);
    }*/

  /*  public double getRCt(int t) {
        if (t == 0) {
            return getKt(0);
        }
        return Math.max(getKt(t) - getGarma(t - 1), 0);
    }*/

  /*  public double getRCSum(int t) {
        double sum = 0;
        for (int i = 0; i < levels; i++) {
            sum += getRCt(t, i);
        }
        return sum;
    }*/

/*
    public double getGarma(int t, int level) {
        if (t == 0) return 0;
        double Wsum = getWSum(getEPrime(t), t, level);
        double Wsum2 = getWSum(getEPrimePrime(t), t, level);
        double RCt = getRCt(t, level);
        if (RCt > Wsum) return 0;
        if (RCt == 0) return getGarma(t - 1, level) - getKt(t, level);
        return Wsum2 - RCt;
    }

    public double getGarma(int t) {
        if (t == 0) return 0;
        return 1;
    }
*/

/*

    public List<Edge> getUB(int t) {
        return null;
        //   return Utility.exclude(getB(t), getRB(t));
    }
*/

/*    public List<Edge> getEPrime(int t) {
        return null;
        //  return getU(t);
    }*/

   /* public List<Edge> getEprimeOnA(List<Edge> actions) {
        return actions;
    }
*/

/*    public List<Edge> getEPrimePrime(int t) {
        List<Edge> ePrime = getEPrime(t);
        Set<Integer> set = new HashSet<>();
        for (Edge each : ePrime) {
            set.add(each.getI());
            set.add(each.getI());
        }
        List<Edge> result = new ArrayList<>();
        for (Edge each : E) {
            if (set.contains(each.getI()) || set.contains(each.getJ())) {
                result.add(each);
            }
        }
        return result;
    }*/

/*

    public List<Edge> getUPrime(List<Edge> observation, int t) {
        return UPrime.get(t);
    }
*/

    public double[] getRS(int t) {
        double[] result = new double[N.size()];
        for (Node each : N) {
            result[each.getId() - 1] = each.getRs(t);
        }
        return result;
    }


    public double[] getRD(int t) {

        double[] result = new double[N.size()];
        for (Node each : N) {
            result[each.getId() - 1] = each.getRd(t);
        }
        return result;
    }


    public int getNsize() {
        if (N == null) return 0;
        return N.size();
    }

    public int getNsizeSqaure() {
        return N.size() * N.size();
    }

    public double[] getCj() {
        double[] result = new double[getNsize()];
        int k = getR0()[0].length;
        for (int i = 0; i < result.length; i++) {
            result[i] = k;
        }
        return result;
    }


    public void initGraph() {
        N = new ArrayList<>();
        E = new ArrayList<>();
        List<List<String>> nodes = ExcelReader.read("./Doc/data.xlsx", 0);
        List<List<String>> edges = ExcelReader.read("./Doc/data.xlsx", 1);
        List<List<String>> globalData = ExcelReader.read("./Doc/data.xlsx", 2);

        nodes.remove(0);
        edges.remove(0);


        for (List<String> eachNode : nodes) {

            Node node = new Node(Integer.parseInt(eachNode.get(0)), eachNode.get(1), Double.parseDouble(eachNode.get(2)), Double.parseDouble(eachNode.get(3)), Double.parseDouble(eachNode.get(4)), Double.parseDouble(eachNode.get(5)), Double.parseDouble(eachNode.get(6)), Double.parseDouble(eachNode.get(7)));

            double[] R0 = new double[13];
            for (int i = 0; i < R0.length; i++) {
                R0[i] = Double.parseDouble(eachNode.get(i + 8));
            }
            node.setR0(R0);
            //    N.add(d1);
            N.add(node);
        }

        for (List<String> eachEdge : edges) {
            E.add(new Edge(Integer.parseInt(eachEdge.get(0)), Integer.parseInt(eachEdge.get(1)), Double.parseDouble(eachEdge.get(2)), Double.parseDouble(eachEdge.get(3)), Double.parseDouble(eachEdge.get(4)), new double[]{Double.parseDouble(eachEdge.get(5)), Double.parseDouble(eachEdge.get(6))}, new double[]{Double.parseDouble(eachEdge.get(7)), Double.parseDouble(eachEdge.get(8))}));

        }

        GlobalData.B = Double.parseDouble(globalData.get(0).get(1));

        GlobalData.RC = Integer.parseInt(globalData.get(1).get(1));

        GlobalData.SIGMA = Double.parseDouble(globalData.get(2).get(1));
        kt = RC;

    }




    /*public void initNodes() {
        N = new ArrayList<>();

        Node d1 = new Node(1, "d", 0, 200, 23.99, 4.1, 0, 56);
        String r0N1 = "0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52";
        d1.setR0(converStringToArray(r0N1));
        N.add(d1);

        Node t2 = new Node(2, "t", 0, 0, 23.99, 4.2, 0, 0);
        String r0N2 = "0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52";
        t2.setR0(converStringToArray(r0N2));
        N.add(t2);

        Node t3 = new Node(3, "t", 0, 0, 23.99, 4.14, 0, 0);
        String r0N3 = "0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52";
        t3.setR0(converStringToArray(r0N3));
        t3.setR0(converStringToArray(r0N3));
        N.add(t3);


        Node s4 = new Node(4, "s", 300, 0, 20.64, 3.30, 5, 0);
        String r0N4 = "0.64\t3.68\t7.31\t11.19\t15.22\t19.36\t0.64\t3.68\t7.31\t11.19\t15.22\t19.36\t0.64\t3.68\t7.31\t11.19\t15.22\t19.36\t0.64\t3.68\t7.31\t11.19\t15.22\t19.36\t0.64\t3.68\t7.31\t11.19\t15.22\t19.36";
        s4.setR0(converStringToArray(r0N4));
        N.add(s4);


        Resource resource5 = new Resource();
        resource5.put(1, 50);
        resource5.put(2, 100);
        Node s5 = new Node(5, "s", 200, 0, 21.75, 4.78, 2, 0);
        String r0N5 = "1.16\t4.38\t8.07\t11.99\t16.04\t20.19\t1.16\t4.38\t8.07\t11.99\t16.04\t20.19\t1.16\t4.38\t8.07\t11.99\t16.04\t20.19\t1.16\t4.38\t8.07\t11.99\t16.04\t20.19\t1.16\t4.38\t8.07\t11.99\t16.04\t20.19";
        s5.setR0(converStringToArray(r0N5));
        N.add(s5);


        Node t6 = new Node(6, "t", 0, 0, 23, 6.69, 0, 0);
        String r0N6 = "1.73\t5.09\t8.84\t12.79\t16.87\t21.03\t1.73\t5.09\t8.84\t12.79\t16.87\t21.03\t1.73\t5.09\t8.84\t12.79\t16.87\t21.03\t1.73\t5.09\t8.84\t12.79\t16.87\t21.03\t1.73\t5.09\t8.84\t12.79\t16.87\t21.03";
        t6.setR0(converStringToArray(r0N6));
        N.add(t6);


        Node d7 = new Node(7, "d", 0, 300, 24.71, 3.30, 0, 100);
        String r0N7 = "2.35\t5.82\t9.62\t13.6\t17.69\t21.87\t2.35\t5.82\t9.62\t13.6\t17.69\t21.87\t2.35\t5.82\t9.62\t13.6\t17.69\t21.87\t2.35\t5.82\t9.62\t13.6\t17.69\t21.87\t2.35\t5.82\t9.62\t13.6\t17.69\t21.87";
        d7.setR0(converStringToArray(r0N7));
        N.add(d7);

        Node t8 = new Node(8, "t", 0, 0, 24.71, 3.30, 0, 0);
        String r0N8 = "0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52";
        t8.setR0(converStringToArray(r0N8));

        N.add(t8);
        Node t9 = new Node(9, "t", 0, 0, 24.71, 3.30, 0, 0);
        String r0N9 = "0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52";

        t9.setR0(converStringToArray(r0N9));
        N.add(t9);
        Node t10 = new Node(10, "t", 0, 0, 24.71, 3.30, 0, 0);
        String r0N10 = "0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52";
        t10.setR0(converStringToArray(r0N10));

        N.add(t10);

        Node t11 = new Node(11, "t", 0, 0, 24.71, 3.30, 0, 56);
        String r0N11 = "0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52";
        t2.setR0(converStringToArray(r0N2));

        N.add(t11);

        Node t12 = new Node(12, "t", 0, 0, 24.71, 3.300, 0, 0);

        String r0N12 = "0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52";
        t12.setR0(converStringToArray(r0N12));

        N.add(t12);

        Node t13 = new Node(13, "t", 0, 0, 23.99, 4.14, 0, 0);
        String r0N13 = "0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52";
        t13.setR0(converStringToArray(r0N13));
        N.add(t13);


        Node s14 = new Node(14, "t", 0, 0, 20.64, 3.30, 0, 0);
        N.add(s14);
        String r0N14 = "0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52";
        s14.setR0(converStringToArray(r0N14));


        Node s15 = new Node(15, "t", 0, 0, 21.75, 4.78, 0, 0);
        String r0N15 = "0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52";
        s15.setR0(converStringToArray(r0N15));
        N.add(s15);


        Node t16 = new Node(16, "s", 200, 0, 23, 6.69, 0, 0);
        String r0N16 = "0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52";
        t16.setR0(converStringToArray(r0N16));
        N.add(t16);


        Node t17 = new Node(17, "t", 0, 0, 24.71, 3.30, 0, 0);
        String r0N17 = "0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52";
        t17.setR0(converStringToArray(r0N17));
        N.add(t17);

        Node t18 = new Node(18, "t", 0, 0, 24.71, 3.30, 0, 0);
        String r0N18 = "0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52";
        t18.setR0(converStringToArray(r0N18));
        N.add(t18);
        Node t19 = new Node(19, "t", 0, 0, 24.71, 3.30, 0, 0);
        String r0N19 = "0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52";
        t19.setR0(converStringToArray(r0N19));
        N.add(t19);
        Node t20 = new Node(20, "t", 0, 0, 24.71, 3.30, 0, 0);
        String r0N20 = "0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52";

        t20.setR0(converStringToArray(r0N20));
        N.add(t20);
        Node t21 = new Node(21, "t", 0, 0, 24.71, 3.30, 0, 0);
        String r0N21 = "0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52";

        t21.setR0(converStringToArray(r0N21));
        N.add(t21);

        Node t22 = new Node(22, "t", 0, 0, 24.71, 3.30, 0, 0);
        String r0N22 = "0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52";

        t22.setR0(converStringToArray(r0N22));
        N.add(t22);

        Node t23 = new Node(23, "t", 0, 0, 24.71, 3.30, 0, 0);
        String r0N23 = "0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52";

        t23.setR0(converStringToArray(r0N23));
        N.add(t23);
        Node t24 = new Node(24, "t", 0, 0, 24.71, 3.30, 0, 0);
        String r0N24 = "0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52";

        t24.setR0(converStringToArray(r0N24));
        N.add(t24);
        Node d25 = new Node(25, "d", 0, 300, 24.71, 3.30, 0, 24);
        String r0N25 = "0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52";

        d25.setR0(converStringToArray(r0N25));
        N.add(d25);


     *//*   N = new ArrayList<>();
        Resource resource1 = new Resource();
        resource1.put(1, 10);
        resource1.put(2, 100);

        Node d1 = new Node(1, "d", 0, 200, 0, 0, 0, 56);
        N.add(d1);

        Node t2 = new Node(2, "t", 0, 0, 0, 0, 0, 0);


        Node t3 = new Node(3, "t", 0, 0, 23.99, 4.14, 1, 0);
        String r0N3 = "0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52\t0.22\t3.01\t6.56\t10.4\t14.41\t18.52";
        t3.setR0(converStringToArray(r0N3));
        N.add(t2);
        N.add(t3);


        Resource resource4 = new Resource();
        resource4.put(1, 10);
        resource4.put(2, 100);
        Node s4 = new Node(4, "s", 300, 0, 20.64, 3.30, 1, 0);
        String r0N4 = "0.64\t3.68\t7.31\t11.19\t15.22\t19.36\t0.64\t3.68\t7.31\t11.19\t15.22\t19.36\t0.64\t3.68\t7.31\t11.19\t15.22\t19.36\t0.64\t3.68\t7.31\t11.19\t15.22\t19.36\t0.64\t3.68\t7.31\t11.19\t15.22\t19.36";
        s4.setR0(converStringToArray(r0N4));
        N.add(s4);


        Resource resource5 = new Resource();
        resource5.put(1, 50);
        resource5.put(2, 100);
        Node s5 = new Node(5, "s", 200, 0, 21.75, 4.78, 1, 0);
        String r0N5 = "1.16\t4.38\t8.07\t11.99\t16.04\t20.19\t1.16\t4.38\t8.07\t11.99\t16.04\t20.19\t1.16\t4.38\t8.07\t11.99\t16.04\t20.19\t1.16\t4.38\t8.07\t11.99\t16.04\t20.19\t1.16\t4.38\t8.07\t11.99\t16.04\t20.19";
        s5.setR0(converStringToArray(r0N5));
        N.add(s5);


        Node t6 = new Node(6, "t", 0, 0, 23, 6.69, 0, 0);
        String r0N6 = "1.73\t5.09\t8.84\t12.79\t16.87\t21.03\t1.73\t5.09\t8.84\t12.79\t16.87\t21.03\t1.73\t5.09\t8.84\t12.79\t16.87\t21.03\t1.73\t5.09\t8.84\t12.79\t16.87\t21.03\t1.73\t5.09\t8.84\t12.79\t16.87\t21.03";
        t6.setR0(converStringToArray(r0N6));
        N.add(t6);


        Node d7 = new Node(7, "d", 0, 300, 24.71, 3.30, 0, 100);
        String r0N7 = "2.35\t5.82\t9.62\t13.6\t17.69\t21.87\t2.35\t5.82\t9.62\t13.6\t17.69\t21.87\t2.35\t5.82\t9.62\t13.6\t17.69\t21.87\t2.35\t5.82\t9.62\t13.6\t17.69\t21.87\t2.35\t5.82\t9.62\t13.6\t17.69\t21.87";
        d7.setR0(converStringToArray(r0N7));
        N.add(d7);
*//*


        //
        //  System.out.println(N);

    }*/
/*
0	0	0	0	0	0	0
0	0	0	0	0	0	0
0	0	0	0.94	1.26	0.63	0.94
0	0	0.94	0	0.77	1.47	1.26
0	0	1.26	0.77	0	0.95	1.47
0	0	0.63	1.47	0.95	0	0.63
0	0	0.94	1.26	1.47	0.63	0
Tij

* */


  /*  public void initEdges() {
        E = new ArrayList<>();
        Edge edge12 = new Edge(1, 2, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
        E.add(edge12);
        Edge edge15 = new Edge(1, 5, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
        E.add(edge15);

        Edge edge23 = new Edge(2, 3, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
        E.add(edge23);
        Edge edge24 = new Edge(2, 4, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
        E.add(edge24);


        Edge edge34 = new Edge(3, 4, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
        E.add(edge34);

        Edge edge39 = new Edge(3, 9, 100, 1.26, 0.03, new double[]{0.5, 0.3}, new double[]{0.5, 0.5});
        E.add(edge39);

        Edge edge45 = new Edge(4, 5, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
        E.add(edge45);
        Edge edge47 = new Edge(4, 7, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
        E.add(edge47);
        Edge edge48 = new Edge(4, 8, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
        E.add(edge48);
        Edge edge49 = new Edge(4, 9, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
        E.add(edge49);
        Edge edge56 = new Edge(5, 6, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
        E.add(edge56);
        Edge edge57 = new Edge(5, 7, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
        E.add(edge57);
        Edge edge67 = new Edge(6, 7, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
        E.add(edge67);
        Edge edge78 = new Edge(7, 8, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
        E.add(edge78);
        Edge edge711 = new Edge(7, 11, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
        E.add(edge711);
        Edge edge712 = new Edge(7, 12, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
        E.add(edge712);
        Edge edge89 = new Edge(8, 9, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
        E.add(edge89);
        Edge edge810 = new Edge(8, 10, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
        E.add(edge810);
        Edge edge811 = new Edge(8, 11, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
        E.add(edge811);
        Edge edge813 = new Edge(8, 13, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
        E.add(edge813);

        Edge edge910 = new Edge(9, 10, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
        E.add(edge910);
        Edge edge1013 = new Edge(10, 13, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
        E.add(edge1013);
        Edge edge1014 = new Edge(10, 14, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
        E.add(edge1014);
        Edge edge1112 = new Edge(11, 12, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
        E.add(edge1112);
        Edge edge1113 = new Edge(11, 13, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
        E.add(edge1113);
        Edge edge1116 = new Edge(11, 16, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
        E.add(edge1116);

        Edge edge1215 = new Edge(12, 15, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
        E.add(edge1215);
        Edge edge1216 = new Edge(12, 16, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
        E.add(edge1216);
        Edge edge1314 = new Edge(13, 14, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
        E.add(edge1314);
        Edge edge1319 = new Edge(13, 19, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
        E.add(edge1319);
        Edge edge1419 = new Edge(14, 19, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
        E.add(edge1419);
        Edge edge1421 = new Edge(14, 21, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
        E.add(edge1421);
        Edge edge1422 = new Edge(14, 22, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
        E.add(edge1422);

        Edge edge1516 = new Edge(15, 16, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
        E.add(edge1516);

        Edge edge1617 = new Edge(16, 17, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
        E.add(edge1617);

        Edge edge1718 = new Edge(17, 18, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
        E.add(edge1718);
        Edge edge1719 = new Edge(17, 19, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
        E.add(edge1719);

        Edge edge1820 = new Edge(18, 20, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
        E.add(edge1820);

        Edge edge1920 = new Edge(19, 20, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
        E.add(edge1920);

        Edge edge2021 = new Edge(20, 21, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
        E.add(edge2021);
        Edge edge2223 = new Edge(22, 23, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
        E.add(edge2223);
        Edge edge2324 = new Edge(23, 24, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
        E.add(edge2324);
        Edge edge2425 = new Edge(24, 25, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
        E.add(edge2425);

     *//*   Edge edge34 = new Edge(3, 4,  0, 0.94, 0.04, new double[]{4, 5},new double[]{0.5, 0.5} );
        E.add(edge34);

        Edge edge35 = new Edge(3, 5, 100, 1.26, 0.03, new double[]{5, 7}, new double[]{0.5, 0.5});

        E.add(edge35);

        Edge edge36 = new Edge(3, 6, 100, 0.63, 0.04, new double[]{4, 7}, new double[]{0.5, 0.5});

        E.add(edge36);

        Edge edge45 = new Edge(4, 5, 100, 0.77, 0.02, new double[]{3, 5},new double[]{0.5, 0.5});
        E.add(edge45);



        Edge edge56 = new Edge(5, 6, 100, 0.95, 0.07, new double[]{3, 5},new double[]{0.5, 0.5});
        E.add(edge56);


        Edge edge67 = new Edge(6, 7, 100, 0.63, 0.03, new double[]{1, 5},new double[]{0.5, 0.5});
        E.add(edge67);


        Edge edge17 = new Edge(1, 7, 100, 0.63, 0.03, new double[]{6, 8},new double[]{0.5, 0.5});
        E.add(edge17);

        Edge edge12 = new Edge(1, 2, 100, 0.63, 0.03, new double[]{4, 7},new double[]{0.5, 0.5});
        E.add(edge12);

        Edge edge13= new Edge(1, 3, 100, 0.63, 0.03, new double[]{2, 6},new double[]{0.5, 0.5});
        E.add(edge13);
*//*
     *//*


        Edge edge45 = new Edge(4, 5, 100, 0.77, 0.02, new double[]{0.6, 0.3}, new double[]{0.5, 0.5});
        E.add(edge45);

        Edge edge46 = new Edge(4, 6, 100, 1.47, 0.07, new double[]{0.2, 0.3}, new double[]{0.5, 0.5});
        E.add(edge46);

        //Edge edge47 = new Edge(4, 7, 100, 1.26, 0.04, new double[]{0.2, 0.3},new double[]{0.5, 0.5});
        //E.add(edge47);

        Edge edge56 = new Edge(5, 6, 100, 0.95, 0.07, new double[]{1.2, 0.3}, new double[]{0.5, 0.5});
        E.add(edge56);

    *//*
     *//*    Edge edge57 = new Edge(5, 7, 100, 1.47, 0.07, new double[]{0.2, 0.9}, new double[]{0.5, 0.5});
        E.add(edge57);*//**//*


        // Edge edge67 = new Edge(6, 7, 100, 0.63, 0.03, new double[]{0.2, 0.8},new double[]{0.5, 0.5});
        // E.add(edge67);// todo: 7 is the one of two demand nodes.
        Edge edge68 = new Edge(6, 8, 100, 0.63, 0.03, new double[]{0.2, 0.8}, new double[]{0.5, 0.5});
        E.add(edge68);
        Edge edge89 = new Edge(8, 9, 100, 0.63, 0.03, new double[]{0.2, 0.8}, new double[]{0.5, 0.5});
        E.add(edge89);
        Edge edge9t10 = new Edge(9, 10, 100, 0.63, 0.03, new double[]{0.2, 0.8}, new double[]{0.5, 0.5});
        E.add(edge9t10);

        Edge edge10t11 = new Edge(10, 11, 100, 0.63, 0.03, new double[]{0.2, 0.8}, new double[]{0.5, 0.5});
        E.add(edge10t11);
        Edge edge11t12 = new Edge(11, 12, 100, 0.63, 0.03, new double[]{0.2, 0.8}, new double[]{0.5, 0.5});
        E.add(edge11t12);
        Edge edge12t13 = new Edge(12, 13, 100, 0.63, 0.03, new double[]{0.2, 0.8}, new double[]{0.5, 0.5});
        E.add(edge12t13);
        Edge edge13t14 = new Edge(13, 14, 100, 0.63, 0.03, new double[]{0.2, 0.8}, new double[]{0.5, 0.5});
        E.add(edge13t14);
        Edge edge14t7 = new Edge(14, 7, 100, 0.63, 0.03, new double[]{0.2, 0.8}, new double[]{0.5, 0.5});
        E.add(edge14t7);
*//*

    }
*/
/*

    public double[][] getbeta(int t, int level) {
        double[][] result = new double[E.size()][E.size()];
        for (Edge each : E) {
            result[each.getI()][each.getJ()] = each.beta.get(t)[level];
        }
        return result;
    }
*/

  /*  public double getsumGarmma(int t) {
        double sum = 0;
        for (int i = 0; i < levels; i++) {
            sum += getGarma(t, i);
        }
        return sum;
    }

    public void updateBeta(int t) {
        if (t == 0) {
            return;
        }
        // todo: xdu: . 1. gamma process is not gamma, we need to find gamma(lammda) method for java. update beta.
        // gamma(lamda)
        double garmaSum = getsumGarmma(t);
        for (Edge edge : E) {
          *//*  double[] previousBeta = edge.beta.get(t-1);
            double[] newBeta = new double[2];
            if (Utility.contains(getUB(t), edge)){
                newBeta[0] = (getGarma(t, 0)/garmaSum) * previousBeta[0];
                newBeta[1] = (getGarma(t, 1)/garmaSum) * previousBeta[1];
            } else if (Utility.contains(getRB(t), edge)){
                newBeta[0] =1;
                newBeta[1] =1;
            }*//*
            //  edge.beta.add(newBeta);
        }
    }
*/

/*
    public double getKt(int t, int level) {
        if (t == 0) {
            return 5;// TODO: xdu. kt should have multiple.
        }
        return getKt(t - 1, level) - getWSum(getEPrime(t), t, level);
    }
*/


    public double getKt(int t) {
        if (t == 0) {
            return 5;// TODO: xdu. kt should have multiple.
        }
        return getKt(t - 1);
    }

    public double[][] getFC() {
        double[][] result = new double[N.size()][N.size()];
        for (Edge edge : E) {
            result[edge.getI() - 1][edge.getJ() - 1] = edge.getFC_ij();
            result[edge.getJ() - 1][edge.getI() - 1] = edge.getFC_ij();
        }
        //   result[3][2] = 100;
        //   result[4][2] = 200;
        //   result[4][5] = 20;
        //   result[5][6] = 200;

        return result;
    }


    public double[][] getPeriodT() {
        int n = N.size();
        double[][] result = new double[n][n];
       /* for (Edge edge : E) {
            result[edge.getI() - 1][edge.getJ() - 1] = edge.getT();
            result[edge.getJ() - 1][edge.getI() - 1] = edge.getT();
        }*/
        double[][] shortestPathCostMatrix = new double[n][n];
        List<List<Integer>> paths = new ArrayList<>();
        dijsktra.fillShortestPath(getW(), shortestPathCostMatrix, paths);
        List<int[]>[][] pathsMatrix = dijsktra.getPath(paths, getW().length);

        dijsktra.getPath(paths, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = getT(pathsMatrix, i, j);

            }
        }


        return result;
    }


    public double getT(List<int[]>[][] pathsMatrix, int i, int j) {
        double res = 0;

        List<int[]> path = pathsMatrix[i][j];
        if (path == null) return 0;
        for (int[] each : path) {
            res += Utility.getEdge(E, each[0] + 1, each[1] + 1).getT();
        }
        return res;
    }


    public double[][] getR() {
        int n = N.size();
        double[][] result = new double[n][n];
        for (Edge edge : E) {
            result[edge.getI() - 1][edge.getJ() - 1] = edge.getR();
            result[edge.getJ() - 1][edge.getI() - 1] = edge.getR();
            result[edge.getI() - 1][edge.getI() - 1] = 999; // why?
            result[edge.getJ() - 1][edge.getJ() - 1] = 999; // why?
        }
        return result;
    }
/*
    public double[] getRS(int t) {
        double[] result = new double[N.size()];
        for (Node each : N) {
            result[each.getId() - 1] = each.getRs(t);
        }
        return result;
    }

    public void updateRS(List<Edge> observation, int t, double[][] fij) {
        if (t == 0) return;

        for (Node each : N) {

            double sum1 = 0;
            for (int j = 1; j <= N.size(); j++) {
                if (Utility.Match(getUPrime(observation, t), each.getId(), j)) {
                    sum1 += fij[each.getId() - 1][j - 1];
                }
            }

            double sum2 = 0;
            for (int j = 1; j <= N.size(); j++) {
                if (Utility.Match(getUPrime(observation, t - 1), j, each.getId())) {
                    sum2 += fij[j - 1][each.getId() - 1];
                }
            }

            each.setRS(t, each.getRs(t - 1) - (sum1 - sum2));

        }
    }

    public void updateRD(int t, double[] xi) {
        if (t == 0) return;
        for (Node each : N) {
            each.setRD(t, each.getRd(t - 1) - xi[each.getId() - 1]);
        }
    }

    public double[] getRD(int t) {

        double[] result = new double[N.size()];
        for (Node each : N) {
            result[each.getId() - 1] = each.getRd(t);
        }
        return result;
    }
*/


    public double[][] getR0() {
        int n = N.size();
        Node node = N.get(0);
        double[][] result = new double[n][node.getR0().length];
        for (Node i : N) {
            result[i.getId() - 1] = i.getR0();
        }


        return result;
    }

    public double[] getLamda(int t) {
        double[] result = new double[N.size()];
        for (Node each : N) {
            result[each.getId() - 1] = each.getLamda(t);
        }
        return result;
    }


    public Node[] getNd() {
        return getNodes("d");
    }

    public Node[] getNs() {
        return getNodes("s");
    }

    public Node[] getNT() {
        return getNodes("t");
    }

    public Node[] getNodes(String s) {
        List<Node> nd = new ArrayList<>();
        for (Node each : N) {
            if (each.getType().equals(s)) {
                nd.add(each);
            }
        }
        return nd.toArray(new Node[nd.size()]);
    }


    public double[] converStringToArray(String s) {
        double[] result;
        String temp[] = s.split("\\s+");
        result = new double[temp.length];
        for (int i = 0; i < temp.length; i++) {
            result[i] = Double.valueOf(temp[i]);
        }
        return result;
    }


    public double[] getBiArray() {
        double[] result = new double[N.size()];
        for (int i = 0; i < N.size(); i++) {
            result[i] = N.get(i).getB();
        }
        return result;
    }


    public List<Node> getN() {
        return N;
    }


    public void converToActions(List<Integer> path, List<List<Edge>> actions, List<Edge> action, List<Edge> remaining, int k) {
        if (k == path.size() - 1) {
            return;
        }

        for (Edge each : remaining) {
            if (each.getI() == path.get(k) && each.getJ() == path.get(k + 1)) {
                action.add(each);
                List<Edge> newEach = new ArrayList<>(action);
                actions.add(newEach);
                break;
            }
        }
        converToActions(path, actions, action, remaining, k + 1);

    }

   /* public List<List<Edge>> getAllActions(int t) {
        List<Edge> eprime = new ArrayList<>(getEPrime(t));
        List<List<Edge>> allSequences = getAllSequences(t);
        for (List<Edge> each : allSequences) {
            for (Edge each2 : eprime) {
                each.remove(each2);
            }
        }
        return allSequences;
    }
*/

    //feasible actions (2)
   /* public List<List<Edge>> getAllFeasibleActions(int t) {
        List<List<Edge>> actions = getAllActions(t);
        List<List<Edge>> result = new ArrayList<>();
        for (List<Edge> eachActions : actions) {
            List<Edge> e = new ArrayList<>(eachActions);
            List<Edge> temp = exclude(e, getEPrime(t));
            if (isWminSumFeasible(temp, t)) {
                result.add(eachActions);
            }
        }
        return result;
    }*/

/*    public boolean isWminSumFeasible(List<Edge> edges, int t) {
        double rct0 = getRCt(t, 0);
        double rct1 = getRCt(t, 1);
        for (Edge e : edges) {
            if (e.W_ij.get(t)[0] > e.W_ij.get(t)[1]) {
                rct1 -= e.W_ij.get(t)[1];
            } else {
                rct0 -= e.W_ij.get(t)[0];
            }
            if (rct0 <= 0 || rct1 <= 0) return false;
        }
        return true;

    }*/

    /*    public double getWMinSum(List<Edge> edges, int t) {
            double sum = 0;
            for (Edge e : edges) {
                double min = Math.min(e.W_ij.get(t)[0], e.W_ij.get(t)[1]);
                for (int i = 2; i < levels; i++) {
                    min = Math.min(min, e.W_ij.get(t)[i]);
                }
                sum += min;
            }
            return sum;
        }*/
    public void getAllSequences(List<Edge> edges, List<Edge> current, List<List<Edge>> result, int k) {
        if (k == current.size()) {
            return;
        }

        Edge e = current.get(k);
        edges.add(e);
        result.add(new ArrayList<>(edges));
        getAllSequences(edges, current, result, k + 1);
        edges.remove(e);

    }

    public void findSequences(List<Edge> current, List<Edge> remain, List<List<Edge>> result) {
        if (remain.size() == 0) {
            return;
        }

        Edge edge = findAdjcentEdge(current, remain);
        if (edge == null) return;
        List<Edge> eachResult = new ArrayList<>(current);
        eachResult.add(edge);
        result.add(eachResult);
        current.add(edge);
        remain.remove(edge);
        findSequences(current, remain, result);
    }


    public Edge findAdjcentEdge(List<Edge> current, List<Edge> remaining) {
        for (Edge e : remaining) {
            if (isAdjcent(current, e) || Utility.isSupply(e.getJ(), N) || Utility.isSupply(e.getI(), N)) {
                return e;
            }
        }
        return null;
    }


    public boolean isAdjcent(Edge a, Edge b) {
        return a.getI() == b.getI() || a.getJ() == b.getI() || a.getJ() == b.getJ() || a.getI() == b.getJ();
    }

    public boolean isAdjcent(List<Edge> edges, Edge b) {

        for (Edge a : edges) {
            if (isAdjcent(a, b)) return true;
        }
        return false;
    }

    public List<Edge> getSupplies() {
        Set<Edge> set = new HashSet<>();
        for (Edge each : E) {
            if (Utility.isSupply(each.getJ(), N) || Utility.isSupply(each.getI(), N)) {
                set.add(each);
            }
        }
        return new ArrayList<>(set);
    }

    public Set<Integer> getNodes(List<Edge> edges) {
        Set<Integer> set = new HashSet<>();
        for (Edge e : edges) {
            set.add(e.getI());
            set.add(e.getJ());
        }

        return set;
    }

    //(4)
    /*public double getPofObservation(List<Edge> observation, int t, int level) {
        double sum = 1;
        for (Edge edge : observation) {
            sum *= edge.beta.get(t)[level];
        }
        return sum;
    }*/

   /* public List<List<Edge>> getObservation(List<Edge> action, int t) {
        List<List<Edge>> result = new ArrayList<>();

        List<Edge> current = new ArrayList<>();
        findAllCombination(action, result, 0, current);

        for (List<Edge> eachSet : new ArrayList<>(result)) {
            if (!eachSet.isEmpty()) {
                result.add(findAllNeigbours(eachSet, getUB(t)));
            }

        }
        return result;
    }*/

    public List<Edge> findAllNeigbours(List<Edge> edges, List<Edge> edgesSet) {
        Set<Edge> result = new HashSet<>();
        for (Edge edge : edgesSet) {
            if (isAdjcent(edges, edge)) {
                result.add(edge);
            }
        }
        result.addAll(edges);

        return new ArrayList<>(result);
    }


    public void findAllCombination(List<Edge> action, List<List<Edge>> result, int n, List<Edge> current) {
        if (n == action.size()) {
            List<Edge> newEdgesSet = new ArrayList<>(current);
            result.add(newEdgesSet);
            return;
        }
        current.add(action.get(n));
        findAllCombination(action, result, n + 1, current);
        current.remove(action.get(n));
        findAllCombination(action, result, n + 1, current);
    }

    /**
     * public double getReward(List<Edge> observation, int t, int level) throws IloException {
     * double bi[] = getBiArray();
     * <p>
     * double z = getZ(this, t, observation, level);
     * double[] rd = StateManager.getInstance().get(t).RD_t;
     * double result = 0;
     * for (Node d : getNd()) {
     * int i = d.getId() - 1;
     * result += bi[i] * (d.getRd(0) - rd[i]);
     * }
     * result += z;
     * return result;
     * }
     **/

 /*   public double getER(int t, List<Edge> action, int level) throws IloException {

        double result = 0;
        for (List<Edge> eachObservation : getObservation(action, t)) {
            result += getPofObservation(eachObservation, t, level) * getReward(eachObservation, t, level);

        }

        return result;
    }*/


   /* public double getBenefit(int t) throws IloException {
        List<List<Edge>> allActions = getAllFeasibleActions(t);

        double max = -1;
        if (t == 2) {
            return 0;
        }
        for (List<Edge> action : allActions) {
            initUPrime(action, t);
            double er = getER(t, action, 0);
            updateLamda(t + 1);
            updateBeta(t + 1);

            updateYt(t + 1);
            max = Math.max(max, er) + getBenefit(t + 1);
            //  System.out.println(action);
            // System.out.println(max);
        }
        return max;
    }
*/
 /*   public void updateLamda(int t) {
        for (Node each : N) {
            each.lamda.put(t, each.getLamda(0));
        }
    }

    public void updateYt(int t) {
        for (Node each : N) {
            each.Yt.put(t, each.getYt(0));
        }
    }*/
    public double getV(int curr, Graph graph, int steps) {
        if (curr == steps) {
            return 0;
        }
        List<List<Edge>> actions = graph.getAllFeasibleActions(t);
        int a = 0;
        double maxV1 = 0;
        double maxV2 = 0;
        double v = 0;
        double[][] X = null;
        double[][] Y = null;
// TODO 1: 1. store X, Y, W, Done.
// TODO 2: read csv Done.
// TODO 3: 2nd equations.

        for (List<Edge> eachAction : actions) {
            System.out.println("action " + a++);

            if (actions == null) continue;
            v = 0;
            try {

                int c = 0;
                int temp = 0;
                List<Observation> observatons = graph.getAllObservations(eachAction, graph.UB.get(t), graph.U.get(t), t);
                if (observatons.size() == 0) continue;
                System.out.println("observation size:  " + observatons.size());
                for (Observation eachObservation : observatons) {
                    // System.out.println("observation " + c++);
                    if (visitedAction.contains(Utility.getString(eachAction) + "~" + eachObservation.toString()))
                        continue;
                    visitedAction.add(Utility.getString(eachAction) + "~" + eachObservation.toString());
                    v = graph.getPofObservation(eachObservation) * graph.getReward(eachObservation, t, bestXY);
                    temp += v;
                    bState = new BState(eachAction, eachObservation);
                    eachObservation.bState = bState;
                    eachObservation.update();

                    graph.B.set(t + 1, eachObservation.B);
                    graph.UB.set(t + 1, eachObservation.UB);
                    graph.RB.set(t + 1, eachObservation.RB);
                    graph.U.set(t + 1, eachObservation.U);
                    BState tempState = graph.bState;
                    graph.bState = eachObservation.bState;
                    t = t + 1;
                    v += getV(curr + 1, graph, steps);
                    t = t - 1;
                    graph.B.set(t + 1, new ArrayList<>());
                    graph.UB.set(t + 1, new ArrayList<>());
                    graph.RB.set(t + 1, new ArrayList<>());
                    graph.U.set(t + 1, new ArrayList<>());
                    graph.bState = tempState;

                }
                if (Math.abs(v) > max && curr == 0) {
                    this.bestAction = eachAction;
                    System.out.println("~~found best action ~");
                    Utility.printArray(bestAction);
                    bestX = bestXY.get(0);
                    max = Math.abs(v);

                }

            } catch (IloException e) {
                e.printStackTrace();
            }
        }

        System.out.println("x-----v1 value: " + maxV1);
        System.out.println("x-----v2 value: " + maxV2);


        return max;
    }


    public static void main(String args[]) {
        System.out.print("Run program ...");
        Graph graph = new Graph();
        //graph.initNodes();
        // graph.initEdges();
        graph.initGraph();
        graph.initBlocked();
        graph.initUnblocked();
        graph.initRB();
        graph.initUB();
        long t_start = System.currentTimeMillis();
        System.out.println(System.currentTimeMillis());
        System.out.print("test .........................................................");

        //  System.out.print("test ...:"+ graph.getBenefit(0));


        double v = graph.getV(0, graph, 2);
        List<Edge> action = graph.bestAction;
        System.out.print("BEST ACTION: ");
        for (Edge edge : action) {
            System.out.println(edge.getI() + "----" + edge.getJ());
        }

        System.out.println("BEST ACTION value: " + graph.max);
        int N = graph.N.size();
        int nX = 0;
        int nY = N * N;
        int nW = nY + (int) Utility.getSum(graph.getCj()); // what it is.
        int nS = nW + nY;
        int nD = nS + N;
        int nF = nD + N;
        int nK = nF + nY;
        int nKn = nK + N;

        //   System.out.print("Best X, Y .........................................................");
        //   Utility.printArray(graph.bestX);
        double X[][] = Matrix.transpose(Matrix.reshape(graph.bestX, 0, nY, N));
        double Y[][] = null;
        double[] Cj = graph.getCj();
        if (unique(Cj).length == 1) {
            //      System.out.println(nW);
            //        System.out.println(nY +1 -1);

            Y = Matrix.transpose(Matrix.reshape(graph.bestX, nY + 1 - 1, nW + 1, (int) Utility.max(Cj)));
        } else {
            //Y = subarray(xDouble, nY +1 -1, nW+1);
        }

        System.out.print("Best X, Y .........................................................");
        System.out.print("Best X.........................................................");
        Utility.printDoubleArray(X);

        System.out.print("Best Y .........................................................");
        Utility.printDoubleArray(Y);
        System.out.print("Best W .........................................................");
        double W[] = subarray(graph.bestX, nW + 1 - 1, nS);
        Utility.printArray(W);

        //    System.out.println(" ---  all actions: " + graph.getAllSequences(graph.E));
        //  System.out.println(graph.getAllFeasibleActions(0).size());
        /*  try {
          //  System.out.print("test ...:"+ graph.getBenefit(0));
            graph.getBenefit(0);
        } catch (IloException e) {
            e.printStackTrace();
        }*/
        long t_end = System.currentTimeMillis();
        System.out.println("xdu: cost: " + (t_end - t_start));

    }


}
