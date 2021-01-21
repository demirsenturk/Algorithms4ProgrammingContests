import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class EulerLine {

    public static class Vector{
        private double x;
        private double y;
        private double z;

        public Vector(double x, double y) {
            this.x = x;
            this.y = y;
            this.z = 1;
        }

        public Vector(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Vector CrossProduct(Vector v) {
            double v_x = this.y * v.z - this.z * v.y;
            double v_y = this.z * v.x - this.x * v.z;
            double v_z = x * v.y - this.y * v.x;
            Vector product = new Vector(v_x, v_y ,v_z);
//            product.simplify();
            return product;
            // AxB = (AyBz − AzBy, AzBx − AxBz, AxBy − AyBx)
        }

        public void simplify(){
            // simplify the vector by z coordinates
            if( this.z == 0.0 ){
                return;
            }
            this.x = this.x / this.z;
            this.y = this.y / this.z;
            this.z = this.z / this.z;
        }

    }

    public static Vector perpendicular(Vector line, Vector p){
        Vector q = line.CrossProduct(new Vector(0,0,1));
        // q = l × (0,0,1)T
        q = new Vector(q.y,-q.x,0);
        // Orthogonal direction to q : q = (y,−x,0) .Then m = p × q.
        Vector m = p.CrossProduct(q);
        return m;
    }

    public static Vector midpoint(Vector p1, Vector p2){
        // find the midpoint of a line
        double midpoint_x = (p1.x + p2.x) / 2;
        double midpoint_y = (p1.y + p2.y) / 2;
        Vector mid = new Vector(midpoint_x,midpoint_y);
        return mid;
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
            sb.append("Case #" + j + ":\n");

            Vector[] triangle = new Vector[3];
            // parsing 3 points of triangle ( p0, p1, p2 )
            for (int i = 0; i < 3; i++) {
                String i_line = in.readLine();
                String[] parts = i_line.split(" ");
                double x = Double.parseDouble(parts[0]);
                double y = Double.parseDouble(parts[1]);
                triangle[i] = new Vector(x,y);
            }

            // midpoint of line p0 and p1
            Vector midpoint1 = midpoint(triangle[0],triangle[1]);
            // midpoint of line p1 and p2
            Vector midpoint2 = midpoint(triangle[1],triangle[2]);

            // median from p2 to midpoint 1
            Vector line_p2_mid1 = triangle[2].CrossProduct(midpoint1);
            // median from p0 to midpoint 2
            Vector line_p0_mid2 = triangle[0].CrossProduct(midpoint2);

            // the intersection of the median is the centroid
            Vector centroid = line_p0_mid2.CrossProduct(line_p2_mid1);
            centroid.simplify();
            sb.append(centroid.x + " " + centroid.y + "\n");

            // line segment between p0 and p1
            Vector line_p0_p1 = triangle[0].CrossProduct(triangle[1]);
            // line segment between p1 and p2
            Vector line_p1_p2 = triangle[1].CrossProduct(triangle[2]);
            // altitude from p0 to line p1_p2
            Vector altitude_p0_line_p1_p2 = perpendicular(line_p1_p2,triangle[0]);
            // altitude from p2 to line p0_p1
            Vector altitude_p2_line_p0_p1 = perpendicular(line_p0_p1,triangle[2]);
            // the intersection of the altitudes is the orthocenter
            Vector orthocenter = altitude_p0_line_p1_p2.CrossProduct(altitude_p2_line_p0_p1);
            orthocenter.simplify();
            sb.append(orthocenter.x + " " + orthocenter.y + "\n");

            // line segment orthogonal to the line p0_p1 crossing midpoint
            Vector orthogonal_p0_p1 = perpendicular(line_p0_p1,midpoint1);
            // line segment orthogonal to the line p1_p2 crossing midpoint
            Vector orthogonal_p1_p2 = perpendicular(line_p1_p2,midpoint2);
            // the intersection of the orthogonal is the circumcenter
            Vector circumcenter = orthogonal_p0_p1.CrossProduct(orthogonal_p1_p2);
            circumcenter.simplify();
            sb.append(circumcenter.x + " " + circumcenter.y + "\n");
        }
        System.out.println(sb.toString());
    }
}
