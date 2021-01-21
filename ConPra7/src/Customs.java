import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Customs {

    static class GreedyDivide{
        private int number_of_cities;

        private int[] degrees;
        private HashMap<Integer,ArrayList<Integer>> adjacents;
        private HashSet<Integer> partition;

        public GreedyDivide(int n, int[] degrees, HashMap<Integer, ArrayList<Integer>> adj) {
            this.number_of_cities = n;
            this.degrees = degrees;
            this.adjacents = adj;
            this.partition = new HashSet<Integer>();
        }

        void update (int v){
            ArrayList<Integer> l = adjacents.get(v);
            for (int i = 0; i < l.size(); i++) {
                int cur = l.get(i);
                degrees[cur-1] -= 2;
            }
        }

        public String solver(){
            while( true ) {
                int max_degree = 0;
                int node = -1;
                for (int i = 0; i < degrees.length; i++) {
                    if ( !partition.contains(i) && max_degree < degrees[i]) {
                        max_degree = degrees[i];
                        node = i;
                    }
                }
                if (0 == max_degree) {
                    break;
                }
                partition.add(node);
                update(node);
            }
            StringBuilder sb = new StringBuilder();
            ArrayList<Integer> l = new ArrayList<>(partition);
            for (int i = 0; i < l.size(); i++) {
                if(i == 0){
                    sb.append( (l.get(i) + 1) );
                } else {
                    sb.append(" " + (l.get(i) + 1 ) );
                }
            }
            sb.append("\n");
            return sb.toString();
        }

//        class Node implements Comparator<Node> {
//
//            private int degree;
//
//            public int compare(Node n1, Node n2) {
//                if (n1.cgpa < n2.cgpa) {
//                    return 1;
//                } else if (n1.cgpa > n2.cgpa) {
//                    return -1;
//                }
//                return 0;
//            }
//        }

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

            // parsing n
            int n = Integer.parseInt(in.readLine());

            int[] degrees = new int[n];
            HashMap<Integer,ArrayList<Integer>> adjacents = new HashMap<Integer, ArrayList<Integer>>();


            if( n != 0 ) {
                for (int i = 0; i < n; i++) {
                    String line = in.readLine();
                    String[] parts = line.split(" ");
                    int ki = Integer.parseInt(parts[0]);
                    degrees[i] = ki;
                    ArrayList<Integer> neighbours = new ArrayList<Integer>();
                    for (int k = 1; k <= ki; k++) {
                        neighbours.add(Integer.parseInt(parts[k]));
                    }
                    adjacents.put(i,neighbours);
                }
            }

            GreedyDivide gd = new GreedyDivide(n, degrees, adjacents);

            String result = gd.solver();
            sb.append("Case #" + j + ":\n" + result);
        }
        System.out.println(sb.toString());
    }
}
