import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Connectivity {

    // dinic implementation source: https://sites.google.com/site/indy256/algo/dinic_flow

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

            List<Edge>[] graph = createGraph( 2*n );

            if( n != 0 ) {
                for (int i = 1; i < n-1; i++) {
                    addEdge(graph,  i, n - 1 + i, 1);
                }
            }

            if( m != 0 ){
                for (int i = 1; i <= m; i++) {
                    String dependency = in.readLine();
                    String[] vertexes = dependency.split(" ");
                    int a_j = Integer.parseInt(vertexes[0]);
                    int b_j = Integer.parseInt(vertexes[1]);
                    // Replace each vertex other than 0 and n - 1 with two vertex
                    // v_in and v_out
                    // all incoming edges will be connected to v_in
                    // all going edges will be connected to v_out
                    if( a_j == n ||  a_j == 1 ) {
                        // if first vertex is 1 or n, no change needed
                        int va_in = a_j;
                        int va_out = a_j;
                        int vb_in = b_j;
                        int vb_out = n - 1 + b_j;
                        addEdge(graph,  va_out - 1, vb_in - 1, 1);
                        addEdge(graph,  vb_out - 1, va_in - 1, 1);
                    } else if( b_j == n || b_j == 1){
                        // if second vertex is 1 or n, no change needed
                        int va_in = a_j;
                        int va_out = n - 1 + a_j;
                        int vb_in = b_j;
                        int vb_out = b_j;
                        addEdge(graph,  va_out - 1, vb_in - 1, 1);
                        addEdge(graph,  vb_out - 1, va_in - 1, 1);
                    } else {
                        // use n - 1 + v, where v denote the node, as the vb_out
                        int va_in = a_j;
                        int va_out = n - 1 + a_j;
                        int vb_in = b_j;
                        int vb_out = n - 1 + b_j;
                        addEdge(graph,  va_out - 1, vb_in - 1, 1);
                        addEdge(graph,  vb_out - 1, va_in - 1, 1);
                    }

//                    graph.addUnicostUndirectedEdge(0, i, 1);
//                    graph.addUnicostUndirectedEdge(i, m_i, 1);
//                    graph.addUndirectedEdge( m_i, n + m + b_i, 1);
                }
            }

            String result;
            int res = maxFlow(graph, 0, n-1);
            // the maximum flow is the amx cardinality of this reduction
            result = Integer.toString(res);
            sb.append("Case #" + j + ": " + result + "\n");
        }
        System.out.println(sb.toString());
    }
}