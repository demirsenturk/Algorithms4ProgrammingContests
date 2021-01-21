import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Dolphins {

    private static final int A = 0;
    private static final int C = 1;
    private static final int T = 2;
    private static final int G = 3;

    static class DNA{

        private int score;
        private int[][] matrix;
        private int[][] occurences;

        public DNA( int[][] occurences ){
            this.matrix = new int[4][4];
            this.score = Integer.MIN_VALUE;
            this.occurences = occurences;
        }

        boolean operation(){
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j <= i; j++) {
                    if( matrix[i][j] < 10 ) {
                        boolean improved = increased(i, j);
                        if (improved) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        boolean increased(int x, int y){
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j <= i; j++) {
                    if( !(i == x && j == y) && matrix[i][j] > -10 ){
                        // not the increased cell
                        int new_result;
                        if( i == j && x == y
                                && matrix[i][j] > 0
                                && matrix[x][y] + 1 <= 10 ) {
                            // diagonal entries
                            matrix[x][y] += 1;
                            matrix[i][j] -= 1;
                            new_result = calculate();
                            if( new_result > score ){
                                score = new_result;
                                return true;
                            } else {
                                // take changes back
                                matrix[x][y] -= 1;
                                matrix[i][j] += 1;
                            }
                        } else if ( i != j && x != y && matrix[x][y] + 1 <= 10 ) {
                            // symmetrical entry
                            matrix[x][y] += 1;
                            matrix[y][x] += 1;
                            matrix[i][j] -= 1;
                            matrix[j][i] -= 1;
                            new_result = calculate();
                            if( new_result > score ){
                                score = new_result;
                                return true;
                            } else {
                                // take changes back
                                matrix[i][j] += 1;
                                matrix[j][i] += 1;
                                matrix[x][y] -= 1;
                                matrix[y][x] -= 1;
                            }
                        } else if ( i != j && x == y && matrix[x][y] + 2 <= 10 ){
                            // symmetrical entry
                            matrix[x][y] += 2;
                            matrix[i][j] -= 1;
                            matrix[j][i] -= 1;
                            new_result = calculate();
                            if( new_result > score ){
                                score = new_result;
                                return true;
                            } else {
                                // take changes back
                                matrix[i][j] += 1;
                                matrix[j][i] += 1;
                                matrix[x][y] -= 2;
                            }
                        } else if ( i == j && x != y
                                && matrix[i][j] - 2 >= 0  ){
                            // symmetrical entry
                            matrix[x][y] += 1;
                            matrix[y][x] += 1;
                            matrix[i][j] -= 2;
                            new_result = calculate();
                            if( new_result > score ){
                                score = new_result;
                                return true;
                            } else {
                                // take changes back
                                matrix[x][y] -= 1;
                                matrix[y][x] -= 1;
                                matrix[i][j] += 2;
                            }
                        }
                    }
                } // end for inner
            } // end for outer
            return false;
        }

        int calculate(){
            int new_score = 0;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    new_score += occurences[i][j] * matrix[i][j];
                }
            }
            return new_score;
        }

        void changeScore(int new_score){
            this.score = new_score;
        }

        public int local_search(){
            boolean changed = true;
            while(changed){
                changed = operation();
            }

//            StringBuilder sb = new StringBuilder();
//            for (int i = 0; i < matrix.length; i++) {
//                for (int k = 0; k < matrix[0].length; k++) {
//                    sb.append( matrix[i][k] + " " );
//                }
//                sb.append("\n");
//            }
//            System.out.println(sb.toString());

            return score;
        }

    }

    public static int Genome(char c){
        if( c == 'A' ){
            return A;
        } else if( c == 'C' ){
            return C;
        } else if( c == 'T' ){
            return T;
        } else {
            return G;
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

            // parsing n and m
            String line = in.readLine();
            String[] parts = line.split(" ");
            int n = Integer.parseInt(parts[0]);
            int m = Integer.parseInt(parts[1]);

            int[][] occurences = new int[4][4];

            String[] human = new String[n];

            for (int i = 0; i < n; i++) {
                String human_line = in.readLine();
                human[i] = human_line;
            }

            String[] mouse = new String[m];

            for (int i = 0; i < m; i++) {
                String mouse_line = in.readLine();
                mouse[i] = mouse_line;
            }

            for (int i = 0; i < n; i++) {
                for (int k = 0; k < m; k++) {
                    for (int c = 0; c < human[i].length(); c++) {
                        int firstGenome = Genome( human[i].charAt(c) );
                        int secondGenome = Genome( mouse[k].charAt(c) );
                        occurences[firstGenome][secondGenome] += 1;
                    }
                }
            }

            DNA d = new DNA(occurences);

//            for (int i = 0; i < occurences.length; i++) {
//                for (int k = 0; k < occurences[0].length; k++) {
//                    sb.append( occurences[i][k] + " " );
//                }
//                sb.append("\n");
//            }

            String result = "" + d.local_search();
            sb.append("Case #" + j + ": " + result + "\n");
        }
        System.out.println(sb.toString());
    }

}
