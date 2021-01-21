import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Poker {

    static class Tournament implements Comparable<Tournament>{
        private int start;
        private int end;
        private int prize;

        public Tournament(int start, int end, int prize) {
            this.start = start;
            this.end = end;
            this.prize = prize;
        }

        @Override
        public int compareTo(Tournament o) {
            return this.end - o.end;
        }
    }

    static int knapsack(  Tournament[] tournaments) {

        int last_date = tournaments[tournaments.length-1].end;
        int[] money = new int[last_date+1];


        for (int j = 0; j <= last_date; j++) {
            if(j != 0 ){
                money[j] = money[j-1];
            } else {
                money[j] = 0;
            }
            for (int i = 0; i < tournaments.length; i++) {
                if( tournaments[i].end > j ){
                    break;
                }
                if( tournaments[i].end == j ){
                    int start = tournaments[i].start;
                    int prize = tournaments[i].prize;
                    money[j] = Math.max( money[start-1] + prize , money[j] );
                }
            }
        }

        return money[last_date];
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

            // parsing n and c
            int n = Integer.parseInt(in.readLine());

            Tournament[] tournaments = new Tournament[n];

            for (int i = 0; i < n; i++) {
                String values_line = in.readLine();
                String[] string_values = values_line.split(" ");
                int a = Integer.parseInt(string_values[0]);
                int b = Integer.parseInt(string_values[1]);
                int p = Integer.parseInt(string_values[2]);
                tournaments[i] = new Tournament(a,b,p);
            }

            Arrays.sort(tournaments);

            String result = "" + knapsack(tournaments);
            sb.append("Case #" + j + ": " + result + "\n");
        }
        System.out.println(sb.toString());
    }


}
