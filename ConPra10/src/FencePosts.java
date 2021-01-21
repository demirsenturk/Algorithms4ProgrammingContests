import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class FencePosts {

    public static Point P_min;

    public static HashSet<Integer> grahamsScan(Point[] points){
        // p ← min≺ P ---> p is first vertex of convex hull
        int p_min_index = 0;
        int y_min = points[0].y;
        for (int i = 1; i < points.length; i++) {
            int y = points[i].y;

            if ( (y < y_min) || ( y_min == y && points[i].x < points[p_min_index].x )) {
                y_min = points[i].y;
                p_min_index = i;
            }
        }
        // put the P_min to the first index
        swap(0,p_min_index,points);
        // set the P_min as global variable
        P_min = points[0];

        // create array for removing not relevant points and sorting them
        ArrayList<Point> points_l = new ArrayList<Point>(Arrays.asList(points));
        List<Point> Q = new ArrayList<Point>(sortPoints(points_l));

        // H(1) ← q1; H(2) ← q2; h ← 2
        Stack<Point> H = new Stack<Point>();
        H.push(Q.get(0));
        H.push(Q.get(1));

        // for i = 3,4,...,n do
        for (int i = 2; i < Q.size(); i++) {

            Point H_h = H.pop();
            Point h_1 = H.peek();
            Point q_i = Q.get(i);

            // CCW( H(h − 1), H(h), qi )
            int ccw = orientation(h_1, H_h, q_i);

            if( ccw == 0 ){
                // collinear
                H.push(q_i);
            } else if( ccw == 1){
                // clockwise
                i--;
            } else {
                // counterclockwise
                H.push(H_h);
                H.push(q_i);
            }
        } // end for

        H.push(Q.get(0));

        HashSet<Integer> results = new HashSet<Integer>();
        // add the results to a set
        while ( !H.isEmpty() ) {
            Point p = H.peek();
            results.add(p.i);
            H.pop();
        }
        // return the result set
        return results;
        // return (H(1), . . . , H(h))

    }

    public static Set<Point> sortPoints(List<Point> points) {

        TreeSet<Point> set = new TreeSet<Point>(new Comparator<Point>() {
            @Override
            public int compare(Point a, Point b) {

                double thetaA = Math.atan2((long)a.y - P_min.y, (long)a.x - P_min.x);
                double thetaB = Math.atan2((long)b.y - P_min.y, (long)b.x - P_min.x);
                // Sort remaining points q ̸= p by angle between line (p, q) and y-axis
                if(thetaA < thetaB) {
                    return -1;
                } else if(thetaA > thetaB) {
                    return 1;
                }
                else {
                    double distance_a = Math.sqrt((((long) P_min.x - a.x) * ((long) P_min.x - a.x)) +
                            (((long) P_min.y - a.y) * ((long) P_min.y - a.y)));
                    double distance_b = Math.sqrt((((long)P_min.x - b.x) * ((long)P_min.x - b.x)) +
                            (((long)P_min.y - b.y) * ((long)P_min.y - b.y)));
                    // if the angles same sort by distance to P_min
                    if( distance_a < distance_b ) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            }
        });

        set.addAll(points);
        return set;
    }

    // source: https://github.com/bkiers/GrahamScan/blob/master/src/main/cg/GrahamScan.java

    static class Point {

        private int x;
        private int y;
        private int i;

        public Point(int x, int y, int i) {
            this.x = x;
            this.y = y;
            this.i = i;
        }

    }

//    public static int orientation(Point p_i, Point p_j, Point r) {
//        int ccw = (p_j.y - p_i.y) * (r.x - p_j.x)
//                - (p_j.x - p_i.x) * (r.y - p_j.y);
//        if ( ccw == 0 ) {
//            // collinear
//            return 0;
//        }
//        if( ccw > 0 ){
//            // clock
//            return 1;
//        } else {
//            // counterclockwise
//            return 2;
//        }
//    }

    public static int orientation(Point p_i, Point p_j, Point r) {

        long ccw = (((long) p_j.x - p_i.x) * ((long) r.y - p_i.y)) -
                (((long) p_j.y - p_i.y) * ((long) r.x - p_i.x));

        if( ccw > 0) {
            return 2;
        } else if( ccw < 0) {
            return 1;
        } else {
            return 0;
        }
    }

    public static void swap(int p1, int p2, Point[] points) {
        Point temp = points[p1];
        points[p1] = points[p2];
        points[p2] = temp;
    }

    public static Point before_last(Stack<Point> H) {
        Point top = H.pop();
        Point h_1 = H.peek();
        H.push(top);
        return h_1;
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
                int x_i = Integer.parseInt(i_line_parts[0]);
                int y_i = Integer.parseInt(i_line_parts[1]);
                points[i-1] = new Point(x_i,y_i,i);
            }
            String list = "";
            HashSet<Integer> res = grahamsScan(points);
            ArrayList<Integer> results = new ArrayList<Integer>(res);
            // naturally sort the results
            Collections.sort(results);
            for (int i = 0; i < results.size(); i++) {
                if(i == 0){
                    list += results.get(i);
                } else {
                    list += " " + results.get(i);
                }
            }
            sb.append("Case #" + j + ": " + list + "\n");
        }
        System.out.println(sb.toString());
    }
}
