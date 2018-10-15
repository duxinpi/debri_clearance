package SDCP;

public class Utility {

    public static double getSum(double[] array) {
        double sum = 0;
        for(int i =0; i < array.length; i ++) {
            sum+= array[i];
        }
        return sum;
    }

    public static double getSum(double[] array, int s, int e) {
        double sum = 0;
        for(int i =s; i < e; i ++) {
            sum+= array[i];
        }
        return sum;
    }

    public static void fill(double[][] array, double v){
        for (int i =0; i < array.length; i++) {
            for (int j =0; j< array[0].length; j++) {
                array[i][j] = v;
            }
        }
    }


    public static void printArray(Object[] array ) {
        for (Object each : array) {
            System.out.print(each + " ");
        }
        System.out.println();
    }

    public static void printArray(double[] array ) {
        for (double each : array) {
            System.out.print(each + " ");
        }
        System.out.println();
    }

    public static void printArray(int[] array ) {
        for (int each : array) {
            System.out.print(each + " ");
        }
        System.out.println();
    }

    public static void printDoubleArray(double[][] array) {
        for (int i =0; i < array.length; i++) {
            for (int j =0; j< array[0].length; j++) {
                System.out.print(array[i][j]);
            }
            System.out.println();
        }
    }

    public static boolean Match(int[][] array, int m, int n) {
        for(int i =0; i < array.length; i++) {
            if (array[i][0] == m && array[i][1] ==n) {
                return true;
            }
        }
        return false;

    }
}
