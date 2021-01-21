import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class currency_exchange {

    static class Graph {

        class Node implements Comparable<Node> {
            private int dest;
            private double weight;

            public Node(int dest, double weight) {
                this.dest = dest;
                this.weight = weight;
            }

            public int compareTo(Node compareNode) {
                if (Double.compare(this.weight, compareNode.weight) == 0){
                    return 0;
                }
                else if (Double.compare(this.weight, compareNode.weight) < 0){
                    return -1;
                }
                else {
                    return 1;
                }
            }

            public int getDest() {
                return dest;
            }

            public double getWeight() {
                return weight;
            }
        }

        class Edge implements Comparable<Edge> {
            private int from;
            private int dest;
            private double weight;

            public Edge(int from, int dest, double weight) {
                this.from = from;
                this.dest = dest;
                this.weight = weight;
            }

            public int compareTo(Edge compareNode) {
                if (Double.compare(this.weight, compareNode.weight) == 0){
                    return 0;
                }
                else if (Double.compare(this.weight, compareNode.weight) < 0){
                    return -1;
                }
                else {
                    return 1;
                }
            }

            public int getFrom() {
                return from;
            }

            public int getDest() {
                return dest;
            }

            public double getWeight() {
                return weight;
            }
        }


        private int number_of_vertices;
        private HashMap<Integer, ArrayList<Node>> adjacency_list;
        private HashSet<Edge> edges;
        private HashSet<Integer> intersections;
        private double dist[];

        public Graph(int number_of_vertices) {
            this.number_of_vertices = number_of_vertices;
            this.adjacency_list = new HashMap<Integer, ArrayList<Node>>();
            this.intersections = new HashSet<Integer>();
            this.edges = new HashSet<Edge>();
            for (int i = 1; i <= number_of_vertices; i++) {
                addNode(i);
            }
            this.dist = new double[this.number_of_vertices+1];
            for (int i = 1; i < dist.length; i++) {
                dist[i] = Double.MAX_VALUE;
            }
        }

        public void addNode(int number) {
            ArrayList<Node> neighbours = new ArrayList<Node>();
            adjacency_list.put(number, neighbours);
            intersections.add(number);
        }

        public void addDirectedEdge(int fromVertex, int toVertex, double weight){
            Node e1 = new Node( toVertex, Math.log(weight) );
            (adjacency_list.get(fromVertex)).add(e1);

            edges.add(new Edge(fromVertex,toVertex, Math.log(weight)));
        }

//        public String BellmanFord() {
//            dist[1] = 0;
//            Queue<Integer> Q = new LinkedList<Integer>();
//            Queue<Integer> Q2 = new LinkedList<Integer>();
//            Q.add(1);
//            // 1 to |V|
//            for (int i = 1; i < number_of_vertices; i++) {
//                while(Q.size() != 0){
//                    int v = Q.poll();
//                    // each neighbor of w of v
//                    Iterator<Node> it = adjacency_list.get(v).listIterator();
//                    while (it.hasNext()) {
//                        Node w_node = it.next();
//                        int w = w_node.getDest();
//                        double v_w_cost = w_node.getWeight();
//                        if( dist[v] != Double.MAX_VALUE
//                                && dist[v] + v_w_cost < dist[w] ){
//                            dist[w] = dist[v] + v_w_cost;
//                            // w not in Q′
//                            if(Q2.contains(w)){
//                                Q2.add(w);
//                            }
//                        }
//                    }
//                } // end while
//                // swap(Q ,Q′)
//                Queue<Integer> temp = Q;
//                Q = Q2;
//                Q2 = temp;
//            } // end for
//            if( Q.size() != 0 ){
//                // there exists a negative cycle
//                return "Jackpot";
//            }
//            if( dist[number_of_vertices] == Double.MAX_VALUE ){
//                return "impossible";
//            }
//            double result = Math.exp(dist[number_of_vertices]);
//            return Double.toString(result);
//        }

        public String BellmanFord2() {
            for (int i = 1; i <= number_of_vertices; i++) {
                dist[i] = Double.MAX_VALUE;
            }
            dist[1] = 0;

            for (int i = 1; i < number_of_vertices; i++) {
                for (int u = 1; u <= number_of_vertices; u++) {
                    Iterator<Node> it = adjacency_list.get(u).listIterator();
                    while (it.hasNext()) {
                        Node v_node = it.next();
                        int v = v_node.getDest();
                        double c = v_node.getWeight();
                        if (dist[u] != Double.MAX_VALUE
                                && dist[u] + c < dist[v]) {
                            dist[v] = dist[u] + c;
                        }
                    }
                }
            }
            Iterator<Edge> it2 = edges.iterator();
            while (it2.hasNext()) {
                Edge e = it2.next();
                int u = e.getFrom();
                int v = e.getDest();
                double c = e.getWeight();
                if (dist[u] != Double.MAX_VALUE
                        && dist[u] + c < dist[v]) {
//                    System.out.println(dist[u] + " + " + c + " < " + dist[v]);
                    return "Jackpot";
                }
            }
            if( dist[number_of_vertices] == Double.MAX_VALUE ){
                return "impossible";
            }
            double result = Math.exp(dist[number_of_vertices]);
            return Double.toString(result);
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
            // parsing n, m
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
                    double c = Double.parseDouble(vertexes[2]);
                    graph.addDirectedEdge(v,w,c);
                }
            }
            String result;
            result = graph.BellmanFord2();
            sb.append("Case #" + j + ": " + result + "\n");
        }
        System.out.println(sb.toString());
    }
}


