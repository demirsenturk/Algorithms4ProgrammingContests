import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class library_hell {

    static class Graph{
        private int number_of_vertices;
        private HashMap<Integer, ArrayList<Integer>> adjacency_list;
//        private HashMap<Integer, Vertex> vertex_list;
        private HashSet<Integer> to_remove;
        private HashSet<Integer> to_work;
        private Boolean[] visited;

        public Graph(int number_of_vertices) {
            this.number_of_vertices = number_of_vertices;
            this.adjacency_list = new HashMap<Integer,ArrayList<Integer>>();
            for (int i = 1; i <= number_of_vertices; i++) {
                addNode(i);
            }
            this.to_remove = new HashSet<Integer>();
            this.to_work = new HashSet<Integer>();
            this.visited = new Boolean[number_of_vertices];
//            for (int i = 0; i < number_of_vertices; i++) {
//                this.visited[i] = false;
//            }
        }

        public void addNode(int number){
            ArrayList<Integer> neighbours = new ArrayList<Integer>();
            adjacency_list.put(number, neighbours);
        }

        public boolean toRemove(int i){
            if(to_work.contains(i)){
                return false;
            }
            to_remove.add(i);
            return true;
        }

        public void toWork(int i){
            to_work.add(i);
        }

        public void addDependency(int fromVertex, int toVertex){
            (adjacency_list.get(fromVertex)).add(toVertex);
        }

        public boolean BFS() {
            Iterator<Integer> it = this.to_work.iterator();
            int[] o = new int[this.number_of_vertices+1];
            for (int i = 1; i < o.length; i++) {
                o[i] = Integer.MAX_VALUE;
            }
            LinkedList<Integer> queue = new LinkedList<Integer>();
            int i = 1;
            while(it.hasNext()){
                int v = it.next();
                if( o[v] == Integer.MAX_VALUE ){
                    queue.add(v);
                    while (queue.size() != 0) {
                        int v1 = queue.poll();
                        o[v1] = i++;
                        Iterator<Integer> it2 = adjacency_list.get(v1).listIterator();
                        while (it2.hasNext()) {
                            int u = it2.next();
                            if(this.to_remove.contains(u)){
                                return false;
                            }
                            if(o[u] == Integer.MAX_VALUE){
                                o[u] = 0;
                                queue.add(u);
                            }
                        }
                    }
                }
            }
            return true;
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
            // parsing n
            String line = in.readLine();
            String[] parts = line.split(" ");
            int N = Integer.parseInt(parts[0]);
            int K = Integer.parseInt(parts[1]);
            int R = Integer.parseInt(parts[2]);
            int D = Integer.parseInt(parts[3]);

            Graph graph = new Graph(N);

            if( K != 0 ){
                String K_line = in.readLine();
                String[] packages_to_keep = K_line.split(" ");
                for (int i = 0; i < K; i++) {
                    int node_number = Integer.parseInt(packages_to_keep[i]);
                    graph.toWork(node_number);
                }
            } else {
                in.readLine();
            }
            boolean ok = true;
            if( R != 0 ){
                String R_line = in.readLine();
                String[] packages_to_remove = R_line.split(" ");
                for (int i = 0; i < R; i++) {
                    int node_number = Integer.parseInt(packages_to_remove[i]);
                    if( ok ) {
                        ok = graph.toRemove(node_number);
                    }
                }
            } else {
                in.readLine();
            }
            if( D != 0 ){
                for (int i = 0; i < D; i++) {
                    String dependency = in.readLine();
                    String[] vertexes = dependency.split(" ");
                    int v1 = Integer.parseInt(vertexes[0]);
                    int v2 = Integer.parseInt(vertexes[1]);
                    graph.addDependency(v1,v2);
                }
            }
            if( ok && !(K == 0) ){
                ok = graph.BFS();
            }
            if( ok || K == 0 ){
                sb.append("Case #" + j + ": ok\n");
            } else {
                sb.append("Case #" + j + ": conflict\n");
            }
        }
        System.out.println(sb.toString());
    }
}
