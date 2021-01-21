import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class party_planning {

    static class Graph {

        class Node implements Comparable<Node> {
            private int task;
            private int cost;

            public Node(int task, int cost) {
                this.task = task;
                this.cost = cost;
            }

            public int compareTo(Node compareNode) {
                if (Integer.compare(this.cost, compareNode.cost) == 0){
                    return 0;
                }
                else if (Integer.compare(this.cost, compareNode.cost) < 0){
                    return -1;
                }
                else {
                    return 1;
                }
            }

            public int getTask() {
                return task;
            }

            public int getCost() {
                return cost;
            }
        }


        private int number_of_vertices;
        private HashMap<Integer, ArrayList<Integer>> adjacency_list;
        private HashMap<Integer, Integer> task;
        private HashSet<Integer> intersections;
        private int[] o;
        private int[] pre;
        private int[] dist;
        private boolean[] visited;

        public Graph(int number_of_vertices) {
            this.number_of_vertices = number_of_vertices;
            this.adjacency_list = new HashMap<Integer, ArrayList<Integer>>();
            this.intersections = new HashSet<Integer>();
            this.task = new HashMap<Integer, Integer>();
            for (int i = 1; i <= number_of_vertices; i++) {
                addNode(i);
            }
            this.pre = new int[this.number_of_vertices+1];
            this.o = new int[this.number_of_vertices+1];
            this.dist = new int[this.number_of_vertices+1];
            this.visited = new boolean[this.number_of_vertices+1];
            for (int i = 1; i < pre.length; i++) {
                pre[i] = 0;
                o[i] = Integer.MAX_VALUE;
                dist[i] = Integer.MIN_VALUE;
                visited[i] = false;
            }
        }

        public void addNode(int number) {
            ArrayList<Integer> neighbours = new ArrayList<Integer>();
            adjacency_list.put( number , neighbours);
            intersections.add(number);
        }

        public void addTask(int number,int cost) {
            task.put(number,cost);
//            dist[number] = cost;
        }

        public void addDirectedEdge(int fromVertex, int toVertex){
            (adjacency_list.get(fromVertex)).add(toVertex);
            pre[toVertex] = pre[toVertex] + 1;
        }

        public int TopologicalSort(){
            Queue<Integer> toposort = new LinkedList<>();
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
                        toposort.add(v);
//                        System.out.println(o[v]);
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
            dist[1] = 0;
            while(!toposort.isEmpty()){
                int a = toposort.remove();
                Iterator<Integer> it2 = adjacency_list.get(a).listIterator();

                while (it2.hasNext()) {
                    int u = it2.next();
                    if( task.get(a) + dist[a]  > dist[u] ){
                        dist[u] = dist[a] + task.get(a);
                    }
                }

            }
            return dist[number_of_vertices] + task.get(number_of_vertices);
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
            int n =  Integer.parseInt(in.readLine());

            Graph graph = new Graph(n);

            if( n != 0 ){
                for (int i = 1; i <= n; i++) {
                    String dependency = in.readLine();
                    String[] vertexes = dependency.split(" ");
                    int p = Integer.parseInt(vertexes[0]);
                    int s = Integer.parseInt(vertexes[1]);
                    graph.addTask(i,p);
                    for (int k = 1; k <= s; k++) {
                        int si = Integer.parseInt(vertexes[1+k]);
                        graph.addDirectedEdge(i,si);
                    }
                }
            }

            int result = graph.TopologicalSort();
            sb.append("Case #" + j + ": " + result + "\n");

        }
        System.out.println(sb.toString());
    }
}

