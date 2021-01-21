import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ContactList {

    static class Trie{
        private Node root;

        public Trie() {
            this.root = new Node(false);
        }

        public Node lookup(String s){
            Node temp;
            if( root.nodes.containsKey(s.charAt(0) ) ){
                temp = root.nodes.get(s.charAt(0));
            } else {
                return null;
            }

            for (int i = 1; i < s.length(); i++) {
                if( temp.nodes.containsKey(s.charAt(i)) ){
                    temp = temp.nodes.get(s.charAt(i));
                } else {
                    return null;
                }
            }
            if( temp.end ) {
                return temp;
            } else {
                return null;
            }
        }

        public int result(){
            return countChanges(this.root);
        }

        public int countChanges(Node v) {
            int count = 0;
            if(v.nodes.isEmpty()){
                return count;
            }
            if( v.end && v.nodes.size() > 0 ){
                count++;
            }
            for (Node pre_node : v.nodes.values()) {
                count += countChanges(pre_node);
            }

            return count;
        }

        public int countPredecessor(Node v) {
            int count = 0;
            for (Node pre_node : v.nodes.values()) {
                if( pre_node.end == true ){
                    count++;
                }
                count += countPredecessor(pre_node);
            }
            return count;
        }

        public void insert(String s){
            Node temp = root;
            for (int i = 0; i < s.length(); i++) {
                if( temp.nodes.containsKey(s.charAt(i)) ){
                    temp = temp.nodes.get(s.charAt(i));
                } else {
                    Node cur = new Node(false);
                    temp.nodes.put( s.charAt(i), cur );
                    temp = cur;
                }
                if( i == s.length() - 1 ) {
                    temp.end = true;
                }
            }
        }

    }

    static class Node{
        HashMap<Character,Node> nodes;
        boolean end;

        public Node(boolean end) {
            this.nodes = new HashMap<Character,Node>();
            this.end = end;
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

            Trie trie = new Trie();

            // parse the names
            for (int i = 0; i < n; i++) {
                String i_line = in.readLine();
                trie.insert(i_line);
            }

            String list = "" + trie.result();

            sb.append("Case #" + j + ": " + list + "\n");
        }
        System.out.println(sb.toString());
    }
}
