import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Bracelets {

    static int rotateString(String str, String compare) {
        int max = 0;
        // current max length of the common sequence for this iteration

        int n = str.length();

        StringBuffer sb = new StringBuffer(str);
        sb.append(str);
        // concatenate string with itself

        for (int i = 0; i < n; i++) {
            StringBuilder a1 = new StringBuilder();
            for (int j=0; j != n; j++) {
                a1.append(sb.charAt(i + j));
            }
            // try for each rotation of the string
            int cur = commonLongestSubsequence( a1.toString() , compare);
            if( cur > max ){
                // if a new value found update
                max = cur;
            }
        }
        return max;
    }

    public static int iteration(String a, String b){
        int maximum = 0;
        StringBuilder sb = new StringBuilder();
        String first;
        String second;
        if( a.length() < b.length() ){
            // take the smaller string as first and longer as second
            sb.append(a);
            first = a;
            second = b;
        } else {
            // take the smaller string as first and longer as second
            sb.append(b);
            first = b;
            second = a;
        }
        // reverse the smaller string
        sb = sb.reverse();

        // find the maximum common sequence length of the strings rotations
        int new_result = rotateString(first,second);
        if( new_result > maximum ){
            maximum = new_result;
        }
        // find the maximum common sequence length of the reverse strings rotations
        new_result = rotateString(sb.toString(),second);
        if( new_result > maximum ){
            maximum = new_result;
        }
        return maximum;
    }

    // algorithm source https://www.geeksforgeeks.org/longest-common-subsequence-dp-4/

    public static int commonLongestSubsequence(String a, String b){
        int m = a.length();
        int n = b.length();

        int[][] c = new int[m+1][n+1];

        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {

                if( i == 0 || j == 0 ){
                    // initialize the matrix | first row and column
                    c[i][j] = 0;
                } else if ( a.charAt(i-1) == b.charAt(j-1) ){
                    // same char found -> increase sequence
                    c[i][j] = c[i-1][j-1] + 1;
                } else {
                    // take previous max common sequence
                    c[i][j] = Math.max( c[i][j-1] , c[i-1][j] );
                }

            }
        }
        // result
        return c[m][n];
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

            String a = in.readLine();
            String b = in.readLine();



            int result = iteration(a, b);
            sb.append("Case #" + j + ": " + result + "\n");
        }
        System.out.println(sb.toString());
    }
}
