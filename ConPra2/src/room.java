import java.util.*;

public class room {

    // extended the BST source https://knpcode.com/java-programs/tree-sort-java-program/
    // added new rank and two field storage features
     static class Node{
        int room_number;
        int station;
        int size;
        Node left;
        Node right;
        Node( int room_number, int station){
            this.room_number = room_number;
            this.station = station;
            this.size = 1;
            left = null;
            right = null;
        }
    }

    static class BST{
        Node node;
        BST(int room_number, int station){
            node = new Node(room_number,station);
        }
        public Node insert(Node node, int room_number, int station){
            if(node == null){
                return new Node(room_number,station);
            }
            // Move to left for value less than parent node
            if(room_number < node.room_number){
                node.size += 1;
                node.left = insert(node.left, room_number, station);
            }
            // Move to right for value greater than parent node
            else if(room_number > node.room_number){
                node.size += 1;
                node.right = insert(node.right, room_number, station);
            }
            else if(station < node.station){
                node.size += 1;
                node.left = insert(node.left, room_number, station);
            }
            else if(station > node.station){
                node.size += 1;
                node.right = insert(node.right, room_number, station);
            }
            return node;
        }

        // For traversing in order
        public void inOrder(Node node){
            if(node != null){
                // recursively traverse left subtree
                inOrder(node.left);
                System.out.println( "(" + node.room_number + " " + node.station + ")");
                // recursively traverse right subtree
                inOrder(node.right);
            }
        }

        public int Select( Node node, int i ) {
            if( node == null ){
                return -1;
            }
            int r;
            if( node.left != null ){
                r = node.left.size + 1;
            } else {
                r = 1;
            }
            if( i == r ){
                return node.room_number;
            }
            if(i < r){
                return Select(node.left, i);
            } else {
                return Select(node.right, i - r );
            }
        }
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
                for (int k = u; k <= v ; k++) {
                    if(!treeset_init){
                        bst = new BST(k,i);
                        treeset_init = true;
                    } else {
                        // creating and adding the lines with u and v
                        bst.insert(bst.node,k,i);
                    }
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
