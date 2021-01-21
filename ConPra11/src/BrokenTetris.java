import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BrokenTetris {

    static class SegmentTree{

        private Node[] t;

        public SegmentTree(int n) {
            int rounded_n = (int) (Math.ceil( Math.log(n) / Math.log(2)) ) + 1;
            int size = 2 * (int) Math.pow(2, rounded_n);
            t = new Node[size];
            int[] a = new int[n+1];
            for (int i = 0; i < t.length; i++) {
                t[i] = new Node(0,-1,-1);
            }
            build( a, 0 ,0, n);
        }

        // Code source all functions from lecture slides

        public int build( int[] a, int p, int l, int r){
            t[p].l = l;
            t[p].r = r;
            if( l == r ){
                t[p].v = a[l];
                return t[p].v;
            }
            int m = (l + r) / 2;
            t[p].v = build(a,2 * p + 1, l, m) + build( a,2*p + 2 ,m+1, r);
            return t[p].v;
        }

        public int sum( int p, int l, int r){
            if( l > t[p].r || r < t[p].l ){
                return 0;
            }
            propagate(p);
            if ( l <= t[p].l && t[p].r <= r ) {
                return t[p].v;
            }
            return sum(2*p + 1,l,r) + sum(2*p + 2,l,r);
        }

        public void add(int p, int i, int v){
            if( i < t[p].l || i > t[p].r){
                return;
            }
            t[p].v = t[p].v + v;
            if ( t[p].l != t[p].r ) {
                add( 2*p + 1, i, v);
                add(2*p + 2, i, v);
            }
        }

        public void propagate(int p){
            t[p].v = t[p].v + (t[p].r - t[p].l + 1) * t[p].lazy;
            if (t[p].l != t[p].r ){
                t[2*p + 1].lazy = t[2*p + 1].lazy + t[p].lazy;
                t[2*p + 2].lazy = t[2*p + 2].lazy + t[p].lazy;
            }
            t[p].lazy = 0;
        }

        public void RangeAdd(int p, int l, int r, int v){
            propagate(p);
            if ( l > t[p].r || r < t[p].l ){
                return;
            }
            if ( l <= t[p].l && t[p].r <= r ) {
                t[p].lazy = t[p].lazy + v;
                propagate(p);
            } else if ( t[p].l != t[p].r ){
                RangeAdd( 2*p + 1, l, r , v );
                RangeAdd( 2*p + 2, l, r , v );
                t[p].v = t[2*p + 1].v + t[2*p + 2].v;
            }
        }

    }

    static class Node{
        private int v;
        private int l;
        private int r;
        private int lazy;

        public Node(int v, int l, int r) {
            this.v = v;
            this.l = l;
            this.r = r;
            this.lazy = 0;
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
            sb.append("Case #" + j + ": " );

            // parsing n and k
            String i_line = in.readLine();
            String[] i_line_parts = i_line.split(" ");
            int n = Integer.parseInt(i_line_parts[0]);
            int k = Integer.parseInt(i_line_parts[1]);

            SegmentTree st = new SegmentTree(n);

            long x = 0;
            String result = "";
            // k queries
            x = 0;
            for (int i = 0; i < k; i++) {
                String i_query_line = in.readLine();
                String[] query_parts = i_query_line.split(" ");
                // parse w, h and p
                int w = Integer.parseInt(query_parts[0]);
                int h = Integer.parseInt(query_parts[1]);
                int p = Integer.parseInt(query_parts[2]);
                int l_i = p;
                int r_i = p + w;
                int range_max = 0;
                int[] sums = new int[r_i-l_i+1];
                for (int l = l_i; l < r_i; l++) {
                    int temp = st.sum(0,l,l);
                    sums[l-l_i] = temp;
                    if( temp > range_max ){
                        range_max = temp;
                    }
                }
                for (int l = l_i; l < r_i; l++) {
                    st.add(0, l, range_max + h - sums[l-l_i]);
                }
                if( x < range_max + h ){
                    x = range_max + h;
                }
                if( i != 0 ) {
                    sb.append(" " + x);
                } else {
                    sb.append(x);
                }
            }

            sb.append("\n");
        }
        System.out.println(sb.toString());
    }
}
