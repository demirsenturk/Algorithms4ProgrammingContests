import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class StreetLights {

    static final int NOT_FOUND = -1;

    public static int greedy(int[] locations , int l , int d) {
        int number_of_lights = 0;
        int last = l+1;

        boolean[] isLight = new boolean[last];
        for (int i = 0; i < locations.length; i++) {
            isLight[locations[i]] = true;
        }

        int[] light_ranges = new int[last];
        for (int i = 0; i < isLight.length; i++){
            // if there is light at the point
            if ( isLight[i] ) {
                int left1 = i - d;
                // most left point that is illuminated
                int left = Math.max(left1, 0);
                int r = i + d;
                // most right point that is illuminated
                int right = Math.min( r , last );
                for (int j = left; j < right; j++) {
                    // go until the range of the lamp
                    light_ranges[j] = Math.max( light_ranges[j], r );
                }
            }
        }
        int illuminated = 0;
        while ( illuminated < last ){
            // till the last point of street not passed
            if ( illuminated >= last - 1 ) {
                // all the street is illuminated
                return number_of_lights;
            }
            if ( light_ranges[illuminated] == 0 ) {
                // can not process further ( no light )
                return NOT_FOUND;
            }
            // light on
            number_of_lights++;
            // illuminate the range of light
            illuminated = light_ranges[illuminated];
        }
        if ( illuminated < last ) {
            // if the last point can not be illuminated
            return NOT_FOUND;
        }

        return number_of_lights;
    }

    // idea from source:
    // https://leetcode.com/problems/minimum-number-of-taps-to-open-to-water-a-garden/discuss/484326/java-dp-greedy


    public static void main(String[] args) throws IOException {
        InputStreamReader r = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(r);
        StringBuilder sb = new StringBuilder();
        int t = Integer.parseInt(in.readLine());

        for (int j = 1; j <= t; j++) {
            if (j != 1) {
                in.readLine();
            }

            // parsing l, n and d
            String line = in.readLine();
            String[] parts = line.split(" ");
            int l = Integer.parseInt(parts[0]);
            int n = Integer.parseInt(parts[1]);
            int d = Integer.parseInt(parts[2]);

            int[] lights = new int[n];
            String n_line = in.readLine();
            if( n != 0 ) {
                String[] n_parts = n_line.split(" ");
                for (int x = 0; x < n; x++) {
                    lights[x] = Integer.parseInt(n_parts[x]);
                }
                Arrays.sort(lights);
            }

            int res = NOT_FOUND;

            if( d != 0 ) {
                res = greedy( lights, l , d );
            }

            String result;
            if( res != NOT_FOUND ){
                result = "" + res;
            } else {
                result = "impossible";
            }
            sb.append("Case #" + j + ": " + result + "\n");
        }
        System.out.println(sb.toString());
    }

}
