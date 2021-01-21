import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class football_champion {

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

    static class championship {
        private int[] wins;
        private int[][] remaining_matches;
        private int number_of_teams;

        public championship(int number_of_teams ,int[] wins) {
            this.number_of_teams = number_of_teams;
            this.wins = wins;
            this.remaining_matches = new int[number_of_teams][number_of_teams];
            for (int i = 0; i < number_of_teams; i++) {
                for (int j = 0; j < number_of_teams; j++) {
                    remaining_matches[i][j] = 0;
                }
            }
        }

        public void addMatch(int team1, int team2){
            (remaining_matches[team1-1][team2-1])++;
            (remaining_matches[team2-1][team1-1])++;
        }

        public int getRemaining(int team1, int team2){
            return remaining_matches[team1-1][team2-1];
        }

        public int getRemainingTotal(int team1){
            int number = 0;
            for (int i = 0; i < number_of_teams; i++) {
                if( team1 - 1 != i ){
                    number += remaining_matches[team1-1][i];
                }
            }
            return number;
        }

        public boolean isEliminated(int team) {
            boolean eliminated = false;
            for (int i = 1; i <= number_of_teams; i++) {
                if( wins[team-1] + getRemainingTotal(team) < wins[i-1] ){
                    // if the elimination is trivial
                    // the number of remaining matches + the wins is not enough
                    // eliminated
                    return true;
                }
            }
            // is not trivial
            // do flow calculation ( max flow )
            eliminated = maxFlowFormulation(team);

            return eliminated;
        }

        public boolean maxFlowFormulation(int team){
            int team_index = team - 1;
            // index shifted
            int gameCombos = combinator(number_of_teams);
            // games left ( the combination of games )
            int size = 2 + number_of_teams + gameCombos - 1;
            // target and source node
            int source = size - 2;
            int target = size - 1;
            // variables for creating new nodes
            int node_count = 0;
            int node_count1 = 0;
            int node_count2 = 0;
            // sum of the matches
            int sum = 0;
            List<Edge>[] graph = createGraph( size );
            // create the flow network
            // here we create a node for each remaining game
            // between teams other then the one investigated
            for (int i = 0; i < number_of_teams; i++) {
                if (i != team_index) {
                    for (int j = 0; j < number_of_teams; j++) {
                        if (j != team_index
                                && j > i) {
                            // connect each edge with source and the game node
                            addEdge(graph,  source , node_count , getRemaining(i+1,j+1) );
                            // update total remaining games
                            sum += getRemaining(i+1,j+1);
                            // add edges with infinite capacities
                            addEdge(graph,  node_count , size - 1 - number_of_teams + node_count1 , Integer.MAX_VALUE );
                            addEdge(graph,  node_count , size - number_of_teams + node_count2 , Integer.MAX_VALUE );
                            node_count++;
                            node_count2++;
                        }
                    }
                    // total games left
                    int weight = wins[team_index] + getRemainingTotal(team) - wins[i];
                    addEdge(graph,target - number_of_teams + node_count1, target , weight);
                    node_count1++;
                    node_count2 = node_count1;
                }
            }
            int res = maxFlow(graph, source , target);
            if (sum == res ) {
                // victory is possible
                return false;
            }
            else {
                // victory is not possible
                return true;
            }
        }

        private int combinator(int x) {
            int n = x - 1;
            return n * (n-1) / 2;
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

            int[] wins = new int[n];
            if( n != 0 ) {
                String line_wins = in.readLine();
                String[] wins_strings = line_wins.split(" ");
                for (int i = 0; i < wins_strings.length; i++) {
                    wins[i] = Integer.parseInt(wins_strings[i]);
                }
            }

            championship ch = new championship( n , wins);

            if( m != 0 ){
                for (int i = 1; i <= m; i++) {
                    String dependency = in.readLine();
                    String[] vertexes = dependency.split(" ");
                    int a_i = Integer.parseInt(vertexes[0]);
                    int b_i = Integer.parseInt(vertexes[1]);
                    ch.addMatch(a_i,b_i);
                }
            }

            String result = "";
            for (int i = 1; i <= n; i++) {
                if( i != 1 ){
                    result += " ";
                } else {

                }

                if( ch.isEliminated(i) ){
                    result += "no";
                } else {
                    result += "yes";
                }
            }
            sb.append("Case #" + j + ": " + result + "\n");

        }
        System.out.println(sb.toString());
    }
}