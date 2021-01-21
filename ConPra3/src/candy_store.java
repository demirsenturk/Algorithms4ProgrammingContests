import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class candy_store {

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
            String line = in.readLine();
            int n = Integer.parseInt(line);

            // initialize adjacency list
            List<Set<Integer>> adjacency_list = new ArrayList<>(n);
            for (int i = 0; i < n; i++){
                // initialize empty set for each edge
                adjacency_list.add(new HashSet<>());
            }

            if( n - 1 != 0 ){
                // parse roads next n - 1 lines
                for (int i = 0; i < n-1; i++) {
                    String road = in.readLine();
                    String[] vertexes = road.split(" ");
                    int a = Integer.parseInt(vertexes[0]);
                    int b = Integer.parseInt(vertexes[1]);
                    // add two directions of the nodes due to bidirectional edges
                    adjacency_list.get(a).add(b);
                    adjacency_list.get(b).add(a);
                }
            }
            // the result list to be updates in each step
            List<Integer> results = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if( adjacency_list.get(i).size() == 1 ){
                    // add the end vertices
                    results.add(i);
                }
            }
            while ( n > 2 ){
                // update the number of remaining vertices
                n = n - results.size();
                // the enw results
                List<Integer> change_results = new ArrayList<>();
                for (int i : results) {
                    // for each end vertice
                    int k = adjacency_list.get(i).iterator().next();
                    // get the connected vertex to the end vertex
                    adjacency_list.get(k).remove(i);
                    // remove the end vertice and its connections
                    if (adjacency_list.get(k).size() == 1){
                        // if the new vertex is an end vertex add it for the enw iteration
                        change_results.add(k);
                    }
                }
                // pass the new end nodes
                results = change_results;
            }
            sb.append("Case #" + j + ": " + results.get(0) + "\n");

        }
        System.out.println(sb.toString());
    }

    // idea from source http://buttercola.blogspot.com/2016/01/leetcode-minimum-height-trees.html

}
