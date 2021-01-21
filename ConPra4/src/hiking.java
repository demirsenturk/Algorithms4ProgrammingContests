import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class hiking {

    static class Graph {

        class Node implements Comparable<Node> {
            private int dest;
            private int weight;

            public Node(int dest, int weight) {
                this.dest = dest;
                this.weight = weight;
            }

            public int compareTo(Node compareNode) {
                return this.weight - compareNode.weight;
            }

            public int getDest() {
                return dest;
            }

            public int getWeight() {
                return weight;
            }
        }

        private int number_of_vertices;
        private HashMap<Integer, ArrayList<Node>> adjacency_list;
        private HashSet<Integer> intersections;
        private HashSet<Integer> visited_rooms;
        private int dist[];

        public Graph(int number_of_vertices) {
            this.number_of_vertices = number_of_vertices;
            this.adjacency_list = new HashMap<Integer, ArrayList<Node>>();
            this.intersections = new HashSet<Integer>();
            this.visited_rooms = new HashSet<Integer>();
            for (int i = 1; i <= number_of_vertices; i++) {
                addNode(i);
            }
            this.dist = new int[this.number_of_vertices+1];
            for (int i = 1; i < dist.length; i++) {
                dist[i] = Integer.MAX_VALUE;
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

        public int Dijkstra() {
            dist[1] = 0;
            PriorityQueue<Node> queue = new PriorityQueue<Node>();
            Iterator<Integer> it1 = intersections.iterator();
            while (it1.hasNext()) {
                int vertex = it1.next();
                queue.add(new Node(vertex, dist[vertex]));
            }
//            queue.add(new Node(1,dist[0]));
            while (!(queue.size() == 0) ) {
                Node v_node = queue.remove();
                int v = v_node.getDest();
//                visited_rooms.add(v_node);
                Iterator<Node> it2 = adjacency_list.get(v).listIterator();
                while (it2.hasNext()) {
                    // get one of the neighbours
                    Node w_node = it2.next();
                    int w = w_node.getDest();
                    int v_w_cost = w_node.getWeight();
                    if ( dist[v] != Integer.MAX_VALUE && !(dist[v] + v_w_cost < 0)
                            && dist[v] + v_w_cost < dist[w]) {
                        dist[w] = dist[v] + v_w_cost;
                        decreaseKey(queue, w, dist[w]);
                    }
                }
            }
            return dist[number_of_vertices];
        }

        private void decreaseKey(PriorityQueue pq, int intersection, int new_dist) {
               Iterator<Node> it = pq.iterator();
                boolean decreased = false;

                while (it.hasNext()) {
                    Node tempNode = it.next();
                    if (tempNode.dest == intersection) {
                        decreased = true;
                        tempNode.weight = new_dist;
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
            // parsing n,m
            String line = in.readLine();
            String[] parts = line.split(" ");
            int n = Integer.parseInt(parts[0]);
            int m = Integer.parseInt(parts[1]);

            Graph graph = new Graph(n);

            if( m != 0 ){
                for (int i = 0; i < m; i++) {
                    String dependency = in.readLine();
                    String[] vertexes = dependency.split(" ");
                    int v = Integer.parseInt(vertexes[0]);
                    int w = Integer.parseInt(vertexes[1]);
                    int c = Integer.parseInt(vertexes[2]);
                    graph.addUndirectedEdge(v,w,c);
                }
            }
            int result;
            if( n == 1 ){
                result = 0;
            } else {
                result = graph.Dijkstra();
            }
            sb.append("Case #" + j + ": " + result + "\n");

        }
        System.out.println(sb.toString());
    }
}

