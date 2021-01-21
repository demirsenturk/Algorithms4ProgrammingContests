import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class goat_riders {

    static class max_flow_formulation{
        private int[][] grid;
        private int n;
        private int riders_count;
        private int days;
        private int[] levels;
        private int[][] start_positions;

        public max_flow_formulation(int n, int riders_count, int days) {
            this.n = n;
            this.days = days;
            this.riders_count = riders_count;
            this.grid = new int[n][n];
            this.levels = new int[days];
            this.start_positions = new int[riders_count][2];
        }

        public void setHeight( int i, int j, int height){
            grid[i][j] = height;
        }

        public void setLevel( int i, int level){
            levels[i] = level;
        }

        public void setPosition( int rider, int i, int j){
            start_positions[rider][0] = i;
            start_positions[rider][1] = j;
        }

        public int getMaxFlow(){
            int size_in = (days + 1) * n * n;
            int size = size_in * 2 + 1 + 1;
//            int size = (days + 1) * n * n + 1 + 1;
            List<Edge>[] graph = createGraph( size );
            int source = size - 2;
            int target = size - 1;
            for (int i = 0; i < riders_count; i++) {
                // from 1 to k -> first days
                int x = start_positions[i][0];
                int y = start_positions[i][1];
                // connect source with initial day = 0
                addEdge(graph,  source , ( x * n ) + y ,1);
            }
            for (int k = 0; k <= days; k++) {
                // days count
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        // row and column
                        addEdge(graph, ((k) * n * n) + (i * n) + j, size_in + ((k) * n * n) + (i * n) + j, 1);
                        for ( int off = -1; off <= 1; off++){
                            for ( int off2 = -1; off2 <= 1; off2++) {
                                // range i + [-1;1] and j + [-1;1]
                                if ( !(off == -1 && off2 == -1)
                                        && !(off == 1 && off2 == 1)
                                        && !(off == -1 && off2 == 1)
                                        && !(off == 1 && off2 == -1)
                                        && i + off >= 0 && i + off <= n - 1
                                        && j + off2 >= 0 && j + off2 <= n - 1
                                        && k != days
                                        && levels[k] < grid[i+off][j+off2]) {
                                    addEdge(graph, size_in + ((k) * n * n) + (i * n) + j, ((k + 1) * n * n) + ((i + off) * n) + (j + off2), 1);
                                }
                            } // end off2
                        } // end off
                        if ( k == days ) {
                            addEdge(graph,( (k) * n * n ) + ( i * n ) + j , target , 1);
                        }
                    } // end for column
                } // end for row
            } // end day count

            int res = maxFlow(graph, source, target);
//            System.out.println(source);
            return res;
        }

    }

    // source: https://sites.google.com/site/indy256/algo/dinic_flow

    static class Edge {
        int t; // destination
        int rev;
        int capacity; // capacity of the edge
        int f; // flow

        public Edge(int t, int rev, int cap) {
            this.t = t;
            this.rev = rev;
            this.capacity = cap;
        }

    }

    public static List<Edge>[] createGraph(int nodes) {
        // initialize adjacency list
        List<Edge>[] graph = new List[nodes];
        for (int i = 0; i < nodes; i++) {
            // neighbor nodes
            graph[i] = new ArrayList<>();
        }
        return graph;
    }

    public static void addEdge(List<Edge>[] graph, int s, int t, int cap) {
        // add the directed edge
        graph[s].add(new Edge(t, graph[t].size(), cap));
        // and the opposite one with 0 capacity
        graph[t].add(new Edge(s, graph[s].size() - 1, 0));
    }

    static boolean dinicBfs(List<Edge>[] graph, int src, int dest, int[] dist) {
        Arrays.fill(dist, -1);
        dist[src] = 0;
        int[] Q = new int[graph.length];
        int sizeQ = 0;
        Q[sizeQ++] = src;
        for (int i = 0; i < sizeQ; i++) {
            int u = Q[i];
            for (Edge e : graph[u]) {
                if (dist[e.t] < 0 && e.f < e.capacity) {
                    dist[e.t] = dist[u] + 1;
                    Q[sizeQ++] = e.t;
                }
            }
        }
        return dist[dest] >= 0;
    }

    static int dinicDfs(List<Edge>[] graph, int[] ptr, int[] dist, int dest, int u, int f) {
        if (u == dest) {
            // if source is destination -> error
            return f;
        }
        for (; ptr[u] < graph[u].size(); ++ptr[u]) {
            Edge e = graph[u].get(ptr[u]);
            if (dist[e.t] == dist[u] + 1 && e.f < e.capacity) {
                int df = dinicDfs(graph, ptr, dist, dest, e.t, Math.min(f, e.capacity - e.f));
                // if a path can be augmented return
                if (df > 0) {
                    e.f += df;
                    graph[e.t].get(e.rev).f -= df;
                    return df;
                }
            }
        }
        return 0;
    }

    public static int maxFlow(List<Edge>[] graph, int src, int dest) {
        // Initial flow is 0
        int flow = 0;
        int[] dist = new int[graph.length];
        // there exists a path p from s to t in the residual network Gf
        while (dinicBfs(graph, src, dest, dist)) {
            int[] parent = new int[graph.length];
            while (true) {
                // Augmentf by fp (f ← f↑fp)
                int df = dinicDfs(graph, parent, dist, dest, src, Integer.MAX_VALUE);
                if (df == 0) {
                    // a path p from s to t in the residual network Gf not found
                    break;
                }
                // update the flow
                flow += df;
            }
        }
        return flow;
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
            int k = Integer.parseInt(parts[1]);
            int d = Integer.parseInt(parts[2]);

            max_flow_formulation formulation = new max_flow_formulation(n,k,d);

            for (int i = 0; i < n; i++) {
                String n_line = in.readLine();
                String[] n_parts = n_line.split(" ");
                for (int l = 0; l < n; l++) {
                    int height = Integer.parseInt(n_parts[l]);
                    formulation.setHeight(i,l,height);
                }
            }

            for (int i = 0; i < k; i++) {
                String position_line = in.readLine();
                String[] positions = position_line.split(" ");
                int x = Integer.parseInt(positions[0]);
                int y = Integer.parseInt(positions[1]);
                formulation.setPosition(i,x,y);
            }

            for (int i = 0; i < d; i++) {
                int level = Integer.parseInt(in.readLine());
                formulation.setLevel(i,level);
            }

            String result;
            if( k == 0 ){
                result = "0";
            } else {
                int res = formulation.getMaxFlow();
                result = Integer.toString(res);
            }
            sb.append("Case #" + j + ": " + result + "\n");
        }
        System.out.println(sb.toString());
    }
}
