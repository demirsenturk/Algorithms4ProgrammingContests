import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class JobScheduling {

    public static int minimal(long[] cut_up_time){
        long cur_min = cut_up_time[0];
        int cutter = 0;
        for (int i = 1; i < cut_up_time.length; i++) {
            if( cur_min > cut_up_time[i] ){
                cur_min = cut_up_time[i];
                cutter = i;
            }
        }
        return cutter;
    }

    public static long scheduling_greedy_ord( ArrayList<Integer> times, int m){
        long[] cut_up_time = new long[m];
        for (int i = 0; i < cut_up_time.length; i++) {
            // P1,...,Pm ← 0
            cut_up_time[i] = 0L;
        }
        // time ← 0
        long time = 0L;
        // sort d1,...,dn in descending order
        Collections.sort( times, Collections.reverseOrder());
        // for i ← 1 to n do
        for (int i = 0; i < times.size(); i++) {
            int longest_tree = times.get(i);
            // find j such that Pj is minimal
            int disk_saw = minimal(cut_up_time);
            // Pj ← Pj + di
            cut_up_time[disk_saw] += longest_tree;
            // time ← max(time, Pj )
            time = Math.max(time, cut_up_time[disk_saw]);
        }
        return time;
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

//            int[] time = new int[n];
            ArrayList<Integer> time = new ArrayList<Integer>();

            for (int i = 0; i < n; i++) {
                int li = Integer.parseInt(in.readLine());
//                time[i] = li;
                time.add(li);
            }

            long res = scheduling_greedy_ord(time,m);

            sb.append("Case #" + j + ": " + res + "\n");

        }
        System.out.println(sb.toString());
    }
}
