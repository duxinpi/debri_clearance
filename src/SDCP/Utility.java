package SDCP;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Utility {

    public static double getSum(double[] array) {
        double sum = 0;
        for (int i = 0; i < array.length; i++) {
            sum += array[i];
        }
        return sum;
    }

    public static double getSum(int[] array) {
        double sum = 0;
        for (int i = 0; i < array.length; i++) {
            sum += array[i];
        }
        return sum;
    }

    public static double getSum(double[] array, int s, int e) {
        double sum = 0;
        for (int i = s; i < e; i++) {
            sum += array[i];
        }
        return sum;
    }

    public static void fill(double[][] array, double v) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                array[i][j] = v;
            }
        }
    }


    public static void printArray(Object[] array) {
        for (Object each : array) {
            System.out.print(each + " ");
        }
        System.out.println();
    }

    public static void printArray(double[] array) {
        for (double each : array) {
            System.out.print(each + " ");
        }
        System.out.println();
    }

    public static void printArray(int[] array) {
        for (int each : array) {
            System.out.print(each + " ");
        }
        System.out.println();
    }

    public static void printDoubleArray(double[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                System.out.print(array[i][j]);
            }
            System.out.println();
        }
    }

    public static boolean Match(int[][] array, int m, int n) {
        for (int i = 0; i < array.length; i++) {
            if (array[i][0] == m && array[i][1] == n) {
                return true;
            }
        }
        return false;

    }

    public static boolean isAllowed(int n, int q, List<String> list) {
        int j = q;
        int k = q;
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i).charAt(q) == 'Q') return false;
            if (j < n && list.get(i).charAt(j++) == 'Q') return false;
            if (k >= 0 && list.get(i).charAt(k--) == 'Q') return false;
        }

        return true;
    }


    public static List<String> generateParenthesis(int n) {
        List<String> result = new ArrayList<>();
        if (n == 0) {
            return result;
        }

        dfs(n, "", result, 0);
        return result;

    }

    // 1 "()"
    public static void dfs(int n, String s, List<String> result, int k) {
        //   System.out.println(s + "---" + k);
        if (n * 2 == k) {
            result.add(s);
            return;
        }


        if (isAllowed(n, '(', s)) {
            dfs(n, s + "(", result, k + 1);
        }
        if (isAllowed(n, ')', s)) {
            dfs(n, s + ")", result, k + 1);
        }


    }

    public static boolean isAllowed(int n, char c, String s) {
        int t = getOccurrance(c, s);
        if (t == n) {
            System.out.println(s + "--" + c);
            return false;
        }
        int count = 0;
        if (c == '(') return true;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                count++;
            }
            if (s.charAt(i) == ')') {
                count--;
            }
            if (count < 0) {
                return false;
            }
        }

        if (count == 0) {
            return false;
        }
        return true;
    }

    public static int getOccurrance(char c, String s) {
        int result = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) result++;
        }
        return result;
    }

    public static void main(String ars[]) {
        // generateParenthesis(3);


     //   compare("javaA.txt","matlabA.txt", 1512, 378);
     //   compare("javaAeq.txt","matlab_Aeq.txt", 36, 378);

        compare("javabeq.txt","matlab_beq.txt", 1512 );
        compare("javab.txt","matlab_b.txt", 1512 );
        compare("javaf.txt","matlab_f.txt", 1512 );
    }

    public static void compare(String file1, String file2, int r, int c) {
        double matlabA[][] = null;
        double javaA[][] = null;
        try {
            javaA = read(file1, r, c);
            matlabA = read(file2, r, c);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i =0; i < matlabA.length; i++) {
            for (int j =0; j < matlabA[0].length; j++) {
                if (Math.abs(matlabA[i][j]- javaA[i][j]) >0.01){
                    System.out.println("file: " + file1 + "diff: " + i + "-----" + j + " : " + matlabA[i][j] +"----java---" + javaA[i][j]);
                }
            }
        }
    }

    public static void compare(String file1, String file2, int r) {
        double matlabA[] = null;
        double javaA[] = null;
        try {
            javaA = read(file1, r);
            matlabA = read(file2, r);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i =0; i < matlabA.length; i++) {

                if (Math.abs(matlabA[i]- javaA[i]) >0.0000001){
                    System.out.println("file: " + file1 + "=====diff: " + i + "----- : " + matlabA[i] +"----java---" + javaA[i]);
                }

        }
    }


    public static void writeFile(String filePath, double[][] array) throws IOException {
        PrintWriter writer = new PrintWriter(filePath, "UTF-8");
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                writer.print(array[i][j]);
                writer.print("\t");
                if (array[i][j] != 0) {
             //       System.out.println(i + "-----" + j + " : " + array[i][j]);
                }
            }
            writer.println();

        }
        writer.close();
    }

    public static void writeFile(String filePath, double[] array) throws IOException {
        PrintWriter writer = new PrintWriter(filePath, "UTF-8");
        for (int i = 0; i < array.length; i++) {
                writer.print(array[i]);
                writer.print("\t");
                if (array[i] != 0) {
                 //   System.out.println(i + "-----" + array[i]);
                }
            writer.println();

        }
        writer.close();
    }


    public static double[][] read(String path, int r, int c) throws Exception {
        // We need to provide file path as the parameter:
        // double backquote is to avoid compiler interpret words
        // like \test as \t (ie. as a escape sequence)
        File file = new File(path);

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        double[][] result = new double[r][c];
        int row = 0;
        while ((st = br.readLine()) != null) {
            String[] line = st.split("\t");
            for (int i = 0; i < line.length; i++) {
                result[row][i] = Double.valueOf(line[i]);
                if (result[row][i] != 0) {
          //          System.out.println(row + "-----" + i + " : " + result[row][i]);

                }
            }
            row++;
        }
        return result;
    }

    public static double[] read(String path, int r) throws Exception {
        // We need to provide file path as the parameter:
        // double backquote is to avoid compiler interpret words
        // like \test as \t (ie. as a escape sequence)
        File file = new File(path);

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        double[] result = new double[r];
        int i =0;

        while ((st = br.readLine()) != null) {


                result[i] = Double.valueOf(st);
                if (result[i] != 0) {
                    //          System.out.println(row + "-----" + i + " : " + result[row][i]);

                }
                i++;
            }


        return result;
    }



}
