import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Surveillance {

    static class Point {

        private double x;
        private double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

    }

    public static double distance(Point p1, Point p2) {
        // distance between two points
        double x_dist = p2.x - p1.x;
        double y_dist = p2.y - p1.y;
        return Math.hypot(x_dist, y_dist);
    }

    public static Double area( int n, double radius, double p1_radius){
        // calculate area of first camera + the other cameras with same radius
        double number_of_points = (n-1);
        double area_of_circles = number_of_points * circle_area(radius);
        double area_of_first_circle = circle_area(p1_radius);
        return area_of_circles + area_of_first_circle;
    }

    public static Double circle_area( double radius ){
        // area of a circle
        return Math.PI * radius * radius;
    }

    public static double firstDerivative( int n, double radius, double p1_radius){
        // calculate the first derivative
        double res = ( 2.0 * n * Math.PI * radius ) - ( 2.0 * Math.PI * p1_radius );
        return res;
    }

    public static double approximate(int n ,double closest_distance, double closest_distance_to_p1, Point[] points){
        // radius can be in the Intervall [0,1]
        double min_radius = 0.0;
        double max_radius = closest_distance / 2.0;
        if( closest_distance == closest_distance_to_p1
                && closest_distance_to_p1 <= closestDistanceWithoutFirst(points) ){
            if( closest_distance_to_p1 <= closestDistanceWithoutFirst(points) / 2.0 ) {
                max_radius = closest_distance_to_p1;
            } else {
                max_radius = closestDistanceWithoutFirst(points) / 2.0;
            }
        }

        double absolute_error = Math.pow(10,-6);

        // starting the binary-search by the middle of [closest_distance,closest_distance_to_p1]
        double approximate = (min_radius + max_radius) / 2.0;
        double temp_res = firstDerivative(n, approximate, closest_distance_to_p1);
        // if the absolute error < 10^-6 terminate
        while ( Math.abs( temp_res ) > absolute_error && min_radius < approximate && approximate < max_radius ){
            // if the result is greater then minimum distance, choose middle of the binary search range as the max_value of new search
            if ( temp_res < 0 ) {
                max_radius = approximate ;
            } else {
                min_radius = approximate ;
            }
            // calculate the mid again
            approximate = (min_radius + max_radius) / 2.0;
            temp_res = firstDerivative(n, approximate, closest_distance_to_p1);
        }
//        System.out.println(approximate);
//        System.out.println(closest_distance_to_p1-approximate);
//        approximate = approximate * 2;
//        System.out.println(Math.PI*approximate*approximate + Math.PI * (closest_distance_to_p1-approximate) * (closest_distance_to_p1-approximate) );
        return area( n,approximate,closest_distance_to_p1 - approximate );
    }

    public static double bruteForce(Point[] points) {
        // find the closest pairs and return their distance
        Point first = points[0];
        Point second = points[1];
        Double cur_min = distance(first, second);
        for (int i = 0; i < points.length - 1; i++) {
            Point point1 = points[i];
            for (int j = i + 1; j < points.length; j++) {
                Point point2 = points[j];
                double distance = distance(point1, point2);
                if (distance < cur_min) {
                    cur_min = distance;
                }
            }
        }
        return cur_min;
    }

    // method brute-force from https://rosettacode.org/wiki/Closest-pair_problem#Java

        public static Double closestDistanceWithoutFirst(Point[] points){
            // find the closest pairs without first camera (point) and return their distance
        Point first = points[1];
        if( points.length < 3 ){
            return 0.0;
        }
        double curmin = distance(first,points[2]);
        for (int i = 1; i < points.length; i++) {
            for (int j = 1; j < points.length; j++) {
                if( i != j ){
                    double temp = distance(points[i],points[j]);
                    if( curmin > temp ){
                        curmin = temp;
                    }
                }
            }
        }
        return curmin;
    }

//    public static Double closestDistance(Point[] points){
//        Point first = points[0];
//        Point second = points[1];
//        double curmin = distance(first,second);
//        for (int i = 0; i < points.length; i++) {
//            for (int j = 0; j < points.length; j++) {
//                if( i != j ){
//                    double temp = distance(points[i],points[j]);
//                    if( curmin > temp ){
//                        curmin = temp;
//                    }
//                }
//            }
//        }
//        return curmin;
//    }

    public static double closestDistanceToFirst(Point[] points) {
        // find the closest distance to the first camera
        Double cur_min = distance(points[0], points[1]);
        for (int i = 2; i < points.length; i++) {
            double distance = distance(points[0], points[i]);
            if (distance < cur_min) {
                cur_min = distance;
            }
        }
        return cur_min;
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
            for (int i = 0; i < n; i++) {
                String i_line = in.readLine();
                String[] i_line_parts = i_line.split(" ");
                Double x_i = Double.parseDouble(i_line_parts[0]);
                Double y_i = Double.parseDouble(i_line_parts[1]);
                points[i] = new Point(x_i,y_i);
            }
            Double closest_distance = bruteForce(points);
            Double closestDistanceToFirst = closestDistanceToFirst(points);
            Double res = approximate(n, closest_distance, closestDistanceToFirst, points);
//            System.out.println(closest_distance);
//            System.out.println(closestDistanceToFirst);
//            System.out.println(closestDistanceWithoutFirst(points));
            String result = "" + res.toString();
            sb.append("Case #" + j + ": " + result + "\n");
        }
        System.out.println(sb.toString());
    }
}
