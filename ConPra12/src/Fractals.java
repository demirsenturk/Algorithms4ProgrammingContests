import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Fractals {

    public static double[] rotate(double[] point, double[][] rotate_matrix){
        double[] rotated_point = new double[3];
        // rotation matrix * (x,y,1)^T
        matrixMultiplication(rotate_matrix,point,rotated_point);
        return rotated_point;
    }

    public static double[][] rotateMatrix( int angle ){
        // calculate rotation matrix
        double[][] m = new double[3][3];
        // convert degree to radian
        double radians = Math.toRadians(angle);
        // first column
        m[0][0] = Math.cos(radians);
        m[1][0] = Math.sin(radians);
        m[2][0] = 0;
        // second column
        m[0][1] = -Math.sin(radians);
        m[1][1] = Math.cos(radians);
        m[2][1] = 0;
        // In projective space adjust the 2x2 matrix as 3x3
        m[0][2] = 0;
        m[1][2] = 0;
        m[2][2] = 1;
        return m;
    }

    // Method for multiplication of a (nxn) Matrix with a (1xn) vector
    public static void matrixMultiplication(double[][] m, double[] vector, double[] product ){
        for (int i = 0; i < m.length; i++) {
            product[i] = 0;
            for (int j = 0; j < m[0].length; j++) {
                product[i] += m[i][j] * vector[j];
            }
        }
    }

    public static void main(String[] args) throws IOException {
        InputStreamReader r = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(r);
        StringBuilder sb = new StringBuilder();
        int t = Integer.parseInt(in.readLine());

        for (int j = 1; j <= t; j++) {
            if (j != 1) {
                in.readLine(); // space between inputs
            }
            // parsing n,d and a
            String i_line = in.readLine();
            String[] parts = i_line.split(" ");
            int n = Integer.parseInt(parts[0]);
            int d = Integer.parseInt(parts[1]);
            int a = Integer.parseInt(parts[2]);
            String s = parts[3];

            // parse productions
            HashMap<Character,String> productions = new HashMap<Character,String>();
            for (int i = 0; i < n; i++) {
                String production_line = in.readLine();
                String[] production = production_line.split("=>");
                productions.put(production[0].charAt(0),production[1]);
            }

            // start angle
            int direction_angle = 0;
            // start point
            Double[] cur_point = {0.0,0.0,1.0};
            // unit vector with length 1 meter
            double[] one_meter = {1.0, 0.0, 1.0};
            // matrix for rotating a vector
            double[][] direction_matrix = rotateMatrix(direction_angle);
            double[] path = rotate(one_meter, direction_matrix);;

            sb.append("Case #" + j + ":\n");

            // repeats this process d times
            for (int i = 0; i < d; i++) {
                int length = 0;
                for (int k = 0; k < s.length(); k++) {
                    char c = s.charAt(k);
                    if( c == '+' || c == '-' ){
                        // “+” and “-” are not replaced
                    } else {
                        // substring before the current char
                        String first = "";
                        first = s.substring(0, k);
                        // substring after the current char
                        String second = "";
                        second = s.substring(k + 1);
                        // remove char and replace it with the production
                        String toReplace = productions.get(c);
                        s = first + toReplace + second;
                        // update length and k for further processing in for loop
                        length = toReplace.length() - 1;
                        k += length;
                    }
                }
            }
            // add the start point
            sb.append(cur_point[0].toString() + " " + cur_point[1].toString() + "\n");
            for (int k = 0; k < s.length(); k++) {
                char c = s.charAt(k);
                if( c == '+' ){
                    // turns a degrees to the left
                    direction_angle = (direction_angle + a) % 360;
                    // calculate the matrix for rotation
                    direction_matrix = rotateMatrix(direction_angle);
                    // rotate unit matrix to the current angle
                    path = rotate(one_meter, direction_matrix);

                } else if ( c == '-' ){
                    // turns a degrees to the right
                    direction_angle = direction_angle - a;
                    if( direction_angle < 0 ){
                        direction_angle = (direction_angle + 360) % 360;
                    }
                    // calculate the matrix for rotation
                    direction_matrix = rotateMatrix(direction_angle);
                    // rotate unit matrix to the current angle
                    path = rotate(one_meter, direction_matrix);

                } else {
                    // go along the current direction/angle
                    cur_point[0] += path[0];
                    cur_point[1] += path[1];
                    // print current point
                    String x = cur_point[0].toString();
                    String y = cur_point[1].toString();
                    sb.append(x + " " + y + "\n");
                }
            }

            String result = "" + s;
        }
        System.out.println(sb.toString());
    }
}
