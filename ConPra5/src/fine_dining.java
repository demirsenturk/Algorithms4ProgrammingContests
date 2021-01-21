import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class fine_dining {

    static class Graph {


        private int number_of_vertices;
        private int graph[][];


        public Graph(int number_of_vertices) {
            graph = new int[number_of_vertices][number_of_vertices];
            for (int i = 0; i < number_of_vertices; i++) {
                for (int j = 0; j < number_of_vertices; j++) {
                    graph[i][j] = 0;
                }
            }
            this.number_of_vertices = number_of_vertices;
        }

        public void addUnicostUndirectedEdge(int fromVertex, int toVertex, int weight){
            graph[fromVertex][toVertex] = weight;
            graph[toVertex][fromVertex] = weight;
        }

        public void addUndirectedEdge(int fromVertex, int toVertex, int weight){
            graph[fromVertex][toVertex] += weight;
            graph[toVertex][fromVertex] += weight;
        }

        public boolean nonZeroEdge(int fromVertex, int toVertex){
            return (graph[fromVertex][toVertex] != 0);
        }

        public boolean BFS(int parent[], int[][] residual_graph) {
//            int[] o = new int[this.number_of_vertices];
            boolean[] vis = new boolean[this.number_of_vertices];
//            for (int i = 1; i < o.length; i++) {
//                o[i] = Integer.MAX_VALUE;
//            }
            LinkedList<Integer> queue = new LinkedList<Integer>();
//            int i = 1;
            queue.add(0);
            parent[0] = -1;

            while (queue.size() != 0) {
                int u = queue.poll();
//                o[u] = i++;
                for (int v = 0; v < number_of_vertices; v++) {
                    if ( vis[v] == false
                            && residual_graph[u][v] > 0 ) {
                        queue.add(v);
                        // save parent
                        parent[v] = u;
                        // mark as seen
                        vis[v] = true;
                    }
                }
            }
            return vis[number_of_vertices-1];
        }

        public int Ford_Fulkerson(){
            int u;
            int v;
            int[][] residual_graph = new int[number_of_vertices][number_of_vertices];
            for (int i = 0; i < number_of_vertices; i++) {
                for (int j = 0; j < number_of_vertices; j++) {
                    residual_graph[i][j] = graph[i][j];
                }
            }
            int parent[] = new int[number_of_vertices];
            // initial flow is 0 (f ← 0)
            int max_flow = 0;

            // there exists a path p from s to t in the residual network Gf
            while( BFS(parent,residual_graph) ){
                int path_flow = Integer.MAX_VALUE;
                // Augment f by fp (f ← f ↑ fp)
                for ( v = number_of_vertices-1; v != 0; v = parent[v]) {
                    u = parent[v];
                    // update the flow
                    path_flow = Math.min( path_flow, residual_graph[u][v] );
                }
                for (v = number_of_vertices-1; v != 0; v = parent[v]) {
                    u = parent[v];
                    // if the edge is element of our path found in DFS
                    // update capacities in Gf
                    residual_graph[u][v] -= path_flow;
                    residual_graph[v][u] += path_flow;
                }
                max_flow += path_flow;
            }
            return max_flow;
        }
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
            // parsing n, m and b
            String line = in.readLine();
            String[] parts = line.split(" ");
            int n = Integer.parseInt(parts[0]);
            int m = Integer.parseInt(parts[1]);
            int b = Integer.parseInt(parts[2]);

            Graph graph = new Graph(m + b + 2 );

            if( m != 0 ) {
                for (int i = 1; i <= m; i++) {
                    graph.addUnicostUndirectedEdge(0, i, 1);
                }
            }

            if( b != 0 ) {
                for (int i = 1; i <= b; i++) {
                    graph.addUnicostUndirectedEdge( m + i, m + b + 1, 1);
                }
            }

            if( n != 0 ){
                for (int i = 1; i <= n; i++) {
                    String dependency = in.readLine();
                    String[] vertexes = dependency.split(" ");
                    int m_i = Integer.parseInt(vertexes[0]);
                    int b_i = Integer.parseInt(vertexes[1]);
                    graph.addUndirectedEdge( m_i, m + b_i, 1);
                }
            }

            String result;
            int res = graph.Ford_Fulkerson();
            result = Integer.toString(res);
            sb.append("Case #" + j + ": " + result + "\n");
        }
        System.out.println(sb.toString());
    }
}

