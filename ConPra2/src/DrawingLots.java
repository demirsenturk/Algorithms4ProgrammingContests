import java.util.Scanner;

public class DrawingLots {

    public static double calculator(int[] prices, int b, double p){
        // calculates the expected total payoff
        double result = 0;
        double power_of_p = 1;
        for (int i = 0; i < prices.length; i++) {
            // calculates i'th power of p
            power_of_p = power_of_p * p;
            result += (prices[i] * power_of_p);
        }
        result = result - b;
        return result;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();
        int t = scanner.nextInt();
        scanner.nextLine();
        double absolute_error = Math.pow(10,-6);

        for (int j = 1; j <= t; j++) {
            // parsing n and b
            int n = scanner.nextInt();
            int b = scanner.nextInt();
            // parsing prices
            int[] prices = new int[n];
            for (int i = 0; i < n; i++) {
                prices[i] = scanner.nextInt();
            }
            // probability can be in the Intervall [0,1]
            double min_prob = 0;
            double max_prob = 1;
            // starting the binary-search by the middle of [0,1] e.g. 0.5
            double approximate = (min_prob + max_prob) / 2;
            double temp_res = calculator(prices, b, approximate);
            // if the absolute error < 10^-6 terminate
            while ( Math.abs( temp_res ) > absolute_error && min_prob < approximate && approximate < max_prob ){
                // if the result is greater then 0, choose middle of the binary search range as the max_value of new search
                if (temp_res > 0) {
                    max_prob = approximate ;
                } else {
                    min_prob = approximate ;
                }
                // calculate the mid again
                approximate = (min_prob + max_prob) / 2;
                temp_res = calculator(prices, b, approximate);
            }
            sb.append("Case #" + j + ": " + approximate + "\n");
        }
        System.out.println(sb.toString());
        scanner.close();
    }

}
