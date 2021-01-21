import java.util.*;

public class networking {

    static class edge {
        // inner class for edges in order to store
        public int fromNode;
        public int toNode;

        public edge(int node1, int node2) {
            if( node1 < node2) {
                this.fromNode = node1;
                this.toNode = node2;
            } else {
                this.fromNode = node2;
                this.toNode = node1;
            }
        }

        public int getFromNode() {
            return fromNode;
        }

        public int getToNode() {
            return toNode;
        }

    }

    private static void order(List<edge> lines) {
        // sort function for list
        Collections.sort( lines, new Comparator() {
            // comparation first with from Nodes
            public int compare(Object o1, Object o2) {

                Integer x1 = ((edge) o1).getFromNode();
                Integer x2 = ((edge) o2).getFromNode();
                int sComp = x1.compareTo(x2);
                // if equal proceed with seconds
                if (sComp != 0) {
                    return sComp;
                }

                Integer y1 = ((edge) o1).getToNode();
                Integer y2 = ((edge) o2).getToNode();
                return y1.compareTo(y2);
            }});
    }

    public static int PQgetMIN(int key[], Boolean included[], int n) {
        int current_min = Integer.MAX_VALUE;
        int index_of_current_min = -1;
        for (int v = 0; v < n; v++)
            // update if there is another not visited node with less loud (weight)
            if (included[v] == false && key[v] < current_min) {
                current_min = key[v];
                index_of_current_min = v;
            }
        // return the minimum
        return index_of_current_min;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();
        int t = scanner.nextInt();
        scanner.nextLine();

        for (int j = 1; j <= t; j++) {
            if (j != 1) {
                scanner.nextLine();
            }
            // parsing n
            int n = scanner.nextInt();
            scanner.nextLine();
            // the adjacency matrix graph
            int graph[][] = new int[n][n];
            // parsing the louds ( weights )
            for (int i = 0; i < n; i++) {
                for (int k = 0; k < n; k++) {
                    graph[i][k] = scanner.nextInt();
                }
            }
            // Prim's algorithm
            // https://www.geeksforgeeks.org/prims-minimum-spanning-tree-mst-greedy-algo-5/
            // and lecture slides
            // nodes already included
            Boolean[] visited = new Boolean[n];
            // values
            int[] c = new int[n];
            // the predecessor of each node
            int[] predecessor = new int[n];
            // initialing start values
            for (int i = 0; i < n; i++) {
                // infinity
                c[i] = Integer.MAX_VALUE;
                // not visited
                visited[i] = false;
            }
            // starting with a node
            c[0] = 0;
            // no predecessor yet
            predecessor[0] = -1;
            // the total number of nodes should be n-1
            for (int count = 0; count < n - 1; count++) {
                // get the minimum value that is not visited
                // our PQ
                int u = PQgetMIN(c, visited, n);
                // PrimVisitStep
                visited[u] = true;
                for (int v = 0; v < n; v++) {
                    if ( u != v // if not reflexive
                            && visited[v] == false  // and not visited yet
                            && graph[u][v] < c[v]) { // the loud (weight) is smaller than the initial
                        // update
                        predecessor[v] = u;
                        c[v] = graph[u][v];
                    }
                }
            }
            // save the results as edges
            List<edge> results = new ArrayList<edge>();
            for (int i = 1; i < n; i++){
                results.add(new edge(predecessor[i], i));
            }
            // order the results lexicographically
            order(results);
            sb.append("Case #" + j + ":\n");
            for(int i = 0; i < results.size(); i++) {
                sb.append((results.get(i).fromNode + 1) + " " + (results.get(i).toNode + 1) + "\n");
            }

        }
        scanner.close();
        System.out.println(sb.toString());
    }

}
