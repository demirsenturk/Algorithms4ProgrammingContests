import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;

public class GameShow {

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

    public static void main(String[] args) throws IOException {
        InputStreamReader r = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(r);
        StringBuilder sb = new StringBuilder();
        int t = Integer.parseInt(in.readLine());

        for (int j = 1; j <= t; j++) {

            // parsing n, p and q
            String line = in.readLine();
            String[] parts = line.split(" ");
            int n = Integer.parseInt(parts[0]);
            Long p = Long.parseLong(parts[1]);
            Long q = Long.parseLong(parts[2]);

            BigInteger big_p = BigInteger.valueOf(p);
            BigInteger big_q = BigInteger.valueOf(q);
            BigInteger gcd = gcdBigInteger(big_p,big_q);
            big_p = big_p.divide(gcd);
            big_q = big_q.divide(gcd);


            BigInteger nominator;
            BigInteger denominator = big_q;
            if( big_p.equals(big_q) ) {
                BigInteger big_n = BigInteger.valueOf(Long.parseLong(parts[0]));
                nominator = big_p.multiply(big_n);
            } else {
                nominator = big_p.pow(n + 1);
//            BigInteger p_over_n = nominator.multiply(big_p);
                denominator = big_q.pow(n + 1);
//            BigInteger q_over_n = denominator.multiply(big_q);

                nominator = denominator.subtract(nominator);
                nominator = nominator.abs();

                BigInteger q_denominator = big_q.subtract(big_p);
                q_denominator = q_denominator.abs();

                nominator = nominator.multiply(big_q);
//            nominator = nominator.multiply(p_over_n);

                denominator = denominator.multiply(q_denominator);
//            denominator = denominator.multiply(q_over_n);

                gcd = gcdBigInteger(nominator, denominator);
                nominator = nominator.divide(gcd);
                denominator = denominator.divide(gcd);

                nominator = denominator.subtract(nominator);
                nominator = nominator.abs();

                gcd = gcdBigInteger(nominator, denominator);
                nominator = nominator.divide(gcd);
                denominator = denominator.divide(gcd);

            }

            String result = "" + nominator.toString() + "/" + denominator.toString();
            sb.append("Case #" + j + ": " + result + "\n");
        }
        System.out.println(sb.toString());
    }
}
