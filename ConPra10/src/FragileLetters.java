import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FragileLetters {

    static class Point {

        private Double x;
        private Double y;
        private int i;

        public Point(Double x, Double y, int i) {
            this.x = x;
            this.y = y;
            this.i = i;
        }

    }

    // Point on side of a line
    public static int ccw(Point p_i, Point p_j, Point r) {

        Double ccw = (( p_j.x - p_i.x) * ( r.y - p_i.y)) - (( p_j.y - p_i.y) * ( r.x - p_i.x));
        // With CCW(a,b,p) := (py −ay)(bx −ax)−(px −ax)(by −ay)

        if( ccw > 0.0 ) {
            // Ml = {p | CCW(a,b,p) > 0}
            return 2;
        } else if( ccw < 0.0) {
            // Mr = {p | CCW(a,b,p) < 0}
            return 1;
        } else {
            // Mo = {p | CCW(a,b,p) = 0}
            return 0;
        }
    }

    public static int stands(Point[] points){
        int count = 0;
        // calculate the center of mass of the polygon
        Point p = centroid(points);
        // consider each edge
        for (int i = 0; i < points.length; i++) {
            // start point of the edge
            Point p1 = points[i];
            int p2_index = ((i + 1) % points.length);
            // end point of the edge
            Point p2 = points[p2_index];
            int orientation_of_centroid;
            Boolean canStand = true;
            // check if the center of mass is over the line
            // e.g. the projection is orthogonal to the line
            if( isOrthogonal(p1,p2,p) ) {
                // calculate the orientation of the center of mass
                // in which side of the edge it stands
                orientation_of_centroid = ccw(p1, p2, p);
                for (int j = 0; j < points.length; j++) {
                    // for every other point, which are not in the current line
                    if (i != j && p2_index != j) {
                        // the orientation of the point
                        int cur_orientation = ccw(p1, p2, points[j]);
                        // check it is on the same side with the center of mass and other points
                        if ( orientation_of_centroid != cur_orientation) {
                            // if not on the same side -> cant stand
                            canStand = false;
                            break;
                        } else {

                        }
                    }
                }
            } else {
                // if the center of mass not on the line -> cant stand
                canStand = false;
            }
            if( canStand == true ){
                // if all points on the same side with the center of mass
                // and the center of mass over the line
                count++; // -> can stand on the current line
            }
        }
        return count; // return total number of lines that the polygon can stand
    }

    // method for calculating area used by centroid method
    public static double area( Point[] points) {
        int i, j, n = points.length;
        double area = 0.0;

        for (i = 0; i < n; i++) {
            j = (i + 1) % n;
            area += points[i].x * points[j].y;
            area -= points[j].x * points[i].y;
        }
        area = area / 2.0;
        return area;
    }

    // source http://paulbourke.net/geometry/polygonmesh/PolygonUtilities.java
    // method for calculating center of mass
    public static Point centroid(Point[] points) {
        double cx = 0;
        double cy = 0;
        double area = area(points);

        int i, j, n = points.length;

        double factor = 0;
        for (i = 0; i < n; i++) {
            j = (i + 1) % n;
            factor = (points[i].x * points[j].y - points[j].x * points[i].y);
            cx += (points[i].x + points[j].x) * factor;
            cy += (points[i].y + points[j].y) * factor;
        }
        area *= 6.0f;
        factor = 1 / area;
        cx *= factor;
        cy *= factor;
        return new Point(cx,cy,-1);
    }

    // check the projection of a point is orthogonal to a line
    // source https://stackoverflow.com/questions/17581738/check-if-a-point-projected-on-a-line-segment-is-not-outside-it
    public static boolean isOrthogonal(Point a, Point b, Point p) {
        double dx = b.x - a.x;
        double dy = b.y - a.y;
        double innerProduct = (p.x - a.x ) * dx + ( p.y - a.y ) * dy;
        Boolean projected = ( 0 <= innerProduct ) && ( innerProduct <= dx * dx + dy * dy );
        return projected;
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

            // array for saving the points
            Point[] points = new Point[n];

            // parse the points of the Polygon
            for (int i = 1; i <= n; i++) {
                String i_line = in.readLine();
                String[] i_line_parts = i_line.split(" ");
                Double x_i = Double.parseDouble(i_line_parts[0]);
                Double y_i = Double.parseDouble(i_line_parts[1]);
                points[i-1] = new Point(x_i,y_i,i);
            }



            String list = "" + stands(points);
            sb.append("Case #" + j + ": " + list + "\n");
        }
        System.out.println(sb.toString());
    }
}
