import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BreakIn {

    public static long EuclideanAlgorithm( long a, long b ){
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

            // parsing n and c
            String line = in.readLine();
            String[] parts = line.split(" ");
            int n = Integer.parseInt(parts[0]);
            long y = Integer.parseInt(parts[1]);

            String result = "" + EuclideanAlgorithm( (long) Math.pow(10,n) , y );
            sb.append("Case #" + j + ": " + result + "\n");
        }
        System.out.println(sb.toString());
    }

}
