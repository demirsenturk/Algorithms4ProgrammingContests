import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class StoryTime {

    static class Graph{

        private Long count;
        // the number of possible answers
        private int num_character;
        // number of characters
        private int num_chapters;
        // number of total chapters

        private int[] ordering;
        // the current ordering to be considered in backtracking

        public static Set<Integer> visited;
        // to keep track used chapters
        public static Map<Integer, List<Integer>> edges;
        // to save inter dependencies

        private int[] lists;
        // list to keep how many chapters left for each character
        private int[] counts;
        // list to save the size of chapters of one character
        private int[] putted;
        // to keep update how many chapters putted for each character

        public Graph(int num_character) {
            this.num_character = num_character;

            visited = new HashSet<Integer>();
            edges = new HashMap<Integer, List<Integer>>();

            this.count = 0L;

            this.lists = new int[this.num_character];
            this.counts = new int[this.num_character];
            this.putted = new int[this.num_character];

            for (int i = 0; i < this.num_character; i++) {
                putted[i] = 0;
                // not any chapter putted yet for each character
            }
        }

        public void setChapters(int num_chapters){
            // total number of chapters
            this.num_chapters = num_chapters;
            // initialize the ordering array
            this.ordering = new int[this.num_chapters];
        }

        public void addChar(int count_previous, int char_index, int size){
            lists[char_index-1] = size;
            counts[char_index-1] = count_previous;
        }

        public void addDependency(int char1, int chap1, int char2, int chap2){
            int firstChapter = getChapterParser(char1,chap1);
            int secondChapter = getChapterParser(char2,chap2);

            List<Integer> adjList = edges.get(firstChapter);
            if (adjList == null) {
                adjList = new ArrayList<Integer>();
            }
            adjList.add(secondChapter);
            edges.put(firstChapter, adjList);
        }

        public int getChapter(int character, int i){
            // get the ith chapter of jth character
            return counts[character] + i;
        }

        public boolean backtrack(int cur_chapter){
            // Validity
//            if( cur_chapter != 0 && !valid(cur_chapter) ) {
//                return false;
//            }
            // complete
            if( cur_chapter >= num_chapters ){
                // if the ordering is complete
                count++;
                return true;

            } else {
                for (int i = 0; i < num_character; i++) {
                    // for each character
                    int nextChapterOfCharacter_i = putted[i];
                    int chapter = getChapter(i,nextChapterOfCharacter_i);

                    if( !visited.contains(chapter)
                            // if not used already
                            // if there is chapters left to put
                            && putted[i] != lists[i]
                            // the sequential (previous) chapter is not from the same character
                            && !sameChar(i,cur_chapter)
                            // inter dependencies fulfilled
                            && valid(chapter) ){
                        visited.add(chapter);
                        putted[i]++;
                        ordering[cur_chapter] = i;
                        backtrack(cur_chapter + 1);
                        ordering[cur_chapter] = -1;
                        putted[i]--;
                        visited.remove(chapter);
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
            return ch1 == ordering[cur_index - 1];
        }

        public static boolean valid(int chapter) {
            List<Integer> adjList = edges.get(chapter);
            for(int i = 0; adjList != null && i < adjList.size(); i++) {
                if (visited.contains(adjList.get(i))) {
                    return false;
                }
            }
            return true;
        }

        public int getChapterParser(int character, int i){
            // get the ith chapter of jth character
            return counts[character - 1] + i - 1;
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
