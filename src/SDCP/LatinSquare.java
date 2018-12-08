package SDCP;

import java.util.Random;

public class LatinSquare {
    int a[][];
    int n;
    int resolved =0;
    public LatinSquare(int n, int sizeS) {
        this.n =n;
        a = new int[n][n];
        getInitS(a, n, sizeS);

    }

    public int getRandom(int n) {
        Random random = new Random();
        return random.nextInt(n);
    }

    public boolean checkColor(int color, int r, int c, int a[][], int n) {
        for (int i =0; i < n; i ++) {
            if (a[r][i] == color || a[i][c] == color){
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
                count ++;
            }
        }
    }

    public void reset(int a[][], int n) {
        System.out.println("reset");
        for (int i=0; i< n; i++) {
            for (int j=0; j< n; j++) {
              a[i][j] =0;
            }
        }

    }
    public void printArray(int a[][], int n) {
        for (int i=0; i< n; i++) {
            for (int j=0; j< n; j++) {
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
        if (k ==n || resolved ==1) {
            resolved =1;
            System.out.println("resolved");
            print();
            return;
        }
        int fillSpot = findNext(a[k], n);
        if (fillSpot !=-1) {
            for (int i =1; i <=n; i++) {
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

    public boolean fillColor (int r, int c, int a[][], int n) {
        for (int i =0; i <= n; i++) {
            if (checkColor(i, r, c, a, n)){
                a[r][c] = i;
                return true;
            }
        }
        return false;
    }

    public int findNext(int b[], int n) {
        for (int i =0; i < n; i++) {
            if (b[i]==0)
            return i;
        }
        return -1;
    }


    public static void main(String args[]) {
        int n = 100;
        int s = (int)0.5*n;
        LatinSquare latinSquare = new LatinSquare(n, s);
        latinSquare.print();
        latinSquare.solve();
    }
}
