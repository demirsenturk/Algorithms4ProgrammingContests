import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AlarmClock {

    static class BruteForce{
        private List<Moment>  solutions;
        private int[][] observations;

        public BruteForce(int[][] observations) {
            this.solutions = new ArrayList<Moment>();
            this.observations = observations;
        }
        
        public List<Moment> solveBruteForce(){

            for (int i = 0; i < 24 * 60; i++) {
                // consider all possible times
                Moment t = new Moment(i);
                Clock c = new Clock();

                for (int j = 0; j < observations.length; j++) {
                    int time_passed = (i + j) % (24 * 60);
                    // j minutes after the start time
                    c.update( new Moment(time_passed), observations[j] );
                    // the broken digits are investigated
                }

                if ( c.valid() ) {
                    // add if the current time is valid
                    solutions.add(t);
                }

            }
            return solutions;
        }

        static class Clock {

            digit_of_clock[] digits;

            public Clock() {
                // 4 digits of the clock
                digits = new digit_of_clock[4];
                digits[0] = new digit_of_clock();
                digits[1] = new digit_of_clock();
                digits[2] = new digit_of_clock();
                digits[3] = new digit_of_clock();
            }

            public void update(Moment current, int[] seen) {
                for (int i = 0; i < 4; i++) {
                    digits[i].consider_digit(current.digits[i], seen[i]);
                }
            }

            public boolean valid() {
                for (int i = 0; i < 4; i++)
                    if (!digits[i].possible()) {
                        // if one digit contradicts
                        return false;
                        // the answer is not theoretically possible
                    }
                return true;
            }
        }

        static class digit_of_clock{
            int working;
            int damaged;

            public digit_of_clock() {
                this.working = 0;
                this.damaged = 0;
            }

            private int code(int digit) {
                // the digits are visualized bitwise by seven segments
                // starting from the upper segment as 0 and then clockwise
                // the middle segment is the last (6)
                if(digit == 0 ){
                    return 0b1111110;
                } else if (digit == 1 ){
                    return 0b0110000;
                } else if (digit == 2 ){
                    return 0b1101101;
                } else if (digit == 3 ){
                    return 0b1111001;
                } else if (digit == 4 ){
                    return 0b0110011;
                } else if (digit == 5 ){
                    return 0b1011011;
                } else if (digit == 6 ){
                    return 0b1011111;
                } else if (digit == 7 ){
                    return 0b1110000;
                } else if (digit == 8 ){
                    return 0b1111111;
                } else if (digit == 9 ){
                    return 0b1111011;
                }
                return -1;
            }

            public void consider_digit(int current, int seen) {
                // number of lighting segments
                int realtime_segcode = code(current);
                // the segment code of the current time
                int seen_time_segcode = code(seen);
                // the segment code of the time seen in the clock
                working = working | seen_time_segcode;
                // number of not lighting segments ( damaged )
                damaged = damaged | ( realtime_segcode ^ seen_time_segcode );
            }

            public boolean possible() {
                if( (working & damaged) == 0){
                    return true;
                }
                return false;
            }

        }
        
    }

    static class Moment{
        int digit1;
        int digit2;
        int digit3;
        int digit4;
        int t;
        int[] digits;

        public Moment(int seconds) {
            digits = new int[4];
            int hours = seconds / 60;
            // hours of the time considered
            this.digit1 = hours / 10;
            // first digit
            this.digit2 = hours % 10;
            // second digit
            this.digit3 = (seconds / 10) % 6;
            // minutes of the time ( first digit )
            this.digit4 = seconds % 10;
            // minutes of the time ( second digit )
            digits[0] = digit1;
            digits[1] = digit2;
            digits[2] = digit3;
            digits[3] = digit4;
            this.t = seconds;
        }

        public String toString() {
            return "" + digit1 + "" + digit2 + ":" + digit3 + "" + digit4;
        }

    }

    public static void main(String[] args) throws IOException {
        InputStreamReader r = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(r);
        StringBuilder sb = new StringBuilder();
        int t = Integer.parseInt(in.readLine());
        // parsing test cases t
        for (int j = 1; j <= t; j++) {
            if (j != 1) {
                in.readLine();
            }
            // parsing n
            int n = Integer.parseInt(in.readLine());
            int[][] watches = new int[n][4];

            for (int i = 0; i < n; i++) {
                String line = in.readLine();
                // parsing digits
                watches[i][0] = Character.getNumericValue(line.charAt(0));
                watches[i][1] = Character.getNumericValue(line.charAt(1));
                watches[i][2] = Character.getNumericValue(line.charAt(3));
                watches[i][3] = Character.getNumericValue(line.charAt(4));
            }

            BruteForce bt = new BruteForce(watches);
            List<Moment> possible_start_times = bt.solveBruteForce();
            sb.append("Case #" + j + ":\n");
            if (possible_start_times.isEmpty()) {
                // no possible start time
                sb.append("none\n");
            } else {
                // theoretically possible times
                for (int i = 0; i < possible_start_times.size(); i++) {
                    sb.append(possible_start_times.get(i) + "\n");
                }
            }
        }
        System.out.println(sb.toString());
    }

}
