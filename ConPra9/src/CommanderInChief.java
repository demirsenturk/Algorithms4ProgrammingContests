import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommanderInChief {

    public static long gcd( long a , long b){
        // Euclidean Algorithm
        if( b == 0 ){
            return a;
        } else {
            return gcd(b, a % b );
        }
    }

    public static long multiGcd(long[] numbers) {
        long res = numbers[0];
        // first number
        for (int i = 1; i < numbers.length; i++){
            res = gcd( numbers[i], res);
            // save the result with the current pair
            if( res == 1 ) {
                // can not be more than 1
                return 1;
            }
        }
        // return result
        return res;
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

            String objects_line = in.readLine();
            String[] string_numbers = objects_line.split(" ");

            long[] numbers = new long[n];
            for (int i = 0; i < n; i++) {
                numbers[i] = Long.parseLong(string_numbers[i]);
            }

            String result = "" + multiGcd(numbers);
            sb.append("Case #" + j + ": " + result + "\n");
        }
        System.out.println(sb.toString());
    }
}
