import java.util.Scanner;

public class diplo {
    // implementation from the lecture
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

    }

    private static final int no_enemy = -1;
    private static UnionFind uf;
    private static int[] enemies;

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

            uf = new UnionFind(n);
            enemies = new int[n];
            for (int i = 0; i < n; i++) {
                enemies[i] = no_enemy;
            }

            for (int i = 0; i < m; i++) {
                String line = scanner.nextLine();
                String[] parts = line.split(" ");
                char c = parts[0].charAt(0);
                int x = Integer.parseInt(parts[1]) - 1;
                int y = Integer.parseInt(parts[2]) - 1;
                if( c == 'F' ){
                    setFriends(x,y);
                } else {
                    setEnemies(x,y);
                }
            }
            int counter = 0;
            for (int i = 1; i < n; i++) {
                if( uf.find(0) == uf.find(i) ){
                    counter += 1;
                }
            }
            if( counter >= (n/2) ){
                sb.append("Case #" + j + ": " + "yes" + "\n");
            } else {
                sb.append("Case #" + j + ": " + "no" + "\n");
            }
        }
        System.out.println(sb.toString());
        scanner.close();
    }

    private static void setFriends(int x, int y) {
        // find the root of x and y, refer to union find class
        Integer root_of_x = uf.find(x);
        Integer root_of_y = uf.find(y);
        // getting the enemies of x and y
        Integer enemy_of_x = enemies[root_of_x];
        Integer enemy_of_y = enemies[root_of_y];
        // merging x and y
        uf.union(x, y);

        Integer newRoot = uf.find(root_of_x);
        if (enemy_of_x != enemy_of_y
                && enemy_of_x != no_enemy
                && enemy_of_y != no_enemy) {
            // x and y are not enemies to each other
            // but already has other enemies
            uf.union(enemy_of_x, enemy_of_y);
            // the enemies of them are now ally
            Integer newEnemy = uf.find(enemy_of_x);
            enemies[newRoot] = newEnemy;
        } else if (enemy_of_x != no_enemy) {
            enemies[newRoot] = enemy_of_x;
        } else if (enemy_of_y != no_enemy) {
            enemies[newRoot] = enemy_of_y;
        }

    }

    private static void setEnemies(int x, int y) {
        // find the root of x and y, refer to union find class
        int root_of_x = uf.find(x);
        int root_of_y = uf.find(y);
        // get the enemy of x and y
        int enemy_of_x = enemies[root_of_x];
        int enemy_of_y = enemies[root_of_y];

        if ( enemy_of_x == no_enemy ) {
            // x had no enemy
            enemies[root_of_x] = root_of_y;
        }
        if ( enemy_of_y == no_enemy ) {
            // y had no enemy
            enemies[root_of_y] = root_of_x;
        }
        if ( enemies[root_of_x] == root_of_y
                && enemies[root_of_y] == root_of_x) {
            // enemy of x is the enemy of y transitively
            // nothing to do
            return;
        }

        if (enemies[root_of_x] != root_of_y) {
            // the enemy of x is not the enemy of y so merge them
            uf.union(enemies[root_of_x], root_of_y);
        }
        if (enemies[root_of_y] != root_of_x) {
            // the enemy of y is not the enemy of x so merge them
            uf.union(enemies[root_of_y], root_of_x);
        }
    }

    // idea adapted from https://belbesy.wordpress.com/2011/08/26/uva-10158-war/amp/

}

