import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;

public class Vaults_Vampires {

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

            // parsing n and c
            String line = in.readLine();
            String[] parts = line.split(" ");
            int n = Integer.parseInt(parts[0]);
            String[] dice_sets = parts[1].split("\\+");
            int[][] dices = new int[dice_sets.length][2];

            int sides = 0;
            for (int i = 0; i < dice_sets.length; i++) {
                String[] a_b = dice_sets[i].split("d");
                int a = Integer.parseInt(a_b[0]);
                int b = Integer.parseInt(a_b[1]);
                dices[i][0] = a;
                dices[i][1] = b;
                sides += a * b;
            }
            BigInteger[] prev = new BigInteger[sides+1];
            BigInteger[] probs = new BigInteger[sides+1];
            probs[0] = BigInteger.ONE;
//            prev[0] = BigInteger.ONE;
            for (int i = 0; i < dices.length; i++) {
                for (int k = 0; k < dices[i][0]; k++) {
                    //
                    for (int l = 0; l < prev.length; l++) {
                        if( probs[l] == null ){
                            probs[l] = BigInteger.ZERO;
                        }
                        prev[l] = probs[l];
                        probs[l] = BigInteger.ZERO;
                    }
//                    probs[0] = BigInteger.ONE;
//                    prev[0] = BigInteger.ONE;
                    //
                    for (int roll = 1; roll <= dices[i][1]; roll++) {
                        for (int total = 0; total <= probs.length-1-roll; total++) {
                            probs[total+roll] = probs[total+roll].add(prev[total]);
                        }
                    }
                }
            }
            BigInteger res = BigInteger.ZERO;
            BigInteger sum = BigInteger.ZERO;
            for (int i = 0; i < probs.length; i++) {
                if( i >= n ) {
                    res = res.add(probs[i]);
                }
                sum = sum.add(probs[i]);
            }
            BigInteger gcd = gcdBigInteger(res,sum);
//            System.out.println(res.toString() + " " + sum.toString() + " - " + gcd.toString());
            res = res.divide(gcd);
            sum = sum.divide(gcd);
            String result = "" + res.toString() + "/" + sum.toString() ;
            sb.append("Case #" + j + ": " + result + "\n");
        }
        System.out.println(sb.toString());
    }
}
