import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class QueensProblem {

    // code adjusted from source https://www.geeksforgeeks.org/n-queen-problem-backtracking-3/

    static class N_QueensProblem{
        private int[][] table;
        private int n;
        private int placed;

        public N_QueensProblem(int n) {
            this.n = n;
            this.placed = 0;
            this.table = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    table[i][j] = 0;
                }
            }
        }

        public String solve(){
            if ( backtrack(0) == false ) {
//                print();
                return "\nimpossible";
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < n; i++) {
                sb.append("\n");
                for (int j = 0; j < n; j++) {
                    if(table[i][j] != 0){
                        sb.append("x");
                    } else {
                        sb.append(".");
                    }
                }
            }
            return sb.toString();
        }


        public boolean valid(int row, int col) {
            int i, j;

            // check the row if there is one more queen
            for (i = 0; i < n; i++) {
                if (table[row][i] != 0 && i != col) {
//                    System.out.println("0 row " + row + " col " + col);
                    return false;
                }
            }

            // Check upper diagonal on left side
            for (i = row, j = col; i >= 0 && j >= 0; i--, j--) {
                if (table[i][j] != 0 && !(i == row && j == col) ) {
//                    System.out.println("1 row " + row + " col " + col);
                    return false;
                }
            }

            // Check lower diagonal on left side
            for (i = row, j = col; j >= 0 && i < n; i++, j--) {
                if (table[i][j] != 0 && !(i == row && j == col) ) {
//                    System.out.println("2 row " + row + " col " + col);
                    return false;
                }
            }

            // Check lower diagonal on right side
            for (i = row, j = col; j < n && i < n; i++, j++) {
                if (table[i][j] != 0 && !(i == row && j == col) ) {
//                    System.out.println("3 row " + row + " col " + col);
                    return false;
                }
            }

            // Check upper diagonal on right side
            for (i = row, j = col; j < n && i >= 0; i--, j++) {
                if (table[i][j] != 0 && !(i == row && j == col) ) {
//                    System.out.println("4 row " + row + " col " + col);
                    return false;
                }
            }

            return true;
        }

        boolean completed(int col){
            if (col >= n) {
                // all queens placed
                return true;
            }
            return false;
        }

        public boolean backtrack(int col) {
            if (completed(col)) {
                // all queens placed
                return true;
            }

            // for all c’ in next(c)
            for (int i = 0; i < n; i++) {
                if ( valid(i, col) ) {

                    if(table[i][col] != 2) {
                        // if there is not a queen from start state
                        table[i][col] = 1;
                        // put a queen
                    }

                    // if backtrack(c’) then
                    if (backtrack(col + 1) == true) {
                        return true;
                    }

                    if(table[i][col] != 2) {
                        // if the queen is not from start state
                        table[i][col] = 0;
                        // delete it
                    }
                }
            }

            return false;
        }

        public void addQueen(int i, int j){
            // add initial queens from start state called via parser
            table[i][j] = 2;
            this.placed++;
        }

        public void print(){
            // for debugging
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < n; i++) {
                sb.append("\n");
                for (int j = 0; j < n; j++) {
                    sb.append(table[i][j]);
                }
            }
            System.out.println(sb.toString());
        }

    }

    public static void main(String[] args) throws IOException {
        InputStreamReader r = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(r);
        StringBuilder sb = new StringBuilder();
        int t = Integer.parseInt(in.readLine());

        for (int j = 1; j <= t; j++) {
            if (j != 1) {
                in.readLine();
            }
            // parsing n
            int n = Integer.parseInt(in.readLine());

            N_QueensProblem solver = new N_QueensProblem(n);
            // create empty table

            for (int i = 0; i < n; i++) {
                String n_line = in.readLine();
                for (int k = 0; k < n; k++) {
                    if(n_line.charAt(k) == 'x' ){
                        // put queen to the table
                        solver.addQueen(i,k);
                    }
                }
            }
//            solver.print();
            String result = solver.solve();
            sb.append("Case #" + j + ":" + result + "\n");
        }
        System.out.println(sb.toString());
    }

}
