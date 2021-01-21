import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.CheckedInputStream;

public class KnightsTour {

    static class Field{
        private int startX;
        private int startY;
        private int[][] field;
        private int Tool_count;
        private int num_tools;
        private int[][] offset;

        private static final int WALL = -1;
        private static final int WALKABLE = 1;
        private static final int TOOL = 2;
        private static final int LEA = 3;
        private static final int ENTERED = 4;

        public Field(int width, int height) {
            // initialize the field
            this.field = new int[height][width];
            // the tool_count for tracking the tools found
            this.Tool_count = 0;
            // Total tool count
            this.num_tools = 0;
            possibleDirections();
        }

        public boolean solver(){
            // Start with the positions of LEA
            return Backtracking(startX, startY);
        }

        public boolean Backtracking(int i, int j){

            if( Tool_count >= num_tools ){
                // COMPLETED
                return true;
            }

            int x, y;
            for (int k = 0; k < 4; k++) {
                // all possible 4 fields
                x = i + offset[k][0];
                y = j + offset[k][1];

                if( Valid(x, y) ) {
                    int temp = field[x][y];
                    // save the entered field for backtracking
                    field[x][y] = ENTERED;
                    // Field marked as entered
                    if(temp == TOOL){
                        // if the field contains a tool increment tool count
                        Tool_count++;
                    }
                    if ( Backtracking( x, y ) ) {
                        // Backtrack(c')
                        return true;
                    }
                    if(temp == TOOL){
                        // Backtrack the changes
                        Tool_count--;
                    }
                    // Backtrack, mark the field as it was
                    field[x][y] = temp;
                }

            }

            return false;
        }

        public boolean Valid(int i, int j){
            if(i >= 0 && i < field.length
                    && j >= 0 && j < field[0].length
                    && field[i][j] != WALL
                    && field[i][j] != ENTERED
                    && field[i][j] != LEA ){
//                System.out.println("x : " + i + " y : " + j);
                return true;
            }
            return false;
        }

        public void possibleDirections() {
            // All possible Directions
            offset = new int[4][2];
            offset[0][0] = 0;
            offset[0][1] = 1;

            offset[1][0] = 1;
            offset[1][1] = 0;

            offset[2][0] = 0;
            offset[2][1] = -1;

            offset[3][0] = -1;
            offset[3][1] = 0;
        }

        public void addWall(int i, int j){
            field[i][j] = WALL;
        }

        public void addWalkable(int i, int j){
            field[i][j] = WALKABLE;
        }

        public void addLea(int i, int j){
            field[i][j] = LEA;
            this.startX = i;
            this.startY = j;
        }

        public void addTool(int i, int j){
            field[i][j] = TOOL;
            num_tools++;
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
            int w = Integer.parseInt(parts[0]);
            int h = Integer.parseInt(parts[1]);

            Field f = new Field(w,h);

            for (int x = 0; x < h; x++) {
                String n_line = in.readLine();
                for (int y = 0; y < w; y++) {

                    if(n_line.charAt(y) == '#' ){
                        // put queen to the table
                        f.addWall(x,y);
                    } else if(n_line.charAt(y) == '_' ){
                        // put queen to the table
                        f.addWalkable(x,y);
                    } else if(n_line.charAt(y) == 'L' ){
                        // put queen to the table
                        f.addLea(x,y);
                    } else if(n_line.charAt(y) == 'T' ){
                        // put queen to the table
                        f.addTool(x,y);
                    }

                }
            }
            String result;
            if( f.solver() ){
                result = "yes";
            } else {
                result = "no";
            }
            sb.append("Case #" + j + ": " + result + "\n");
        }
        System.out.println(sb.toString());
    }

}
