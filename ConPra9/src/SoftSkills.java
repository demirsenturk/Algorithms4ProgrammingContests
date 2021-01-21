import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SoftSkills {

    public static BigInteger gcd(BigInteger a , BigInteger b){
        // Euclidean Algorithm
        if( b.equals(BigInteger.ZERO) ){
            return a;
        } else {
            return gcd(b, a.mod(b) );
        }
    }

    public static BigInteger gcdBigInteger(BigInteger a, BigInteger b){
        BigInteger gcd = gcd(a,b);
        return gcd;
    }

    static int power(int x, int y, int p) {
        int res = 1;
        x = x % p;
        while (y > 0) {
            if (y % 2 == 1) {
                res = (res * x) % p;
            }
            y = y / 2;
            x = (x * x) % p;
        }

        return res;
    }

    // Returns n^(-1) mod p
    static int modInverse(int n, int p) {
        return power(n, p - 2, p);
    }

    // Returns nCr % p using Fermat's
    // little theorem.
    static int nCrModPFermat(int n, int r, int p) {

        // Base case
        if (r == 0) {
            return 1;
        }

        int[] fac = new int[n + 1];
        fac[0] = 1;

        for (int i = 1; i <= n; i++) {
            fac[i] = ( fac[i - 1] * i ) % p;
        }

        return ( fac[n] * ( ( ( modInverse(fac[r], p) % p ) * modInverse(fac[n - r], p) ) % p) ) % p;
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

    public static long chineseRemainder(int[] p, int[] a){

        // Chinese remainder theorem

        long M = 223092870;
        long res = 0;

        for (int i = 0; i < p.length; i++) {
            int mi = p[i];
            // calculate M / mi
            Long Mi = M / p[i];

            Long mi_l = Long.valueOf(mi);
            Long ti = extendedEuclideanAlgorithm(mi_l, Mi);
            // ei
            Long ei = Mi * ti;
            // sum of all remainder * ei
            res = res + ( ei * a[i] * mi );
        }

        return res;
    }


    public static void main(String[] args) throws IOException {
        InputStreamReader r = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(r);
        StringBuilder sb = new StringBuilder();
        int t = Integer.parseInt(in.readLine());
        int[] p = new int[]{2,3,5,7,11,13,17,19,23};

        for (int j = 1; j <= t; j++) {

            // parsing n and m
            String line = in.readLine();
            String[] parts = line.split(" ");
            int n = Integer.parseInt(parts[0]);
            int m = Integer.parseInt(parts[1]);

            int[] a_i = new int[p.length];
            for (int i = 0; i < p.length; i++) {
                a_i[i] = nCrModPFermat( n , m , p[i] );
                System.out.println( n + " C " +  m + " % " + p[i] + " = " + a_i[i]);
            }

            String result = "" + chineseRemainder( p, a_i );
            sb.append("Case #" + j + ": " + result + "\n");
        }
        System.out.println(sb.toString());
    }
}
