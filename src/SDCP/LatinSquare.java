package SDCP;

import java.util.*;

public class LatinSquare {
    int a[][];
    int n;
    int resolved = 0;

    public LatinSquare(int n, int sizeS) {
        this.n = n;
        a = new int[n][n];
        getInitS(a, n, sizeS);

    }

    public int getRandom(int n) {
        Random random = new Random();
        return random.nextInt(n);
    }

    public boolean checkColor(int color, int r, int c, int a[][], int n) {
        for (int i = 0; i < n; i++) {
            if (a[r][i] == color || a[i][c] == color) {
                return false;
            }
        }
        return true;
    }


    // color range [1, n] inclusive.
    //filled size:  0.2*N^2, 0.5*N^2, etc.

    public void getInitS(int a[][], int n, int filledSize) {
        int count = 0;
        while (count < filledSize) {
            int r = getRandom(n);
            int c = getRandom(n);
            if (a[r][c] == 0) {
                // file a random color

                if (!fillColor(r, c, a, n)) {

                    reset(a, n);
                }
                count++;
            }
        }
    }

    public void reset(int a[][], int n) {
        System.out.println("reset");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = 0;
            }
        }

    }

    public void printArray(int a[][], int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print("  " + a[i][j]);
            }
            System.out.println();
        }
    }

    public void print() {
        printArray(a, a.length);
    }

    public void solve() {
        solve(a, n, 0);
    }

    public void solve(int a[][], int n, int k) {
        if (k == n || resolved == 1) {
            resolved = 1;
            System.out.println("resolved");
            print();
            return;
        }
        int fillSpot = findNext(a[k], n);
        if (fillSpot != -1) {
            for (int i = 1; i <= n; i++) {
                if (checkColor(i, k, fillSpot, a, n)) {
                    a[k][fillSpot] = i;
                    solve(a, n, k);
                    a[k][fillSpot] = 0;
                }
            }
        } else {
            solve(a, n, k + 1);
        }
    }

    public boolean fillColor(int r, int c, int a[][], int n) {
        for (int i = 0; i <= n; i++) {
            if (checkColor(i, r, c, a, n)) {
                a[r][c] = i;
                return true;
            }
        }
        return false;
    }

    public int findNext(int b[], int n) {
        for (int i = 0; i < n; i++) {
            if (b[i] == 0)
                return i;
        }
        return -1;
    }

    public void solveSudoku(char[][] board) {

        dfs(board, 0, 0);

    }

    public boolean dfs(char[][] board, int i, int j) {
        System.out.println("~~~~" + i + "~~~" + j);
        if (i == 8&& j == 9) {
            return true;
        }
        if (i ==0 && j ==6) {
            System.out.println("~~~~" + i + "~~~" + j);
        }


        i = j / 8 + i;
        j = j % 8;

        if (board[i][j] != '.') {
            return dfs(board, i, j + 1);
        }
        for (int k = 1; k <= 9; k++) {
            if (isAllowed(board, i, j, k)) {
                board[i][j] = (char) ('0' + k);
                if (dfs(board, i, j + 1)) return true;
                board[i][j] = '.';
            }

        }
        return false;

    }

    public boolean isAllowed(char[][] board, int i, int j, int val) {
        //   System.out.println( checking  +"~~~"+i + "~~~~" + j );
        return isRowValid(board, i, val) && isColValid(board, j, val) && isBlockValid(board, i, j, val);
    }

    public boolean isRowValid(char[][] board, int i, int val) {
        for (int c = 0; c < 9; c++) {
            if (board[i][c] - val == '0') {
                return false;
            }
        }
        return true;

    }

    public boolean isColValid(char[][] board, int j, int val) {
        for (int r = 0; r < 9; r++) {
            if (board[r][j] - val == '0') {
                return false;
            }
        }
        return true;
    }

    public boolean isBlockValid(char[][] board, int i, int j, int val) {
        int c = (i / 3)*3;
        int r = (j / 3)*3;

        for (int m = r; m < r + 3; m++) {
            for (int n = c; n < c + 3; n++) {
                if (board[n][m] - val == '0') {
                    return false;
                }
            }
        }
        return true;
    }

    public static int[] asteroidCollision(int[] asteroids) {
        Stack<Integer> stack = new Stack<>();

        for (int i : asteroids) {
            if (i >0) {
                stack.push(i);
            } else {
                if (stack.isEmpty()||stack.peek()<0) {
                    stack.push(i);
                    continue;
                }
                while(!stack.isEmpty()&&-i >= stack.peek()){
                    stack.pop();
                }
            }
        }

        return stack.stream().mapToInt(i->i).toArray();

    }

    public static void main(String args[]) {
        int n = 100;
        int s = (int) 0.5 * n;

        char[][] board = {
                {'5', '3', '.', '.', '7', '.', '.', '.', '.'},
                {'6', '.', '.', '1', '9', '5', '.', '.', '.'},
                {'.', '9', '8', '.', '.', '.', '.', '6', '.'},
                {'8', '.', '.', '.', '6', '.', '.', '.', '3'},
                {'4', '.', '.', '8', '.', '3', '.', '.', '1'},
                {'7', '.', '.', '.', '2', '.', '.', '.', '6'},
                {'.', '6', '.', '.', '.', '.', '2', '8', '.'},
                {'.', '.', '.', '4', '1', '9', '.', '.', '5'},
                {'.', '.', '.', '.', '8', '.', '.', '7', '9'}
        };
        LatinSquare latinSquare = new LatinSquare(n, s);
        int a [] = {-2, -2, 1, -2};


        String p = "bob!";
         String b[]  =   {"hit"};
        Solution solution = new Solution();
        solution.mostCommonWord(p, b);
   //     asteroidCollision(a);
      //  latinSquare.solveSudoku(board);
     //   latinSquare.print();
    //    latinSquare.solve();
    }

    public static class Solution {
        public String mostCommonWord(String paragraph, String[] banned) {
            Map<String, Integer> map = new HashMap<>();
            Set<String> banSet = new HashSet<>();
            int i =0;


            for (String each : banned) {
                banSet.add(each);
            }
            String each = "";
            while(i < paragraph.length()) {

                char c = paragraph.charAt(i);
                if (isLetter(c)) {
                    each += toLower(c);
                } else {
                    if (!banSet.contains(each)&& !each.equals("")) {
                        map.put(each, map.getOrDefault(each, 0) +1);
                    }
                    each = "";
                }
                i++;

            }
            if (each.equals("")) {
                if (!banSet.contains(each)) {
                    map.put(each, map.getOrDefault(each, 0) +1);
                }
            }

            int max =0;
            String res ="";
            for (String key : map.keySet()) {
                if (map.get(key) > max) {
                    max = map.get(key);
                    res = key;
                }
            }
            return res;

        }
        private boolean isLetter(char c) {
            return c != ',' && c!='.' && c !=' ';
        }
        private char toLower(char c) {
            if ( c>='A' && c <='Z') {
                return (char) ('a' + (c -'A'));
            } else return c;
        }
    }
}

