import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class SoupDelivery {

    static class FacilityLocationProblem{

        private double factor;
        private int total_cost;

        private int number_of_facilities;
        private int number_of_locations;

        private int[][] cost;
        private int[] facility_cost;

        private HashSet<Integer> opened_facility;
        private HashSet<Integer> closed;

        private int[] assignedTo;

        public FacilityLocationProblem(int N, int M, int[][] matrix, int[] f_costs) {
            this.number_of_facilities = N;
            this.number_of_locations = M;
            this.cost = matrix;
            this.facility_cost = f_costs;
            this.factor = 1.0 - (1.0 / (2.0 * (M + N) * (M + N) ));

            this.assignedTo = new int[M];
            this.opened_facility = new HashSet<Integer>();
            this.closed = new HashSet<Integer>();

            for (int i = 0; i < N; i++) {
                closed.add(i);
            }

            this.total_cost = Integer.MAX_VALUE;
        }

        public int optimalCost(){
            int total = 0;

            Iterator<Integer> it = opened_facility.iterator();
            while(it.hasNext()){
                int i = it.next();
                total += facility_cost[i];
            }

            for (int j = 0; j < number_of_locations; j++) {
                int min = Integer.MAX_VALUE;
                Iterator<Integer> it2 = opened_facility.iterator();
                while(it2.hasNext()){
                    int i = it2.next();
                    if( cost[i][j] < min ){
                        min = cost[i][j];
                    }
                }
                total += min;
            }
            return total;
        }

        public void doOperation(){
            int total = 0;

            Iterator<Integer> it = opened_facility.iterator();
            while(it.hasNext()){
                int i = it.next();
                total += facility_cost[i];
            }

            for (int j = 0; j < number_of_locations; j++) {
                int min = Integer.MAX_VALUE;
                Iterator<Integer> it2 = opened_facility.iterator();
                while(it2.hasNext()){
                    int i = it2.next();
                    if( cost[i][j] < min ){
                        min = cost[i][j];
                        assignedTo[j] = i;
                    }
                }
                total += min;
            }
            this.total_cost =  total;
        }



        public String solver(){

            opened_facility.add(0);
            optimalCost();

            while( true ){
                boolean changed = false;

                for (int i = 0; i < number_of_facilities; i++) {
                    if( !opened_facility.contains(i) ){
                        // adding facility
                        opened_facility.add(i);
                        closed.remove(i);
                        // calculate after operation
                        int op_cost = optimalCost();
                        if( op_cost <= total_cost * factor ){
                            // if improved with significant factor
                            doOperation();
                            changed = true;
                            break;
                        } else {
                            // take changes back
                            closed.add(i);
                            opened_facility.remove(i);
                        }
                    } else if ( opened_facility.size() > 1 ) {
                        // deleting facility
                        opened_facility.remove(i);
                        closed.add(i);
                        int op_cost = optimalCost();
                        if( op_cost <= total_cost * factor ){
                            // if improved with significant factor
                            doOperation();
                            changed = true;
                            break;
                        } else {
                            // take changes back
                            closed.remove(i);
                            opened_facility.add(i);
                        }
                    }
                }

                if( changed == false && !closed.isEmpty() ){
                    // Swap
                    List<Integer> l = new ArrayList<Integer>(closed);
                    for (int k = 0; k < l.size(); k++) {
                        // to add
                        int to_swap = l.get(k);
                        for (int i = 0; i < number_of_facilities; i++) {
                            if( opened_facility.contains(i) ){
                                // i : to remove
                                // SWAP
                                // change i and to_swap
                                opened_facility.remove(i);
                                opened_facility.add(to_swap);
                                closed.add(i);
                                closed.remove(to_swap);
                                // calculate
                                int swap_cost = optimalCost();
                                if( swap_cost <= total_cost * factor ){
                                    // if significant improvement
                                    doOperation();
                                    // update closed
                                    changed = true;
                                    // exit for loop
                                    break;
                                } else {
                                    // take changes back
                                    closed.remove(i);
                                    closed.add(to_swap);
                                    opened_facility.remove(to_swap);
                                    opened_facility.add(i);
                                }
                            } // end if
                        } // end for
                        if( changed == true ){
                            break;
                        }
                    } // end while
                    // end swap
                }

                if( changed != true ){
                    // if there exist not an operation with significant improvement
                    break;
                }

            }
            StringBuilder sb = new StringBuilder();
            sb.append("" + total_cost + "\n");
            Iterator<Integer> it2 = opened_facility.iterator();
            while(it2.hasNext()){
                int i = it2.next();
                sb.append((i+1));
                for (int j = 0; j < assignedTo.length; j++) {
                    if( assignedTo[j] == i ) {
                        sb.append( " " + (j + 1) );
                    }
                }
                sb.append("\n");
            }

            return sb.toString();
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

            // parsing N and M
            String line = in.readLine();
            String[] parts = line.split(" ");
            int N = Integer.parseInt(parts[0]);
            int M = Integer.parseInt(parts[1]);

            int[] facility_cost = new int[N];
            String n_line = in.readLine();
            String[] n_parts = n_line.split(" ");
            for (int i = 0; i < N; i++) {
                facility_cost[i] = Integer.parseInt(n_parts[i]);
            }

            int[][] deliver_cost = new int[N][M];
            for (int i = 0; i < N; i++) {
                String di_line = in.readLine();
                String[] di_parts = di_line.split(" ");
                for (int k = 0; k < M; k++) {
                    deliver_cost[i][k] = Integer.parseInt(di_parts[k]);
                }
            }

            FacilityLocationProblem FL = new FacilityLocationProblem(N,M,deliver_cost,facility_cost);


            String res = FL.solver();
            sb.append("Case #" + j + ": " + res + "\n");

        }
        System.out.println(sb.toString());
    }

}
