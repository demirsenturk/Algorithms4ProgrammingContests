import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class StoryTelling {

    static class Graph{

        private int num_character;
        private int num_chapters;
        private int[] ordering;
        private boolean[][] adjacencyMatrix;
//        private int[] chapterList;
        private boolean[] inBook;
        private int count;
        private int[] chapters;
        private int[] lists;
        private HashMap<Integer, Integer> hlists;

        public Graph(int num_character) {
            this.num_character = num_character;
            this.lists = new int[this.num_character];
//            for (int i = 1; i < this.num_chapters; i++) {
//                for (int j = 1; j < this.num_chapters; j++) {
//                    adjacencyMatrix[i][j] = false;
//                }
//            }
            this.count = 0;
            this.hlists = new HashMap<Integer, Integer>();
        }

        public void setChapters(int num_chapters){
            this.num_chapters = num_chapters;
            this.adjacencyMatrix = new boolean[this.num_chapters][this.num_chapters];
            this.inBook = new boolean[this.num_chapters];
            this.chapters = new int[this.num_chapters];
            this.ordering = new int[this.num_chapters];
        }

        public void addChar(int count_previous, int char_index, int size){
            lists[char_index-1] = count_previous;
            for (int i = 0; i < size; i++) {
                hlists.put(count_previous + i , char_index);
            }
        }

        public void addDependency(int char1, int chap1, int char2, int chap2){
            int firstChapter = getChapter(char1,chap1);
            int secondChapter = getChapter(char2,chap2);
            adjacencyMatrix[firstChapter][secondChapter] = true;
        }

        public int getChapter(int character, int i){
            return lists[character - 1] + i - 1;
        }

        public void print(){
            StringBuilder st = new StringBuilder();
            for (int i = 0; i < num_chapters; i++) {
                st.append(ordering[i] + " ");
            }
            System.out.println(st.toString() + "\n");
        }

        public boolean backtrack(int cur_chapter){

            if( cur_chapter >= num_chapters ){
                count++;
                print();
                return true;
            } else {
                for (int i = 0; i < chapters.length; i++) {
                    if( !inBook[i] && !sameChar(i,cur_chapter) && valid(i , cur_chapter) ){
                        inBook[i] = true;
                        ordering[cur_chapter] = i;
                        backtrack(cur_chapter + 1);
                        inBook[i] = false;
                    }
                }
            }

            return true;
        }

        public boolean sameChar(int ch1, int cur_index){
            if(cur_index == 0){
                return false;
            }
            return hlists.get(ch1) == hlists.get(ordering[cur_index-1]);
        }

        public boolean comesBefore(int toPut, int inBook){
            return adjacencyMatrix[toPut][inBook];
        }

        public boolean valid(int toPut, int current_chapter){
            for (int i = 0; i < current_chapter; i++) {
                if( comesBefore( toPut , ordering[i] ) == true ){
                    return false;
                }
            }
            return true;
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
            // parsing n and m
            String line = in.readLine();
            String[] parts = line.split(" ");
            int n = Integer.parseInt(parts[0]);
            int m = Integer.parseInt(parts[1]);

            Graph g = new Graph(n);

            int chapter_count = 0;

            for (int i = 1; i <= n; i++) {
                int a_i = Integer.parseInt(in.readLine());
                g.addChar(chapter_count, i, a_i);
                chapter_count = chapter_count + a_i;
                if(i == n){
                    g.setChapters(chapter_count);
                }
            }

            if( m != 0 ){
                for (int i = 0; i < m; i++) {
                    String j_line = in.readLine();
                    String[] c_p_d_q = j_line.split(" ");
                    int c = Integer.parseInt(c_p_d_q[0]);
                    int p = Integer.parseInt(c_p_d_q[1]);
                    int d = Integer.parseInt(c_p_d_q[2]);
                    int q = Integer.parseInt(c_p_d_q[3]);
                    g.addDependency(c,p,d,q);
                }
            }

            g.backtrack(0);
            String result = "" + g.count;
            sb.append("Case #" + j + ": " + result + "\n");
        }
        System.out.println(sb.toString());
    }
}
