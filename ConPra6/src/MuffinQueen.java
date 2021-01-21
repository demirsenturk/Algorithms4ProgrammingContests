import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class MuffinQueen {

    static class SAT {
        private int n;
        private static ArrayList<Clause> likes;
        private static Boolean[] muffins;

        public SAT(int n, int m) {
            this.n = n;
            this.likes = new ArrayList<Clause>();
            muffins = new Boolean[m+1];
            for (int i = 0; i < muffins.length; i++) {
                muffins[i] = false;
            }
        }

        public void addJury(int i, HashSet<Variable> variables) {
            Clause c = new Clause(variables);
            likes.add(c);
        }

        public static void printMuffins(){
            System.out.println("printing...");
            for (int i = 1; i < muffins.length; i++) {
                if( muffins[i] ) {
                    System.out.println("" + i);
                } else {
                    System.out.println("-" + i);
                }
            }
        }

        public static Boolean Backtrack(int count){
//            if(!valid(count)){
//                System.out.println("here valid " + count);
//                printMuffins();
//                return false;
//            }
            if(completed(count)){
//                System.out.println("here " + count);
//                printMuffins();
                return true;
            } else if( count >= muffins.length ){
                return false;
            }
            if( count < muffins.length ) {
                muffins[count] = true;
                if (Backtrack(count + 1)) {
//                System.out.println("here Backtrack1 " + count);
                    return true;
                }
                muffins[count] = false;
                if (Backtrack(count + 1)) {
                    return true;
                }
            }
            return false;
        }

        public static Boolean valid(int count){
            Iterator<Clause> it = likes.listIterator();
            while (it.hasNext()) {
                Clause jury = it.next();
                if(jury.canNotBeSatisfied(count)){
                    return false;
                }
            }
            return true;
        }

        public static Boolean completed(int count){
            if( count < muffins.length ){
                return false;
            }
            Iterator<Clause> it = likes.listIterator();
            while (it.hasNext()) {
                Clause jury = it.next();
                if(!jury.isSatisfied()){
                    return false;
                }
            }
            return true;
        }


        static class Clause{
            private HashSet<Variable> variables;

            public Clause(HashSet<Variable> variables) {
                this.variables = variables;
            }

            public Boolean canNotBeSatisfied(int c){
                if( variables.isEmpty() ){
                    return true;
                }
                int not_chosen = 0;
                Iterator<Variable> it = variables.iterator();
                while (it.hasNext()) {
                    Variable var = it.next();
                    if( !var.isValid(c) ){
//                        System.out.println( "var " + var.variable + " - " + "c " + c );
                        not_chosen++;
                    } else {
//                        System.out.println( "var " + var.variable + " - " + "c " + c );
                        return false;
                    }
                }
                if( not_chosen >= variables.size() ){
//                    System.out.println("size " + variables.size() );
                    return true;
                }
//                System.out.println("can sat");
                return false;
            }

            public Boolean isSatisfied(){
                if( variables.isEmpty() ){
                    return false;
                }
                Iterator<Variable> it = variables.iterator();
                while (it.hasNext()) {
                    Variable var = it.next();
                    if( var.isTrue() ){
//                        System.out.println("satis var " + Boolean.toString(var.negate) + " " + var.variable);
                        return true;
                    }
                }
                return false;
            }

        }

        static class Variable{
            private int variable;
            private Boolean negate;

            public Variable(int variable, Boolean negate) {
                this.variable = variable;
                this.negate = negate;
            }

            public Boolean isValid(int c){
                if(variable >= muffins.length){
                    return false;
                }
                if( variable >= c ){
//                    System.out.println(variable + " > " + c);
                    return true;
                }
                return (!negate) == muffins[variable];
            }

            public Boolean isTrue(){
                return (!negate) == muffins[variable];
            }

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
            // parsing m and n
            String line = in.readLine();
            String[] parts = line.split(" ");
            int m = Integer.parseInt(parts[0]);
            int n = Integer.parseInt(parts[1]);

            SAT s = new SAT(n , m);

            for (int i = 1; i <= n; i++) {
                String n_line = in.readLine();
                String[] n_parts = n_line.split(" ");
                HashSet<SAT.Variable> variables = new HashSet<SAT.Variable>();
                for (int k = 0; k < n_parts.length; k++) {
                    if(n_parts[k].equals("0")){
                        break;
                    } else if(n_parts[k].charAt(0) == '-'){
                        int muffin_k = Integer.parseInt(n_parts[k].substring(1));
                        variables.add(new SAT.Variable(muffin_k,true));
                    } else {
                        int muffin_k = Integer.parseInt(n_parts[k]);
                        if( muffin_k != 0 ) {
                            variables.add(new SAT.Variable(muffin_k, false));
                        }
                    }
                }
                s.addJury(i,variables);
            }

            String result;
            if(s.Backtrack(1)){
                result = "yes";
            } else {
                result = "no";
            }
            sb.append("Case #" + j + ": " + result + "\n");
        }
        System.out.println(sb.toString());
    }

}
