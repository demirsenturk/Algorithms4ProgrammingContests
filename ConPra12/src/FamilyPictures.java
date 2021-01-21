import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FamilyPictures {

    public static void calculateM(double[][] abc, double[] d, double[][] result){
        double[][] inverse_abc = new double[3][3];
        // calculate the inverse of the ( a | b | c ) matrix
        invert(abc,inverse_abc);
        // initialize (λa,λb,λc)^T
        double[] lambda_abc = new double[3];
        // ( a | b | c )^(-1) * (d) = (λa,λb,λc)^T
        matrixMultiplication(inverse_abc, d, lambda_abc);

        // calculate Matrix M = (λaa′ | λbb′ | λcc′)
        for (int i = 0; i < 3; i++) {
            for (int k = 0; k < 3; k++) {
                result[k][i] = abc[k][i] * lambda_abc[i];
            }
        }
    }

    public static void backProjection( double[][] M, double[] projected, double[] point ){
        matrixMultiplication(M,projected,point);
        // simplify the vector by making coordinate z = 1
        point[0] = point[0] / point[2];
        point[1] = point[1] / point[2];
        point[2] = point[2] / point[2];
    }

    // Method for multiplication of a (nxn) Matrix with a (1xn) vector
    public static void matrixMultiplication(double[][] m, double[] vector, double[] product ){
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                product[i] += m[i][j] * vector[j];
            }
        }
    }

    // method adjusted from source: https://www.thejavaprogrammer.com/java-program-find-inverse-matrix/
    public static void invert(double mat[][], double[][] inverse){
        int i, j;
        Double det = 0.0;

        for(i = 0; i < 3; i++) {
            det = det + (mat[0][i] * (mat[1][(i + 1) % 3] * mat[2][(i + 2) % 3] - mat[1][(i + 2) % 3] * mat[2][(i + 1) % 3]));
        }

        for(i = 0; i < 3; ++i) {
            for(j = 0; j < 3; ++j) {
                inverse[i][j] = (((mat[(j + 1) % 3][(i + 1) % 3] * mat[(j + 2) % 3][(i + 2) % 3]) - (mat[(j + 1) % 3][(i + 2) % 3] * mat[(j + 2) % 3][(i + 1) % 3])) / det);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        InputStreamReader r = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(r);
        StringBuilder sb = new StringBuilder();
        int t = Integer.parseInt(in.readLine());

        // Fixed a,b,c and d points for achieving special case of transformation
        // Fix λd = 1
        double[][] abc = new double[3][3];
        double[] d = new double[3];
        // lower-left corner at (0,0)
        abc[0][0] = 0;
        abc[1][0] = 0;
        abc[2][0] = 1;
        // lower-right corner at (1,0)
        abc[0][1] = 1;
        abc[1][1] = 0;
        abc[2][1] = 1;
        // upper-right corner at (1,1)
        abc[0][2] = 1;
        abc[1][2] = 1;
        abc[2][2] = 1;
        // upper-left corner at (0,1)
        d[0] = 0;
        d[1] = 1;
        d[2] = 1;

        for (int j = 1; j <= t; j++) {
            // parsing 12 points
            String i_line = in.readLine();
            String[] parts = i_line.split(" ");

            double[][] m = new double[3][3];
            double[] d_prime = new double[3];

            Double a_x = Double.parseDouble(parts[0]);
            Double a_y = Double.parseDouble(parts[1]);
            // vector a'
            m[0][0] = a_x;
            m[1][0] = a_y;
            m[2][0] = 1;


            Double b_x = Double.parseDouble(parts[2]);
            Double b_y = Double.parseDouble(parts[3]);
            // vector b'
            m[0][1] = b_x;
            m[1][1] = b_y;
            m[2][1] = 1;

            Double c_x = Double.parseDouble(parts[4]);
            Double c_y = Double.parseDouble(parts[5]);
            // vector c'
            m[0][2] = c_x;
            m[1][2] = c_y;
            m[2][2] = 1;

            Double d_x = Double.parseDouble(parts[6]);
            Double d_y = Double.parseDouble(parts[7]);
            // vector d'
            d_prime[0] = d_x;
            d_prime[1] = d_y;
            d_prime[2] = 1;

            // calculate M1
            double[][] M1 = new double[3][3];
            calculateM(m,d_prime,M1);

            // calculate M2
            double[][] M2 = new double[3][3];
            calculateM(abc,d,M2);

            // calculate inverse M1
            double[][] inverseM1 = new double[3][3];
            invert(M1,inverseM1);

            // calculate M^(-1) = M2 * M1^(-1), which is the inverse transformation matrix
            double[][] M_product = new double[3][3];
            for(int i=0; i < 3; i++){
                for(int jj=0; jj < 3; jj++){
                    M_product[i][jj] = 0.0;
                    for(int k=0; k<3; k++) {
                        M_product[i][jj] += M2[i][k] * inverseM1[k][jj];
                    }
                }
            }

            // parse the projection point of Lea
            Double x5 = Double.parseDouble(parts[8]);
            Double y5 = Double.parseDouble(parts[9]);
            double[] lea_projection = new double[3];
            // lea's projection vector v5'
            lea_projection[0] = x5;
            lea_projection[1] = y5;
            lea_projection[2] = 1;

            double[] lea_real = new double[3];
            // v5' * M^(-1) = v5 lea's real point in the picture
            backProjection(M_product,lea_projection,lea_real);

            // parse the projection point of the clock
            Double x6 = Double.parseDouble(parts[10]);
            Double y6 = Double.parseDouble(parts[11]);
            double[] clock_projection = new double[3];
            // clocks projection vector v6'
            clock_projection[0] = x6;
            clock_projection[1] = y6;
            clock_projection[2] = 1;

            double[] clock_real = new double[3];
            // v6' * M^(-1) = v6 clock's real point in the picture
            backProjection(M_product,clock_projection,clock_real);

            // calculate height as |y1 - 0|
            double clock_height = Math.abs(clock_real[1]);
            double lea_height = Math.abs(lea_real[1]);

            double ratio = lea_height / clock_height;

            String result = "" + ratio;
            sb.append("Case #" + j + ": " + result + "\n");
        }
        System.out.println(sb.toString());
    }
}
