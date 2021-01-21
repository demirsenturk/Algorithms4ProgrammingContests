import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Meteorite {

    // Ray Casting Algorithm ( to the right )
    public static boolean rayCastingAlgorithm( int x_impact , int y_impact, int edges[][]){
        boolean inPolygon = false;

        double x = x_impact;
        double y = y_impact;

        for (int k = 0; k < edges.length; k++) {
            double x_i = edges[k][0];
            double y_i = edges[k][1];
            double x_j = edges[k][2];
            double y_j = edges[k][3];

            // check if the y coordinates of the point is within the current edges y-coordinates
            // otherwise not cross the line
            if( (y_i > y) != (y_j > y) ){
                double m = ( x_j - x_i ) / ( y_j - y_i );
                // the slope-intercept
                double intersection_point_x = m * ( y - y_i ) + x_i;
                // the x-coordinate of the point crossed at the edge
                if( x < intersection_point_x ){
                    // odd -> point is inside polygon
                    inPolygon = !inPolygon;
                    // even -> point is outside polygon
                }
            }

        }

        return inPolygon;
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

            // parsing x_impact, y_impact and n
            String line = in.readLine();
            String[] parts = line.split(" ");
            int x_impact = Integer.parseInt(parts[0]);
            int y_impact = Integer.parseInt(parts[1]);
            int n = Integer.parseInt(parts[2]);

            int[][] edges = new int[n][4];

            // parse the edges of the Polygon
            for (int i = 0; i < n; i++) {
                String i_line = in.readLine();
                String[] i_line_parts = i_line.split(" ");
                int x1 = Integer.parseInt(i_line_parts[0]);
                int y1 = Integer.parseInt(i_line_parts[1]);
                int x2 = Integer.parseInt(i_line_parts[2]);
                int y2 = Integer.parseInt(i_line_parts[3]);
                edges[i][0] = x1;
                edges[i][1] = y1;
                edges[i][2] = x2;
                edges[i][3] = y2;
            }

            // print "jackpot" if the point is in the Polygon or "too bad" if not
            String result;
            if( rayCastingAlgorithm(x_impact,y_impact, edges) ){
                result = "jackpot";
            } else {
                result = "too bad";
            }
            sb.append("Case #" + j + ": " + result + "\n");
        }
        System.out.println(sb.toString());
    }
}
