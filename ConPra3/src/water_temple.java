import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class water_temple {

    static class Graph {

        class Edge implements Comparable<Edge> {
            private int dest;
            private int weight;

            public Edge(int dest, int weight) {
                this.dest = dest;
                this.weight = weight;
            }

            public int compareTo(Edge compareEdge) {
                return  compareEdge.weight - this.weight;
            }

            public int getDest() {
                return dest;
            }

            public int getWeight() {
                return weight;
            }
        }

        private int number_of_vertices;
        private HashMap<Integer, ArrayList<Edge>> adjacency_list;
        private HashSet<Integer> rooms;
        private HashSet<Integer> visited_rooms;
        private HashMap<Integer, Integer> station;
        private int[] o;
        private boolean[] visited;
        private int dist[];
        private int water_level;
        private int can_drainable;

        public Graph(int number_of_vertices, int level) {
            this.water_level = level;
            this.can_drainable = Integer.MAX_VALUE;
            this.number_of_vertices = number_of_vertices;
            this.adjacency_list = new HashMap<Integer, ArrayList<Edge>>();
            this.station = new HashMap<Integer, Integer>();
            this.rooms = new HashSet<Integer>();
            this.visited_rooms = new HashSet<Integer>();
            for (int i = 1; i <= number_of_vertices; i++) {
                addNode(i);
            }
            this.o = new int[this.number_of_vertices+1];
            this.dist = new int[this.number_of_vertices+1];
            this.visited = new boolean[this.number_of_vertices+1];
            for (int i = 1; i < o.length; i++) {
                o[i] = Integer.MAX_VALUE;
                dist[i] = 0;
                visited[i] = false;
            }
        }

        public void addNode(int number) {
            ArrayList<Edge> neighbours = new ArrayList<Edge>();
            adjacency_list.put(number, neighbours);
            rooms.add(number);
        }

        public void addControlRoom(int number,int d) {
            station.put(number,d);
        }

        public void addDirectedEdge(int fromVertex, int toVertex, int weight){
            Edge e1 = new Edge(toVertex,weight);
            Edge e2 = new Edge(fromVertex,weight);
            (adjacency_list.get(fromVertex)).add(e1);
            (adjacency_list.get(toVertex)).add(e2);
        }

        public int BFS() {
            // used priority queue instead of queue
            PriorityQueue<Edge> queue = new PriorityQueue<Edge>();
            int i = 1;
            // initialize first room and add it to queue
            Edge v = new Edge(1,Integer.MAX_VALUE);
            // dist stores the water level required to reach the room
            dist[1] = Integer.MAX_VALUE;
            if( station.containsKey(1) && station.get(1) < can_drainable ){
                can_drainable = station.get(1);
            }
            queue.add(v);
            while (!(visited_rooms.size() == number_of_vertices) ) {
                if( queue.size() == 0 ){
                    return -1;
                }
                v = queue.remove();
                int v1 = v.getDest();
                // get next node
                int e1 = v.getWeight();
                // get the weight to the node

                // if the room can be visited ( water level condition passed )
                if( water_level > e1 ){
                    if( can_drainable > e1 ){
                        // can not be visited, enough water can not be drained
                        return -1;
                    } else {
                        // water can be drained and visited
                        water_level = e1;
                    }
                }

                if( station.containsKey(v1) && station.get(v1) < can_drainable ){
                    // if the node is a station and tha drainable water is less then current
                    can_drainable = station.get(v1);
                    // update the drainable water
                }
                // visit the room
                visited_rooms.add(v1);
                // if all rooms are visited
                if(visited_rooms.size() == number_of_vertices ){
                    // success (possible)
                    return water_level;
                }
                o[v1] = i++;
                int edgeDistance = -1;
                int newDistance = -1;
                // visit the neighbours of the node
                Iterator<Edge> it2 = adjacency_list.get(v1).listIterator();
                while (it2.hasNext()) {
                    // get one of the neighbours
                    Edge e = it2.next();
                    int u = e.getDest();
                    // if not already visited
                    if(!visited_rooms.contains(u)) {
                        edgeDistance = e.weight;
                        if (dist[v1] > edgeDistance) {
                            newDistance = edgeDistance;
                        } else {
                            newDistance = dist[v1];
                        }
                        if (newDistance > dist[u]) {
                            dist[u] = newDistance;
                        }
                        o[u] = 0;
                        queue.add(new Edge(u, dist[u]));
                    }
                }
            }
            if(visited_rooms.size() == number_of_vertices){
                return water_level;
            } else {
                return -1;
            }
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
            // parsing n,m,l,l
            String line = in.readLine();
            String[] parts = line.split(" ");
            int n = Integer.parseInt(parts[0]);
            int m = Integer.parseInt(parts[1]);
            int k = Integer.parseInt(parts[2]);
            int l = Integer.parseInt(parts[3]);

            Graph graph = new Graph(n,l);

            if( m != 0 ){
                for (int i = 0; i < m; i++) {
                    String dependency = in.readLine();
                    String[] vertexes = dependency.split(" ");
                    int a = Integer.parseInt(vertexes[0]);
                    int b = Integer.parseInt(vertexes[1]);
                    int li = Integer.parseInt(vertexes[2]);
                    graph.addDirectedEdge(a,b,li);
                }
            }

            if( k != 0 ){
                for (int i = 0; i < k; i++) {
                    String dependency = in.readLine();
                    String[] vertexes = dependency.split(" ");
                    int a = Integer.parseInt(vertexes[0]);
                    int di = Integer.parseInt(vertexes[1]);
                    graph.addControlRoom(a,di);
                }
            }

            int result = graph.BFS();
            if( l == 0 ){
                result = 0;
            }
            if( result != -1 ){
                sb.append("Case #" + j + ": " + result + "\n");
            } else {
                sb.append("Case #" + j + ": impossible\n");
            }

        }
        System.out.println(sb.toString());
    }
}
