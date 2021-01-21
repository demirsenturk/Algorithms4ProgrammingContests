import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Swordfighting {


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

    public static int ccw(Vector p_i, Vector p_j, Vector r) {

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



    public static void main(String[] args) throws IOException {
        InputStreamReader r = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(r);
        StringBuilder sb = new StringBuilder();
        int t = Integer.parseInt(in.readLine());

        for (int j = 1; j <= t; j++) {
            // parsing 12 points
            String i_line = in.readLine();
            String[] parts = i_line.split(" ");
            double x = Integer.parseInt(parts[0]);
            double y = Integer.parseInt(parts[1]);
            Vector v1 = new Vector(x,y);

            x = Integer.parseInt(parts[2]);
            y = Integer.parseInt(parts[3]);
            Vector v2 = new Vector(x,y);

            x = Integer.parseInt(parts[4]);
            y = Integer.parseInt(parts[5]);
            Vector v3 = new Vector(x,y);

            x = Integer.parseInt(parts[6]);
            y = Integer.parseInt(parts[7]);
            Vector v4 = new Vector(x,y);

            x = Integer.parseInt(parts[8]);
            y = Integer.parseInt(parts[9]);
            Vector v5 = new Vector(x,y);

            x = Integer.parseInt(parts[10]);
            y = Integer.parseInt(parts[11]);
            Vector v6 = new Vector(x,y);

            Vector crossguard1 = v1.CrossProduct(v2);

            Vector crossguard2 = v4.CrossProduct(v5);

            Vector sword1 = perpendicular(crossguard1,v3);
            Vector sword2 = perpendicular(crossguard2,v6);

            Vector crush = sword1.CrossProduct(sword2);
            crush.simplify();

            String result;
            if( crush.z == 0.0 ){
                result = "strange";
//                System.out.println(crush.x + " - " + crush.y + " - " + crush.z);
            } else {
                int orientation_of_hild1 = ccw(v1,v2,v3);
                int orientation_of_crush1 = ccw(v1,v2,crush);

                int orientation_of_hild2 = ccw(v4,v5,v6);
                int orientation_of_crush2 = ccw(v4,v5,crush);
                Boolean strange = orientation_of_hild1 == orientation_of_crush1
                        || orientation_of_hild2 == orientation_of_crush2;
                if( strange ) {
                    result = "strange";
                } else {
                    result = crush.x + " " + crush.y;
                }
            }
            sb.append("Case #" + j + ": " + result + "\n");
        }
        System.out.println(sb.toString());
    }
}
