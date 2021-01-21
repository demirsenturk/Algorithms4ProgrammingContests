import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class diplomacy2 {

    private static final int NOBODY = -1;

    private static UnionSet<Integer> unionSet;

    private static int[] enemyOf;

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

            unionSet = new UnionSet<Integer>();
            enemyOf = new int[n];
            for (int i = 0; i < n; i++) {
                unionSet.makeSet(i);
                enemyOf[i] = NOBODY;
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
                if( areFriends(0,i) ){
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
        Integer rootX = unionSet.getRoot(x);
        Integer rootY = unionSet.getRoot(y);
        Integer enemyX = enemyOf[rootX];
        Integer enemyY = enemyOf[rootY];
        unionSet.union(x, y);

        Integer newRoot = unionSet.getRoot(rootX);
        if (enemyX != enemyY && enemyX != NOBODY && enemyY != NOBODY) {
            unionSet.union(enemyX, enemyY);
            Integer newEnemy = unionSet.getRoot(enemyX);
            enemyOf[newRoot] = newEnemy;
        } else if (enemyX != NOBODY) {
            enemyOf[newRoot] = enemyX;
        } else if (enemyY != NOBODY) {
            enemyOf[newRoot] = enemyY;
        }
    }

    private static void setEnemies(int x, int y) {
        Integer rootX = unionSet.getRoot(x);
        Integer rootY = unionSet.getRoot(y);

        Integer enemyX = enemyOf[rootX];
        Integer enemyY = enemyOf[rootY];

        if (enemyX == NOBODY) {
            enemyOf[rootX] = rootY;
        }
        if (enemyY == NOBODY) {
            enemyOf[rootY] = rootX;
        }
        if (enemyOf[rootX] == rootY && enemyOf[rootY] == rootX)
            return;

        if (enemyOf[rootX] != rootY)
            unionSet.union(enemyOf[rootX], rootY);
        if (enemyOf[rootY] != rootX)
            unionSet.union(enemyOf[rootY], rootX);
    }

    private static boolean areFriends(int x, int y) {
        return unionSet.areOnSameSet(x, y);
    }

    private static boolean areEnemies(int x, int y) {
        Integer rootX = unionSet.getRoot(x);
        Integer rootY = unionSet.getRoot(y);
        return enemyOf[rootX] == rootY;
    }

    private static class UnionSet<T> {

        private Map<T, UnionSetElement<T>> dataToElement = new HashMap<T, UnionSetElement<T>>();

        public void makeSet(T data) {
            dataToElement.put(data, createElement(data));
        }

        public void makeSets(Iterable<T> datas) {
            for (T data : datas) {
                dataToElement.put(data, createElement(data));
            }
        }

        public boolean areOnSameSet(T x, T y) {
            UnionSetElement<T> xElement = dataToElement.get(x);
            UnionSetElement<T> yElement = dataToElement.get(y);
            return find(xElement) == find(yElement);
        }

        public T getRoot(T x) {
            UnionSetElement<T> element = dataToElement.get(x);
            UnionSetElement<T> root = find(element);
            return root.data;
        }

        public void union(T a, T b) {
            UnionSetElement<T> aElement = dataToElement.get(a);
            UnionSetElement<T> bElement = dataToElement.get(b);
            union(aElement, bElement);
        }

        private UnionSetElement<T> createElement(T data) {
            UnionSetElement<T> element = new UnionSetElement<T>();
            element.parent = element;
            element.rank = 0;
            element.data = data;
            return element;
        }

        // i.e. getRoot
        private UnionSetElement<T> find(UnionSetElement<T> element) {
            if (element.parent != element) {
                element.parent = find(element.parent);
            }
            return element.parent;
        }

        private void union(UnionSetElement<T> x, UnionSetElement<T> y) {
            UnionSetElement<T> xRoot = find(x);
            UnionSetElement<T> yRoot = find(y);
            if (xRoot == yRoot)
                return;

            // x and y are not already in same set. Merge them.
            if (xRoot.rank < yRoot.rank) {
                xRoot.parent = yRoot;
            } else if (xRoot.rank > yRoot.rank) {
                yRoot.parent = xRoot;
            } else {
                yRoot.parent = xRoot;
                xRoot.rank = xRoot.rank + 1;
            }

        }

        private static class UnionSetElement<T> {
            public UnionSetElement<T> parent;
            public int rank;
            public T data;
        }

    }
}
