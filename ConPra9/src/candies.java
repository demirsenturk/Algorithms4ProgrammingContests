import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class candies {

    public static class Euclid{

        private static HashSet<Long> sums;
        private static long[] numbers;

        public Euclid(long[] numbers) {
            this.sums = new HashSet<Long>();
            this.numbers = numbers;
        }

        public static long result(){
            return multiGcd();
        }

        public static long gcd(long a , long b){
            // Euclidean Algorithm
            if( b == 0 ){
                return a;
            } else {
                return gcd(b, a % b );
            }
        }

        public static void setCombinationSumsRec( long sum, int left, int right ) {
            if (left > right) {
                long sum_with_lea = sum + 1;
                sums.add(sum_with_lea);
                return;
            }
            setCombinationSumsRec(sum + numbers[left], left + 1, right);
            setCombinationSumsRec( sum, left + 1, right);
        }

        public static long multiGcd() {
            List<Long> numbers = new ArrayList<Long>(sums);
            long res = numbers.get(0);
            for (int i = 1; i < numbers.size(); i++){
                long cur_gcd = gcd( numbers.get(i) , res );
                // to avoid overflow first divide by gcd
                long product = res / cur_gcd;
                // then multiply with current number
                res = numbers.get(i) * product;

            }
            // return result
            return res;
        }

        public static HashSet<Long> getSums() {
            return sums;
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

            String objects_line = in.readLine();
            String[] string_numbers = objects_line.split(" ");

            long[] c = new long[n];
            for (int i = 0; i < n; i++) {
                c[i] = Long.parseLong(string_numbers[i]);
            }

            Euclid euc = new Euclid(c);
            euc.setCombinationSumsRec(0, 0,n-1);
//            Iterator it = euc.getSums().iterator();
//            while (it.hasNext()) {
//                System.out.print(it.next() + " ");
//            }
//            System.out.println();

            String result = "" + euc.result();
            sb.append("Case #" + j + ": " + result + "\n");
        }
        System.out.println(sb.toString());
    }

}
