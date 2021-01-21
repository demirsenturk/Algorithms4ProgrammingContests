import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Treehouse {

    static class Graph {

        static class Node {
            private int index;

            public Node(int index) {
                this.index = index;
            }
        }

        private int n;
        private HashMap<Integer, ArrayList<Node>> adjacency_list;

        private HashSet<Integer> visited;
        private static int[] euler_tour;
        private int idx;
        private static int[] depth;
        private static int[] first;

        private static Node_seg[] segmentTree;

        public Graph(int n) {
            this.n = n;
            this.adjacency_list = new HashMap<Integer, ArrayList<Node>>();
            for (int i = 1; i <= n; i++) {
                addNode(i);
            }
            euler_tour = new int[2*n-1];
            this.depth = new int[n+1];
            this.first = new int[n+1];
            this.visited = new HashSet<Integer>();
            this.idx = 0;

            // Segment Tree
            int rounded_n = (int) (Math.ceil( Math.log(depth.length) / Math.log(2)) ) + 1;
            int size = 2 * (int) Math.pow(2, rounded_n);
            // size of the segment tree ( power of 2 )
            this.segmentTree = new Node_seg[size];
            // initialize empty segment tree
            for (int i = 0; i < segmentTree.length; i++) {
                segmentTree[i] = new Node_seg();
            }
        }

        public void addNode(int number) {
            ArrayList<Node> neighbours = new ArrayList<Node>();
            adjacency_list.put(number, neighbours);
        }

        public void addUndirectedEdge(int fromVertex, int toVertex){
            Node e1 = new Node(toVertex);
            Node e2 = new Node(fromVertex);
            (adjacency_list.get(fromVertex)).add(e1);
            (adjacency_list.get(toVertex)).add(e2);
        }

        public void eulerTour(Node u, int h) {
            // calculate the euler tour representation of the tree
            visited.add(u.index);
            this.euler_tour[idx] = u.index;
            this.depth[u.index] = h;
            this.first[u.index] = idx;
            idx++;
            // travel through the children similar to DFS
            ArrayList<Node> children = this.adjacency_list.get(u.index);
            for (int j = 0; j < children.size(); j++) {
                Node cur = children.get(j);
                if( !visited.contains(cur.index) ){
                    // if not visited -> visit and update values
                    eulerTour(cur, h + 1);
                    this.euler_tour[idx] = u.index;
                    this.depth[u.index] = h;
                    idx++;
                }
            }
        }

        // build and query methods changed from source:
        // https://cp-algorithms.com/graph/lca.html

        void build(int node, int b, int e) {
            if (b == e) {
                // assign the node_index to the segment_tree node
                segmentTree[node].index = euler_tour[b];
            } else {
                int mid = (b + e) / 2;
                build(node << 1, b, mid);
                build(node << 1 | 1, mid + 1, e);
                // calculate the minimum of left and right side of the trees
                int l = segmentTree[node << 1].index;
                int r = segmentTree[node << 1 | 1].index;
                // take the minimum of them to the father
                segmentTree[node].index = ( depth[l] < depth[r] ) ? l : r;
            }
        }

        int query(int node, int b, int e, int L, int R) {
            if (b > R || e < L) {
                // out of range
                return -1;
            }
            if (b >= L && e <= R) {
                // of in the interval -> return the node_index
                return segmentTree[node].index;
            }
            int mid = (b + e) >> 1;
            // find the minimum node index of the left and right subtrees
            int left = query(node << 1, b, mid, L, R);
            int right = query(node << 1 | 1, mid + 1, e, L, R);
            if (left == -1) {
                // if left side out of range return right
                return right;
            }
            if (right == -1) {
                // if right side out of range return left
                return left;
            }
            // return the node_index with minimum depth
            return depth[left] < depth[right] ? left : right;
        }

        public int distance(int u, int v) {
            // calculate the min distance between two nodes
            int left = first[u];
            int right = first[v];
            if (left > right) {
                int temp = left;
                left = right;
                right = temp;
            }
            // find the lca of the nodes u and v
            int lca = query(1 ,0, euler_tour.length - 1, left, right);
            // LCA(u,v) is the node associated to the minimum in the interval [x,y]
            // of the depth array, where x and y are the indices of the
            // first occurrences of u and v in the ETR.

            // calculate the distance of the nodes
            int diff = 0;
            if( lca == v ){
                // if lca is one of the nodes
                // calculate the distance between them directly
                diff = depth[u] - depth[v];
            } else if ( lca == u ){
                // if lca is one of the nodes
                // calculate the distance between them directly
                diff =  depth[v] - depth[u];
            } else {
                // if lca is not equal one of the nodes u and v
                // calculate the distance |u-lca| + |v-lca|
                diff = Math.abs(depth[lca] - depth[u]) + Math.abs(depth[v] - depth[lca]);
            }
            return diff;
        }

        static class Node_seg{
            // Node class for segment tree
            private int index;

            public Node_seg() {
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

            // parsing n
            int n = Integer.parseInt(in.readLine());

            // initialize graph for the tree
            Graph g = new Graph(n);

            for (int i = 1; i <= n; i++) {
                String i_branch_line = in.readLine();
                String[] branch_parts = i_branch_line.split(" ");
                int c_i = Integer.parseInt(branch_parts[0]);
                if( c_i != 0 ) {
                    for (int k = 1; k <= c_i; k++) {
                        // add the edges of the tree
                        int bi_j = Integer.parseInt(branch_parts[k]);
                        g.addUndirectedEdge(i,bi_j);
                    }
                }
            }
            // calculate the euler tour representation of the tree
            g.eulerTour(new Graph.Node(1), 0);
            // Build a segment tree on the depth array using the minimum operator
            g.build(1, 0, g.euler_tour.length-1);

            long x = 0;
            String i_line = in.readLine();
            String[] line_parts = i_line.split(" ");
            int v = Integer.parseInt(line_parts[0]);
            // start from the ground ( node number 1 )
            int cur_branch = 1;
            for (int k = 1; k <= v; k++) {
                // travel to v_i
                int v_i = Integer.parseInt(line_parts[k]);
                // calculate the distance between current branch and v_i
                // add it to the total distance
                x += g.distance(cur_branch, v_i);
                // you are now at v_i -> update cur_branch
                cur_branch = v_i;
            }

            String result = "" + x;
            sb.append("Case #" + j + ": " + result + "\n");
        }
        System.out.println(sb.toString());
    }
}
