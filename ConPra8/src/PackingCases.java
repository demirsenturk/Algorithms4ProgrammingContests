import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class PackingCases {

    static class Case implements Comparable<Case>{

        private int h;
        private int w;
        private int d;
        private int base_area;

        public Case(int h, int w, int d) {
            this.h = h;
            this.w = w;
            this.d = d;
            this.base_area = this.w * this.d;
        }

        @Override
        public int compareTo(Case o) {
            return o.base_area - this.base_area;
        }

        public int getHeight() {
            return h;
        }

    }

    // code source adjusted: https://www.geeksforgeeks.org/box-stacking-problem-dp-22/

    public static boolean canReach( Case[] array, int n, int h){

        int number_of_boxes = 3 * n;

        Case[] possible_boxes = new Case[number_of_boxes];

        for(int i = 0;i < n;i++){
            Case box = array[i];
            int x = box.getHeight();
            int y = Math.max(box.w,box.d);
            int z = Math.min(box.w,box.d);
            // all possible rotations
            possible_boxes[3*i] = new Case( x, y, z );
            x = box.w;
            y = Math.max(box.h,box.d);
            z = Math.min(box.h,box.d);
            possible_boxes[3*i + 1] = new Case( x, y , z );
            x = box.d;
            y = Math.max(box.w,box.h);
            z = Math.min(box.w,box.h);
            possible_boxes[3*i + 2] = new Case( x, y , z );
        }

        // sort boxes in decreasing order of base areas
        Arrays.sort(possible_boxes);

        // lengths of the longest possible height with length s[i] that ends with the box v[i]
        int[]s = new int[number_of_boxes];

        // bottom-up
        // Longest Increasing Subsequence
        for(int i = 0; i < number_of_boxes; i++){
            s[i] = 0;
            Case box = possible_boxes[i];

            int current_height = 0;

            // find longest increasing subsequence at a smaller index i and v[i] < v[j]
            for(int j = 0; j < i; j++){
                Case prevBox = possible_boxes[j];
                if( box.w < prevBox.w && box.d < prevBox.d){
                    // can be stacked onto case b
                    // if xa < xb and ya < yb
                    current_height = Math.max( current_height, s[j] );
                    // if the height is greater then the current max
                    // update height
                }
            }
            // stack box and update the maximum reachable height
            s[i] = current_height + box.getHeight();
        }

        int max = 0;

        for(int i = 0; i < number_of_boxes; i++){
            // find the maximum
            max = Math.max( max, s[i] );
        }

        return max >= h;
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
            String line = in.readLine();
            String[] parts = line.split(" ");
            int h = Integer.parseInt(parts[0]);
            int n = Integer.parseInt(parts[1]);

            Case[] boxes = new Case[n];

            for (int i = 0; i < n; i++) {
                String values_line = in.readLine();
                String[] string_values = values_line.split(" ");
                int x_i = Integer.parseInt(string_values[0]);
                int y_i = Integer.parseInt(string_values[1]);
                int z_i = Integer.parseInt(string_values[2]);
                boxes[i] = new Case(x_i,y_i,z_i);
            }
            boolean reachable = canReach(boxes, n , h);
            String result;
            if( reachable ){
                result = "yes";
            } else {
                result = "no";
            }
            sb.append("Case #" + j + ": " + result + "\n");
        }
        System.out.println(sb.toString());
    }
}
