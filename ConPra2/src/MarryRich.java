import java.util.Scanner;

public class MarryRich {

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
//                b = a;
            }
            parent[b] = a;
            size[a] = a_size + b_size;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();
        int t = scanner.nextInt();
        scanner.nextLine();
        for (int j = 1; j <= t; j++) {
            // there is a blank line before the case lines if it is not the first case
            if( j != 1 ){
                scanner.nextLine();
            }
            // parsing a b c
            int a = scanner.nextInt();
            int b = scanner.nextInt();
            int c = scanner.nextInt();

            // parsing (a-1) the amount of money person i owns
            int[] wealths = new int[a-1];

            for (int k = 0; k < a-1; k++) {
                wealths[k] = scanner.nextInt();
            }
            //Union Find class created
            UnionFind uf = new UnionFind(a);
            // parsing the family relations and doing an union for each of them
            for (int i = 0; i < b; i++) {
                int d = scanner.nextInt() - 1;
                int e = scanner.nextInt() - 1;
                uf.union(d,e);
            }
            // initialize a boolean array for point who is married
            Boolean[] married = new Boolean[a-1];
            for (int i = 0; i < married.length; i++) {
                married[i] = false;
            }
            // parsing the marriages and doing an union for each of them
            // also point as married in boolean array
            for (int i = 0; i < c; i++) {
                int f = scanner.nextInt() - 1;
                int g = scanner.nextInt() - 1;
                if( f != g ){
                    uf.union(f,g);
                    married[f] = true;
                    married[g] = true;
                }
            }
            // comparing the results to find the most amount of the money
            int output = -1;
            for (int i = 0; i < a-1; i++) {
                if( ( uf.find(a-1) == uf.find(i) ) || married[i] ){
                    // in family/relative or married -> not possible to marry
                } else {
                    if( output == -1 || output < wealths[i] ){
                        // possible candidate for marriage
                        // if richer the output is updated
                        output = wealths[i];
                    }
                }
            }
            if( output == -1 ){
                // output is not updated
                sb.append("Case #" + j + ": impossible\n");
            } else {
                sb.append("Case #" + j + ": " + output + "\n");
            }
        }
        scanner.close();
        System.out.print(sb.toString());
    }
}
