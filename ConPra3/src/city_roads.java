import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class city_roads {

    static class Graph {
        private int number_of_vertices;
        private HashMap<Integer, ArrayList<Integer>> adjacency_list;
        private HashSet<Integer> intersections;
        private int[] o;
        private int[] pre;
        private boolean[] visited;

        public Graph(int number_of_vertices) {
            this.number_of_vertices = number_of_vertices;
            this.adjacency_list = new HashMap<Integer, ArrayList<Integer>>();
            this.intersections = new HashSet<Integer>();
            for (int i = 1; i <= number_of_vertices; i++) {
                addNode(i);
            }
            this.pre = new int[this.number_of_vertices+1];
            this.o = new int[this.number_of_vertices+1];
            this.visited = new boolean[this.number_of_vertices+1];
            for (int i = 1; i < pre.length; i++) {
                pre[i] = 0;
                o[i] = Integer.MAX_VALUE;
                visited[i] = false;
            }
        }

        public void addNode(int number) {
            ArrayList<Integer> neighbours = new ArrayList<Integer>();
            adjacency_list.put(number, neighbours);
            intersections.add(number);
        }

        public void addDirectedEdge(int fromVertex, int toVertex){
            (adjacency_list.get(fromVertex)).add(toVertex);
            pre[toVertex] = pre[toVertex] + 1;
        }

        public boolean TopologicalSort(){
            LinkedList<Integer> queue = new LinkedList<Integer>();
            int i = 1;
            Iterator<Integer> it = this.intersections.iterator();
            while(it.hasNext()) {
                int v = it.next();
                if (pre[v] == 0 && !visited[v]) {
                    // procedure TSExplore
                    queue.add(v);
                    this.visited[v] = true;
                    while (queue.size() != 0) {
                        v = queue.getFirst();
                        queue.removeFirst();
                        o[v] = i;
//                        System.out.println(v + " : " + o[v]);
                        i = i + 1;
                        Iterator<Integer> it2 = adjacency_list.get(v).listIterator();
                        while (it2.hasNext()) {
                            int u = it2.next();
                            if(!visited[u]){
                                this.pre[u] = this.pre[u] - 1;
                                if(pre[u] == 0){
                                    queue.add(u);
                                    visited[u] = true;
                                }
                            }
                        }
                    }
                    // procedure TSExplore
                }
            }
            for (int j = 1; j < o.length; j++) {
                if(o[j] == Integer.MAX_VALUE ){
                    return false;
                }
            }
            return true;
        }

        public int getTopologicalRank(int v){
            return this.o[v];
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
            // parsing n,m,l
            String line = in.readLine();
            String[] parts = line.split(" ");
            int n = Integer.parseInt(parts[0]);
            int m = Integer.parseInt(parts[1]);
            int l = Integer.parseInt(parts[2]);

            Graph graph = new Graph(n);

            if( m != 0 ){
                for (int i = 0; i < m; i++) {
                    String dependency = in.readLine();
                    String[] vertexes = dependency.split(" ");
                    int a = Integer.parseInt(vertexes[0]);
                    int b = Integer.parseInt(vertexes[1]);
                    graph.addDirectedEdge(a,b);
                }
            }

            boolean result = graph.TopologicalSort();
            if( result ){
                sb.append("Case #" + j + ": yes\n");
            } else {
                sb.append("Case #" + j + ": no\n");
            }

            if( l != 0 ){
                for (int i = 0; i < l; i++) {
                    String dependency = in.readLine();
                    String[] vertexes = dependency.split(" ");
                    int a = Integer.parseInt(vertexes[0]);
                    int b = Integer.parseInt(vertexes[1]);
                    if(result){
                        if( graph.getTopologicalRank(a) < graph.getTopologicalRank(b) ){
//                            sb.append("sort: " + graph.getTopologicalRank(0) + " " + graph.getTopologicalRank(3) + " " + graph.getTopologicalRank(4) + "\n");
                            sb.append(vertexes[0] + " " + vertexes[1] + "\n");
                        } else {
//                            sb.append("sort: " + graph.getTopologicalRank(b) + " " + graph.getTopologicalRank(a) + " - " + vertexes[1] + " " + vertexes[0] + "\n");
                            sb.append(vertexes[1] + " " + vertexes[0] + "\n");
                        }
                    }
                }
            }

        }
        System.out.println(sb.toString());
    }
}
