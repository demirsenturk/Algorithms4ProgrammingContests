import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Fallingwater {

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

        public void simplify(double x){
//            if( this.z == 0.0 ){
//                return;
//            }
//            this.x = this.x / this.z;
//            this.y = this.y / this.z;
//            this.z = this.z / this.z;

            double ratio = this.x / x;

            this.x = this.x / ratio;
            this.y = this.y / ratio;
            this.z = this.z / ratio;

            if( this.x < 0 && this.y < 0 ){
                this.x *= -1;
                this.y *= -1;
                this.z *= -1;
            }
        }

        public double getY(Vector x){
            Vector flow_projection = new Vector(x.x,0);
            Vector flow = flow_projection.CrossProduct(x);
            Vector cross_point = this.CrossProduct(flow);
            cross_point.simplify(x.x);
            if( cross_point.y == 17.0 ){
                System.out.println(cross_point.z);
            }
            return cross_point.y;
        }

    }

    public static class Ledge implements Comparable<Ledge>{
        private Vector p1;
        private Vector p2;
        private double maxY;
        private double miny;

        public Ledge(Vector p1, Vector p2) {
            this.p1 = p1;
            this.p2 = p2;
            this.maxY = (p1.y > p2.y) ? p1.y : p2.y;
            this.miny = (p1.y < p2.y) ? p1.y : p2.y;
        }

        public HashSet<Vector> water_fall(double x){
            HashSet<Vector> points = new HashSet<Vector>();
            if( inRange(x) == false ){
                return points;
            }
            if( p1.x == x ){
                points.add(p1);
            } else if( p2.x == x){
                points.add(p2);
            }
            if( p2.y == p1.y ){
                points.add(p2);
                points.add(p1);
                return points;
            }
            if( p2.y > p1.y ){
                points.add(p1);
            } else {
                points.add(p2);
            }
            return points;
        }

        public boolean inRange(double x){
            return (p1.x <= x && x <= p2.x) || (p2.x <= x && x <= p1.x);
        }

        @Override
        public int compareTo(Ledge o) {
            if(this.maxY == o.maxY){
                if( this.miny == o.miny ){
                    return 0;
                } else if( this.miny < o.miny ){
                    return 1;
                } else {
                    return -1;
                }
            }
            if( this.maxY < o.maxY ){
                return 1;
            } else {
                return -1;
            }
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

    public static void main(String[] args) throws IOException {
        InputStreamReader r = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(r);
        StringBuilder sb = new StringBuilder();
        int t = Integer.parseInt(in.readLine());

        for (int j = 1; j <= t; j++) {
            if (j != 1) {
                in.readLine();
            }
            // parsing n, x and y
            String i_line = in.readLine();
            String[] parts = i_line.split(" ");
            int n = Integer.parseInt(parts[0]);
            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            Vector source = new Vector(x,y);

            Ledge[] ledge_points = new Ledge[n];
            Vector[] ledges = new Vector[n];
            for (int i = 0; i < n; i++) {
                // parse ledge coordinates xi_1, yi_1, xi_2, yi_2
                String ledge_line = in.readLine();
                String[] coordinates = ledge_line.split(" ");
                Vector p1 = new Vector(Integer.parseInt(coordinates[0]),Integer.parseInt(coordinates[1]));
                Vector p2 = new Vector(Integer.parseInt(coordinates[2]),Integer.parseInt(coordinates[3]));
                ledge_points[i] = new Ledge(p1,p2);
                ledges[i] = p1.CrossProduct(p2);
//                ledges[i].simplify();
            }

//            Vector floor = new Vector(source.x,0);
//            Vector flow = source.CrossProduct(floor);
//            Vector cross = ledges[1].CrossProduct(flow);
//            cross.simplify();
//            System.out.println("x: "+ cross.x + " y: " + cross.y);

            // sort the ledges by their max y value
//            Arrays.sort(ledge_points);

            HashSet<Vector> water_split = new HashSet<Vector>();
            water_split.add(source);

            boolean changed = true;
            while( changed ) {
                changed = false;
                // water splits
                List<Vector> list = new ArrayList<Vector>(water_split);
                // iterate over each of the water split
                for (int k = 0; k < list.size(); k++) {
                    Vector current_flow = list.get(k);
                    int max_ledge = -1;
                    double max_val = Double.MIN_VALUE;
                    // find the highest ledge under the current flow
                    for (int i = 0; i < ledges.length; i++) {
                        Vector cur_ledge = ledges[i];
                        Ledge cur_ledge_points = ledge_points[i];
//                        if( i == 2 ) {
//                            Boolean b = cur_ledge_points.inRange(current_flow.x);
//                            System.out.println(current_flow.x + " " + current_flow.y + " - " + b.toString());
//                            System.out.println(cur_ledge_points.p1.x);
//                        }
                        if ( (!current_flow.equals(cur_ledge_points.p1) && !current_flow.equals(cur_ledge_points.p2) )
                                && cur_ledge_points.inRange(current_flow.x) && current_flow.y >= cur_ledge_points.miny) {
//                            double y_coordinate = cur_ledge.getY(current_flow);

                            Vector first = (new Vector(current_flow.x,0)).CrossProduct(current_flow);
//                            first.simplify();
                            Vector prod = cur_ledge.CrossProduct(first);
//                            System.out.println(prod.x + " " + prod.y + " " + prod.z);
                            prod.simplify();
                            double y_coordinate = prod.y;

//                            System.out.println(current_flow.y + " and " + y_coordinate);

                            if ( max_val < y_coordinate && current_flow.y >= y_coordinate ) {
                                max_val = y_coordinate;
                                max_ledge = i;
                            }
                        }
                    }
                    if (max_ledge != -1) {
                        Ledge cur_ledge_point = ledge_points[max_ledge];
                        water_split.remove(current_flow);
                        HashSet<Vector> new_flows = cur_ledge_point.water_fall(current_flow.x);
                        if (!new_flows.isEmpty()) {
                            Iterator<Vector> it = new_flows.iterator();
//                            System.out.println("from " + cur_ledge_point.p1.x + " " + cur_ledge_point.p1.y
//                                    + " -> " + cur_ledge_point.p2.x + " " + cur_ledge_point.p2.y);
                            while (it.hasNext()) {
                                Vector cur = it.next();
//                                System.out.println("new point " + cur.x + " " + cur.y);
                                water_split.add(cur);
                                changed = true;
                            }
                        }
                    }
                }
            }


            String result = "";
            List<Vector> vector_list = new ArrayList<Vector>(water_split);
            HashSet<Integer> results_hset = new HashSet<Integer>();
            for (int i = 0; i < vector_list.size(); i++) {
                results_hset.add((int) vector_list.get(i).x);
            }
            List<Integer> results_list = new ArrayList<Integer>(results_hset);
            Collections.sort(results_list);
            for (int i = 0; i < results_list.size(); i++) {
                int x_i = results_list.get(i);
                if( i == 0 ) {
                    result += x_i;
                } else {
                    result += " " + x_i;
                }
            }
            sb.append("Case #" + j + ": " + result + "\n");
        }
        System.out.println(sb.toString());
    }


}
