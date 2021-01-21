import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Woodchucking {

    public static int minCutter(int[] cut_up_time){
        int cur_min = Integer.MAX_VALUE;
        int cutter = -1;
        for (int i = 0; i < cut_up_time.length; i++) {
            if( cur_min > cut_up_time[i] ){
                cur_min = cut_up_time[i];
                cutter = i;
            }
        }
        return cutter;
    }

    public static int greedy( int[] time, int n, int m){
        int[] cut_up_time = new int[m];
        PriorityQueue<Integer> pq = new PriorityQueue<>((x, y) -> y - x);
        for (int i = 0; i < time.length; i++) {
            pq.add(time[i]);
        }
        while(pq.isEmpty() == false){
            int longest_tree = pq.remove();
            int disk_saw = minCutter(cut_up_time);
            cut_up_time[disk_saw] += longest_tree;
        }
        int max_time = Integer.MIN_VALUE;
        for (int i = 0; i < cut_up_time.length; i++) {
            if( max_time < cut_up_time[i] ){
                max_time = cut_up_time[i];
            }
        }
        return max_time;
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

            // parsing n and m
            String line = in.readLine();
            String[] parts = line.split(" ");
            int n = Integer.parseInt(parts[0]);
            int m = Integer.parseInt(parts[1]);

            int[] time = new int[n];

            for (int i = 0; i < n; i++) {
                int li = Integer.parseInt(in.readLine());
                time[i] = li;
            }

            int res = greedy(time,n,m);

            sb.append("Case #" + j + ": " + res + "\n");

        }
        System.out.println(sb.toString());
    }
}
