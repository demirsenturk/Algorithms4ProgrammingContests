import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.*;

public class Nathlon {
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

    public static long extendedEuclideanAlgorithm( long a, long b ){
        long s = 0;
        long s1 = 1;
        long t = 1;
        long t1 = 0;
        long r = b;
        long r1 = a;
        while( r != 0 ){
            long q = r1 / r;
            long save0 = r;
            long save1 = r1;
            r1 = r;
            r = save1 - q * save0;
            save0 = s;
            save1 = s1;
            s1 = save0;
            s = save1 - q * save0;
            save0 = t;
            save1 = t1;
            t1 = save0;
            t = save1 - q * save0;
//            System.out.println(r1 + " " + r + " " + q + " " + s1 + " " + s + " " + t1 + " " + t );
        }
        if( t1 < 0 ){
            t1 = t1 + a;
        }
        return t1;
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

            // parsing n and k
            String line = in.readLine();
            String[] parts = line.split(" ");
            int n = Integer.parseInt(parts[0]);
            BigInteger k = new BigInteger(parts[1]);

            HashMap<BigInteger,BigInteger> hset_remainders = new HashMap<BigInteger,BigInteger>();
            Boolean impossible = false;
            for (int i = 0; i < n; i++) {
                String line_i = in.readLine();
                String[] line_i_parts = line_i.split(" ");
                BigInteger size_i = new BigInteger(line_i_parts[0]);
                BigInteger rest_i = new BigInteger(line_i_parts[1]);
                if( hset_remainders.containsKey(size_i) ){
                    // if the size already parsed before
                    BigInteger remainder = hset_remainders.get(size_i);
                    if( remainder.equals(rest_i) ){
                        // if the remainder is same, do nothing as it is already considered
                    } else {
                        // if the remainder is different for same size -> impossible to solve
                        impossible = true;
                    }
                } else {
                    // if the size value was first occurred now, add it to the map
                    hset_remainders.put(size_i,rest_i);
                }
            }

            // initialize res for the Sigma
            BigInteger res = BigInteger.ZERO;

            if( !impossible ) {

                // initialize an array for the values in HashMap
                n = hset_remainders.size();
                BigInteger[][] teams = new BigInteger[n][2];

                // Convert HashMap to Array
                Iterator<HashMap.Entry<BigInteger, BigInteger>> it = hset_remainders.entrySet().iterator();
                int count = 0;
                while (it.hasNext()) {
                    Map.Entry<BigInteger, BigInteger> entry = it.next();
                    teams[count][0] = entry.getKey();
                    teams[count][1] = entry.getValue();
                    count++;
                }

                // Chinese remainder theorem

                // Find the M ( product of all modulo )
                BigInteger product = BigInteger.ONE;
                for (int i = 0; i < n; i++) {
                    product = product.multiply(teams[i][0]);
                }

                for (int i = 0; i < n; i++) {
                    BigInteger mi = teams[i][0];
                    // calculate M / mi
                    BigInteger Mi = product.divide(teams[i][0]);
                    Long mi_l = mi.longValue();
                    Long Mi_l = Mi.longValue();
                    Long ti = extendedEuclideanAlgorithm(mi_l, Mi_l);
                    // ei
                    BigInteger ei = Mi.multiply(BigInteger.valueOf(ti));
                    // sum of all remainder * ei
                    res = res.add(ei.multiply(teams[i][1]));
                }

                // find the maximal result less or equal than k
                while (res.compareTo(k) != -1 && res.signum() != -1) {
                    res = res.subtract(product);
                }
                while (k.compareTo(res.add(product)) != -1) {
                    res = res.add(product);
                }

            }

            // print result or impossible
            String result;
            if( impossible || res.signum() == -1 ){
                result = "impossible";
            } else {
                result = "" + res.toString();
            }
            sb.append("Case #" + j + ": " + result + "\n");
        }
        System.out.println(sb.toString());
    }
}
