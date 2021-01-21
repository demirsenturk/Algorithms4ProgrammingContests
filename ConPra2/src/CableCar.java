import java.util.Arrays;
import java.util.Scanner;

public class CableCar {


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();
        int t = scanner.nextInt();
        scanner.nextLine();
        double absolute_error = Math.pow(10, -4);

        for (int j = 1; j <= t; j++) {
            // parsing d, p, u, v
            int d = scanner.nextInt();
            int p = scanner.nextInt();
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            // initialize the binary search interval [min,max]
            double min = 0;
            double max = d;
            while ( absolute_error < Math.abs( max - min ) ) {
                // midpoint of the search interval [min,max]
                double distance = (min + max) / 2;
                int left_spaces = (int) ( u / distance );
                // number of posts before canyon
                int left_posts = left_spaces + 1;
                if (left_posts > p) {
                    // number of spaces can not be greater than the number of points - 1
                    left_spaces = p - 1;
                }
                int right_spaces = (p - 1) - (left_spaces + 1);
                // the last post before the canyon
                double post_before_canyon = distance * left_spaces;
                // the first post before the canyon
                double post_after_canyon = d - distance * right_spaces;
                if (post_before_canyon + distance <= post_after_canyon && post_after_canyon >= v) {
                    // feasible so try to find a greater space size
                    min = distance;
                } else {
                    // not feasible so continue with a less space size
                    max = distance;
                }
            }
            sb.append("Case #" + j + ": " + min + "\n");

        }
        System.out.println(sb.toString());
        scanner.close();
    }
}
