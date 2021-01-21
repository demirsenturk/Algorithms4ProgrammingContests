import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class AzrieliTowers {

    static class Point {

        private int x;
        private int y;
        private int id;

        public Point(int x, int y,int id) {
            this.x = x;
            this.y = y;
            this.id = id;
        }

    }

    // check if any line of rectangle or triangle intersects
    public static Boolean triangle_rectangle_intersect( ArrayList<Point> triangle, Point[] rectangle ){
        Boolean intersect = false;
        for (int i = 0; i < 3; i++) {
            Point t1;
            Point t2;
            if( i == 2 ){
                t1 = triangle.get(i);
                t2 = triangle.get(0);
            } else {
                t1 = triangle.get(i);
                t2 = triangle.get(i+1);
            }
            for (int j = 0; j < 4; j++) {
                Point r1;
                Point r2;
                if( j == 3 ){
                    r1 = rectangle[j];
                    r2 = rectangle[0];
                } else {
                    r1 = rectangle[j];
                    r2 = rectangle[j+1];
                }
                if( doIntersect(t1,t2,r1,r2) ){
                    return true;
                }
            }
        }
        return intersect;
    }

    // pseudo code adjusted from source
    // https://stackoverflow.com/questions/2303278/find-if-4-points-on-a-plane-form-a-rectangle/2304031
    public static boolean IsOrthogonal(Point a, Point b, Point c) {
        return (b.x - a.x) * (b.x - c.x) + (b.y - a.y) * (b.y - c.y) == 0;
    }

    public static boolean IsRectangle(Point a, Point b, Point c, Point d) {
        return IsOrthogonal(a, b, c) && IsOrthogonal(b, c, d) && IsOrthogonal(c, d, a);
    }

    public static Point[] IsRectangleAnyOrder(Point a, Point b, Point c, Point d) {
        if( IsRectangle(a, b, c, d) ){
            // if the rectangle can be built in this order then return this order
            return new Point[]{a,b,c,d};
        } else if( IsRectangle(b, c, a, d) ){
            // if the rectangle can be built in this order then return this order
            return new Point[]{b, c, a, d};
        } else if( IsRectangle(c, a, b, d) ){
            // if the rectangle can be built in this order then return this order
            return new Point[]{c, a, b, d};
        }
        // if rectangle can not be formed with this points return null
        return null;
    }

    static double area(int x1, int y1, int x2, int y2, int x3, int y3) {
        return Math.abs((x1 * (y2-y3) + x2 * (y3-y1)+ x3 * (y1-y2)) /2.0 );
    }

    // source adjusted from
    // source https://www.geeksforgeeks.org/check-whether-a-given-point-lies-inside-a-triangle-or-not/?ref=lbp
    /* A function to check whether point P(x, y) lies
       inside the triangle formed by p1, p2 and p3 */
    static boolean isInsideTriangle(Point p1, Point p2, Point p3, Point p) {
        /* Calculate area of triangle ABC */
        double A = area (p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);

        /* Calculate area of triangle PBC */
        double A1 = area (p.x, p.y, p2.x, p2.y, p3.x, p3.y);

        /* Calculate area of triangle PAC */
        double A2 = area (p1.x, p1.y, p.x, p.y, p3.x, p3.y);

        /* Calculate area of triangle PAB */
        double A3 = area (p1.x, p1.y, p2.x, p2.y, p.x, p.y);

        /* Check if sum of A1, A2 and A3 is same as A */
        return (A == A1 + A2 + A3);
    }

    // combination method adjusted from
    // source https://hmkcode.com/calculate-find-all-possible-combinations-of-an-array-using-java/
    public static String Rectangle_and_Triangle(Point[]  elements, int k){
        int N = elements.length;

        Point[] test = new Point[4];

        if(k > N){
            return "impossible\n";
        }

        // init combination index array
        int pointers[] = new int[k];


        int r = 0; // index for combination array
        int i = 0; // index for elements array

        while(r >= 0){

            // forward step if i < (N + (r-K))
            if(i <= (N + (r - k))){
                pointers[r] = i;

                // if combination array is full print and increment i;
                if(r == k-1){
                    for (int j = 0; j < 4; j++) {
                        // Rectangle corner coordinates as array
                        test[j] = elements[pointers[j]];
                    }
                    Point[] buildsRectangle = IsRectangleAnyOrder(test[0], test[1], test[2], test[3]);
                    // if the 4 points build a rectangle return them in order
                    // if not return null
                    if( buildsRectangle != null ){
                        ArrayList<Point> triangle_candidates = new ArrayList<Point>();
                        // take the other points to form a triangle
                        for (int j = 0; j < elements.length; j++) {
                            if( !elements[j].equals(buildsRectangle[0]) && !elements[j].equals(buildsRectangle[1])
                                    && !elements[j].equals(buildsRectangle[2]) && !elements[j].equals(buildsRectangle[3])){
                                // if the points is not one of the points of rectangle -> candidate for forming triangle
                                triangle_candidates.add(elements[j]);
                            }
                        }

                        Point[] points_arr = new Point[triangle_candidates.size()];
                        points_arr = triangle_candidates.toArray(points_arr);

                        ArrayList<ArrayList<Point>> triangles = TriangleCombination(points_arr, 3);
                        // make the combinations of array size -> 3
                        // candidates for triangle
                        for (int j = 0; j < triangles.size(); j++) {
                            if( triangles.get(j).size() == 3 ){
                                // Points of triangles
                                Point t1 = triangles.get(j).get(0);
                                Point t2 = triangles.get(j).get(1);
                                Point t3 = triangles.get(j).get(2);
                                // Points of rectangle
                                Point r1 = buildsRectangle[0];
                                Point r2 = buildsRectangle[1];
                                Point r3 = buildsRectangle[2];
                                Point r4 = buildsRectangle[3];
                                if( !isInsideTriangle(t1,t2,t3,buildsRectangle[0])
                                        && !isInsideTriangle(t1,t2,t3,buildsRectangle[1])
                                        && !isInsideTriangle(t1,t2,t3,buildsRectangle[2])
                                        && !isInsideTriangle(t1,t2,t3,buildsRectangle[3])
                                        && !checkInsideRectangle(r1,r2,r3,r4,t1)
                                        && !checkInsideRectangle(r1,r2,r3,r4,t2)
                                        && !checkInsideRectangle(r1,r2,r3,r4,t3) ){
                                    // check if a point of rectangle inside a triangle
                                    // and if a point of triangle is inside of rectangle
                                    Boolean overlapped = triangle_rectangle_intersect( triangles.get(j), buildsRectangle );
                                    // check any of the lines of triangle and rectangle overlaps
                                    if( overlapped == false ){
                                        // if not pass the results
                                        StringBuilder buildings = new StringBuilder();
                                        buildings.append("possible\n");
                                        for (int l = 0; l < buildsRectangle.length; l++) {
                                            // pass the points of Rectangle
                                            buildings.append(buildsRectangle[l].x + " " + buildsRectangle[l].y + "\n");
                                        }
                                        for (int l = 0; l < triangles.get(j).size(); l++) {
                                            // pass the points of triangle
                                            buildings.append(triangles.get(j).get(l).x + " " + triangles.get(j).get(l).y + "\n" );
                                        }
                                        return buildings.toString();
                                    }
                                }
                            }
                        }
                    }
                    i++;
                } else{
                    // if combination is not full yet, select next element
                    i = pointers[r] + 1;
                    r++;
                }
            } else {
                // backward step
                r--;
                if(r >= 0)
                    i = pointers[r] + 1;

            }
        }
        return "impossible\n";
    }

    // combination method adjusted from
    // source https://hmkcode.com/calculate-find-all-possible-combinations-of-an-array-using-java/
    public static ArrayList<ArrayList<Point>> TriangleCombination(Point[]  elements, int k){
        ArrayList<ArrayList<Point>> triangles = new ArrayList<ArrayList<Point>>();

        int N = elements.length;

        if(k > N){
            System.out.println("Invalid input, K > N");
            return null;
        }

        // init combination index array
        int pointers[] = new int[k];

        int r = 0; // index for combination array
        int i = 0; // index for elements array

        while(r >= 0){
            // forward step if i < (N + (r-K))
            if(i <= (N + (r - k))){
                pointers[r] = i;
                // if combination array is full print and increment i;
                if(r == k-1){
                    ArrayList<Point> triangle = new ArrayList<Point>();
                    for (int j = 0; j < 3; j++) {
                        // Triangle corner coordinates as array
                        triangle.add(elements[pointers[j]]);
                    }
                    triangles.add(triangle);
                    i++;
                } else{
                    // if combination is not full yet, select next element
                    i = pointers[r] + 1;
                    r++;
                }
            }
            // backward step
            else{
                r--;
                if(r >= 0)
                    i = pointers[r]+1;
            }
        }
        return triangles;
    }

    // source   https://www.geeksforgeeks.org/check-if-two-given-line-segments-intersect/
    static boolean onSegment(Point p, Point q, Point r) {
        if (q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) &&
                q.y <= Math.max(p.y, r.y) && q.y >= Math.min(p.y, r.y)) {
            return true;
        }

        return false;
    }

    static int orientation(Point p, Point q, Point r) {
        int val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);

        if (val == 0) {
            return 0;
        } // colinear

        return (val > 0)? 1: 2; // clock or counterclock wise
    }

    // The main function that returns true if line segment 'p1q1' and 'p2q2' intersect.
    static boolean doIntersect(Point p1, Point q1, Point p2, Point q2) {
        // Find the four orientations needed for general and special cases
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        if (o1 != o2 && o3 != o4) {
            return true;
        }

        // p1, q1 and p2 are colinear and p2 lies on segment p1q1
        if (o1 == 0 && onSegment(p1, p2, q1)){
            return true;
        }

        // p1, q1 and q2 are colinear and q2 lies on segment p1q1
        if (o2 == 0 && onSegment(p1, q2, q1)){
            return true;
        }

        // p2, q2 and p1 are colinear and p1 lies on segment p2q2
        if (o3 == 0 && onSegment(p2, p1, q2)){
            return true;
        }

        // p2, q2 and q1 are colinear and q1 lies on segment p2q2
        if (o4 == 0 && onSegment(p2, q1, q2)){
            return true;
        }

        return false; // Doesn't fall in any of the above cases
    }

    // check a point inside rectangle
    static Double area2(int x1, int y1, int x2, int y2, int x3, int y3) {
        return (Double) Math.abs((x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2)) / 2.0);
    }

    // source https://www.geeksforgeeks.org/check-whether-given-point-lies-inside-rectangle-not/
    /* A function to check whether point P
    lies inside the rectangle formed by p1, p2, p3 and p4 */
    static boolean checkInsideRectangle( Point p1, Point p2, Point p3, Point p4, Point p) {
        /* Calculate area of rectangle ABCD */
        Double A = area2( p1.x, p1.y, p2.x, p2.y, p3.x, p3.y)+
                area2(p1.x, p1.y, p4.x, p4.y, p3.x, p3.y);

        /* Calculate area of triangle PAB */
        Double A1 = area2(p.x, p.y, p1.x, p1.y, p2.x, p2.y);

        /* Calculate area of triangle PBC */
        Double A2 = area2(p.x, p.y, p2.x, p2.y, p3.x, p3.y);

        /* Calculate area of triangle PCD */
        Double A3 = area2(p.x, p.y, p3.x, p3.y, p4.x, p4.y);

        /* Calculate area of triangle PAD */
        Double A4 = area2(p.x, p.y, p1.x, p1.y, p4.x, p4.y);

        /* Check if sum of A1, A2, A3 and A4 is same as A */
        return (A == A1 + A2 + A3 + A4);
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

            // parse the points
            for (int i = 0; i < n; i++) {
                String i_line = in.readLine();
                String[] i_line_parts = i_line.split(" ");
                int x_i = Integer.parseInt(i_line_parts[0]);
                int y_i = Integer.parseInt(i_line_parts[1]);
                points[i] = new Point(x_i,y_i,i);
            }


            String result = Rectangle_and_Triangle(points, 4);
            sb.append("Case #" + j + ": " + result);
        }
        System.out.println(sb.toString());
    }
}
