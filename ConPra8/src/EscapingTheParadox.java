import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class EscapingTheParadox {

    static class Graph {

        class Way implements Comparable<Way> {
            private int distance;
            private int objects;

            public Way(int distance, int objects) {
                this.distance = distance;
                this.objects = objects;
            }

            public int compareTo(Way o) {
                return this.distance - o.distance;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof Way)) return false;
                Way way = (Way) o;
                return distance == way.distance &&
                        objects == way.objects;
            }

            @Override
            public int hashCode() {
                return Objects.hash(distance, objects);
            }

        }

        class Node implements Comparable<Node> {
            private int dest;
            private int cost;

            public Node(int dest, int cost) {
                this.dest = dest;
                this.cost = cost;
            }

            public int compareTo(Node compareNode) {
                if (Double.compare(this.cost, compareNode.cost) == 0){
                    return 0;
                }
                else if (Double.compare(this.cost, compareNode.cost) < 0){
                    return -1;
                }
                else {
                    return 1;
                }
            }

            public int getDest() {
                return dest;
            }

            public int getCost() {
                return cost;
            }
        }


        private int number_of_vertices;
        private HashMap<Integer, ArrayList<Node>> adjacency_list;
        private HashMap<Integer, HashSet<Way>> ways;
        private HashSet<Integer> intersections;
        private HashSet<Integer> visited;

        private int g;
        private int dist[];
        private int[] objects;
        private HashSet<Integer> cities_wit_store;

        public Graph(int number_of_vertices, int g, int[] objects) {
            this.number_of_vertices = number_of_vertices;
            this.adjacency_list = new HashMap<Integer, ArrayList<Node>>();
            this.ways = new HashMap<Integer, HashSet<Way>>();
            this.intersections = new HashSet<Integer>();
            this.g = g;
            this.cities_wit_store = new HashSet<Integer>();
            this.visited = new HashSet<Integer>();

            this.objects = objects;
            for (int i = 0; i <= number_of_vertices; i++) {
                addNode(i);
            }
            this.dist = new int[this.number_of_vertices+1];
            for (int i = 0; i < dist.length; i++) {
                dist[i] = Integer.MAX_VALUE;
            }
        }

        public void addNode(int number) {
            ArrayList<Node> neighbours = new ArrayList<Node>();
            adjacency_list.put(number, neighbours);
            HashSet<Way> node_ways = new HashSet<Way>();
            ways.put(number,node_ways);
            intersections.add(number);
        }

        public void addUndirectedEdge(int fromVertex, int toVertex, int weight){
            Node e1 = new Node(toVertex,weight);
            Node e2 = new Node(fromVertex,weight);
            (adjacency_list.get(fromVertex)).add(e1);
            (adjacency_list.get(toVertex)).add(e2);
        }

        public int result(){
            Dijkstra(this.dist, g );
            ways.get(number_of_vertices).add(new Way(0, objects[number_of_vertices-1]) );
            for (int i = number_of_vertices; i >= 0 ; i--) {
                int v = i;
                HashSet<Way> ways_hset = ways.get(v);
                List<Way> ways_list = new ArrayList<Way>(ways_hset);

                Iterator<Node> it2 = adjacency_list.get(v).listIterator();
                while ( it2.hasNext() ) {
                    // get one of the neighbours
                    Node w_node = it2.next();
                    int w = w_node.getDest();
                    int v_w_cost = w_node.getCost();
                    // update the ways set
                    for (int j = 0; j < ways_list.size(); j++) {
                        Way wy = ways_list.get(j);
                        int distance = wy.distance;
                        int object_count = wy.objects;
                        if( dist[w] > distance + v_w_cost ){
                            Way new_way;
                            if( w != 0 ) {
                                new_way = new Way(distance + v_w_cost, object_count + objects[w - 1]);
                            } else {
                                new_way = new Way(distance + v_w_cost, object_count );
                            }
                            ways.get(w).add(new_way);
                        }
                    }
                }
            }
            HashSet<Way> surface_hset = ways.get(0);
            if( surface_hset.isEmpty() ){
                return -1;
            }
            List<Way> surface_ways = new ArrayList<Way>(surface_hset);
            int max = -1;
            for (int i = 0; i < surface_ways.size(); i++) {
                int cur = surface_ways.get(i).objects;
                if( cur > max ){
                    max = cur;
                }
            }
            return max;
        }

        public void Dijkstra( int[] dist , int src ) {
            dist[src] = 0;
            PriorityQueue<Node> queue = new PriorityQueue<Node>();
            Iterator<Integer> it1 = intersections.iterator();
//            while (it1.hasNext()) {
//                int vertex = it1.next();
//                queue.add(new Node(vertex, dist[vertex]));
//            }
            queue.add(new Node(src, dist[src]));
            while (!(queue.size() == 0) ) {
                Node v_node = queue.remove();
                int v = v_node.getDest();
                if(cities_wit_store.contains(v)){
                    visited.add(v);
                }
                Iterator<Node> it2 = adjacency_list.get(v).listIterator();
                while (it2.hasNext()) {
                    // get one of the neighbours
                    Node w_node = it2.next();
                    int w = w_node.getDest();
                    int v_w_cost = w_node.getCost();
                    if ( dist[v] != Integer.MAX_VALUE && !(dist[v] + v_w_cost < 0)
                            && dist[v] + v_w_cost < dist[w]) {
                        dist[w] = dist[v] + v_w_cost;
                        decreaseKey(queue, w, dist[w]);
                    }
                }
            }

        }

        private void decreaseKey(PriorityQueue pq, int intersection, int new_dist) {
            Iterator<Node> it = pq.iterator();
            boolean decreased = false;

            while (it.hasNext()) {
                Node tempNode = it.next();
                if (tempNode.dest == intersection) {
                    decreased = true;
                    tempNode.cost = new_dist;
                    break;
                }
            }
            if (!decreased){
                pq.add(new Node(intersection, new_dist));
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

            // parsing n and c
            String line = in.readLine();
            String[] parts = line.split(" ");
            int n = Integer.parseInt(parts[0]);
            int m = Integer.parseInt(parts[1]);
            int g = Integer.parseInt(parts[2]);


            String objects_line = in.readLine();
            String[] string_objects = objects_line.split(" ");

            int[] objects = new int[n];
            for (int i = 0; i < n; i++) {
                objects[i] = Integer.parseInt(string_objects[i]);
            }

            Graph graph = new Graph(n,g,objects);

            for (int i = 0; i < m; i++) {
                String tunnel_line = in.readLine();
                String[] string_tunnel = tunnel_line.split(" ");
                int x_j = Integer.parseInt(string_tunnel[0]);
                int y_j = Integer.parseInt(string_tunnel[1]);
                int l_j = Integer.parseInt(string_tunnel[2]);
                graph.addUndirectedEdge(x_j,y_j,l_j);
            }

            int max = graph.result();
            String result;
            if( max != -1 ) {
                result = "" + max;
            } else {
                result = "impossible";
            }
            sb.append("Case #" + j + ": " + result + "\n");
        }
        System.out.println(sb.toString());
    }
}
