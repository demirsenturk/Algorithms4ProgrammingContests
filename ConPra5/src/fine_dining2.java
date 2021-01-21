import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class fine_dining2 {

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
            int m = Integer.parseInt(parts[1]);
            int b = Integer.parseInt(parts[2]);

            List<Edge>[] graph = createGraph(m + b + 2);

            if( m != 0 ) {
                for (int i = 1; i <= m; i++) {
//                    graph.addUnicostUndirectedEdge(0, i, 1);
                    // add for each main dish a node
                    // and connect it with s node by a uni cost edge
                    addEdge( graph, 0, i, 1);
                }
            }

            if( b != 0 ) {
                for (int i = 1; i <= b; i++) {
//                    graph.addUnicostUndirectedEdge( m + i, m + b + 1, 1);
                    // add for each beverage a node
                    // connect it with target node by a uni cost edge
                    addEdge(graph, m + i, m + b + 1, 1);
                }
            }

            if( n != 0 ){
                for (int i = 1; i <= n; i++) {
                    String dependency = in.readLine();
                    String[] vertexes = dependency.split(" ");
                    int m_i = Integer.parseInt(vertexes[0]);
                    int b_i = Integer.parseInt(vertexes[1]);
                    // for each preference add an edge from the
                    // preferred main dish node to preferred beverage node
                    addEdge(graph,  m_i, m + b_i, 1);
//                    graph.addUnicostUndirectedEdge(0, i, 1);
//                    graph.addUnicostUndirectedEdge(i, m_i, 1);
//                    graph.addUndirectedEdge( m_i, n + m + b_i, 1);
                }
            }

            String result;
            int res = maxFlow(graph, 0, m + b + 1);
            // the maximum flow is the amx cardinality of this reduction
            result = Integer.toString(res);
            sb.append("Case #" + j + ": " + result + "\n");
        }
        System.out.println(sb.toString());
    }
}
