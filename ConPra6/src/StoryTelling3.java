import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class StoryTelling3 {

    static class Graph{

        private Long count;
        // the number of possible answers
        private int num_character;
        // number of characters
        private int num_chapters;
        // number of total chapters

        private int[] ordering;
        // the current ordering to be considered in backtracking

        private boolean[][] adjacencyMatrix;
        // to keep inter-dependencies

        private HashMap<Integer, Integer> hlists;

        private int[] lists;
        // list to keep how many chapters left for each character
        private int[] counts;
        // list to save the size of chapters of one character
        private int[] putted;
        // to keep update how many chapters putted for each character

        public Graph(int num_character) {
            this.num_character = num_character;
            this.lists = new int[this.num_character];
            this.counts = new int[this.num_character];
            this.putted = new int[this.num_character];
            for (int i = 0; i < this.num_character; i++) {
                putted[i] = 0;
                // not any chapter putted yet for each character
            }
            this.count = 0L;
            this.hlists = new HashMap<Integer, Integer>();
        }

        public void setChapters(int num_chapters){
            this.num_chapters = num_chapters;
            // total number of chapters
            this.adjacencyMatrix = new boolean[this.num_chapters][this.num_chapters];
            // initialize the ordering array
            this.ordering = new int[this.num_chapters];
        }

        public void addChar(int count_previous, int char_index, int size){
            lists[char_index-1] = size;
            counts[char_index-1] = count_previous;
            for (int i = 0; i < size; i++) {
                hlists.put(count_previous + i , char_index);
            }
        }

        public void addDependency(int char1, int chap1, int char2, int chap2){
            int firstChapter = getChapter(char1,chap1);
            int secondChapter = getChapter(char2,chap2);
            // get the global number of the chap1 of the char1
            adjacencyMatrix[firstChapter][secondChapter] = true;
            // add the interdependency constraint
        }

        public int getChapter(int character, int i){
            // get the ith chapter of jth character
            return counts[character - 1] + i - 1;
        }

        public void print(){
            StringBuilder st = new StringBuilder();
            for (int i = 0; i < num_chapters; i++) {
                st.append(ordering[i] + " ");
            }
            System.out.println(st.toString() + "\n");
        }

        public boolean backtrack(int cur_chapter){
            // Validity
            if( cur_chapter != 0 && !valid(cur_chapter) ) {
                print();
                return false;
            }
            // complete
            if( cur_chapter >= num_chapters ){
                // if the ordering is complete
                count++;
                return true;

            } else {
                for (int i = 0; i < num_character; i++) {
                    // for each character
                    if( lists[i] > 0 && !sameChar(i,cur_chapter) ){
                        // if there is chapters left to put
                        // the sequential (previous) chapter is not from the same character
                        // inter dependencies fulfilled

                        int chap_toPut = getChapter(i + 1, putted[i] + 1);
                        ordering[cur_chapter] = chap_toPut;
                        // put the character in ordering
                        lists[i]--;
                        // decrease the number of chapters left for the character
                        putted[i]++;
                        // increase the number of chapters already putted for the character
                        backtrack(cur_chapter + 1);
                        // Process with next backtracking step
                        putted[i]--;
                        lists[i]++;
                        // reverse the changes
                    }
                }
            }
            return false;
        }

        public boolean sameChar(int ch1, int cur_index){
            if(cur_index == 0){
                // if the first chapter
                return false;
            }
            // check with the previous chapter
            return hlists.get(ch1) == hlists.get(ordering[cur_index-1]);
        }

        public boolean valid(int cur){
            int character1 = ordering[cur-1];
            for (int i = 0; i < cur; i++) {
                int character2 = ordering[i];
                // check for each other character
                if( i != cur && adjacencyMatrix[character1][character2] == true ){
                    // if there is an inter-dependency and it is not fulfilled
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
            // count for the total number of chapters

            for (int i = 1; i <= n; i++) {
                // parse a_i
                int a_i = Integer.parseInt(in.readLine());
                // add character with total number of previous chapters, character index and size
                g.addChar(chapter_count, i, a_i);
                // update the total chapter count
                chapter_count = chapter_count + a_i;
                if(i == n){
                    // add the total chapter count
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
                    // add dependency
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
