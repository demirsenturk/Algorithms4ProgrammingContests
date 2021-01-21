import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ChangeMaking {

    public static String dynamicChange(int arr[], int n, int k){
        int[] dp = new int[n+1];
        dp[0] = 0;
        int[] pre = new int[n+1];

        for(int j=1; j<=n; j++) {
            int minimum = j;
            pre[j] = j-1;
            for(int i=1; i < k ; i++) {
                if(j >= arr[i]) {
                    if( minimum < 1 + dp[j - arr[i]] ){

                    } else {
                        minimum = 1 + dp[j - arr[i]];
                        pre[j] = j - arr[i];
                    }
//                    minimum = Math.min(minimum, 1 + dp[j - arr[i]]);
                }
            }
            dp[j] = minimum;
        }
        HashMap<Integer,Integer> exchange = new HashMap<Integer,Integer>();

        int o = n;
        while( o != 0 ){
            int next = pre[o];
            int difference = o - next;
//            System.out.println(difference);
            int count = exchange.containsKey(difference) ? exchange.get(difference) : 0;
            exchange.put(difference, count + 1);

            o = next;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            int amount = exchange.containsKey(arr[i]) ? exchange.get(arr[i]) : 0;
            if( i == 0 ){
                sb.append(amount);
            } else {
                sb.append(" " + amount);
            }
        }

        return sb.toString();
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

            // parsing n and c
            String line = in.readLine();
            String[] parts = line.split(" ");
            int n = Integer.parseInt(parts[0]);
            int c = Integer.parseInt(parts[1]);

            String values_line = in.readLine();
            String[] string_values = values_line.split(" ");
            int[] values = new int[n];
            for (int i = 0; i < n; i++) {
                int v_i = Integer.parseInt(string_values[i]);
                values[i] = v_i;
            }

            String result = dynamicChange(values, c , n);
            sb.append("Case #" + j + ": " + result + "\n");
        }
        System.out.println(sb.toString());
    }
}
