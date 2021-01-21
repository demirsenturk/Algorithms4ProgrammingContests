import java.util.*;

public class room2 {

    // extended the BST source https://knpcode.com/java-programs/tree-sort-java-program/
    // added new rank and duplicate storage features
    static class Node{
        int room_number;
        int count;
        int size;
        Node left;
        Node right;
        Node( int room_number){
            this.room_number = room_number;
            this.count = 0;
            this.size = 1;
            left = null;
            right = null;
        }
    }

    static class BST{
        Node node = null;

        BST(int room_number){
            node = new Node(room_number);
        }

        public static Node insert(Node node, int room_number){
            if(node == null){
                return new Node(room_number);
            }
            // Move to left for value less than parent node
            if(room_number < node.room_number){
                node.size += 1;
                node.left = insert(node.left, room_number);
            }
            // Move to right for value greater than parent node
            else if(room_number > node.room_number){
                node.size += 1;
                node.right = insert(node.right, room_number);
            }
            if(room_number == node.room_number){
                (node.count)++;
            }
            return node;
        }

        public int Select( Node node, int i ) {
            if( node == null ){
                return -1;
            }
            int left;
            int right;
            if( node.left != null ){
                left = node.left.size + node.left.count + 1;
                right = node.left.size + node.left.count + node.count + 1;
            } else {
                left = 1;
                right = node.count + 1;
            }
            if( left <= i && i <= right ){
                return node.room_number;
            }
            if(i < left){
                return Select(node.left, i);
            } else {
                return Select(node.right, i - right );
            }
        }

    }

    public static void sortedArrayToBST(BST tree, int arr[], int start, int end) {
        if (start > end) {
            return;
        }
        int mid = (start + end) / 2;
        tree.insert( tree.node , arr[mid]);
        sortedArrayToBST(tree, arr, start, mid - 1);
        sortedArrayToBST(tree, arr, mid + 1, end);
    }

    public static void sortedArrayToBST2(BST tree, int arr[], int start, int end, boolean f ) {
        if (start > end) {
            return;
        }
        int mid = (start + end) / 2;
        if( f ) {
            tree.insert( tree.node , arr[mid]);
        }
        sortedArrayToBST2(tree, arr, start, mid - 1, true);
        sortedArrayToBST2(tree, arr, mid + 1, end, true);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();
        int t = scanner.nextInt();
        scanner.nextLine();

        for (int j = 1; j <= t; j++) {
            if(j != 1){
                scanner.nextLine();
            }
            // parsing s and f
            int s = scanner.nextInt();
            int f = scanner.nextInt();
            scanner.nextLine();
            // create a list to store all lines
            boolean treeset_init = false;
            BST bst = null;
            // parsing the line information
            for (int i = 1; i <= s; i++) {
                int u = scanner.nextInt();
                int v = scanner.nextInt();
                int[] sorted = new int[v-u+1];
                int a = 0;
                for (int k = u; k <= v ; k++) {
                    sorted[a++] = k;
                }
                if(!treeset_init){
                    int mid = (0 + sorted.length - 1) / 2;
                    bst = new BST(sorted[mid]);
                    treeset_init = true;
                    sortedArrayToBST2( bst,  sorted, 0, sorted.length - 1,false);
                } else {
                    sortedArrayToBST( bst, sorted, 0, sorted.length - 1);
                    // creating and adding the lines with u and v
//                    bst.insert(bst.node,k);
                }
            }
            sb.append("Case #" + j + ":" + "\n");
            for (int i = 0; i < f; i++) {
                int r = scanner.nextInt();
                int room_number_of_friend = bst.Select( bst.node, r );
                sb.append(room_number_of_friend + "\n");
            }
        }
        System.out.println(sb.toString());
        scanner.close();
    }
}

