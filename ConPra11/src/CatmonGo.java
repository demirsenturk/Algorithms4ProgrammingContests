import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CatmonGo {

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
            build( a, 1 ,1, n );
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
            propagate(p);
            if( l > t[p].r || r < t[p].l ){
                return 0;
            }
            if ( l <= t[p].l && t[p].r <= r ) {
                return t[p].v;
            }
            return sum(2*p + 1,l,r) + sum(2*p + 2,l,r);
        }

        public void decrement(int p, int i){
            if( i < t[p].l || i > t[p].r){
                return;
            }
            if( t[p].v > 0 && !t[p].lazy ) {
                t[p].v = t[p].v - 1;
            } else if( t[p].lazy ){
                propagate(p);
            }
            if ( t[p].l != t[p].r ) {
                decrement( 2*p + 1, i);
                decrement(2*p + 2, i);
            }
        }

        public void add(int p, int i, int v){
            if( i < t[p].l || i > t[p].r){
                return;
            }
            if( t[p].lazy ){
                propagate(p);
                t[p].v = v;
            } else {
                t[p].v = t[p].v + v;
            }
            if ( t[p].l != t[p].r ) {
                add( 2*p + 1, i, v);
                add(2*p + 2, i, v);
            }
        }

        public void propagate(int p){
            if(t[p].lazy) {
                t[p].v = 0;
            }
            if (t[p].l != t[p].r ){
                t[2*p + 1].lazy = t[2*p + 1].lazy || t[p].lazy;
                t[2*p + 2].lazy = t[2*p + 2].lazy || t[p].lazy;
            }
            t[p].lazy = false;
        }

        public void RangeAdd(int p, int l, int r){
            propagate(p);
            if ( l > t[p].r || r < t[p].l ){
                return;
            }
            if ( l <= t[p].l && t[p].r <= r ) {
                t[p].lazy = true;
                propagate(p);
            } else if ( t[p].l != t[p].r ){
                RangeAdd( 2*p + 1, l, r);
                RangeAdd( 2*p + 2, l, r);
                if( t[2*p + 1].lazy && t[2 * p + 2].lazy ){
                    t[p].v = 0;
                } else if( t[2*p + 1].lazy ){
                    t[p].v = 0 + t[2 * p + 2].v;
                } else if( t[2 * p + 2].lazy ){
                    t[p].v = t[2 * p + 1].v + 0;
                } else {
                    t[p].v = t[2 * p + 1].v + t[2 * p + 2].v;
                }
            }
        }

    }

    static class Node{
        private int v;
        private int l;
        private int r;
        private boolean lazy;

        public Node(int v, int l, int r) {
            this.v = v;
            this.l = l;
            this.r = r;
            this.lazy = false;
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

            // parsing n and k
            String i_line = in.readLine();
            String[] i_line_parts = i_line.split(" ");
            int n = Integer.parseInt(i_line_parts[0]);
            int k = Integer.parseInt(i_line_parts[1]);

            SegmentTree st = new SegmentTree(n);

            long x = 0;
            // k queries
            for (int i = 0; i < k; i++) {
                String i_query_line = in.readLine();
                String[] query_parts = i_query_line.split(" ");
                if( query_parts[0].equals("s") && query_parts.length == 2 ){
                    int a = Integer.parseInt(query_parts[1]);
                    st.add(1,a,1);
                } else if( query_parts[0].equals("d") && query_parts.length == 2 ){
                    int a = Integer.parseInt(query_parts[1]);
                    int sum = st.sum(1,a,a);
                    if( sum > 0 ){
                        st.add(1,a,-1);
                    }
//                    st.decrement(1, a);
                } else if ( query_parts[0].equals("c") && query_parts.length == 3 ) {
                    int l_i = Integer.parseInt(query_parts[1]);
                    int r_i = Integer.parseInt(query_parts[2]);
                    int catched_count = st.sum(1,l_i,r_i);
                    st.RangeAdd(1,l_i,r_i);
                    x += catched_count;
                }
            }



            String result = "" + x;
            sb.append("Case #" + j + ": " + result + "\n");
        }
        System.out.println(sb.toString());
    }
}
