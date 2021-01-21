import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class supermarkets {

    static class Graph {

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

        class Supermarket {
            private int city;
            private int time;

            public Supermarket(int city, int time) {
                this.city = city;
                this.time = time;
            }

            public int getCity() {
                return city;
            }

            public int getTime() {
                return time;
            }
        }


        private int number_of_vertices;
        private HashMap<Integer, ArrayList<Node>> adjacency_list;
        private HashSet<Integer> intersections;
        private HashSet<Integer> visited;
        private HashSet<Supermarket> markets;
        private int dist[];
        private int rev_dist[];
        private int source;
        private int target;
        private int number_of_stores;
        private HashSet<Integer> cities_wit_store;

        public Graph(int number_of_vertices, int s, int a, int b) {
            this.number_of_stores = s;
            this.source = a;
            this.target = b;
            this.number_of_vertices = number_of_vertices;
            this.adjacency_list = new HashMap<Integer, ArrayList<Node>>();
            this.intersections = new HashSet<Integer>();
            this.cities_wit_store = new HashSet<Integer>();
            this.visited = new HashSet<Integer>();
            this.markets = new HashSet<Supermarket>();
            for (int i = 1; i <= number_of_vertices; i++) {
                addNode(i);
            }
            this.dist = new int[this.number_of_vertices+1];
            this.rev_dist = new int[this.number_of_vertices+1];
            for (int i = 1; i < dist.length; i++) {
                dist[i] = Integer.MAX_VALUE;
                rev_dist[i] = Integer.MAX_VALUE;
            }
        }

        public void addNode(int number) {
            ArrayList<Node> neighbours = new ArrayList<Node>();
            adjacency_list.put(number, neighbours);
            intersections.add(number);
        }

        public void addUndirectedEdge(int fromVertex, int toVertex, int weight){
            Node e1 = new Node(toVertex,weight);
            Node e2 = new Node(fromVertex,weight);
            (adjacency_list.get(fromVertex)).add(e1);
            (adjacency_list.get(toVertex)).add(e2);
        }

        public void addSupermarket(int c, int w){
            markets.add(new Supermarket(c,w));
            if(!cities_wit_store.contains(c)){
                cities_wit_store.add(c);
            }
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

        public String result(){
            if( number_of_stores == 0 ){
                return "impossible";
            }
            Dijkstra(this.dist,source);
            if(this.dist[target] == Integer.MAX_VALUE ){
                return "impossible";
            }
            this.visited.clear();
            this.cities_wit_store = new HashSet<Integer>();
            Dijkstra(this.rev_dist, target);
            int current_min = Integer.MAX_VALUE;
            Iterator<Supermarket> it = markets.iterator();
            while (it.hasNext()) {
                Supermarket m = it.next();
                int city = m.getCity();
                int visit_time = m.getTime();
                if( dist[city] != Integer.MAX_VALUE
                        && rev_dist[city] != Integer.MAX_VALUE
                        && !(dist[city] + visit_time < 0)
                        && !(rev_dist[city] + visit_time < 0)
                        && !(dist[city] + visit_time + rev_dist[city] < 0)
                        && dist[city] + visit_time + rev_dist[city] < current_min ){
                    current_min = dist[city] + visit_time + rev_dist[city];
                }
            }
            if( current_min == Integer.MAX_VALUE){
                return "impossible";
            }
            String result = "";
            int hours = current_min / 60;
            result = result + hours;
            int minutes = current_min % 60;
            if( minutes < 10 ){
                result = result + ":0" + minutes;
            } else {
                result = result + ":" + minutes;
            }
            return result;
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
            // parsing n, m, s, a and b
            String line = in.readLine();
            String[] parts = line.split(" ");
            int n = Integer.parseInt(parts[0]);
            int m = Integer.parseInt(parts[1]);
            int s = Integer.parseInt(parts[2]);
            int a = Integer.parseInt(parts[3]);
            int b = Integer.parseInt(parts[4]);

            Graph graph = new Graph(n,s,a,b);

            if( m != 0 ){
                for (int i = 0; i < m; i++) {
                    String dependency = in.readLine();
                    String[] vertexes = dependency.split(" ");
                    int x = Integer.parseInt(vertexes[0]);
                    int y = Integer.parseInt(vertexes[1]);
                    int z = Integer.parseInt(vertexes[2]);
                    graph.addUndirectedEdge(x,y,z);
                }
            }

            if( s != 0 ){
                for (int i = 0; i < s; i++) {
                    String dependency = in.readLine();
                    String[] vertexes = dependency.split(" ");
                    int c = Integer.parseInt(vertexes[0]);
                    int w = Integer.parseInt(vertexes[1]);
                    graph.addSupermarket(c,w);
                }
            }

            String result;
            result = graph.result();
            sb.append("Case #" + j + ": " + result + "\n");
        }
        System.out.println(sb.toString());
    }
}
