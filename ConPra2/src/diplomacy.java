import java.util.*;
class diplomacy {

    static class UnionFind{
        public int[] parent;
        public int[] size;

        public UnionFind(int n) {
            parent = new int[n];
            size = new int[n];

            for (int i = 0; i < n; i++) {
                this.size[i] = 1;
                this.parent[i] = i;
            }
        }

        public int find(int a){
            int root = a;
            while(true){
                int parent = this.parent[root];
                if (parent == root){
                    break;
                }
                root = parent;
            }

            int current = a;
            while ( current != root ){
                int next_elem = this.parent[current];
                this.parent[current] = root;
                current = next_elem;
            }
            return root;
        }

        public void union(int a_param, int b_param){
            int a = find(a_param);
            int b = find(b_param);
            if(a == b){
                return;
            }
            int a_size = size[a];
            int b_size = size[b];
            if (a_size < b_size){
                int temp = a;
                a = b;
                b = temp;
            }
            parent[b] = a;
            size[a] = a_size + b_size;
        }
        public boolean areEnemies(int u, int v, int n) {
            return find(u) == find(2*n - 1 - v);
        }
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
            // parsing s and f
            int n = scanner.nextInt();
            int m = scanner.nextInt();
            scanner.nextLine();
            UnionFind uf = new UnionFind(2 * n );
            for (int i = 0; i < m; i++) {
                String line = scanner.nextLine();
                String[] parts = line.split(" ");
                char c = parts[0].charAt(0);
                int x = Integer.parseInt(parts[1]) - 1;
                int y = Integer.parseInt(parts[2]) - 1;
                if( c == 'F' ){
                    uf.union(x,y);
                    uf.union(2*n - 1 - x, 2*n - 1 - y);
                } else {
//                    int a = uf.find(x);
//                    int b = uf.find(y);
//                    if( a == b ){
//                        uf.parent[a] = 0;
//                    }
                    uf.union( x, 2*n - 1 - y);
                    uf.union(2*n - 1 - x , y);
                }
            }
            int counter = 0;
            for (int i = 1; i < n; i++) {
                uf.areEnemies(0,i,n);
            }
            if( counter <= (n/2) ){
                sb.append("Case #" + j + ": " + "yes" + "\n");
                sb.append( uf.size[1] + "\n" );
            } else {
                sb.append("Case #" + j + ": " + "no" + "\n");
                sb.append( uf.size[1] + "\n" );
            }

        }
        System.out.println(sb.toString());
        scanner.close();
    }


}

