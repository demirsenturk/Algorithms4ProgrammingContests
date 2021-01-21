import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Ghost {

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

        public String result(int round){
            int can_win = MinMax(root,true);
            int p1_can_loose = MinMax(root, false);

            boolean can_win_no_loose = can_win > 0 && p1_can_loose > 0;
            boolean can_win_can_loose = can_win > 0 && p1_can_loose < 0;
            boolean no_win_no_loose = can_win < 0 && p1_can_loose > 0;
            boolean no_win_can_loose = can_win < 0 && p1_can_loose < 0;
//            System.out.println(can_win);
//            System.out.println(p1_can_loose);
            boolean isOdd = (round % 2 == 1)? true : false;
            StringBuilder sb = new StringBuilder();
            // SC1
            if( can_win_can_loose ){
                // beginner can win and loose -> dominant
                sb.append("victory\n");
            } else if( can_win_no_loose ){
                // beginner can win but can not loose
                sb.append("victory\n");
            } else if( !isOdd && no_win_can_loose ){
                // beginner always loose
                sb.append("victory\n");
            } else {
                sb.append("defeat\n");
            }

            // SC2
            if( can_win_can_loose ){
                // beginner can win and loose -> dominant
                sb.append("victory\n");
            } else if( can_win_no_loose && !isOdd ){
                // beginner can win but can not loose
                sb.append("defeat\n");
            }  else if( can_win_no_loose && isOdd ){
                // beginner can win but can not loose
                sb.append("victory\n");
            } else {
                // beginner cant win always loose
                sb.append("defeat\n");
            }


            // SC3
            if( can_win_can_loose ){
                sb.append("defeat\n");
            } else if( can_win_no_loose ){
                sb.append("defeat\n");
            } else if( !isOdd && no_win_can_loose ){
                sb.append("defeat\n");
            } else {
                sb.append("victory\n");
            }

            // SC4
            if( can_win_can_loose ){
                // beginner can win and loose -> dominant
                sb.append("defeat\n");
            } else if( can_win_no_loose && !isOdd ){
                // beginner can win but can not loose
                sb.append("victory\n");
            }  else if( can_win_no_loose && isOdd ){
                // beginner can win but can not loose
                sb.append("defeat\n");
            } else {
                // beginner cant win always loose
                sb.append("victory\n");
            }

            return sb.toString();
        }

        public int MinMax(Node v, boolean isMax) {
            if( v.end == true ){
                return v.score;
            }
            if ( isMax ) {
                int max = Integer.MIN_VALUE;
                for (Node pre_node : v.nodes.values()) {
                    int pre_node_score = MinMax(pre_node, false);
                    if (max < pre_node_score) {
                        max = pre_node_score;
                    }
                }
                return max;
            } else {
                int min = Integer.MAX_VALUE;
                for (Node pre_node : v.nodes.values()) {
                    int pre_node_score = MinMax(pre_node, true);
                    if ( min > pre_node_score) {
                        min = pre_node_score;
                    }
                }
                return min;
            }
        }

        public void insert(String s){
            Node temp = root;
            for (int i = 0; i < s.length(); i++) {
                if( temp.nodes.containsKey(s.charAt(i)) ){
                    temp = temp.nodes.get(s.charAt(i));
                    if( temp.end ){
                        // dont add the word as its prefix already included
                        return;
                    }
                } else {
                    Node cur = new Node(false);
                    temp.nodes.put( s.charAt(i), cur );
                    temp = cur;
                }
                if( i == s.length() - 1 ) {
                    temp.end = true;
                    if( s.length() % 2 == 0 ){
                        // even
                        temp.score = 1;
                    } else {
                        // odd
                        temp.score = -1;
                    }
                }
            }
        }

    }

    static class Node{
        HashMap<Character,Node> nodes;
        boolean end;
        int score;

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

            // parsing n and w
            String i_line = in.readLine();
            String[] i_line_parts = i_line.split(" ");
            int n = Integer.parseInt(i_line_parts[0]);
            int w = Integer.parseInt(i_line_parts[1]);

            Trie trie = new Trie();

            // parse the names
            for (int i = 0; i < w; i++) {
                String word = in.readLine();
                trie.insert(word);
            }

            String list = "" + trie.result(n);

            sb.append("Case #" + j + ":" + "\n" + list);
        }
        System.out.println(sb.toString());
    }
}
